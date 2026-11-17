import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class ApplyLeave extends JFrame {

    ApplyLeave() {
        setTitle("Apply Leave");
        setSize(600, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("APPLY LEAVE");
        title.setBounds(230, 30, 200, 40);
        add(title);

        JLabel leaveType = new JLabel("Leave Type:");
        leaveType.setBounds(70, 100, 100, 30);
        add(leaveType);

        JLabel fromDate = new JLabel("From Date:");
        fromDate.setBounds(70, 150, 100, 30);
        add(fromDate);

        JLabel toDate = new JLabel("To Date:");
        toDate.setBounds(70, 200, 100, 30);
        add(toDate);

        JLabel reason = new JLabel("Reason:");
        reason.setBounds(70, 250, 100, 30);
        add(reason);

        JLabel contact = new JLabel("Contact No:");
        contact.setBounds(320, 100, 100, 30);
        add(contact);

        JLabel email = new JLabel("Email:");
        email.setBounds(320, 150, 100, 30);
        add(email);

        String[] leaves = {"Casual Leave", "Medical Leave", "Earned Leave"};
        JComboBox<String> leaveBox = new JComboBox<>(leaves);
        leaveBox.setBounds(170, 100, 130, 30);
        add(leaveBox);

        JTextField fromField = new JTextField();
        fromField.setBounds(170, 150, 130, 30);
        add(fromField);

        JTextField toField = new JTextField();
        toField.setBounds(170, 200, 130, 30);
        add(toField);

        JTextField contactField = new JTextField();
        contactField.setBounds(420, 100, 130, 30);
        add(contactField);

        JTextField emailField = new JTextField();
        emailField.setBounds(420, 150, 130, 30);
        add(emailField);

        JTextArea reasonArea = new JTextArea();
        reasonArea.setBounds(170, 250, 380, 80);
        add(reasonArea);

        JButton submit = new JButton("Submit Leave");
        submit.setBounds(170, 360, 130, 40);
        add(submit);

        JButton clear = new JButton("Clear");
        clear.setBounds(320, 360, 100, 40);
        add(clear);

        JButton cancel = new JButton("Cancel");
        cancel.setBounds(440, 360, 100, 40);
        add(cancel);

        submit.addActionListener(e -> {
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String contactNo = contactField.getText().trim();
            String emailAddress = emailField.getText().trim();
            String reasonText = reasonArea.getText().trim();

            if (AppSession.userId <= 0) {
                JOptionPane.showMessageDialog(this, "Please login first.");
                return;
            }
            if (from.isEmpty() || to.isEmpty() || contactNo.isEmpty()
                    || emailAddress.isEmpty() || reasonText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all details.");
                return;
            }

            String sql = "INSERT INTO leave_applications " +
                    "(faculty_id, leave_type, from_date, to_date, reason, contact_no, email, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending')";

            try (Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006");
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, AppSession.userId);
                ps.setString(2, (String) leaveBox.getSelectedItem());
                ps.setDate(3, Date.valueOf(from));
                ps.setDate(4, Date.valueOf(to));
                ps.setString(5, reasonText);
                ps.setString(6, contactNo);
                ps.setString(7, emailAddress);

                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Leave Application Submitted Successfully.");
                    fromField.setText("");
                    toField.setText("");
                    contactField.setText("");
                    emailField.setText("");
                    reasonArea.setText("");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Enter dates in YYYY-MM-DD format.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Could not save leave:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        clear.addActionListener(e -> {
            fromField.setText("");
            toField.setText("");
            contactField.setText("");
            emailField.setText("");
            reasonArea.setText("");
        });

        cancel.addActionListener(e -> dispose());
        setVisible(true);
    }

    public static void main(String[] args) {
        new ApplyLeave();
    }
}
