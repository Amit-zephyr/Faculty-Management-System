import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveApproval extends JFrame {

    private int currentLeaveId = -1;
    private JTextField facultyField, leaveField, fromField, toField, statusField;
    private JTextArea reasonArea;

    LeaveApproval() {
        setTitle("Leave Approval");
        setSize(650, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("LEAVE APPROVAL");
        title.setBounds(240, 30, 200, 40);
        add(title);

        JLabel faculty = new JLabel("Faculty Name:");
        faculty.setBounds(70, 100, 120, 30);
        add(faculty);

        JLabel leaveType = new JLabel("Leave Type:");
        leaveType.setBounds(70, 150, 120, 30);
        add(leaveType);

        JLabel fromDate = new JLabel("From Date:");
        fromDate.setBounds(70, 200, 120, 30);
        add(fromDate);

        JLabel toDate = new JLabel("To Date:");
        toDate.setBounds(70, 250, 120, 30);
        add(toDate);

        JLabel status = new JLabel("Status:");
        status.setBounds(70, 300, 120, 30);
        add(status);

        JLabel reason = new JLabel("Reason:");
        reason.setBounds(350, 100, 100, 30);
        add(reason);

        facultyField = new JTextField();
        facultyField.setBounds(180, 100, 150, 30);
        facultyField.setEditable(false);
        add(facultyField);

        leaveField = new JTextField();
        leaveField.setBounds(180, 150, 150, 30);
        leaveField.setEditable(false);
        add(leaveField);

        fromField = new JTextField();
        fromField.setBounds(180, 200, 150, 30);
        fromField.setEditable(false);
        add(fromField);

        toField = new JTextField();
        toField.setBounds(180, 250, 150, 30);
        toField.setEditable(false);
        add(toField);

        statusField = new JTextField("Pending");
        statusField.setBounds(180, 300, 150, 30);
        statusField.setEditable(false);
        add(statusField);

        reasonArea = new JTextArea();
        reasonArea.setBounds(420, 100, 170, 100);
        reasonArea.setEditable(false);
        add(reasonArea);

        JButton approve = new JButton("Approve");
        approve.setBounds(150, 370, 120, 40);
        add(approve);

        JButton reject = new JButton("Reject");
        reject.setBounds(280, 370, 120, 40);
        add(reject);

        JButton clear = new JButton("Clear");
        clear.setBounds(410, 370, 100, 40);
        add(clear);

        approve.addActionListener(e -> updateStatus("Approved"));
        reject.addActionListener(e -> updateStatus("Rejected"));
        clear.addActionListener(e -> {
            clearFields();
            loadNextPending();
        });

        loadNextPending();
        setVisible(true);
    }

    private void loadNextPending() {
        String sql = "SELECT l.leave_id, u.name, l.leave_type, l.from_date, l.to_date, l.status, l.reason " +
                "FROM leave_applications l JOIN users u ON l.faculty_id=u.user_id " +
                "WHERE l.status='Pending' ORDER BY l.applied_at LIMIT 1";

        try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006");
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                currentLeaveId = rs.getInt("leave_id");
                facultyField.setText(rs.getString("name"));
                leaveField.setText(rs.getString("leave_type"));
                fromField.setText(rs.getString("from_date"));
                toField.setText(rs.getString("to_date"));
                statusField.setText(rs.getString("status"));
                reasonArea.setText(rs.getString("reason"));
            } else {
                currentLeaveId = -1;
                clearFields();
                JOptionPane.showMessageDialog(this, "No pending leave applications.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load pending leave:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateStatus(String newStatus) {
        if (currentLeaveId == -1) {
            JOptionPane.showMessageDialog(this, "No pending leave selected.");
            return;
        }

        String sql = "UPDATE leave_applications SET status=? WHERE leave_id=? AND status='Pending'";

        try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, currentLeaveId);

            if (ps.executeUpdate() > 0) {
                statusField.setText(newStatus);
                JOptionPane.showMessageDialog(this, "Leave " + newStatus + " Successfully.");
                currentLeaveId = -1;
            } else {
                JOptionPane.showMessageDialog(this, "This application was already processed.");
                loadNextPending();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not update leave:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        facultyField.setText("");
        leaveField.setText("");
        fromField.setText("");
        toField.setText("");
        statusField.setText("Pending");
        reasonArea.setText("");
    }

    public static void main(String[] args) {
        new LeaveApproval();
    }
}
