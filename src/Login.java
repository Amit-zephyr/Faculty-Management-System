import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    JLabel titleLabel, userLabel, passwordLabel, roleLabel;
    JTextField userField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;
    JButton loginButton, resetButton;

    Login() {
        setTitle("Login");
        setSize(600, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("FACULTY LEAVE & SUBSTITUTION MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(20, 55, 130));
        titleLabel.setBounds(75, 40, 480, 35);
        add(titleLabel);

        userLabel = new JLabel("User ID:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setBounds(120, 120, 100, 30);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(230, 120, 220, 30);
        add(userField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(120, 170, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(230, 170, 220, 30);
        add(passwordField);

        roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setBounds(120, 220, 100, 30);
        add(roleLabel);

        String[] roles = {"Faculty", "Admin / HOD", "Substitute Faculty"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(230, 220, 220, 30);
        add(roleBox);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBounds(180, 290, 110, 40);
        add(loginButton);

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBounds(310, 290, 110, 40);
        add(resetButton);

        loginButton.addActionListener(e -> loginUser());

        resetButton.addActionListener(e -> {
            userField.setText("");
            passwordField.setText("");
            roleBox.setSelectedIndex(0);
        });

        getContentPane().setBackground(new Color(240, 245, 252));
        setVisible(true);
    }

    private void loginUser() {
        String userIdText = userField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (userIdText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID and Password",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT user_id, name, role FROM users WHERE username=? AND password_hash=? AND role=?";

        try (
                
                Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userIdText);
            ps.setString(2, password);
            ps.setString(3, role);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AppSession.userId = rs.getInt("user_id");
                    AppSession.name = rs.getString("name");
                    AppSession.role = rs.getString("role");

                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    dispose();

                    if ("Admin / HOD".equals(role)) {
                        new LeaveApproval();
                    } else {
                        new FacultyDashboard();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid User ID, Password or Role",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database connection/login error:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
