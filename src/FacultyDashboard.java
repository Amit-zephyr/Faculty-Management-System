import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyDashboard extends JFrame {

    private JLabel total, approved, pending, substitute;

    FacultyDashboard() {
        setTitle("Faculty Leave Management System");
        setSize(700, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("FACULTY DASHBOARD");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds(240, 30, 300, 40);
        add(title);

        JLabel welcome = new JLabel("Welcome, " +
                (AppSession.name == null ? "Faculty" : AppSession.name));
        welcome.setBounds(500, 75, 180, 30);
        add(welcome);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(20, 90, 150, 330);
        add(panel);

        JButton dashboard = new JButton("Dashboard");
        dashboard.setBounds(10, 20, 130, 40);
        panel.add(dashboard);

        JButton applyLeave = new JButton("Apply Leave");
        applyLeave.setBounds(10, 70, 130, 40);
        panel.add(applyLeave);

        JButton myLeaves = new JButton("My Leaves");
        myLeaves.setBounds(10, 120, 130, 40);
        panel.add(myLeaves);

        JButton substitution = new JButton("Substitution");
        substitution.setBounds(10, 170, 130, 40);
        panel.add(substitution);

        JButton records = new JButton("Records");
        records.setBounds(10, 220, 130, 40);
        panel.add(records);

        JButton logout = new JButton("Logout");
        logout.setBounds(10, 270, 130, 40);
        panel.add(logout);

        total = new JLabel("Total Leaves: 0");
        total.setFont(new Font("Arial", Font.BOLD, 18));
        total.setBounds(220, 130, 200, 40);
        add(total);

        approved = new JLabel("Approved Leaves: 0");
        approved.setFont(new Font("Arial", Font.BOLD, 18));
        approved.setBounds(220, 190, 250, 40);
        add(approved);

        pending = new JLabel("Pending Leaves: 0");
        pending.setFont(new Font("Arial", Font.BOLD, 18));
        pending.setBounds(220, 250, 250, 40);
        add(pending);

        substitute = new JLabel("Substitution Assigned: 0");
        substitute.setFont(new Font("Arial", Font.BOLD, 18));
        substitute.setBounds(220, 310, 300, 40);
        add(substitute);

        dashboard.addActionListener(e -> loadDashboard());
        applyLeave.addActionListener(e -> new ApplyLeave());
        myLeaves.addActionListener(e -> new LeaveSubstitutionRecords());
        substitution.addActionListener(e -> new SubstitutionAssignment());
        records.addActionListener(e -> new LeaveSubstitutionRecords());
        logout.addActionListener(e -> {
            dispose();
            new Login();
        });

        loadDashboard();
        setVisible(true);
    }

    private void loadDashboard() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM leave_applications WHERE faculty_id=?) AS total_count, " +
                "(SELECT COUNT(*) FROM leave_applications WHERE faculty_id=? AND status='Approved') AS approved_count, " +
                "(SELECT COUNT(*) FROM leave_applications WHERE faculty_id=? AND status='Pending') AS pending_count, " +
                "(SELECT COUNT(*) FROM substitution WHERE substitute_faculty_id=?) AS substitution_count";

        try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006");
             PreparedStatement ps = con.prepareStatement(sql)) {

            int id = AppSession.userId;
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, id);
            ps.setInt(4, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total.setText("Total Leaves: " + rs.getInt("total_count"));
                    approved.setText("Approved Leaves: " + rs.getInt("approved_count"));
                    pending.setText("Pending Leaves: " + rs.getInt("pending_count"));
                    substitute.setText("Substitution Assigned: " + rs.getInt("substitution_count"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Dashboard database error:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
