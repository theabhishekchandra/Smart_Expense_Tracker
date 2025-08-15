# Smart Daily Expense Tracker
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

---

## AI Usage Summary
AI tools were leveraged extensively to accelerate development and improve code quality:

**ChatGPT**:  
1. Generated **Jetpack Compose UI layouts**, reusable components, and screen structures.  
2. Assisted in writing **ViewModel logic**, repository functions, and **CSV export / data aggregation** functions.  
3. Suggested **input validation**, **duplicate detection**, and **performance optimizations** for smooth app behavior.  
 
---

## Prompt Logs
| Feature | AI Prompt | Result / Notes |
|---------|-----------|----------------|
| Expense Entry Screen | "Generate Jetpack Compose input fields for title, amount, category dropdown, notes, receipt image, submit button" | Created UI with validation and animated submit |
| Last 7 Days Totals | "Write Kotlin function to calculate last 7 days expenses using StateFlow" | Implemented in ViewModel |
| CSV Export | "Generate CSV export function for list of Expense data class" | Works with share intent |
| Report Screen | "Create Jetpack Compose report screen with daily totals, category totals, and bar chart" | Implemented LazyColumn-based report |
| Duplicate Detection | "Add duplicate expense detection before inserting into repository" | Added in addExpense() function |


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
[Smart Daily Expense Tracker APK](https://github.com/theabhishekchandra/Smart_Expense_Tracker/blob/master/APK/app-debug.apk)  

---

## Screenshots

**Expense Entry Screen**  
<img src="https://github.com/theabhishekchandra/Smart_Expense_Tracker/blob/master/Screenshots/Add%20expense%20without%20data.jpg" width="400"/>

**Expense List Screen**  
<img src="https://github.com/theabhishekchandra/Smart_Expense_Tracker/blob/master/Screenshots/Expense%20With%20data%20filter.jpg" width="400"/>

**Report Screen (Last 7 Days)**  
<img src="https://github.com/theabhishekchandra/Smart_Expense_Tracker/blob/master/Screenshots/Last%207%20Days%20Report.jpg" width="400"/>

**Export / Share CSV**  
<img src="https://github.com/theabhishekchandra/Smart_Expense_Tracker/blob/master/Screenshots/Share%20last%207%20day%20report.jpg" width="400"/>

---

## Source Code
Full source code is available in the `Source_Code/` folder.  
- **Jetpack Compose UI**  
- **MVVM architecture**  
- **Clean & modular project structure**  

---

**Note:**  
All AI usage has been documented honestly. The project demonstrates AI-assisted Android development, combining human coding expertise with AI support for efficiency and quality.
