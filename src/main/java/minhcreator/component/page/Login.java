package minhcreator.component.page;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.PopUp;
import minhcreator.component.Security.FieldCheck;
import minhcreator.functional.database.DB;
import minhcreator.functional.imageSupport.imgRender;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Login class is used to create a custom login window.
 *
 * @author MinhCreatorVN
 */
//  ALL OF THIS CODE FILE UNDER EXPERIMENTAL and NOT RECOMMENDED TO USE
public class Login extends JPanel {

    public Login() {
        init();
    }

    private void init() {

        setLayout(new BorderLayout());
        add(createLeftPanel(), BorderLayout.WEST);
        add(createRightPanel(), BorderLayout.CENTER);
//        setVisible(true);
    }

    private Component createLeftPanel() {
        JPanel Lpanel = new JPanel();
        Lpanel.setBackground(Color.decode("#f5f5f5"));
        Lpanel.setLayout(new BorderLayout());
        Lpanel.setPreferredSize(new Dimension(800, 700));

        ImageIcon icons = new imgRender().renderImg("/minhcreator/assets/warehouse_management.png", 800, 700);
        JLabel Bg_label = new JLabel();
        Bg_label.setIcon(icons);
        Bg_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        Bg_label.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel overlay = new JPanel();
//        overlay.setBackground(Color.decode("#f5f5f5"));
        JLabel textLabel = new JLabel("Warehouse Management System");
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(new Font("JetBrainsMono", Font.BOLD, 35));

        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        overlay.setLayout(new OverlayLayout(overlay));
        overlay.setAlignmentY(Component.TOP_ALIGNMENT);
        overlay.add(textLabel);
        overlay.add(Box.createVerticalGlue());
        Lpanel.add(Bg_label);
//        Lpanel.add(overlay);
        return Lpanel;
    }

    private Component createRightPanel() {
        JPanel panel = new JPanel(
                new MigLayout(
                        "wrap, fillx, insets 35 45 30 45",
                        "fill,250:280"
                )
        );

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cmdLogin = new JButton("Login");

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,3%);"
        );

        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground: lighten(@foreground, 10%);" +
                "[dark]foreground: darken(@foreground, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "selectedBackground: darken(@background, 30%)"
        );

        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Login successfully");
        cmdLogin.addActionListener(this::cmdLoginActionPerformed);

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email please");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password please");

        JLabel lbtitle = new JLabel("Welcome back!");

        JLabel desc = new JLabel("Please login to access SWMS Dashboard");

        lbtitle.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "font:bold +12"
        );

        desc.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );

        panel.add(lbtitle);
        panel.add(desc);
        panel.add(new JLabel("Email"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(createUserAgreement(), "gapx 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignupLabel(), "gapy 10");
        add(panel);

        return panel;
    }

    private Component createSignupLabel() {
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 0, 0
                )
        );
        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "background:null"
        );

        JButton cmdRegister = new JButton(
                "<html>" +
                        "<a href=\"#\">Sign up</a>" +
                        "</html>"
        );
        cmdRegister.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "border:0,0,0,0"
        );
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> Application.signup());


        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );

        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }

    private Component createUserAgreement() {
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 10, 0
                )
        );
        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "background:null"
        );

        UserAgreement = new JCheckBox("I agree to our");
        UserAgreement.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton Trigger_UA_dialog = new JButton(
                "<html>" +
                        "<a href=\"#\">User Agreement</a>" +
                        "</html>"
        );
        Trigger_UA_dialog.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "border:0,0,0,0"
        );

        Trigger_UA_dialog.setContentAreaFilled(false);
        Trigger_UA_dialog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Trigger_UA_dialog.addActionListener(e -> UA_dialog());

        panel.add(UserAgreement);
        panel.add(Trigger_UA_dialog);

        return panel;
    }

    private Component UA_dialog() {
        PopUp popUp = new PopUp(Application.getInstance(), "", 760, 600);
        popUp.setLayout(new MigLayout(
                "fill, insets 20 20 20 20",
                "[center]",
                "[center]"
        ));

        JPanel panel = new JPanel();

        panel.setLayout(new MigLayout(
                "fill, insets 30",
                "[center]",
                "[center]"
        ));

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%);"
        );
        JLabel lb = new JLabel("User Agreement");

        lb.setFont(new Font("JetBrainsMono", Font.BOLD, 20));

        String content = "\n" +
                "Copyright (c) [2025] [MinhCreatorVN]\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                "in the Software without restriction, including without limitation the rights\n" +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                "copies of the Software, and to permit persons to whom the Software is\n" +
                "furnished to do so, subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all\n" +
                "copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                "SOFTWARE.";
        JTextArea textArea = new JTextArea(content);

        textArea.setEditable(false);
        textArea.setFont(new Font("JetBrainsMono", Font.BOLD, 13));
        textArea.setFocusable(false);
        panel.add(lb, "wrap");
        panel.add(textArea, "center, wrap");

        JButton close = new JButton("Close");
        close.setSize(50, 50);
        close.putClientProperty(FlatClientProperties.STYLE, "" +
                "borderWidth: 0;" +
                "disabledBorderColor: @background;"
        );
        close.addActionListener(_ -> popUp.dispose());
        panel.add(close, "center");
        popUp.add(panel);
        return popUp;
    }

    private void cmdLoginActionPerformed(java.awt.event.ActionEvent evt) {


        if (isValidLogin()) {  // Only proceed if login is valid
            session = new sessionManager();
            String email = txtUsername.getText().trim();
            String user = getUserUsername(email);
            String pass = getUserUserPass(email);  // Get password from DB, not from input
            String id = getId(email);
            session.login(id, user, email, pass);
            session.createSession(user, email, pass);
            Application.login();
            // Only show success message if we actually logged in
            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Login successful"
            );
            isLogin = true;
        } else {
            Application.logout();
            isLogin = false;
        }
