package com.finance;

import java.time.LocalDate;

/**
 * Represents money spent by the user (food, travel, shopping, etc.)
 */
public class Expense extends Transaction {

    public Expense(int transactionId, String description, double amount, LocalDate date, String category) {
        super(transactionId, description, amount, date, category, "EXPENSE");
    }
}
