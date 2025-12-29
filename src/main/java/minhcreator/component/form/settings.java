package minhcreator.component.form;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.functional.database.DB;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class settings extends JPanel {
    private JPanel settingPanel;
    private JButton log_OutButt, UpdateBut, delMyAcc;
    private JLabel Email_Lb, Username_Lb, Password_Lb;
    private JTextField Email_Tf, Username_Tf;
    private JPasswordField Password_Tf;
    private Connection conn;
    public sessionManager session_SignUp = Sign_up.session;
    public sessionManager session_Login = Login.session;

    public settings() {
        init();
        initComponent();
    }

    private void init() {
        setVisible(true);
        setLayout(new BorderLayout());
    }

    private void initComponent() {
        add(settingPanel());
    }

    private JPanel settingPanel() {
        settingPanel = new JPanel();
        settingPanel.setLayout(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]", "[center]"));

        Email_Lb = new JLabel("Email");
        Email_Tf = new JTextField();
        Email_Tf.setText(session_Login.getEmail());

        Username_Lb = new JLabel("Username");
        Username_Tf = new JTextField();
        Username_Tf.setText(session_Login.getUsername());

        Password_Lb = new JLabel("Password");
        Password_Tf = new JPasswordField();
        Password_Tf.setText(session_Login.getPassword());
        Password_Tf.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        log_OutButt = new JButton("Log out");
        log_OutButt.addActionListener(e -> {
            letLogOut();
            session_Login.clearSession();
        });
        UpdateBut = new JButton("Update");
        UpdateBut.addActionListener(e -> {
            letUpdate();
        });
        delMyAcc = new JButton("Delete me");
        delMyAcc.addActionListener(e -> {
            deleteMyAccount();
            session_Login.clearSession();
            letLogOut();
        });
        settingPanel.add(Email_Lb);
        settingPanel.add(Email_Tf);
        settingPanel.add(Username_Lb);
        settingPanel.add(Username_Tf);
        settingPanel.add(Password_Lb);
        settingPanel.add(Password_Tf);
        settingPanel.add(UpdateBut);
        settingPanel.add(delMyAcc);
        settingPanel.add(log_OutButt);
        return settingPanel;
    }

    private void letLogOut() {
        Application.logout();
    }

    private void letUpdate() {
        try {
            conn = new DB().getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (conn == null) return;

        String sql = "UPDATE users SET username=?, email=?, password=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String pass = new String(Password_Tf.getPassword());
            ps.setString(1, Username_Tf.getText().trim());
            ps.setString(2, Email_Tf.getText().trim());
            ps.setString(3, pass);
            ps.setString(4, session_Login.getId());
            ps.executeUpdate();
            try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE id=?")) {
                pst.setString(1, session_Login.getId());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    session_Login.login(rs.getString("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
                    reloadSettingPanel(rs.getString("username"), rs.getString("email"), rs.getString("password"));
                }
            }
            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Update User information successful!"
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
        }
    }

    private void deleteMyAccount() {
        DB getOnline = new DB();
        String sql = "DELETE FROM users " + " WHERE id = ?";
        Statement stmt = null;
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Assuming you want to delete the currently logged-in user
            // You'll need to get the current user's ID from the session
            String currentUserId = session_Login.getId();  // Adjust this based on how you get the current user ID
            pstmt.setString(1, currentUserId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Account deleted successfully
                // Show success message
                stmt = conn.createStatement();
                stmt.execute("DROP TABLE " + session_Login.getYour_inventory() + ";");
                stmt.execute("DROP TABLE " + session_Login.getYour_stock_add() + ";");
                stmt.execute("DROP TABLE " + session_Login.getYour_export_table() + ";");
                stmt.execute("DROP TABLE " + session_Login.getYour_credit() + ";");
                Notifications.getInstance().show(
                        Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "Your account has been deleted successfully"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message
            Notifications.getInstance().show(
                    Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Error deleting account: " + e.getMessage()
            );
        }

    }

    private void reloadSettingPanel(String username, String email, String password) {
        remove(settingPanel);
        add(settingPanel());
        revalidate();
        repaint();
        session_Login.setUsername(username);
        session_Login.setEmail(email);
        session_Login.setPassword(password);
    }
}