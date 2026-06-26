package minhcreator.functional.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static DB instance;

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

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public int executionSQL(String query) {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ResultSet selectSQL(String query) {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
