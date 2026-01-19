package minhcreator.debugFeature;

import java.util.UUID;

/**
 *
 * @author MinhCreatorVN
 */
public class functional_debug {
//
//    public static boolean does_Email_Exist(String email) {
//        // The SQL query to count matching usernames
//        String query = "SELECT COUNT(email) FROM users WHERE email = ?";
//
//        // Use try-with-resources for automatic closing of connections and statements
//        try (Connection conn = new DB().getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
//
//            // Set the email parameter safely
//            pst.setString(1, email);
//
//            // Execute the query
//            try (ResultSet rs = pst.executeQuery()) {
//                if (rs.next()) {
//                    int count = rs.getInt(1); // Get the value of the first (and only) column
//
//                    if (count > 0) {
//                        return true; // email exists
//                    } else {
//                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, " Your email entered not exists!");
//                    }
//                }
//            }
//        } catch (SQLException ex) {
//            System.err.println("Database Error during username check: " + ex.getMessage());
//            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, ex.getMessage());
//
//        }
//        return false; // Username is Unavailable in table
//    }
//
//    public static void time() {
//        LocalDateTime times = LocalDateTime.now();
//        System.out.println(times);
//        DateTimeFormatter pattern_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String format = pattern_format.format(times);
//        System.out.println(format + " after parser");
//    }


    public static void main(String[] args) {
//        time();

//        System.out.println(does_Email_Exist("testvn@gmail.com"));
//        String subString = uniqueID.substring(0, 8);
//        System.out.println(subString);
//
//        for (int i = 0; i <= 10; i++) {
//            String uniqueID = UUID.randomUUID().toString();
//            System.out.println(uniqueID);
//        }
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid);

//        System.out.println("b8f4983a-7092-4b6e-adac-e052ffc27e37".length());
//        System.out.println("e052ffc27e37".length());
//        System.out.println("b8f4983a-7092-4b6e-adac-e052ffc27e37".substring(24));
    }


}