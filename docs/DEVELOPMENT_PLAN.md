# Spendly Development Plan

This ties together three things already produced for this project:
`docs/BACKEND_API_SPEC.md` (the data model and API surface to build),
`docs/MARKET_RESEARCH.md` (what actually needs to be true for this to be a
paying business), and the current state of the Android client (a local-only
app with a freshly redesigned UI, 6 critical bugs fixed this session, but no
real backend, no real payments, and zero automated tests).

The plan is phased by **dependency and risk**, not by calendar time — I
don't know your available hours/week, so treat each phase as "the next
thing to build," and re-sequence Phase 4/5 earlier if a real customer
conversation demands it sooner.

## Phase 0 — Harden the foundation (before adding a backend on top of it)

Adding a backend and real users on top of known cracks makes every future
bug harder to isolate ("is this a client bug or a sync bug?"). Fix these
first — all are small, all are already scoped:

- **Room schema fixes**: add the missing `Allocation.staffId` FK/index,
  fix the `LendingTransaction → Contact` cascade (should be `RESTRICT`, not
  `CASCADE`, or a real ledger disappears when someone deletes a contact),
  add FK/indices on `Contact.parentId`, `Category.parentId`,
  `Staff.managerId`, `Budget.createdBy`. This needs a real `Migration(4,5)`
  this time (schema history for 1→4 was already lost, but the discipline
  starts now) — the `fallbackToDestructiveMigrationFrom(1,2,3)` policy set
  up this session already scopes destructive fallback to just the
  unrecoverable old versions, so this one bump needs a proper migration.
- **First real tests.** Currently 0 real unit tests exist across 137+
  Kotlin files. Before wiring a backend, add unit tests for the
  ViewModels/repositories that are about to get touched most —
  `ExpenseViewModel`, `AuthViewModel`, the lending repayment-status logic in
  `LendingRepository` — so backend integration has a safety net, not a
  blind rewrite.
- **Build/release hygiene**: enable `isMinifyEnabled`/`isShrinkResources`
  for release, add a real release signing config (currently release builds
  use debug signing), remove the committed `app-debug.apk` from git
  history, gate Ktor's `Logging` plugin to `LogLevel.NONE` in release
  builds (currently unconditional `LogLevel.BODY` — a real leak risk once
  requests carry auth tokens), and set `android:allowBackup="false"` (or
  populate the currently-empty backup/data-extraction rules) so a user's
  financial DB doesn't get backed up unencrypted.
- **Move `authToken` out of plaintext `SharedPreferences`** into
  `EncryptedSharedPreferences` — trivial swap, but do it before there's a
  real token worth stealing.

## Phase 1 — Build the backend, wire the client to it

This is where `docs/BACKEND_API_SPEC.md` gets implemented.

1. Stand up the service (suggested: Postgres + JWT auth, per the spec) with
   just **auth + Expense CRUD + Category** first — the smallest vertical
   slice that makes the app "real" (a signup that persists, a login that
   works, one expense type round-tripping through a server).
2. Wire the Android side: replace the commented-out
   `// TODO: viewModel?.submitEmailLogin(...)` stubs in `AuthViewModel`/
   `LoginScreen` with real Ktor calls against `/auth/*`; do the same for
   `ExpenseViewModel.addExpense/editExpense/deleteExpense` against
   `/expenses`.
