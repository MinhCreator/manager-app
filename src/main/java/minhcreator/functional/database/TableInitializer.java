package minhcreator.functional.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TableInitializer creates unified tables for Hibernate-based schema.
 * Uses Hibernate's hbm2ddl.auto=update, so tables are auto-created.
 * This class is kept for backward compatibility and explicit table creation.
 *
 * @author MinhCreatorVN
 */
public class TableInitializer {

    public static String[] table_sql(String username) {
        // Unified schema — tables are shared across users with user_id column.
        // Hibernate hbm2ddl.auto handles table creation.
        // This method is kept for backward compatibility only.
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(255), " +
                "email VARCHAR(255), " +
                "password VARCHAR(255)" +
                ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS products (" +
                "id SERIAL PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "UPID VARCHAR(50), " +
                "name VARCHAR(255), " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        String CREATE_INVENTORY_TABLE = "CREATE TABLE IF NOT EXISTS inventory (" +
                "product_id INT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "category VARCHAR(255), " +
                "price DOUBLE PRECISION, " +
                "selling_price DOUBLE PRECISION, " +
                "quantity INT DEFAULT 0, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        String CREATE_PURCHASE_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS purchase_orders (" +
                "id SERIAL PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "product_id INT, " +
                "quantity INT, " +
                "import_price DOUBLE PRECISION, " +
                "date DATE, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        String CREATE_SALES_ORDERS_TABLE = "CREATE TABLE IF NOT EXISTS sales_orders (" +
                "id SERIAL PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "product_id INT, " +
                "quantity INT, " +
                "selling_price DOUBLE PRECISION, " +
                "date DATE, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        String CREATE_INVOICES_TABLE = "CREATE TABLE IF NOT EXISTS invoices (" +
                "invoice_id SERIAL PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "customer_name VARCHAR(255), " +
                "total_amount DOUBLE PRECISION, " +
                "created_at DATE, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        String CREATE_INVOICE_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS invoice_details (" +
                "detail_id SERIAL PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "invoice_id INT, " +
                "product_id INT, " +
                "quantity INT, " +
                "unit_price DOUBLE PRECISION, " +
                "FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        return new String[]{
                CREATE_USERS_TABLE,
                CREATE_PRODUCTS_TABLE,
                CREATE_INVENTORY_TABLE,
                CREATE_PURCHASE_ORDERS_TABLE,
                CREATE_SALES_ORDERS_TABLE,
                CREATE_INVOICES_TABLE,
                CREATE_INVOICE_DETAILS_TABLE
        };
    }

    public static void initializeTable(String username) {
        String[] tables = table_sql(username);
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                stmt.execute(table);
            }
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void dropAllTables(String username) {
        // Hibernate hbm2ddl will handle schema. This is kept for compatibility.
        String[] tables = {
                "invoice_details",
                "invoices",
                "sales_orders",
                "purchase_orders",
                "inventory",
                "products",
                "users"
        };
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                try {
                    stmt.execute("DROP TABLE IF EXISTS " + table + " CASCADE");
                } catch (SQLException e) {
                    System.err.println("Error dropping table " + table + ": " + e.getMessage());
                }
            }
            System.out.println("All tables dropped successfully!");
        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
        }
    }

    public static void resetTable(String username) {
        dropAllTables(username);
        initializeTable(username);
    }
}