//        Application.login();
    }

    public static Login getInstance() {
        if (login == null) {
            login = new Login();
        }
        return login;
    }

    public boolean isValidLogin() {
        String getInputBox = txtUsername.getText().trim();
        String getPass = new String(txtPassword.getPassword()).trim();
        boolean isAgree = UserAgreement.isSelected();
        String match = matchText(getInputBox);
        String sucMess = "Login successfully";
        String errMess = "Invalid username or password";

        if (!getInputBox.isEmpty() && !getPass.isEmpty()) {

            if (isAgree) {
                if (match.equals("email")) {
                    return attemp_Email_Login();

//
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Invalid email format");
                }
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Please agree to our User Agreement");
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Please fill in all fields");
        }
        return false;
    }

    public boolean attemp_Email_Login() {

        String email = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        String query = "SELECT password FROM users WHERE email =?";
        try (Connection conn = DB.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    if (rs.getString("password").equals(pass)) {
                        if (does_Email_Exist(email)) {
                            return true;
                        }
                    } else {
                        Notifications.getInstance().show(
                                Notifications.Type.ERROR,
                                Notifications.Location.TOP_CENTER,
                                "Invalid email or password and May be your account not existed"
                        );
                    }
                }
            } catch (Exception e) {
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Invalid username or password and May be your account not existed"
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean does_Email_Exist(String email) {
        // The SQL query to count matching usernames

        String query = "SELECT COUNT(email) FROM users WHERE email = ?";

        // Use try-with-resources for automatic closing of connections and statements
        try (Connection conn = DB.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            // Set the email parameter safely
            pst.setString(1, email);

            // Execute the query
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Get the value of the first (and only) column

                    return count > 0; // email exists
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error during username check: " + ex.getMessage());
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, ex.getMessage());

        }
        return false; // Username is Unavailable in table
    }

    public String matchText(String text) {

        if (fieldCheck.emailCheck(text)) {
            return "email";
        } else {
            return "invalid";
        }
    }

    public String getUserUsername(String email) {
        String sql = "SELECT username FROM users WHERE email = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getUserUserPass(String email) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getId(String email) {
        String execution = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement pst = conn.prepareStatement(execution)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public sessionManager getSession() {
        return session;
    }

    public static Login login;
    public JTextField txtUsername;
    public JPasswordField txtPassword;
    private JCheckBox UserAgreement;
    private JButton cmdLogin;
    private boolean isLogin;
    public DB database = new DB();
    FieldCheck fieldCheck = new FieldCheck();
    public Connection conn = null;
    public static sessionManager session;

}