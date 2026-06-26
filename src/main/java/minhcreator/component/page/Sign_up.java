package minhcreator.component.page;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.PasswordStrengthStatus;
import minhcreator.entity.UserEntity;
import minhcreator.functional.database.dao.UserDAO;
import minhcreator.functional.database.TableInitializer;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import minhcreator.util.AppLogger;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sign_up class is used to create a custom sign-up window.
 *
 * @author MinhCreatorVN
 */
public class Sign_up extends JPanel {
    private static Sign_up signUp;
    public JTextField txtUsername;
    public JTextField txtEmail;
    public JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton cmdRegister;
    private PasswordStrengthStatus passwordStrengthStatus;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+[A-Za-z8-9+_.-]+@[A-Za-z8-9.-]+$");
    public static sessionManager session;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z8-9+_.-]+$");


    public Sign_up() {
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
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        cmdRegister = new JButton("Register");


        passwordStrengthStatus = new PasswordStrengthStatus();

        JPanel panel = new JPanel(
                new MigLayout(
                        "wrap, fillx, insets 35 45 30 45",
                        "[fill,360]"
                )
        );

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email please");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password please");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password please");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%);"
        );

        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground: lighten(@foreground, 10%);" +
                "[dark]foreground: darken(@foreground, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0"
        );


//        cmdRegister.doClick();
        JLabel lbtitle = new JLabel("Welcome to Admin dashboard!");
        JLabel desc = new JLabel("Super easy admin dashboard!");

        lbtitle.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "font:bold +12"
        );

        desc.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );
        passwordStrengthStatus.initPasswordField(txtPassword);

        cmdRegister.addActionListener(e -> {
            cmdRegisterPerformed();

        });

        panel.add(lbtitle);
        panel.add(desc);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JSeparator(), "gapy 5 5");
        panel.add(new JLabel("Email"), "gapy 8");
        panel.add(txtEmail);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 8");
        panel.add(txtConfirmPassword);
        panel.add(cmdRegister, "gapy 20");
        panel.add(createLoginLabel(), "gapy 20");
        add(panel);
    }

    private Component createLoginLabel() {
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
                        "<a href=\"#\">Login now</a>" +
                        "</html>"
        );
        cmdRegister.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "border:0,0,0,0"
        );
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {

            Application.logout();
        });
        JLabel label = new JLabel("Already have an admin account ?");
        label.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );

        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }

    private void cmdRegisterPerformed() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();

        if (
                !username.isEmpty() &&
                        !email.isEmpty() &&
                        emailCheck(email) &&
                        usernameCheck(username) &&
                        txtPassword.getPassword().length > 0 &&
                        txtPassword.getPassword().length <= 20 &&
                        isMatchPassword()
        ) {
            Register();
            Application.logout();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Register successfully! and you will be redirected to login page");
            session = new sessionManager();
            session.register(username, email, new String(txtPassword.getPassword()));
            TableInitializer.initializeTable(username);
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Something went wrong. Try again!");
        }
    }

    public boolean isMatchPassword() {
        String pass = new String(txtPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());
        if (pass.equals(confirmPass) == false) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Passwords don't match. Try again!");
        }
        return pass.equals(confirmPass);
    }

    public Sign_up getInstance() {
        return signUp;
    }

    public static boolean emailCheck(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean usernameCheck(String username) {
        boolean space = username.contains(" ");
        return USERNAME_PATTERN.matcher(username).matches() && !space;
    }

    private void Register() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        System.out.println(username + " " + email + " [password hashed with BCrypt]");

        UserDAO userDAO = new UserDAO();
        UserEntity user = new UserEntity(username, email, hashed);
        userDAO.save(user);
        AppLogger.info("SignUp", "New user registered: " + username);
        Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Sign up successful");
    }

    public String getUserName() {
        String input = txtUsername.getText().trim();
        UserDAO userDAO = new UserDAO();
        UserEntity user;
        if (emailCheck(input)) {
            user = userDAO.findByEmail(input);
        } else if (usernameCheck(input)) {
            user = userDAO.findByUsername(input);
        } else {
            return null;
        }
        return user != null ? user.getUsername() : null;
    }

}