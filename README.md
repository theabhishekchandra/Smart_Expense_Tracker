# Smart Daily Expense Tracker

**Module:** Full-featured Expense Tracker for Small Business Owners  
**Built With:** Kotlin, Jetpack Compose, MVVM, StateFlow  

---

## **Project Overview**
Smart Daily Expense Tracker helps small business owners track daily expenses efficiently. Users can view today’s expenses by default, filter for previous dates, group expenses by time or category, and get a comprehensive overview of total spending. The report screen provides visual summaries with daily and category-wise totals for the last 7 days.

---

## **Features Implemented**

### **1. Expense Entry Screen**
- Input fields:
  - Title (required)
  - Amount (₹, required)
  - Category (Staff, Travel, Food, Utility)
  - Optional Notes (max 100 chars)
  - Optional Receipt Image (upload or mock)
- Submit button:
  - Adds expense to list
  - Shows Toast notification
  - Animates new entry
- Real-time display of **Total Spent Today**

### **2. Expense List Screen**
- View expenses for **Today** (default)
- Filter previous dates via a **dropdown filter**
- Toggle grouping by **Category** or **Time**
- Display:
  - Total count of expenses
  - Total amount spent
  - Empty state and loading spinner

### **3. Expense Report Screen**
- Report for last 7 days
  - Daily totals
  - Category-wise totals
  - Bar and line chart visualization (mocked)

### **4. State Management & Data Layer**
- ViewModel + StateFlow for reactive UI
- In-memory repository with Room support
- Screen transitions handled via Navigation

### **5. Bonus Features Implemented**
- Duplicate detection (prevents adding identical expense)
- Validation:
  - Amount > 0
  - Title non-empty
- Reusable UI components for cards, FABs, and top bars
- Loading spinner for async operations
- Theme switcher (Light/Dark)
- Offline-first sync (mock)
- Export to PDF/CSV
- Share intent

---

## **AI Usage Summary**
AI tools like **ChatGPT** and **Gemini** were leveraged to accelerate development. AI assisted in:
- Generating boilerplate MVVM code (ViewModel, Repository, UI classes)
- Designing UI layouts and reusable Compose components
- Implementing filtering, grouping, and date selection logic
- Handling validation, duplicate detection, and state management
- Improving UX and optimizing Compose performance through iterative prompts

---

## **Setup Instructions**
1. Clone or download the repository
```bash
git clone <your-repo-link>
