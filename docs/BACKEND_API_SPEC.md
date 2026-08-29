# Spendly — Business Data Model & Backend API Specification

## 1. Purpose and current state

Spendly is currently a **local-only** Android app: all data lives in a Room
(SQLite) database on-device, and there is no real backend — a Ktor HTTP
client is scaffolded (`core/ktor/KtorClientFactory.kt`, `core/di/NetworkModule.kt`)
but never wired to a live API. `SyncLogEntity`/`SyncLogDao` exist as a
sync-queue table but nothing populates or drains them.

This document specifies the backend needed to give the app:
- real multi-user accounts and authentication (today, login/signup screens
  exist but don't persist anything — any input "succeeds")
- a server-of-record for all data, so a user's expenses/budgets/staff/lending
  records survive a reinstall and sync across devices
- the staff-approval workflow, budget/allocation tracking, and lending ledger
  the client UI already assumes

The data model below is extracted directly from the Android app's Room
entities (`app/src/main/java/.../data/local/room/entity/`) so the backend
stays a faithful mirror of what the client already expects — not a
reinterpretation.

## 2. Suggested stack

- **API style:** REST + JSON (matches the Ktor `ContentNegotiation`/
  `kotlinx.serialization` client already in the app; no GraphQL client
  exists on-device).
- **Auth:** JWT access token + refresh token, `Authorization: Bearer <token>`.
- **Database:** PostgreSQL — the domain is heavily relational (every entity
  below has explicit foreign keys in the Room schema), so a relational store
  matches the existing design rather than fighting it.
- **IDs:** the client uses `Long` auto-increment IDs everywhere (Room's
  `@PrimaryKey(autoGenerate = true)`). Two options:
  1. Keep server-side `BIGSERIAL` IDs and have the client treat the ID
     returned by `POST` as authoritative (simplest; requires the client to
     replace its local temp ID with the server ID on first sync).
  2. Switch to client-generated UUIDs everywhere (bigger client migration,
     avoids ID-remapping on sync). **Recommended if you're building the
     backend from scratch now** — it removes a whole class of sync bugs.
  The schema below is written with `BIGSERIAL`/`Long` to match the app
  as-is; swap to `UUID` throughout if you take option 2.

## 3. Core entities

Each entity lists: table name, fields (name — type — constraints), and the
exact source file it was extracted from, so you can cross-check against the
client at any time.

### 3.1 User
Source: `entity/UserEntity.kt`

| Field | Type | Notes |
|---|---|---|
| `userId` | bigint PK | |
| `name` | text, not null | |
| `email` | text, not null, unique | |
| `passwordHash` | text, nullable | null for OAuth/phone-only accounts |
| `role` | text, default `"user"` | `admin` / `staff` / `viewer` / `approver` / `user` |
| `phone` | text, nullable | |
| `profilePicUri` | text, nullable | URL after upload, not a local URI on the server |
| `createdAt` | bigint (epoch ms) | |
| `isActive` | boolean, default true | |

**Per-user settings** (currently `DataStore` on-device only — promote to a
`user_settings` table, one row per user, so settings sync across devices):

| Field | Type | Values |
|---|---|---|
| `themeMode` | text | `light` / `dark` |
| `businessMode` | text | `personal` / `business` |
| `premiumType` | text | `basic` / `monthly` / `yearly` |
| `currency` | text | `Rupee` / `Dollar` / `Pound` / `Yen` / `Ruble` / `Bitcoin` / `Euro` (symbol in parens) |
| `language` | text | `Hindi` / `English` |
| `exportFormat` | text | `PDF` / `CSV` / `Excel` |
| `syncWith` | text | `Google Drive` / `App Drive` / `OneDrive` |
| `syncFrequency` | text | `Daily` / `Weekly` / `Monthly` |

### 3.2 Staff
Source: `entity/StaffEntity.kt` — business-mode team members (distinct from
`User`; a business owner's `User` account manages many `Staff` rows).

| Field | Type | Notes |
|---|---|---|
| `staffId` | bigint PK | |
| `employeeId` | text, unique | |
| `name` | text, not null | |
| `email` | text, unique | |
| `phone` | text, nullable | |
| `role` | text, not null | maps to `UserRole` (§4) |
| `department` | text, nullable | |
| `designation` | text, nullable | |
| `managerId` | bigint, nullable, FK → `staff.staffId` | self-referencing |
| `joiningDate` | text (ISO date), nullable | |
| `salary` | numeric, nullable | |
| `profilePicUri` | text, nullable | |
| `permissionsJson` | jsonb, nullable | flexible per-staff permission overrides |
| `isActive` | boolean, default true | |
| `lastLoginAt` | text, nullable | |
| `notes` | text, nullable | |
| `createdAt` | bigint | |

### 3.3 Category
Source: `entity/CategoryEntity.kt`

| Field | Type | Notes |
|---|---|---|
| `categoryId` | bigint PK | |
| `name` | text, not null | |
| `description` | text, nullable | |
| `icon` | text, nullable | icon key/URL |
| `parentId` | bigint, nullable, FK → `categories.categoryId` | supports sub-categories |
| `createdAt` | bigint | |
| `isSystem` | boolean, default false | seeded defaults (Food/Travel/Staff/Utility) vs. user-created |

### 3.4 Expense
Source: `entity/ExpenseEntity.kt` — the central record.

| Field | Type | Notes |
|---|---|---|
| `expenseId` | bigint PK | |
| `userId` | bigint, FK → `users.userId`, **ON DELETE CASCADE** | |
| `allocationId` | bigint, nullable, FK → `allocations.allocationId`, **ON DELETE SET NULL** | which staff budget allocation this was charged against |
| `categoryId` | bigint, nullable, FK → `categories.categoryId`, **ON DELETE SET NULL** | |
| `title` | text, not null | |
| `amount` | numeric(12,2), not null | |
| `notes` | text, nullable | |
| `receiptUri` | text, nullable | URL to uploaded receipt image |
| `timestamp` | bigint | when the expense occurred |
| `status` | text, default `"Pending"` | `Pending` / `Approved` / `Rejected` — see §4 workflow |
| `approvedBy` | bigint, nullable, FK → `users.userId` | |
| `synced` | boolean | client-only concept; irrelevant server-side (server is always "synced") |

### 3.5 Income
Source: `entity/IncomeEntity.kt`

| Field | Type | Notes |
|---|---|---|
| `incomeId` | bigint PK | |
| `userId` | bigint, FK → `users.userId` | |
| `categoryId` | bigint, nullable, FK → `categories.categoryId` | |
| `amount` | numeric(12,2), not null | |
| `notes` | text, nullable | |
| `receiptUri` | text, nullable | |
| `timestamp` | bigint | |
| `synced` | boolean | client-only |

### 3.6 Budget
Source: `entity/BudgetEntity.kt` — admin-set overall spending caps.

| Field | Type | Notes |
|---|---|---|
| `budgetId` | bigint PK | |
| `name` | text, default `"Default Budget"` | |
| `periodType` | text, not null | e.g. `weekly` / `monthly` |
| `periodStart` | bigint | |
| `periodEnd` | bigint | |
| `totalLimit` | numeric(12,2), not null | |
| `usedAmount` | numeric(12,2), default 0 | **computed** — sum of linked expenses in the period; recompute server-side, don't trust client-sent values |
| `createdBy` | bigint, nullable, FK → `users.userId` | |
| `createdAt` | bigint | |
| `status` | text, default `"Active"` | |

### 3.7 Allocation
Source: `entity/AllocationEntity.kt` — admin assigns a budget slice to a
specific staff member.

| Field | Type | Notes |
|---|---|---|
| `allocationId` | bigint PK | |
| `staffId` | bigint, FK → `staff.staffId` | **missing FK/index in the current Room schema — add both server-side** |
| `title` | text, not null | |
| `category` | text, not null | free-text category label |
| `allocatedAmount` | numeric(12,2), not null | |
| `usedAmount` | numeric(12,2), default 0 | computed from linked expenses, like Budget |
| `notes` | text, nullable | |
| `createdAt` | bigint | |
| `expiresAt` | bigint, nullable | |
| `status` | text, default `"Active"` | `Active` / `Closed` / `Expired` |

### 3.8 Contact
Source: `entity/ContactEntity.kt` — people in the lending/borrowing ledger.

| Field | Type | Notes |
|---|---|---|
| `contactId` | bigint PK | |
| `userId` | bigint, nullable, FK → `users.userId` | owner of this contact book entry |
| `name` | text, not null | |
| `phone` | text, nullable | used as the natural dedupe key client-side |
| `email` | text, nullable | |
| `address` | text, nullable | |
| `type` | text, default `"both"` | `lender` / `borrower` / `both` / `customer` |
| `createdAt` | bigint | |

### 3.9 LendingTransaction
Source: `entity/LendingTransactionEntity.kt` — personal lending/borrowing
ledger, separate from `Expense`.

| Field | Type | Notes |
|---|---|---|
| `lendingId` | bigint PK | |
| `userId` | bigint, nullable, FK → `users.userId`, **ON DELETE CASCADE** | |
| `contactId` | bigint, FK → `contacts.contactId`, **ON DELETE CASCADE** | |
| `amount` | numeric(12,2), not null | |
| `transactionType` | text, not null | `lent` / `borrowed` / `udhar_sale` |
| `dueDate` | bigint, nullable | |
| `status` | text, default `"pending"` | `pending` / `partial` / `paid` / `overdue` — derived, see §4 |
| `notes` | text, nullable | |
| `createdAt` | bigint | |

> The current client cascades this table's FK on `Contact` deletion, meaning
> deleting a contact silently destroys their lending history. **Recommend
> the backend use `ON DELETE RESTRICT` instead** — a financial ledger
> shouldn't disappear because someone tidied their contact list.

### 3.10 Repayment
Source: `entity/RepaymentEntity.kt`

| Field | Type | Notes |
|---|---|---|
| `repaymentId` | bigint PK | |
| `lendingId` | bigint, FK → `lending_transactions.lendingId`, **ON DELETE CASCADE** | |
| `amountPaid` | numeric(12,2), not null | |
| `date` | bigint | |
| `paymentMethod` | text, nullable | maps to `PaymentMode`: `Cash` / `Card` / `UPI` / `NetBanking` |
| `notes` | text, nullable | |

### 3.11 Notification
Source: `entity/NotificationEntity.kt`

| Field | Type | Notes |
|---|---|---|
| `notificationId` | bigint PK | |
| `userId` | bigint, FK → `users.userId` | |
| `type` | text, not null | `budget_warning` / `expense_status` / `allocation_assigned` |
| `message` | text, not null | |
| `relatedEntity` | text, nullable | e.g. `"expense"`, `"budget"` |
| `relatedId` | bigint, nullable | polymorphic reference — do not add a DB-level FK, resolve by `relatedEntity` in application code |
| `isRead` | boolean, default false | |
| `createdAt` | bigint | |

### 3.12 Sync log (server-side mirror)
Source: `entity/SyncLogEntity.kt` (client-side queue). Server-side, this
becomes the **audit trail** the `/sync` endpoint reads/writes rather than a
user-facing table:

| Field | Type | Notes |
|---|---|---|
| `syncLogId` | bigint PK | |
| `userId` | bigint, FK → `users.userId` | whose change this is |
| `entityType` | text | `expense` / `income` / `budget` / `allocation` / `contact` / `lending` / `repayment` |
| `entityId` | bigint | |
| `action` | text | `insert` / `update` / `delete` |
| `timestamp` | bigint | client-side event time, for conflict resolution |
| `appliedAt` | bigint | server-side apply time |

## 4. Enums (shared vocabulary between client and API)

Source: `data/model/ExpenseDM.kt`, `core/datastore/AppMainDM.kt`.

```
UserRole        = PERSONAL | ADMIN | APPROVER | ENTRY_ONLY | VIEWER
ExpenseStatus   = PENDING | APPROVED | REJECTED
BusinessMode    = Personal | Business
PremiumType     = BASIC | MONTHLY | YEARLY
ThemeType       = LIGHT | DARK
PaymentMode     = Cash | Card | UPI | NetBanking
Currency        = Rupee(₹) | Dollar($) | Pound(£) | Yen(¥) | Ruble(₽) | Bitcoin(₿) | Euro(€)
ExportFormat    = PDF | CSV | Excel
```

`UserRole` gates what a `Staff` member can do:
- **ADMIN** — full access, approves expenses, manages staff/allocations/budgets.
- **APPROVER** — can approve/reject expenses, cannot manage staff.
- **ENTRY_ONLY** — can create/edit their own expenses only, no approval rights.
- **VIEWER** — read-only.
- **PERSONAL** — the non-business, single-user mode; no staff/approval concepts apply.

## 5. Authentication

| Method | Path | Body | Notes |
|---|---|---|---|
| POST | `/auth/signup` | `{ name, email, phone?, password }` | creates `User`, returns access + refresh token |
| POST | `/auth/login` | `{ email, password }` | |
| POST | `/auth/login/otp/request` | `{ phone }` | sends OTP |
| POST | `/auth/login/otp/verify` | `{ phone, otp }` | returns tokens on success |
| POST | `/auth/refresh` | `{ refreshToken }` | rotates access token |
| POST | `/auth/logout` | `{ refreshToken }` | revokes it |
| POST | `/auth/password/forgot` | `{ email }` | sends reset OTP/link |
| POST | `/auth/password/reset` | `{ email, otp, newPassword }` | |
| GET | `/me` | — | current `User` + settings |
| PATCH | `/me` | partial `User`/settings fields | |
| PATCH | `/me/settings` | partial settings | theme/business-mode/premium/currency/etc. |

All other endpoints below require `Authorization: Bearer <accessToken>`.

## 6. Resource endpoints

Standard CRUD shape unless noted. `{id}` is the entity's numeric ID.

### Categories
- `GET /categories` — list (system + user's own)
- `POST /categories` — `{ name, description?, icon?, parentId? }`
- `PATCH /categories/{id}`
- `DELETE /categories/{id}` — reject if expenses/income reference it; client should reassign first

### Expenses
- `GET /expenses?filter=today|yesterday|last7days|all&groupBy=category|date&status=&userId=` — `userId` filter only honored for ADMIN/APPROVER/VIEWER roles; `ENTRY_ONLY`/`PERSONAL` always scoped to self
- `POST /expenses` — `{ title, amount, categoryId?, notes?, receiptUri?, timestamp, allocationId? }`
- `GET /expenses/{id}`
- `PATCH /expenses/{id}` — only the owner or ADMIN, and only while `status = Pending`
- `DELETE /expenses/{id}`
- `POST /expenses/{id}/approve` — ADMIN/APPROVER only; sets `status=Approved`, `approvedBy=<caller>`; increments the linked `Budget.usedAmount`/`Allocation.usedAmount`; fires an `expense_status` notification to the submitter
- `POST /expenses/{id}/reject` — same auth; `{ reason? }` folded into the notification message
- `GET /expenses/pending` — queue for ADMIN/APPROVER (mirrors `ExpenseDao.getPendingExpenses`)
- `GET /expenses/summary?from=&to=&userId=` — daily/category/monthly aggregates (mirrors `getMonthlyExpenseTrend`, `getCategoryWiseSpending`, `getTotalUsedByStaff`)

### Income
- `GET /income`, `POST /income`, `PATCH /income/{id}`, `DELETE /income/{id}`

### Budgets
- `GET /budgets` — includes computed `usedAmount`/`utilizationPercent`
- `POST /budgets` — ADMIN only — `{ name, periodType, periodStart, periodEnd, totalLimit }`
- `PATCH /budgets/{id}`, `DELETE /budgets/{id}`
- Server recomputes `usedAmount` on every expense approve/reject/delete that falls in the budget's period — never accept a client-supplied `usedAmount`.

### Allocations
- `GET /allocations?staffId=`
- `POST /allocations` — ADMIN only — `{ staffId, title, category, allocatedAmount, expiresAt? }`
- `PATCH /allocations/{id}`, `DELETE /allocations/{id}`
- `GET /allocations/{id}/usage` — utilization %, mirrors `AllocationDao.getAllocationUsageForStaff`

### Staff
- `GET /staff` — ADMIN/APPROVER
- `POST /staff` — ADMIN only — creates a `Staff` row + sends an invite (email/SMS with a signup link tied to the `employeeId`)
- `GET /staff/{id}`, `PATCH /staff/{id}`, `DELETE /staff/{id}` (soft delete → `isActive=false`, never hard-delete someone with expense history)
- `GET /staff/{id}/allocations` — mirrors `StaffWithAllocations`
- `GET /staff/summary` — mirrors `StaffExpenseSummary`/`StaffUsageSummary` (leaderboard data)

### Contacts (lending address book)
- `GET /contacts`, `POST /contacts`, `PATCH /contacts/{id}`, `DELETE /contacts/{id}`
- `POST /contacts/find-or-create` — `{ name, phone }` → returns existing contact by phone match or creates one (mirrors the client's `getOrCreateContactId` pattern used when logging a lending transaction inline)

### Lending & Repayments
- `GET /lending?contactId=&status=`
- `POST /lending` — `{ contactId, amount, transactionType, dueDate?, notes? }`
- `PATCH /lending/{id}`, `DELETE /lending/{id}`
- `GET /lending/{id}/repayments`
- `POST /lending/{id}/repayments` — `{ amountPaid, paymentMethod?, notes? }` — **transactional**: insert the repayment, sum all repayments for the lending record, then set `status`:
  - `totalRepaid <= 0` → `pending`
  - `0 < totalRepaid < lending.amount` → `partial`
  - `totalRepaid >= lending.amount` → `paid`
  (this exact rule is already implemented client-side in `LendingRepository.repayAndUpdateStatus` — keep server and client logic identical)

### Notifications
- `GET /notifications?unreadOnly=`
- `PATCH /notifications/{id}/read`
- `DELETE /notifications?olderThan=` — bulk cleanup (mirrors `clearOldNotifications`)

### Sync
- `POST /sync/push` — client sends its offline queue: `[{ entityType, entityId (client-local), action, payload, clientTimestamp }]`; server applies each, returns the server-assigned ID for inserts so the client can remap its local ID
- `GET /sync/pull?since=<serverTimestamp>` — returns everything changed since the given server timestamp, scoped to the caller's own data (and, for ADMIN, their staff's data)
- Conflict rule: **last-write-wins by `clientTimestamp`**, but an `Expense`/`Budget`/`Allocation` update is rejected (409) if the server-side record's `status` has moved to `Approved`/`Closed` since the client last pulled — surface this to the user as "this was already approved, refresh and retry" rather than silently overwriting.

### Subscriptions / Premium
- `GET /subscription/plans` — returns the 3 tiers (`basic` free, `monthly`, `yearly`) with price + feature list
- `POST /subscription/checkout` — `{ plan }` → integrates with a payment provider (Razorpay/Stripe depending on target market — the app's `PaymentMode` enum includes UPI, which points to Razorpay/Cashfree as the natural fit for an India-focused app); returns a checkout session
- `POST /subscription/webhook` — payment-provider webhook → on success, sets `user_settings.premiumType`
- `GET /subscription/status`

## 7. Sample payloads

**Login response**
```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "8f2c1e...",
  "user": {
    "userId": 42,
    "name": "Abhishek Chandra",
    "email": "ac927920@gmail.com",
    "role": "admin",
    "settings": {
      "themeMode": "light",
      "businessMode": "business",
      "premiumType": "monthly",
      "currency": "Rupee"
    }
  }
}
```

**Create expense**
```json
POST /expenses
{
  "title": "Client lunch",
  "amount": 850.00,
  "categoryId": 3,
  "notes": "Lunch with vendor",
  "timestamp": 1735459200000
}
```
```json
201 Created
{
  "expenseId": 501,
  "status": "Pending",
  "userId": 42,
  "categoryId": 3,
  "title": "Client lunch",
  "amount": 850.00,
  "timestamp": 1735459200000,
  "approvedBy": null
}
```

**Repay a lending record**
```json
POST /lending/17/repayments
{ "amountPaid": 2000.00, "paymentMethod": "UPI" }
```
```json
201 Created
{
  "repaymentId": 88,
  "lendingId": 17,
  "amountPaid": 2000.00,
  "lendingStatus": "partial",
  "totalRepaid": 6000.00,
  "amountRemaining": 4000.00
}
```

## 8. Notes for whoever builds this

- This spec deliberately mirrors the existing Room schema field-for-field so
  the Android client needs minimal changes to adopt it (mostly: replace the
  commented-out repository calls with real Ktor HTTP calls, and swap
  `ExpenseDM.id`/etc. from purely-local Longs to server-confirmed IDs).
- Three schema gaps flagged during an earlier review of the client DB are
  called out inline above (§3.7 Allocation FK, §3.9 Lending cascade rule) —
  fix them in the backend schema even though the client's local Room DB
  still has them, so the server doesn't inherit the same bugs.
- None of this is implemented yet — the Ktor client, `NetworkModule`, and
  `SyncLogEntity` are scaffolding only. Building the actual backend service
  (in whatever language/framework you choose) and wiring the Android
  repositories to call it is a separate, follow-on piece of work from this
  spec.
