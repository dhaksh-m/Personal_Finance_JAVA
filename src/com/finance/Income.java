package com.finance;

import java.time.LocalDate;

/**
 * Represents money received by the user (salary, part-time work, etc.)
 */
public class Income extends Transaction {

    public Income(int transactionId, String source, double amount, LocalDate date) {
        super(transactionId, source, amount, date, "Income", "INCOME");
    }

    public Income(int transactionId, String source, double amount, LocalDate date, String category) {
        super(transactionId, source, amount, date,
                (category == null || category.isEmpty()) ? "Income" : category, "INCOME");
    }

    public String getSource() {
        return getDescription();
    }

    public void setSource(String source) {
        setDescription(source);
    }
}
