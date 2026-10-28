package com.finance;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Form for adding a new expense.
 */
public class AddExpenseFrame extends JFrame {

    private MainFrame parent;
    private FinanceManager manager;

    private JTextField txtDescription;
    private JTextField txtAmount;
    private JComboBox<String> cmbCategory;
    private JTextField txtDate;

    public static final String[] CATEGORIES = {
        "Food", "Travel", "Shopping", "Education", "Bills", "Entertainment", "Other"
    };

    public AddExpenseFrame(MainFrame parent, FinanceManager manager) {
        this.parent = parent;
        this.manager = manager;

        setTitle("Add Expense");
        setSize(380, 280);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        panel.add(txtDescription);

        panel.add(new JLabel("Amount (Rs.):"));
        txtAmount = new JTextField();
        panel.add(txtAmount);

        panel.add(new JLabel("Category:"));
        cmbCategory = new JComboBox<>(CATEGORIES);
        panel.add(cmbCategory);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField(LocalDate.now().toString());
        panel.add(txtDate);

        JButton btnAdd = new JButton("Add Expense");
        JButton btnCancel = new JButton("Cancel");
        panel.add(btnAdd);
        panel.add(btnCancel);

        add(panel);

        btnAdd.addActionListener(e -> addExpense());
        btnCancel.addActionListener(e -> dispose());
    }

    private void addExpense() {
        try {
            String desc = txtDescription.getText().trim();
            String amtText = txtAmount.getText().trim();
            String dateText = txtDate.getText().trim();
            String category = (String) cmbCategory.getSelectedItem();

            if (desc.isEmpty() || amtText.isEmpty() || dateText.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            double amount = Double.parseDouble(amtText);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }

            LocalDate date = parseDate(dateText);

            Expense expense = new Expense(manager.getNextId(), desc, amount, date, category);
            manager.addTransaction(expense);
            parent.refreshDashboard();

            if (manager.isBudgetExceeded()) {
                JOptionPane.showMessageDialog(this,
                        "Expense added successfully.\nWarning: Monthly budget exceeded!",
                        "Budget Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Expense added successfully.");
            }
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount. Please enter a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date. Use YYYY-MM-DD (e.g. 2026-10-01) or DD/MM/YYYY.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Accepts both YYYY-MM-DD and DD/MM/YYYY. */
    public static LocalDate parseDate(String text) {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(text, formatter);
        }
    }
}