3. Decide the offline story explicitly: keep Room as an **offline cache**
   (write-through to server when online, queue in `SyncLog` when not,
   drain via `/sync/push` + `/sync/pull` as spec'd) rather than trying to
   make server calls block the UI. This is the harder-but-correct choice
   given the app's "offline-first" framing in its own README.
4. Once that slice works end-to-end, extend to the rest of the spec in this
   order (roughly matching how tightly each depends on the last):
   Income → Budgets/Allocations → Staff (needed before approval workflow
   makes sense) → expense approve/reject → Contacts → Lending/Repayments →
   Notifications.
5. Skip building `/subscription/*` in this phase — that's Phase 2, once
   there's something worth subscribing to.

**Don't** try to migrate all 12 entities to the server in one pass — each
vertical slice (entity + its screens + its tests) should ship and be
usable before starting the next.

## Phase 2 — Payments and premium gating

Per the market research: **gate business-mode staff-approval, budgets, and
reporting/export behind premium — keep personal tracking and the lending
ledger free.** Neither the free incumbents (Money View, Khatabook) nor your
own target segment give you room to charge for those two; the segment that
actually pays (Zoho Expense/Fyle-style buyers) pays for approval workflows
and audit visibility, not for a personal tracker or an udhar book.

1. Integrate an India-based payment aggregator — Razorpay or Cashfree —
   both handle RBI-mandated data localization for you, so you don't have to
   solve that yourself.
2. Wire `/subscription/checkout` + webhook (per the backend spec) and
   replace the currently no-op "Buy Premium" button with a real checkout
   flow.
3. Add server-side + client-side gating: business-mode approval workflow,
   multi-staff allocations, and report export all check
   `user_settings.premiumType != BASIC` before allowing access.
4. Price starting closer to the ~$6.68/month median found in research than
   the "safe-feeling" $9.99, and strongly consider a longer free trial
   (17-32 days converts far better than a short one) over a hard feature
   wall, given fintech's low ~4% freemium conversion baseline.

## Phase 3 — The trust features that make people actually pay

Two concrete gaps the market research flagged as missing versus what buyers
of this category expect:

1. **Configurable spending policies** — today approval is just role-gated
   approve/reject; add per-category or per-staff spending limits an admin
   can set on an `Allocation`, with the approval flow flagging (not
   necessarily blocking) anything over the limit.
2. **A visible, exportable audit trail** — the data already exists
   (`SyncLog`, `approvedBy`/status changes on `Expense`), it's just not
   surfaced. Add a simple "Activity" view per expense/staff member and a
   CSV export (the app's `ExportUtils`/CSV logic already exists for
   expenses generally — extend it to an audit log).

Both are what turns "an indie app I'm trying out" into "a tool I trust with
my staff's expense approvals" for the buyer this pricing model depends on.

## Phase 4 — Compliance, done as architecture not a checklist

- DPDP Act consent language and data-subject-request handling at signup —
  enforcement doesn't start until May 2027, but retrofitting consent flows
  onto an existing user base later is far more expensive than building them
  into the signup flow now.
- Confirm the chosen payment aggregator's compliance covers the RBI 2018
  payment-data-localization mandate (both Razorpay and Cashfree do) rather
  than trying to self-host payment data.
- Basic security pass before any real user data hits the new backend:
  password hashing (bcrypt/argon2, never plaintext — obvious, but worth
  stating since the client currently has no real auth to check against),
  HTTPS-only, rate-limiting on `/auth/*`.

## Phase 5 — Go-to-market (runs in parallel with Phases 1-3, not after)

This doesn't block engineering and shouldn't wait for it:

- Validate the "one app instead of three" pitch with a handful of real
  target users **before** finalizing pricing — the research flagged this as
  a real risk (switching-cost objection), not a settled assumption.
- Resolve who the actual buyer is: price-sensitive shopkeepers (khata-app
  buying pattern) vs. small service/professional businesses (closer to the
  Zoho Expense buyer) — the two need different pricing and different
  marketing, and this wasn't resolved by the research pass.
- Start SEO content now, even pre-backend — content has lead time, and the
  research shows SEO + niche community presence (India-specific
  indie-hacker/SaaS groups, small-business communities) outperforming paid
  acquisition for a solo-founder budget in this category.

## Suggested next action

Given everything above, the highest-leverage single next step is **Phase 0's
Room migration + FK fixes, done alongside the first Phase 1 vertical slice
(auth + Expense CRUD)** — they touch the same files, so doing them together
avoids a second pass through the same code. Say the word and I'll turn that
into an actual implementation plan (via plan mode, since it's real
architecture work) rather than just this roadmap.
