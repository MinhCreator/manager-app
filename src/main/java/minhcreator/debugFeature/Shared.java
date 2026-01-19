package minhcreator.debugFeature;

import minhcreator.component.page.Login;
import minhcreator.component.page.Sign_up;
import minhcreator.functional.database.DB;

/**
 *
 * @author MinhCreatorVN
 */
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