package minhcreator.functional.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Legacy JDBC connection manager. Credentials are loaded from
 * appConfig.properties at runtime, falling back to defaults.
 *
 * @author MinhCreatorVN
 */
public class DB {
    public static Connection conn = null;
    private static DB instance;
    public Statement stmt;
    public PreparedStatement pstmt;

    private static String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static String USER = "postgres";
    private static String PASSWORD = "postgres";

    static {
        loadDbConfig();
    }

    private static void loadDbConfig() {
        Properties props = new Properties();
        try (InputStream is = DB.class.getResourceAsStream(
                "/minhcreator/config/appConfig.properties")) {
            if (is != null) {
                props.load(is);
                DB_URL = props.getProperty("db.url", DB_URL);
                USER = props.getProperty("db.user", USER);
                PASSWORD = props.getProperty("db.password", PASSWORD);
            }
        } catch (IOException e) {
            System.err.println("Warning: could not load appConfig.properties: " + e.getMessage());
        }
    }

    public static synchronized DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    private synchronized static void initializeConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
    }

    public synchronized static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            initializeConnection();
        }
        return conn;
    }

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

    public ResultSet selectSQL(String query) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = getConnection();
            stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
