package com.finance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Dashboard window - entry point of the GUI.
 */
public class MainFrame extends JFrame {

    private FinanceManager manager;

    private JLabel lblIncome;
    private JLabel lblExpenses;
    private JLabel lblBalance;
    private JLabel lblBudget;
    private JLabel lblBudgetStatus;

    public MainFrame(FinanceManager manager) {
        this.manager = manager;

        setTitle("Personal Finance Manager");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        setLayout(new BorderLayout(10, 10));

        // ---- Title ----
        JLabel title = new JLabel("PERSONAL FINANCE MANAGER", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // ---- Summary panel ----
        JPanel summary = new JPanel(new GridLayout(5, 2, 8, 8));
        summary.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        summary.add(new JLabel("Total Income:"));
        lblIncome = new JLabel("Rs. 0.00");
        summary.add(lblIncome);

        summary.add(new JLabel("Total Expenses:"));
        lblExpenses = new JLabel("Rs. 0.00");
        summary.add(lblExpenses);

        summary.add(new JLabel("Current Balance:"));
        lblBalance = new JLabel("Rs. 0.00");
        lblBalance.setFont(new Font("Arial", Font.BOLD, 13));
        summary.add(lblBalance);

        summary.add(new JLabel("Monthly Budget:"));
        lblBudget = new JLabel("Rs. 0.00");
        summary.add(lblBudget);

        summary.add(new JLabel("Budget Status:"));
        lblBudgetStatus = new JLabel("No budget set");
        summary.add(lblBudgetStatus);

        add(summary, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel buttons = new JPanel(new GridLayout(3, 2, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnAddIncome = new JButton("Add Income");
        JButton btnAddExpense = new JButton("Add Expense");
        JButton btnTransactions = new JButton("Transactions");
        JButton btnSetBudget = new JButton("Set Budget");
        JButton btnSearch = new JButton("Search");
        JButton btnExit = new JButton("Exit");

        buttons.add(btnAddIncome);
        buttons.add(btnAddExpense);
        buttons.add(btnTransactions);
        buttons.add(btnSetBudget);
        buttons.add(btnSearch);
        buttons.add(btnExit);

        add(buttons, BorderLayout.SOUTH);

        // ---- Event handling ----
        btnAddIncome.addActionListener(e -> new AddIncomeFrame(this, manager).setVisible(true));

        btnAddExpense.addActionListener(e -> new AddExpenseFrame(this, manager).setVisible(true));

        btnTransactions.addActionListener(e -> new TransactionFrame(this, manager).setVisible(true));

        btnSetBudget.addActionListener(e -> setBudgetDialog());

        btnSearch.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog(this,
                    "Enter keyword (description / category / date / id):",
                    "Search Transactions", JOptionPane.QUESTION_MESSAGE);
            if (keyword != null) {
                TransactionFrame frame = new TransactionFrame(this, manager);
                frame.searchAndShow(keyword);
                frame.setVisible(true);
            }
        });

        btnExit.addActionListener(e -> {
            manager.saveToFile();
            System.exit(0);
        });

        refreshDashboard();
    }

    /** Re-calculate totals and update labels. Called after every change. */
    public void refreshDashboard() {
        double income = manager.getTotalIncome();
        double expenses = manager.getTotalExpenses();
        double balance = manager.getBalance();
        double budget = manager.getMonthlyBudget();

        lblIncome.setText(String.format("Rs. %.2f", income));
        lblExpenses.setText(String.format("Rs. %.2f", expenses));
        lblBalance.setText(String.format("Rs. %.2f", balance));
        lblBudget.setText(String.format("Rs. %.2f", budget));

        if (budget <= 0) {
            lblBudgetStatus.setText("No budget set");
            lblBudgetStatus.setForeground(Color.GRAY);
        } else if (manager.isBudgetExceeded()) {
            lblBudgetStatus.setText(String.format("Exceeded! (Remaining Rs. %.2f)", manager.getRemainingBudget()));
            lblBudgetStatus.setForeground(Color.RED);
        } else {
            lblBudgetStatus.setText(String.format("OK (Remaining Rs. %.2f)", manager.getRemainingBudget()));
            lblBudgetStatus.setForeground(new Color(0, 128, 0));
        }

        if (balance < 0) {
            lblBalance.setForeground(Color.RED);
        } else {
            lblBalance.setForeground(new Color(0, 128, 0));
        }
    }

    private void setBudgetDialog() {
        String input = JOptionPane.showInputDialog(this,
                "Enter monthly budget (Rs.):", manager.getMonthlyBudget());
        if (input == null) {
            return; // cancelled
        }
        try {
            double amount = Double.parseDouble(input.trim());
            if (amount < 0) {
                throw new NumberFormatException("Budget cannot be negative.");
            }
            manager.setMonthlyBudget(amount);
            refreshDashboard();
            if (manager.isBudgetExceeded()) {
                JOptionPane.showMessageDialog(this,
                        "Warning: Total expenses already exceed this budget!",
                        "Budget Exceeded", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Monthly budget updated successfully.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount. Please enter a valid non-negative number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
