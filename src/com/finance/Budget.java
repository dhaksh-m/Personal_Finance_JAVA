package com.finance;

/**
 * Stores the monthly budget set by the user.
 */
public class Budget {
    private double monthlyBudget;

    public Budget() {
        this.monthlyBudget = 0.0;
    }

    public Budget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    /** Remaining Budget = Monthly Budget - Total Expenses */
    public double getRemaining(double totalExpenses) {
        return monthlyBudget - totalExpenses;
    }

    /** True if total expenses exceed the budget (and budget > 0). */
    public boolean isExceeded(double totalExpenses) {
        return monthlyBudget > 0 && totalExpenses > monthlyBudget;
    }
}
