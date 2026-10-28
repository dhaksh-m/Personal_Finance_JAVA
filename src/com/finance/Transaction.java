package com.finance;

import java.time.LocalDate;

/**
 * Base class for all financial transactions.
 * Demonstrates Encapsulation, Constructors, Methods, Inheritance.
 */
public abstract class Transaction {
    protected int transactionId;
    protected String description;
    protected double amount;
    protected LocalDate date;
    protected String category;
    protected String type; // "INCOME" or "EXPENSE"

    public Transaction(int transactionId, String description, double amount,
                       LocalDate date, String category, String type) {
        this.transactionId = transactionId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.type = type;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    /**
     * Convert transaction to a CSV line.
     * Format: id,type,description,amount,date(yyyy-MM-dd),category
     * Commas in text fields are replaced with semicolons to keep CSV simple.
     */
    public String toCSV() {
        String safeDesc = description.replace(",", ";");
        String safeCat = category.replace(",", ";");
        return transactionId + "," + type + "," + safeDesc + "," + amount + "," + date.toString() + "," + safeCat;
    }

    /**
     * Create an Income or Expense object from a CSV line.
     * Returns null if the line is invalid.
     */
    public static Transaction fromCSV(String line) {
        try {
            String[] parts = line.split(",", 6);
            if (parts.length < 6) {
                return null;
            }
            int id = Integer.parseInt(parts[0].trim());
            String type = parts[1].trim();
            String desc = parts[2].trim();
            double amt = Double.parseDouble(parts[3].trim());
            LocalDate date = LocalDate.parse(parts[4].trim());
            String cat = parts[5].trim();

            if (type.equalsIgnoreCase("INCOME")) {
                return new Income(id, desc, amt, date, cat);
            } else {
                return new Expense(id, desc, amt, date, cat);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[" + transactionId + "] " + type + " | " + description
                + " | Rs." + amount + " | " + date + " | " + category;
    }
}
