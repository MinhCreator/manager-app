package minhcreator.functional.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TableInitializer class is used to create and initialize the database tables.
 *
 * @author MinhCreatorVN
 */
public class TableInitializer {

    // create table
    public static String[] table_sql(String username) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + username + "_products (id INT PRIMARY KEY AUTO_INCREMENT, UPID VARCHAR(50) UNIQUE, name VARCHAR(255))";

        String CREATE_INVENTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + username + "_inventory (product_id INT PRIMARY KEY,UPID VARCHAR(255) UNIQUE,category VARCHAR(255),price DOUBLE,selling_price DOUBLE, quantity INT DEFAULT 0,FOREIGN KEY (product_id) REFERENCES " + username + "_products(id) ON DELETE CASCADE,FOREIGN KEY (UPID) REFERENCES " + username + "_products(UPID) ON DELETE CASCADE)";

        String CREATE_PURCHASE_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS " + username + "_purchase_orders (id INT PRIMARY KEY AUTO_INCREMENT,product_id INT,quantity INT,import_price DOUBLE,date DATE, FOREIGN KEY (product_id) REFERENCES " + username + "_products(id) ON DELETE CASCADE)";

        String CREATE_SALES_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS " + username + "_sales_orders (id INT PRIMARY KEY AUTO_INCREMENT,product_id INT,quantity INT,selling_price DOUBLE,date DATE, FOREIGN KEY (product_id) REFERENCES " + username + "_products(id) ON DELETE CASCADE)";

        String CREATE_INVOICES_TABLE = "CREATE TABLE IF NOT EXISTS " + username + "_invoices (invoice_id INT PRIMARY KEY AUTO_INCREMENT,customer_name VARCHAR(255),total_amount DOUBLE,created_at DATE)";

        String CREATE_INVOICE_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + username + "_invoice_details (detail_id INT PRIMARY KEY AUTO_INCREMENT,invoice_id INT,product_id INT,quantity INT,unit_price DOUBLE,FOREIGN KEY (invoice_id) REFERENCES " + username + "_invoices(invoice_id) ON DELETE CASCADE,FOREIGN KEY (product_id) REFERENCES " + username + "_products(id) ON DELETE CASCADE)";

        return new String[]{CREATE_PRODUCTS_TABLE, CREATE_INVENTORY_TABLE, CREATE_PURCHASE_ORDERS_TABLE, CREATE_SALES_ORDERS_TABLE, CREATE_INVOICES_TABLE, CREATE_INVOICE_DETAILS_TABLE};
    }

    public static void initializeTable(String username) {
        String[] tables = table_sql(username);
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement()) {

            // Disable foreign key checks temporarily
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");

            // Create tables
            for (String table : tables) {
                stmt.execute(table);
            }

            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to drop all tables (useful for testing)

    /**
     * Drops all tables in the database.
     *
     * @param username the username of the database
     */
    public static void dropAllTables(String username) {
        String[] tables = {
                username + "_invoice_details",
                username + "_invoices",
                username + "_sales_orders",
                username + "_purchase_orders",
                username + "_inventory",
                username + "_products"
        };

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SET FOREIGN_KEY_CHECKS=0");

            for (String table : tables) {
                try {
                    stmt.execute("DROP TABLE IF EXISTS " + table);
                } catch (SQLException e) {
                    System.err.println("Error dropping table " + table + ": " + e.getMessage());
                }
            }

            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
            System.out.println("All tables dropped successfully!");

        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
        }
    }

    // Reset database (drop and recreate all tables)

    /**
     * Resets the database by dropping all tables and reinitializing them.
     *
     * @param username the username of the database
     */
    public static void resetTable(String username) {
        dropAllTables(username);
        initializeTable(username);
    }
}