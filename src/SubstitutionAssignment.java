import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class SubstitutionAssignment extends JFrame {

    SubstitutionAssignment() {
        setTitle("Substitution Assignment");
        setSize(650, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("SUBSTITUTION ASSIGNMENT");
        title.setBounds(220, 30, 250, 40);
        add(title);

        JLabel faculty = new JLabel("Absent Faculty:");
        faculty.setBounds(70, 100, 120, 30);
        add(faculty);

        JLabel subject = new JLabel("Subject:");
        subject.setBounds(70, 150, 120, 30);
        add(subject);

        JLabel date = new JLabel("Date:");
        date.setBounds(70, 200, 120, 30);
        add(date);

        JLabel period = new JLabel("Period:");
        period.setBounds(70, 250, 120, 30);
        add(period);

        JLabel substitute = new JLabel("Substitute Faculty:");
        substitute.setBounds(330, 100, 140, 30);
        add(substitute);

        JTextField facultyField = new JTextField();
        facultyField.setBounds(190, 100, 120, 30);
        add(facultyField);

        JTextField subjectField = new JTextField();
        subjectField.setBounds(190, 150, 120, 30);
        add(subjectField);

        JTextField dateField = new JTextField();
        dateField.setBounds(190, 200, 120, 30);
        add(dateField);

        JTextField periodField = new JTextField();
        periodField.setBounds(190, 250, 120, 30);
        add(periodField);

        String[] facultyList = {
                "Select Faculty", "Rahul Sharma", "Neha Verma",
                "Amit Kumar", "Priya Singh"
        };
        JComboBox<String> substituteBox = new JComboBox<>(facultyList);
        substituteBox.setBounds(470, 100, 140, 30);
        add(substituteBox);

        JButton assign = new JButton("Assign");
        assign.setBounds(180, 340, 110, 40);
        add(assign);

        JButton clear = new JButton("Clear");
        clear.setBounds(300, 340, 110, 40);
        add(clear);

        JButton cancel = new JButton("Cancel");
        cancel.setBounds(420, 340, 110, 40);
        add(cancel);

        assign.addActionListener(e -> {
            String absentName = facultyField.getText().trim();
            String subjectName = subjectField.getText().trim();
            String dateText = dateField.getText().trim();
            String periodName = periodField.getText().trim();
            String selectedFaculty = (String) substituteBox.getSelectedItem();

            if (absentName.isEmpty() || subjectName.isEmpty() || dateText.isEmpty()
                    || periodName.isEmpty() || selectedFaculty == null
                    || selectedFaculty.equals("Select Faculty")) {
                JOptionPane.showMessageDialog(this, "Please enter all details.");
                return;
            }

            String findAbsent = "SELECT user_id FROM users WHERE name=?";
            String findSubstitute = "SELECT user_id FROM users WHERE name=?";
            String insert = "INSERT INTO substitution " +
                    "(leave_id, absent_faculty_id, substitute_faculty_id, subject, substitution_date, period) " +
                    "VALUES (NULL, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006")) {

                int absentId;
                int substituteId;

                try (PreparedStatement ps = con.prepareStatement(findAbsent)) {
                    ps.setString(1, absentName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(this, "Absent faculty not found in users table.");
                            return;
                        }
                        absentId = rs.getInt("user_id");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(findSubstitute)) {
                    ps.setString(1, selectedFaculty);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(this, "Substitute faculty not found in users table.");
                            return;
                        }
                        substituteId = rs.getInt("user_id");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(insert)) {
                    ps.setInt(1, absentId);
                    ps.setInt(2, substituteId);
                    ps.setString(3, subjectName);
                    ps.setDate(4, Date.valueOf(dateText));
                    ps.setString(5, periodName);

                    if (ps.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Substitution Assigned Successfully.");
                    }
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Enter date in YYYY-MM-DD format.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Could not assign substitution:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        clear.addActionListener(e -> {
            facultyField.setText("");
            subjectField.setText("");
            dateField.setText("");
            periodField.setText("");
            substituteBox.setSelectedIndex(0);
        });

        cancel.addActionListener(e -> dispose());
        setVisible(true);
    }

    public static void main(String[] args) {
        new SubstitutionAssignment();
    }
}
