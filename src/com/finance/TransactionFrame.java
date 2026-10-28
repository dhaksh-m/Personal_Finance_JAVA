package com.finance;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Shows all transactions in a table with search and delete options.
 */
public class TransactionFrame extends JFrame {

    private MainFrame parent;
    private FinanceManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public TransactionFrame(MainFrame parent, FinanceManager manager) {
        this.parent = parent;
        this.manager = manager;

        setTitle("All Transactions");
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---- Top: search bar ----
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(20);
        topPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        JButton btnShowAll = new JButton("Show All");
        topPanel.add(btnSearch);
        topPanel.add(btnShowAll);
        add(topPanel, BorderLayout.NORTH);

        // ---- Center: table ----
        String[] columns = {"ID", "Type", "Description", "Amount (Rs.)", "Date", "Category"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Bottom: actions ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRemove = new JButton("Remove Selected");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");
        bottomPanel.add(btnRemove);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);

        // ---- Events ----
        btnSearch.addActionListener(e -> searchAndShow(txtSearch.getText()));
        btnShowAll.addActionListener(e -> {
            txtSearch.setText("");
            loadTable(manager.getAllTransactions());
        });
        btnRefresh.addActionListener(e -> loadTable(manager.getAllTransactions()));
        btnClose.addActionListener(e -> dispose());

        btnRemove.addActionListener(e -> removeSelected());

        // Enter key in search box triggers search
        txtSearch.addActionListener(e -> searchAndShow(txtSearch.getText()));

        loadTable(manager.getAllTransactions());
    }

    public TransactionFrame(FinanceManager manager) {
        this(null, manager);
    }

    public void searchAndShow(String keyword) {
        txtSearch.setText(keyword == null ? "" : keyword);
        List<Transaction> result = manager.searchTransactions(keyword);
        loadTable(result);
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for: " + keyword);
        }
    }

    private void loadTable(List<Transaction> list) {
        tableModel.setRowCount(0);
        for (Transaction t : list) {
            tableModel.addRow(new Object[]{
                t.getTransactionId(),
                t.getType(),
                t.getDescription(),
                String.format("%.2f", t.getAmount()),
                t.getDate().toString(),
                t.getCategory()
            });
        }
    }

    private void removeSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a transaction to remove.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete transaction ID " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean removed = manager.removeTransactionById(id);
            if (removed) {
                loadTable(manager.getAllTransactions());
                if (parent != null) {
                    parent.refreshDashboard();
                }
                JOptionPane.showMessageDialog(this, "Transaction removed.");
            } else {
                JOptionPane.showMessageDialog(this, "Could not remove transaction.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
