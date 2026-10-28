package com.finance;

import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.time.format.DateTimeParseException;

/**
 * Form for adding a new income entry.
 */
public class AddIncomeFrame extends JFrame {

    private MainFrame parent;
    private FinanceManager manager;

    private JTextField txtSource;
    private JTextField txtAmount;
    private JTextField txtDate;

    public AddIncomeFrame(MainFrame parent, FinanceManager manager) {
        this.parent = parent;
        this.manager = manager;

        setTitle("Add Income");
        setSize(380, 240);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Source:"));
        txtSource = new JTextField();
        panel.add(txtSource);

        panel.add(new JLabel("Amount (Rs.):"));
        txtAmount = new JTextField();
        panel.add(txtAmount);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        txtDate = new JTextField(LocalDate.now().toString());
        panel.add(txtDate);

        JButton btnAdd = new JButton("Add Income");
        JButton btnCancel = new JButton("Cancel");
        panel.add(btnAdd);
        panel.add(btnCancel);

        add(panel);

        btnAdd.addActionListener(e -> addIncome());
        btnCancel.addActionListener(e -> dispose());
    }

    private void addIncome() {
        try {
            String source = txtSource.getText().trim();
            String amtText = txtAmount.getText().trim();
            String dateText = txtDate.getText().trim();

            if (source.isEmpty() || amtText.isEmpty() || dateText.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            double amount = Double.parseDouble(amtText);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }

            LocalDate date = AddExpenseFrame.parseDate(dateText);

            Income income = new Income(manager.getNextId(), source, amount, date);
            manager.addTransaction(income);
            parent.refreshDashboard();

            JOptionPane.showMessageDialog(this, "Income added successfully.");
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
}
