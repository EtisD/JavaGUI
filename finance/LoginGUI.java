package finance;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginGUI extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginGUI() {
        // Set Look and Feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        setTitle("Luxury Stay - Staff Login");
        setSize(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel header = new JLabel("Staff Portal", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.add(new JLabel("Username:"));
        userField = new JTextField();
        form.add(userField);
        
        form.add(new JLabel("Password:"));
        passField = new JPasswordField();
        form.add(passField);
        
        mainPanel.add(form, BorderLayout.CENTER);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        
        loginBtn.addActionListener(e -> verifyLogin());
        mainPanel.add(loginBtn, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void verifyLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.equals("etis") && password.equals("markos")) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            
            this.dispose(); 
            new HotelBookingGUI(); 
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Hint: Liqeni", 
                                          "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
