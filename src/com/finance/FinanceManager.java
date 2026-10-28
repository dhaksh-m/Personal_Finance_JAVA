package com.finance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Central business-logic class.
 * Responsible for adding/removing transactions, calculations and file storage.
 *
 * Calculations:
 *   Total Income   = Sum of all INCOME transactions
 *   Total Expenses = Sum of all EXPENSE transactions
 *   Balance        = Total Income - Total Expenses
 *   Remaining      = Monthly Budget - Total Expenses
 */
public class FinanceManager {
    private ArrayList<Transaction> transactions;
    private Budget budget;
    private int nextId;

    private static final String DATA_FILE = "transactions.csv";
    private static final String BUDGET_FILE = "budget.txt";

    public FinanceManager() {
        transactions = new ArrayList<>();
        budget = new Budget();
        nextId = 1;
        loadFromFile();
    }

    // ---------- CRUD ----------

    public synchronized int getNextId() {
        return nextId++;
    }

    public synchronized void addTransaction(Transaction t) {
        transactions.add(t);
        if (t.getTransactionId() >= nextId) {
            nextId = t.getTransactionId() + 1;
        }
        saveToFile();
    }

    public synchronized boolean removeTransactionById(int id) {
        boolean removed = transactions.removeIf(t -> t.getTransactionId() == id);
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    public Transaction getTransactionById(int id) {
        for (Transaction t : transactions) {
            if (t.getTransactionId() == id) {
                return t;
            }
        }
        return null;
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Search by description, category, type, date or id (case-insensitive).
     */
    public List<Transaction> searchTransactions(String keyword) {
        List<Transaction> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTransactions();
        }
        String key = keyword.trim().toLowerCase();
        for (Transaction t : transactions) {
            if (t.getDescription().toLowerCase().contains(key)
                    || t.getCategory().toLowerCase().contains(key)
                    || t.getType().toLowerCase().contains(key)
                    || t.getDate().toString().contains(key)
                    || String.valueOf(t.getTransactionId()).equals(key)) {
                result.add(t);
            }
        }
        return result;
    }

    // ---------- Calculations ----------

    public double getTotalIncome() {
        double sum = 0;
        for (Transaction t : transactions) {
            if (t.getType().equals("INCOME")) {
                sum += t.getAmount();
            }
        }
        return sum;
    }

    public double getTotalExpenses() {
        double sum = 0;
        for (Transaction t : transactions) {
            if (t.getType().equals("EXPENSE")) {
                sum += t.getAmount();
            }
        }
        return sum;
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    // ---------- Budget ----------

    public double getMonthlyBudget() {
        return budget.getMonthlyBudget();
    }

    public void setMonthlyBudget(double amount) {
        budget.setMonthlyBudget(amount);
        saveToFile();
    }

    public double getRemainingBudget() {
        return budget.getRemaining(getTotalExpenses());
    }

    public boolean isBudgetExceeded() {
        return budget.isExceeded(getTotalExpenses());
    }

    // ---------- File storage (CSV + txt) ----------

    public synchronized void saveToFile() {
        // Save transactions
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Transaction t : transactions) {
                writer.write(t.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
        // Save budget
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGET_FILE))) {
            writer.write(String.valueOf(budget.getMonthlyBudget()));
        } catch (IOException e) {
            System.err.println("Error saving budget: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        // Load transactions
        File dataFile = new File(DATA_FILE);
        if (dataFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String line;
                int maxId = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    Transaction t = Transaction.fromCSV(line);
                    if (t != null) {
                        transactions.add(t);
                        if (t.getTransactionId() > maxId) {
                            maxId = t.getTransactionId();
                        }
                    }
                }
                nextId = maxId + 1;
            } catch (IOException e) {
                System.err.println("Error loading transactions: " + e.getMessage());
            }
        }
        // Load budget
        File budgetFile = new File(BUDGET_FILE);
        if (budgetFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(budgetFile))) {
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    budget.setMonthlyBudget(Double.parseDouble(line.trim()));
                }
            } catch (Exception e) {
                System.err.println("Error loading budget: " + e.getMessage());
            }
        }
    }
}
