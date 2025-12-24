package minhcreator.util;

import minhcreator.component.form.Login;
import minhcreator.component.form.Sign_up;
import minhcreator.functional.database.DB;

public class Shared {
    // variables test
    public static final String casting_value = "CASTING";

    // Database connection
    public static DB database = new DB();
    // UI components values
    // login
    public static String login_username = new Login().txtUsername.getText();
    public static String login_password = new Login().txtPassword.toString();
    // sign up
    public static String signup_username = new Sign_up().txtUsername.getText();
    public static String signup_password = new Sign_up().txtPassword.toString();
    public static String signup_email = new Sign_up().txtEmail.getText();
}