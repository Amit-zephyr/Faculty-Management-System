import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveSubstitutionRecords extends JFrame {

    private DefaultTableModel model;
    private JTextField searchField;

    LeaveSubstitutionRecords() {
        setTitle("Leave / Substitution Records");
        setSize(800, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("LEAVE / SUBSTITUTION RECORDS");
        title.setBounds(280, 30, 300, 40);
        add(title);

        JLabel searchLabel = new JLabel("Search Faculty:");
        searchLabel.setBounds(50, 90, 120, 30);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(160, 90, 180, 30);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(350, 90, 100, 30);
        add(searchButton);

        String[] columns = {"Faculty", "Leave Type", "From Date", "To Date", "Status", "Substitute"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 140, 700, 220);
        add(scrollPane);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(250, 390, 100, 40);
        add(refresh);

        JButton close = new JButton("Close");
        close.setBounds(370, 390, 100, 40);
        add(close);

        searchButton.addActionListener(e -> {
            String name = searchField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter faculty name");
            } else {
                loadRecords(name);
            }
        });

        refresh.addActionListener(e -> {
            searchField.setText("");
            loadRecords("");
        });

        close.addActionListener(e -> dispose());

        loadRecords("");
        setVisible(true);
    }

    private void loadRecords(String facultyName) {
        model.setRowCount(0);

        String sql = "SELECT u.name AS faculty, l.leave_type, l.from_date, l.to_date, l.status, " +
                "COALESCE(sub.name, '-') AS substitute " +
                "FROM leave_applications l " +
                "JOIN users u ON l.faculty_id=u.user_id " +
                "LEFT JOIN substitution s ON l.leave_id=s.leave_id " +
                "LEFT JOIN users sub ON s.substitute_faculty_id=sub.user_id " +
                "WHERE (? = '' OR u.name LIKE ?) ORDER BY l.applied_at DESC";

        try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/faculty_leave_db", "root", "Amit@2006");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, facultyName);
            ps.setString(2, "%" + facultyName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("faculty"),
                            rs.getString("leave_type"),
                            rs.getDate("from_date"),
                            rs.getDate("to_date"),
                            rs.getString("status"),
                            rs.getString("substitute")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not load records:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LeaveSubstitutionRecords();
    }
}
