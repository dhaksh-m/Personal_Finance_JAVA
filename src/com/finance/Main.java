package com.finance;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point.
 */
public class Main {
    public static void main(String[] args) {
        // Use system look and feel for a native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            FinanceManager manager = new FinanceManager();
            MainFrame frame = new MainFrame(manager);
            frame.setVisible(true);
        });
    }
}
