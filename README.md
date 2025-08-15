![Screenshot_2025-08-15-19-34-35-89_c307508fac67777b1683d5a68fd1917e](https://github.com/user-attachments/assets/b2326920-46f9-48e5-9f16-be89103c5789)# Smart Daily Expense Tracker
**By: Abhishek Chandra**

---

## App Overview
The Smart Daily Expense Tracker helps small business owners manage daily expenses efficiently.  
Users can:
- Add expenses with title, amount, category, optional notes, and optional receipt images.  
- View expenses for today, previous days, or last 7 days.  
- Group expenses by category or date.  
- Generate reports with daily totals, category totals, and bar charts.  
- Export and share expense data as CSV (simulated PDF support).  

This app is built with **Jetpack Compose**, **MVVM architecture**, and **StateFlow** for modern Android best practices.

---

## AI Usage Summary
AI tools were leveraged extensively to accelerate development:  
- **ChatGPT**: Generated Jetpack Compose layouts, reusable components, ViewModel logic, and report aggregation functions.  
- **GitHub Copilot**: Assisted in writing boilerplate code, repository functions, and CSV export logic.  
- **AI-assisted UX & Validation**: Suggested input validation, duplicate detection, and performance improvements.  

---

## Prompt Logs
| Feature | AI Prompt | Result / Notes |
|---------|-----------|----------------|
| Expense Entry Screen | "Generate Jetpack Compose input fields for title, amount, category dropdown, notes, receipt image, submit button" | Created UI with validation and animated submit |
| Last 7 Days Totals | "Write Kotlin function to calculate last 7 days expenses using StateFlow" | Implemented in ViewModel |
| CSV Export | "Generate CSV export function for list of Expense data class" | Works with share intent |
| Report Screen | "Create Jetpack Compose report screen with daily totals, category totals, and bar chart" | Implemented LazyColumn-based report |
| Duplicate Detection | "Add duplicate expense detection before inserting into repository" | Added in addExpense() function |

(Full prompt logs are available in `Prompt_Logs.md`)

---

## Feature Checklist

- [x] Expense Entry Screen: Title, Amount, Category, Notes, Receipt Image  
- [x] Total Spent Today displayed on top  
- [x] Expense List Screen: Filter by Today, Yesterday, Last 7 Days, All  
- [x] Toggle grouping by category or date  
- [x] Report Screen: Daily totals, Category totals, Bar chart  
- [x] Export and Share CSV (simulated PDF)  
- [x] Duplicate detection on adding expenses  
- [x] Input validation (amount > 0, title non-empty)  
- [x] MVVM Architecture with StateFlow  
- [x] Jetpack Compose UI  
- [x] Offline-first mock support  

---

## APK
Download and install the app from:  
[Smart Daily Expense Tracker APK](APK/SmartDailyExpenseTracker.apk)  

---

## Screenshots

**Expense Entry Screen**  
![Expense Entry](Screenshots/ExpenseEntry.png)

**Expense List Screen**  
![Expense List](Screenshots/ExpenseList.png)

**Report Screen (Last 7 Days)**  
![Report Screen](Screenshots/ExpenseReport.png)

**Export / Share CSV**  
![Export Share](Screenshots/ExportShare.png)

---

## Source Code
Full source code is available in the `Source_Code/` folder.  
- **Jetpack Compose UI**  
- **MVVM architecture**  
- **Clean & modular project structure**  

---

## Resume
Abhishek Chandra’s latest resume is included as `Resume.pdf` in the root folder.

---

**Note:**  
All AI usage has been documented honestly. The project demonstrates AI-assisted Android development, combining human coding expertise with AI support for efficiency and quality.
