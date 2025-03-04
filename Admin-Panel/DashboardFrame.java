import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Admin Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255)); 
        panel.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        JLabel welcomeLabel = new JLabel("Welcome to Admin Panel", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(34, 45, 50)); 
        panel.add(welcomeLabel, BorderLayout.CENTER);
    }
}
