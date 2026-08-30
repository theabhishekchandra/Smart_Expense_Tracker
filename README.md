# Spendly

**By Abhishek Chandra**

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-757575?style=flat-square&logo=materialdesign&logoColor=white)](https://m3.material.io)
[![License: MIT](https://img.shields.io/badge/License-MIT-3DDC84?style=flat-square)](LICENSE)
[![Last commit](https://img.shields.io/github/last-commit/theabhishekchandra/Spendly?style=flat-square&color=3DDC84)](https://github.com/theabhishekchandra/Spendly/commits/master)

> Personal spending, staff expense approvals, and an informal lending ledger — one native Android app instead of three separate ones.

**Contents:** [The problem](#the-problem) · [Features](#features) · [Screenshots](#screenshots) · [Tech stack](#tech-stack) · [Project structure](#project-structure) · [Getting started](#getting-started) · [Documentation](#documentation) · [Roadmap](#roadmap) · [License](#license)

---

## The problem

A solo professional or small business owner in India who wants to actually
track money ends up juggling three unrelated apps: a personal tracker like
Money View or Axio for their own spending, a khata/udhar app like Khatabook
or OkCredit to remember who owes them what, and — if they have even one or
two staff submitting expenses — something like Zoho Expense or Fyle just to
approve a reimbursement. None of those apps talk to each other, and none of
them is built for someone who needs all three at once.

Spendly is that combination in one app:

| | Personal expense tracking | Staff expense approval | Informal lending ledger |
|---|:---:|:---:|:---:|
| Money View / Axio | ✅ | | |
| Zoho Expense / Fyle / Happay | | ✅ | |
| Khatabook / OkCredit | | | ✅ |
| **Spendly** | ✅ | ✅ | ✅ |

It isn't trying to out-feature any single one of those category leaders —
it's for the owner-operator who'd rather have one decent app that does all
three than three great apps that don't.

### Who it's for

- **Individuals** who want a clean, private, on-device expense tracker without SMS auto-parsing or ads
- **Small business owners** with a handful of staff who need to submit and approve expenses, without paying for full enterprise expense software
- **Anyone who lends or borrows informally** — with friends, family, or contacts — and wants that tracked separately from regular spending, with reminders

---

## Features

### Personal expense tracking

- Add an expense with title, amount, category, notes, and an optional
  receipt photo picked from the gallery
- Expense list filterable by today, yesterday, last 7 days, or all time, and
  groupable by category or date
- A dedicated expense detail view for any past entry
- Budgets per category, with a progress bar and an alert once you're near or
  over the limit
- Home dashboard: today's spend hero card, income vs. expense for the month,
  a weekly spending trend chart, category breakdown, recent transactions,
  and AI-style tips/suggestions

### Business mode

Toggle Personal/Business from Settings — the whole app (color scheme,
dashboard layout, navigation) adapts, no separate install or account.

- **Staff management** — add, edit, and remove staff; assign one of four
  roles (Admin, Approver, Entry-only, Viewer); tap any staff member to open
  their profile
- **Staff dashboard** — the view a staff member sees: submit an expense
  against a budget an admin allocated to them, and see their own pending
  approvals
- **Pending approvals** — an admin/approver queue with approve/reject
  (optionally with a rejection note), reachable from Home or from the queue
  itself, with a link through to the processed history
- **Processed expenses** — a filterable (by staff, by category) history of
  everything already approved or rejected
- **Staff profile** — a per-staff hero card (avatar, role badge, total
  expenses logged, pending count) with quick actions to add an expense or
  jump to that staff's own contribution history
- **Staff reports** — total staff spend plus a full leaderboard of every
  staff member sorted by spend
- **Home business dashboard** — today's spend, income vs. expense, weekly
  trend, a pending-approvals widget with inline approve/reject, a staff
  leaderboard with a "Manage" shortcut into Staff Management, staff
  performance, and outstanding dues

### Lending & borrowing (the udhar ledger)

A ledger for informal money given to or taken from people you know, kept
completely separate from regular expenses.

- Add a record: name, mobile number, amount, a Given/Taken toggle, a due
  date, and notes
- List view with search-by-name, filter by status (Pending / Paid /
  Overdue), and swipe-to-edit or swipe-to-delete
- Detail view: a total-amount hero card, status, transaction history, a
  Mark as Paid action, an Edit action, and Send Reminder — which opens a
  pre-filled SMS to that contact
- Surfaced on Home via the Outstanding Dues card and an "Add Lender" quick
  action

### Reports & subscription

- A dedicated Reports screen with category breakdowns and a spending trend
  chart
- Three tiers:

  | Plan | Price | What's included |
  |---|---|---|
  | Free | ₹0 | Manual tracking, basic categories, monthly summary report |
  | Premium Monthly | ₹199/month | + AI-powered insights, unlimited categories, multi-device sync, Excel/CSV/PDF export, staff/team expense tracking |
  | Premium Yearly | ₹1,999/year (~20% off monthly) | + priority support, early access to new features |

### Profile & account

- Profile screen showing your avatar, name, and email (kept in sync with
  Settings), a premium status banner, and a business details card
- Edit Profile — name, email, phone, date of birth, gender, currency, and a
  real photo picker for the avatar
- Edit Business Details — business name, owner name, business type, email,
  phone, currency, and a photo picker for the logo

### Settings

- Language (English or Hindi) and currency (7 supported: Rupee, Dollar,
  Pound, Yen, Ruble, Bitcoin, Euro)
- Dark mode — a real dedicated dark palette per app flavor, not an inverted
  light theme — and the Personal/Business mode toggle
- Export format (PDF, CSV, or Excel) and a cloud sync target (Google Drive,
  OneDrive, or App Drive) with a sync frequency
- Change password and biometric login
- Support: chat support, email support (opens a real email intent), and an
  expandable FAQ
- About: Privacy Policy, Terms & Conditions, About App, and Share App (a
  real share-sheet intent)

### Onboarding & authentication

- Animated splash screen and a 4-page onboarding carousel with
  illustrations
- Login by email/password or phone/OTP, with a "Remember me" checkbox and a
  forgot-password flow
- Signup with name, mobile, email, and password/confirm
- Forgot password → reset password → create new password flow

---

## Screenshots

**Onboarding & login**

| <img src="Screenshots/onboarding.png" width="200"/> | <img src="Screenshots/login.png" width="200"/> | <img src="Screenshots/signup.png" width="200"/> |
|:---:|:---:|:---:|
| Onboarding | Login | Sign up |

**Personal mode**

| <img src="Screenshots/home.png" width="200"/> | <img src="Screenshots/add-expense.png" width="200"/> | <img src="Screenshots/expense-list.png" width="200"/> | <img src="Screenshots/budgets.png" width="200"/> |
|:---:|:---:|:---:|:---:|
| Home dashboard | Add expense | Expense list | Budgets |

| <img src="Screenshots/reports.png" width="200"/> | <img src="Screenshots/profile.png" width="200"/> | <img src="Screenshots/settings.png" width="200"/> | <img src="Screenshots/subscription.png" width="200"/> |
|:---:|:---:|:---:|:---:|
| Reports | Profile | Settings | Subscription |

**Business mode & staff**

| <img src="Screenshots/home-business.png" width="200"/> | <img src="Screenshots/home-business-staff-leaderboard.png" width="200"/> | <img src="Screenshots/home-business-outstanding-dues.png" width="200"/> | <img src="Screenshots/pending-approvals.png" width="200"/> |
|:---:|:---:|:---:|:---:|
| Business home | Staff leaderboard | Outstanding dues | Pending approvals |

| <img src="Screenshots/staff-management.png" width="200"/> | <img src="Screenshots/staff-dashboard.png" width="200"/> | <img src="Screenshots/staff-profile.png" width="200"/> | <img src="Screenshots/staff-reports.png" width="200"/> |
|:---:|:---:|:---:|:---:|
| Staff management | Staff dashboard | Staff profile | Staff reports |

**Lending ledger**

| <img src="Screenshots/lender-list.png" width="200"/> | <img src="Screenshots/lender-details.png" width="200"/> | <img src="Screenshots/edit-lender.png" width="200"/> |
|:---:|:---:|:---:|
| Lender list | Lender details | Edit lender |

---

## Tech stack

- **UI:** Jetpack Compose, Material 3 — a custom theme system with
  per-flavor color schemes, a brand gradient that adapts correctly between
  light and dark mode, shared shape/spacing tokens, and a small motion
  library for screen transitions and micro-interactions
- **Architecture:** MVVM with `StateFlow`, Hilt for dependency injection
- **Persistence:** Room (local database), DataStore (preferences)
- **Networking:** Ktor client (scaffolded for a future backend — see
  [docs/BACKEND_API_SPEC.md](docs/BACKEND_API_SPEC.md); the app is
  local-only today)
- **Async:** Kotlin Coroutines

## Project structure

```
app/src/main/java/com/abhishek/spendly/
├── core/            # DI modules, navigation graphs, DataStore, utils
├── data/            # Room entities/DAOs, repositories, domain models
└── ui/
    ├── components/  # Shared composables (buttons, cards, top bars, theme-aware widgets)
    ├── screens/     # One package per feature area (expense, home, staff, lender, ...)
    └── theme/       # Color schemes, typography, shapes, spacing, gradients, motion
```

---

## Getting started

Requires Android Studio (or the command line) with JDK 17–21. **Note:** if
your default JDK is newer than 23, Gradle 8.13 will fail to build — either
switch your `JAVA_HOME` to a JDK in that range or use Android Studio's
bundled JBR and confirm the project's Gradle JDK setting (`File > Project
Structure > SDK Location`, or `.idea/gradle.xml`) points to it.

```bash
git clone https://github.com/theabhishekchandra/Spendly.git
cd Spendly
./gradlew assembleDebug     # build a debug APK
./gradlew installDebug      # build and install on a connected device/emulator
./gradlew build             # full build: both variants, unit tests, lint
```

| Requirement | Value |
|---|---|
| Min SDK | 24 (Android 7.0) |
| Target / compile SDK | 36 |
| Build tool | Gradle 8.13 |

---

## Documentation

- [docs/BACKEND_API_SPEC.md](docs/BACKEND_API_SPEC.md) — data model and REST
  API spec for the backend this app doesn't have yet
- [docs/MARKET_RESEARCH.md](docs/MARKET_RESEARCH.md) — the competitive
  landscape and monetization research behind the positioning above
- [docs/DEVELOPMENT_PLAN.md](docs/DEVELOPMENT_PLAN.md) — phased plan for
  turning the above into a shipped product

## Roadmap

Phased by dependency and risk, not calendar time — full detail in
[docs/DEVELOPMENT_PLAN.md](docs/DEVELOPMENT_PLAN.md).

- [ ] **Phase 0** — Harden the foundation: Room schema/FK/index cleanup, real unit tests, build hygiene
- [ ] **Phase 1** — Build the backend and wire the client to it
- [ ] **Phase 2** — Payments and premium gating
- [ ] **Phase 3** — Trust features that make people actually pay: configurable spending limits/approval policies and a visible, exportable audit log
- [ ] **Phase 4** — Compliance, designed as architecture rather than a checklist (RBI data-localization, DPDP consent)
- [ ] **Phase 5** — Go-to-market, run in parallel with Phases 1–3

---

## License

MIT — see [LICENSE](LICENSE).
