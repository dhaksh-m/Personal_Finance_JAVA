# Personal Finance & Expense Management System

A simple Java Swing desktop application to record income and daily expenses,
track total spending, remaining balance, monthly budget, and view/search/remove transactions.

Built as a Java lab project using OOP, `ArrayList`, event handling, exception handling,
`LocalDate`, and CSV file storage. No database required.

## Features

- **Income Management:** Add income with source, amount, date. View total income.
- **Expense Management:** Add expense with description, amount, category, date. View total expenses.
- **Categories:** Food, Travel, Shopping, Education, Bills, Entertainment, Other
- **Budget:** Set monthly budget, view remaining budget, red warning when exceeded.
- **Transactions:** View all in a table, search by keyword (description/category/date/id/type), remove by ID, auto balance calculation.
- **Persistence:** Auto-saves to `transactions.csv` and `budget.txt` in the run directory.

### Calculations
```
Total Income     = Sum of all INCOME transactions
Total Expenses   = Sum of all EXPENSE transactions
Balance          = Total Income - Total Expenses
Remaining Budget = Monthly Budget - Total Expenses
```

## Project Structure

```
PersonalFinance/
├── src/
│   └── com/finance/
│       ├── Main.java              # Entry point
│       ├── Transaction.java       # Abstract base class
│       ├── Income.java            # Extends Transaction
│       ├── Expense.java           # Extends Transaction
│       ├── Budget.java            # Monthly budget logic
│       ├── FinanceManager.java    # ArrayList storage, calculations, CSV I/O
│       ├── MainFrame.java         # Dashboard GUI
│       ├── AddIncomeFrame.java    # Add income form
│       ├── AddExpenseFrame.java   # Add expense form
│       └── TransactionFrame.java  # Table view + search + delete
├── README.md
├── run.bat                        # Windows one-click compile & run
├── run.sh                         # Linux/Mac compile & run
├── transactions.csv               # Auto-created at runtime
└── budget.txt                     # Auto-created at runtime
```

## Requirements

- **JDK 17 or higher** ([Download](https://adoptium.net/))
- Any OS: Windows / Linux / Mac
- Optional IDE: Eclipse, IntelliJ IDEA, NetBeans, VS Code

Check Java:
```bash
java -version
javac -version
```

## How to Run

### Option 1 — Windows (easiest, double-click)
1. Install JDK 17+ and ensure `java` is on PATH.
2. Double-click **`run.bat`**. It compiles and launches the app.

Or from terminal in the project folder:
```bat
run.bat
```

### Option 2 — Windows / Linux / Mac (manual commands)
From the `PersonalFinance` folder:

```bash
# 1. Compile (creates 'out' folder with .class files)
javac -d out src/com/finance/*.java

# 2. Run
java -cp out com.finance.Main
```

### Option 3 — Linux / Mac script
```bash
chmod +x run.sh
./run.sh
```

### Option 4 — VS Code
1. Open the `PersonalFinance` folder in VS Code.
2. Install **Extension Pack for Java**.
3. Open `src/com/finance/Main.java` → click **Run** ▶ above `main()`.

### Option 5 — Eclipse
1. `File → New → Java Project` → name `PersonalFinance`.
2. Copy `src/com/finance/*.java` into `src/com/finance/`.
3. Right-click `Main.java` → `Run As → Java Application`.

### Option 6 — IntelliJ IDEA
1. `File → Open` → select `PersonalFinance` folder.
2. Mark `src` as Sources Root (right-click → `Mark Directory as`).
3. Open `Main.java` → click green ▶ **Run**.

## How to Use

1. **Dashboard** shows Total Income, Total Expenses, Balance, Budget, Budget Status.
2. Click **Add Income** → enter Source (e.g. Part-time Work), Amount, Date → Add.
3. Click **Add Expense** → enter Description, Amount, Category, Date → Add.
4. Click **Set Budget** → enter monthly budget (e.g. 4000).
5. Click **Transactions** → view all, search, select a row → Remove Selected.
6. Click **Search** → enter keyword → filtered table opens.
7. **Exit** saves data. Re-launch reloads `transactions.csv` + `budget.txt`.

Date formats accepted: `YYYY-MM-DD` (e.g. `2026-10-01`) or `DD/MM/YYYY` (e.g. `01/10/2026`).

## Example

```
Income:
Part-time Work = Rs.10,000

Expenses:
Food      = Rs.1,500
Travel    = Rs.1,000
Shopping  = Rs.2,000
Education = Rs.500
```
App shows:
```
Total Income:   Rs.10,000
Total Expenses: Rs.5,000
Balance:        Rs.5,000
Budget:         Rs.4,000 → Status: Budget Exceeded (red warning)
```

## Java Concepts Used

Classes, Objects, Encapsulation, Constructors, Methods, Inheritance (`Income`/`Expense` extend `Transaction`),
`ArrayList`, Exception Handling (`NumberFormatException`, `DateTimeParseException`), Java Swing,
Event Handling (`ActionListener`), `LocalDate`, File Handling (`BufferedReader`/`BufferedWriter`, CSV).

## Troubleshooting

| Problem | Fix |
|---|---|
| `'javac' is not recognized` | Install JDK 17+, restart terminal, check PATH. On Windows set `JAVA_HOME`. |
| `Could not find or load main class` | Run from project folder, use `java -cp out com.finance.Main` (not `java Main`). |
| GUI doesn't open / HeadlessException | Run on a machine with display. Don't run with `-Djava.awt.headless=true`. |
| Data not saving | Ensure app has write permission in its folder; don't run from a zip without extracting. |
| Date error | Use `YYYY-MM-DD` e.g. `2026-10-01`. |

## Future Enhancements

MySQL database, monthly charts, CSV export, login, recurring expenses, PDF reports, mobile version.
