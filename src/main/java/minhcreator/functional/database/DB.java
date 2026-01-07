package minhcreator.functional.database;

import raven.toast.Notifications;

import java.sql.*;

/**
 *
 * @author MinhCreatorVN
 */
public class DB {
    public static Connection conn = null;
    public Statement stmt;
    public PreparedStatement pstmt;
    private static final String DB_URL = "jdbc:mysql://localhost/warehouse";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private synchronized static void initializeConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
//            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Connection successful");
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Connection failed");
        }
    }

    public synchronized static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            initializeConnection();
        }
        return conn;
    }

    // Insert, Update, Delete
    public int executionSQL(String query) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = getConnection();
            stmt = connection.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(null, stmt, null);
        }
    }

    // Select
    public ResultSet selectSQL(String query) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = getConnection();
            stmt = connection.createStatement();
            return stmt.executeQuery(query);
            // Note: The caller is responsible for closing the ResultSet
        } catch (SQLException e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, e.getMessage());
            return null;
        }
    }

    private static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            // Don't close the connection here if you want to reuse it
            // if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Call this when your application shuts down
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // test
    public static void main(String[] args) {

    }
}