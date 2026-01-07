package minhcreator.debugPage;

import minhcreator.functional.database.DB;
import raven.toast.Notifications;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author MinhCreatorVN
 */
public class functional_debug {

    public static boolean does_Email_Exist(String email) {
        // The SQL query to count matching usernames
        String query = "SELECT COUNT(email) FROM users WHERE email = ?";

        // Use try-with-resources for automatic closing of connections and statements
        try (Connection conn = new DB().getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            // Set the email parameter safely
            pst.setString(1, email);

            // Execute the query
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Get the value of the first (and only) column

                    if (count > 0) {
                        return true; // email exists
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, " Your email entered not exists!");
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error during username check: " + ex.getMessage());
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, ex.getMessage());

        }
        return false; // Username is Unavailable in table
    }

    public static void time() {
        LocalDateTime times = LocalDateTime.now();
        System.out.println(times);
        DateTimeFormatter pattern_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String format = pattern_format.format(times);
        System.out.println(format + " after parser");
    }


    public static void main(String[] args) {
//        time();

        System.out.println(does_Email_Exist("testvn@gmail.com"));
    }

}