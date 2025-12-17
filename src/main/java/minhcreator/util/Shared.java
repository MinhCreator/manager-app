package minhcreator.util;

import minhcreator.component.form.Login;
import minhcreator.component.form.Sign_up;
import minhcreator.functional.database.DB;

import java.sql.Connection;

public class Shared {
    // variables test
    public static final String casting_value = "CASTING";

    // Database connection
    public static DB database = new DB();

    // UI components values
        // login
        public String login_username = new Login().txtUsername.getText();
        public String login_password = new Login().txtPassword.toString();
        // sign up
        public String signup_username = new Sign_up().txtUsername.getText();
        public String signup_password = new Sign_up().txtPassword.toString();
        public String signup_email = new Sign_up().txtEmail.getText();
}
