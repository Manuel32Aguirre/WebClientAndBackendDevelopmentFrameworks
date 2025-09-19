package adapters.desktop;

import infrastructure.database.ConnectionFactory;

import javax.swing.*;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection conn = ConnectionFactory.get();
                MainFrame mainFrame = new MainFrame(conn);
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
