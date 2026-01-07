package minhcreator.debugPage;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.UIManager.UIManager;
import minhcreator.component.page.Sign_up;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Raven
 * @author Modified by MinhCreatorVN
 */
public class LoginBackup extends JPanel {
    public LoginBackup() {
        init();
    }

    private void init() {

        setLayout(
                new MigLayout(
                        "fill, insets 20",
                        "[center]",
                        "[center]"
                )
        );
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        remmeberme = new JCheckBox("Remmeber me");
        cmdLogin = new JButton("Login");

        JPanel panel = new JPanel(
                new MigLayout(
                        "wrap, fillx, insets 35 45 30 45",
                        "fill,250:280"
                )
        );

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

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email please");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password please");

        JLabel lbtitle = new JLabel("Welcome back!");

        JLabel desc = new JLabel("Please sign in to access Dashboard");

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
        panel.add(remmeberme, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignupLable(), "gapy 10");
        add(panel);
    }

    private Component createSignupLable() {
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
        cmdRegister.addActionListener(e -> {
            UIManager.getInstance().showForm(new Sign_up());
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

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox remmeberme;
    private JButton cmdLogin;
}