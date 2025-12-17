package minhcreator.component.form;
import java.awt.*;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.Alert.FieldCheck;
import minhcreator.component.PopUp;
import minhcreator.functional.*;
import net.miginfocom.swing.MigLayout;
import minhcreator.main.Application;
import javax.swing.*;


//  ALL OF THIS CODE FILE UNDER EXPERIMENTAL and NOT RECOMMENDED TO USE
public class Login extends JPanel{

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
        remember_me = new JCheckBox("Remember me");
        remember_me.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin = new JButton("Login");

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%);"
        );

        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground: lighten(@foreground, 10%);" +
                "[dark]foreground: darken(@foreground, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0"
        );

        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(evt -> {
//            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Login successfully");
            cmdLoginActionPerformed(evt);
        });

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email please");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password please");

        JLabel lbtitle = new JLabel("Welcome back!");

        JLabel desc = new JLabel("Please login to access SWMS Dashboard");

        lbtitle.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "font:bold +10"
        );

        desc.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );

        panel.add(lbtitle);
        panel.add(desc);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(remember_me, "gapx 10");
//        panel.add(UserAgreement, "split 2");
        panel.add(createUserAgreement(), "gapx 0");

        panel.add(cmdLogin, "gapy 10");

        panel.add(createSignupLabel(), "gapy 10");
        add(panel);

        return panel;
    }

    private Component createSignupLabel() {
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,0,0
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
                "" + "border:3,3,3,3"
        );
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {
            Application.signup();
        });



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
                        FlowLayout.LEFT,10,0
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
                "" + "border:2,2,2,2"
        );

        Trigger_UA_dialog.setContentAreaFilled(false);
        Trigger_UA_dialog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Trigger_UA_dialog.addActionListener(e -> {
//                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "User Agreement");
                UA_dialog();
        });

        panel.add(UserAgreement);
        panel.add(Trigger_UA_dialog);

        return panel;
    }

    private Component UA_dialog() {
        PopUp popUp = new PopUp(Application.getInstance(), "User Agreement", 700, 650);
//        popUp.initFrame("User Agreement", 700, 650);
        popUp.setLayout(new MigLayout(
                "fill, insets 20",
                "[center]",
                "[center]"
        ));

        JPanel panel = new JPanel();

        panel.setLayout(new MigLayout(
                "fill, insets 20",
                "[center]",
                "[center]"
        ));

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%);"
        );
        JLabel lb = new JLabel("User Agreement");
        lb.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb.setFont(new Font("JetBrainsMono", Font.BOLD, 20));
        panel.add(lb, BorderLayout.NORTH);
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
        textArea.setFont(new Font("JetBrainsMono", Font.BOLD, 12));
        panel.add(textArea);

        JButton close = new JButton("Close");
        close.addActionListener(_ -> {
            popUp.dispose();
        });
        panel.add(close, BorderLayout.SOUTH);
        popUp.add(panel);
        return popUp;
    }

    private void cmdLoginActionPerformed(java.awt.event.ActionEvent evt) {
//        DB db = new DB();
//        db.getDBConnect("localhost", "mysql", "warehouse", "root", "");
//        Application.login();
        isValidLogin();

    }

    public static Login getInstance() {
        return login;
    }

    public void isValidLogin(){
        FieldCheck fieldCheck = new FieldCheck();
        String sucMess = "Login successfully";
        String errMess = "Invalid username or password";
        if (txtUsername.getText().trim().isEmpty() || new String(txtPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } else if (fieldCheck.fusionCheckNoDialog(txtUsername.getText().trim())) {
            if (UserAgreement.isSelected()) {
                Application.login();
            } else JOptionPane.showMessageDialog(
                    this,
                    "Please accept user agreement",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } else JOptionPane.showMessageDialog(
                this,
                "Invalid username or email",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

    }


    private static Login login;
    public JTextField txtUsername;
    public JPasswordField txtPassword;
    private JCheckBox remember_me;
    private JCheckBox UserAgreement;
    private JButton cmdLogin;
}
