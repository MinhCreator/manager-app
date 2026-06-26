package minhcreator.component.Security;

import minhcreator.main.Application;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author MinhCreatorVN
 */
public class FieldCheck {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+$");

    private Application app = Application.getInstance();

    public boolean emailCheck(String email) {
        Matcher match = EMAIL_PATTERN.matcher(email);

        if (match.matches()) {
//            JOptionPane.showMessageDialog(app, "email is valid", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
//            JOptionPane.showMessageDialog(app, "Invalid email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean usernameCheck(String username) {
        Matcher match = USERNAME_PATTERN.matcher(username);
        Boolean space = username.contains(" ");

        if (match.matches() && !space) {
//            JOptionPane.showMessageDialog(app, "username is valid", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
//            JOptionPane.showMessageDialog(app, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean fusionCheck(String obj, String successMess, String errorMess) {
        Matcher Emailmatch = EMAIL_PATTERN.matcher(obj);
        Matcher Usermatch = USERNAME_PATTERN.matcher(obj);
        Boolean space = obj.contains(" ");

        if (Emailmatch.matches() || Usermatch.matches() && !space) {
            JOptionPane.showMessageDialog(app, successMess, "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(app, errorMess, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean fusionCheckNoDialog(String obj) {
        Matcher Emailmatch = EMAIL_PATTERN.matcher(obj);
        Matcher Usermatch = USERNAME_PATTERN.matcher(obj);
        Boolean space = obj.contains(" ");

        return (Emailmatch.matches() || Usermatch.matches() && !space) ? true : false;
    }

}