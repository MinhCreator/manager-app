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

    // regex email checking
    private String regex = "^\\w+[A-Za-z8-9+_.-]+@[A-Za-z8-9.-]+$";

    // regex username checking
    private String regexU = "^[A-Za-z8-9+_.-]+$";

    // create app instanced
    private Application app = Application.getInstance();

    public boolean emailCheck(String email) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(email);

        if (match.matches()) {
//            JOptionPane.showMessageDialog(app, "email is valid", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
//            JOptionPane.showMessageDialog(app, "Invalid email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean usernameCheck(String username) {
        Pattern pattern = Pattern.compile(regexU);
        Matcher match = pattern.matcher(username);
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
        Pattern Emailpattern = Pattern.compile(regex);
        Pattern Userpattern = Pattern.compile(regexU);
        Matcher Emailmatch = Emailpattern.matcher(obj);
        Matcher Usermatch = Userpattern.matcher(obj);
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
        Pattern Emailpattern = Pattern.compile(regex);
        Pattern Userpattern = Pattern.compile(regexU);
        Matcher Emailmatch = Emailpattern.matcher(obj);
        Matcher Usermatch = Userpattern.matcher(obj);
        Boolean space = obj.contains(" ");

        return (Emailmatch.matches() || Usermatch.matches() && !space) ? true : false;
    }

}