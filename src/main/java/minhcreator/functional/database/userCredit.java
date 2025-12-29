package minhcreator.functional.database;


import minhcreator.component.form.Login;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import raven.toast.Notifications;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a userCredit. It is used to interact with the userCredit table in the database.
 * It provides methods to update, set, and get the user's credit.
 * <p>
 * The class is a singleton and can only be instantiated once by calling {@link userCredit#getInstance()}.
 *
 * @author MinhCreator
 * @version 1.0.0
 * @since 1.0.0
 */
public class userCredit {
    private static userCredit instance;
    private Connection conn;
    private static final sessionManager user_session = Login.session;
    private double credit;

    // Private constructor to prevent instantiation
    private userCredit() {
    }

    // Singleton getInstance method
    public static synchronized userCredit getInstance() {
        if (instance == null) {
            instance = new userCredit();
        }
        return instance;
    }

    /**
     * Initializes the user's credit in the database.
     * If the user's credit does not exist, it creates a new entry with the user's ID, username, and initial credit of 0.
     */
    public void initUserCredit() {

        String initCredit = "INSERT INTO " + user_session.getYour_credit() + " (owner_ID, Owner_name, Current_credit) VALUES (?, ?, ?)";
        try {
            conn = DB.getConnection();
            double credit = 0.0;
            if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(initCredit);
            ps.setString(1, user_session.getId());
            ps.setString(2, user_session.getUsername());
            ps.setString(3, Double.toString(credit));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Retrieves the user's current credit from the database.
     *
     * @return The user's current credit as a double. Returns 0.0 if the credit cannot be retrieved.
     */
    public double GetCredit() {
        String getCredit = "SELECT Current_credit FROM " + user_session.getYour_credit() + " WHERE owner_ID = ?";
        try {
            conn = DB.getConnection();
            if (conn == null) return 0.0;
            PreparedStatement ps = conn.prepareStatement(getCredit);
            ps.setString(1, user_session.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("Current_credit");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Something went wrong!");
        }
        return 0.0;
    }

    /**
     * Sets the user's credit to the specified value and updates the credit in the database.
     *
     * @param credit The new credit value to set.
     */
    public void setCredit(double credit) {
        this.credit = credit;
        updateCredit(credit);
    }

    /**
     * Updates the user's credit in the database to the specified value.
     *
     * @param credit The new credit value to update to.
     */
    public void updateCredit(double credit) {
        String sql = "UPDATE " + user_session.getYour_credit() + " SET Current_credit=? WHERE owner_ID=?";
        try{
            conn = DB.getConnection();
            if (conn == null) return;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Double.toString(credit));
            ps.setString(2, user_session.getId());
            ps.executeUpdate();
            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Update credit " + " successful!"
            );
        } catch (SQLException ex) {
//            ex.printStackTrace();
            JOptionPane.showMessageDialog(Application.getInstance(), "Update credit failed: " + ex.getMessage());
        }
    }


    // testing reload feature (Not recommented to using in stable programme version)
    public void reloadCreditValue() {
        // Example: Assume that the user's credit is stored in a variable called "currentCredit"
        // and it is updated in real time.
        // To reload the credit from the local variable to the database, you can do something like this:
        double currentCredit = GetCredit(); // Get the current credit from the local variable
        setCredit(currentCredit); // Update the credit in the database with the current credit


        // Sync local credit to database
        setCredit(GetCredit()); // Update the credit in the database with the current credit in the local variable

        // Sync database credit to local
        credit = GetCredit(); // Get the current credit from the database and update the local variable
    }
}