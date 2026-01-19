package minhcreator.service;

import minhcreator.component.model.Product;
import minhcreator.functional.database.DB;
import raven.toast.Notifications;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * WarehouseService class provides methods for managing warehouse operations.
 *
 * @author MinhCreatorVN
 * @version 1.0 PreTest
 */
public class WarehouseService {
    private Connection conn;
    private PreparedStatement ps;

    public WarehouseService() {
    }

    /**
     * Processes the stock of a product.
     *
     * @param ProductID     the ID of the product
     * @param quantity      the quantity to process
     * @param price         the price of the product
     * @param type          the type of processing (IN for import, OUT for export)
     * @param user_inv      the name of the inventory table
     * @param user_purchase the name of the purchase table
     * @param user_sale     the name of the sale table
     * @throws Exception if there is an error processing the stock
     */
    public static void processStock(int ProductID, int quantity, double price, String type,
                                    String user_inv, String user_purchase, String user_sale) throws Exception {
        try {
            Connection conn = DB.getConnection();
            conn.setAutoCommit(false); // Đảm bảo tính toàn vẹn (Transaction)
            try {
                // 1. Cập nhật bảng tồn kho
                String sqlInv = type.equals("IN") ?
                        "INSERT INTO " + user_inv + " (product_id, quantity) VALUES (?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?" :
                        "UPDATE " + user_inv + " SET quantity = quantity - ? WHERE product_id = ?";
                PreparedStatement ps1 = conn.prepareStatement(sqlInv);
                ps1.setInt(1, type.equals("IN") ? ProductID : quantity);
                ps1.setInt(2, type.equals("IN") ? quantity : ProductID);
                if (type.equals("IN")) ps1.setInt(3, quantity);
                ps1.executeUpdate();

                // 2. Ghi lịch sử Nhập/Xuất để tính lợi nhuận (Write log imports/Exports product to cal profit and revene)
                addPurchase_or_sale(conn, ProductID, quantity, price, type, user_inv, user_purchase, user_sale);

                conn.commit();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Product updated successfully");
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new product to the warehouse
     *
     * @param upid            Unique Product ID
     * @param name            Product name
     * @param category        Product category
     * @param price           Purchase price
     * @param sellingPrice    Selling price
     * @param initialQuantity Initial quantity in stock
     * @return The ID of the newly created product, or -1 if failed
     * @throws SQLException If a database error occurs
     */
    public static int addNewProduct(String upid, String name, String category,
                                    double price, double sellingPrice, int initialQuantity,
                                    String user_inv, String user_product, String user_purchase, String user_sale) throws Exception {
        int productId = -1;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert into products table
            String sqlProduct = "INSERT INTO " + user_product + " (UPID, name) VALUES (?, ?)";
            try (PreparedStatement psProduct = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
                psProduct.setString(1, upid);
                psProduct.setString(2, name);
                int affectedRows = psProduct.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }

                try (ResultSet generatedKeys = psProduct.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productId = generatedKeys.getInt(1);
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "No ID obtained");
                        throw new SQLException("Creating product failed, no ID obtained.");
                    }
                }
            }

            // 2. Insert into inventory table
            String sqlInventory = "INSERT INTO " + user_inv + " (product_id, category, price, selling_price, quantity) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psInventory = conn.prepareStatement(sqlInventory)) {
                psInventory.setInt(1, productId);
                psInventory.setString(2, category);
                psInventory.setDouble(3, price);
                psInventory.setDouble(4, sellingPrice);
                psInventory.setInt(5, initialQuantity);
                psInventory.executeUpdate();

                // Update log import product into purchase table
                addPurchase_or_sale(conn, productId, initialQuantity, price, "IN", user_inv, user_purchase, user_sale);
            }

            conn.commit();
            return productId;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Log the error but don't mask the original exception
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Get a list of all products along with their inventory quantity.

    /**
     * Retrieves a list of all products along with their inventory quantity.
     *
     * @param user_product the name of the product table
     * @param user_inv     the name of the inventory table
     * @return a list of Product objects representing the products and their inventory
     * @throws SQLException if there is an error retrieving the products and inventory
     */
    public static List<Product> getAllProducts(String user_product, String user_inv) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.UPID, p.name, i.category, i.price, i.selling_price, i.quantity " +
                "FROM " + user_product + " p " +
                "LEFT JOIN " + user_inv + " i ON p.id = i.product_id";

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Product(

                        rs.getInt("id"),
                        rs.getString("UPID"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getDouble("selling_price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // delete product

    /**
     * Deletes a product from the database.
     *
     * @param productId     the ID of the product to be deleted
     * @param user_inv      the name of the inventory table
     * @param user_purchase the name of the purchase table
     * @param user_sale     the name of the sale table
     * @param user_product  the name of the product table
     * @return true if the product was deleted successfully, false otherwise
     * @throws SQLException if there is an error deleting the product
     */
    public static boolean deleteProduct(int productId, String user_inv, String user_purchase, String user_sale, String user_product) throws SQLException {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

            // 1. Delete from sales_orders (child of products)
            String deleteSalesSql = "DELETE FROM " + user_sale + " WHERE product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSalesSql)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // 2. Delete from purchase_orders (child of products)
            String deletePurchasesSql = "DELETE FROM " + user_purchase + " WHERE product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deletePurchasesSql)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // 3. Delete from inventory (child of products)
            String deleteInventorySql = "DELETE FROM " + user_inv + " WHERE product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteInventorySql)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // 4. Finally, delete from products
            String deleteProductSql = "DELETE FROM " + user_product + " WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteProductSql)) {
                ps.setInt(1, productId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // Product not found
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Update sell price of Product

    /**
     * Updates the sell price of a product.
     *
     * @param ProductID  the ID of the product to update
     * @param sell_price the new sell price of the product
     * @param user_inv   the name of the inventory table
     * @throws Exception if there is an error updating the sell price
     */
    public static void UpdateSellPrice(int ProductID, double sell_price, String user_inv) throws Exception {
        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false); // Đảm bảo tính toàn vẹn (Transaction)
            try {
                // Update sell price of Product
                String sqlInv = "UPDATE " + user_inv + " SET selling_price = ? WHERE product_id = ?";
                PreparedStatement ps1 = conn.prepareStatement(sqlInv);
                ps1.setDouble(1, sell_price);
                ps1.setInt(2, ProductID);
                ps1.executeUpdate();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }


    /**
     * Updates the name, category, and UPID of a product.
     *
     * @param ProductID    the ID of the product to update
     * @param UPID         the new UPID of the product
     * @param name         the new name of the product
     * @param category     the new category of the product
     * @param user_inv     the name of the inventory table
     * @param user_product the name of the products table
     * @throws Exception if there is an error updating the name, category, or UPID
     */
    public static void UpdateName_Category_UPID(int ProductID, String UPID, String name, String category, String user_inv, String user_product) throws Exception {
        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false); // Đảm bảo tính toàn vẹn (Transaction)
            try {
                // Update UPID and category of Product in inventory
                String sqlInv = "UPDATE " + user_inv + " SET category=? WHERE product_id = ?";

                // Update name of Product in products table
                String sqlProduct = "UPDATE " + user_product + " SET UPID=?, NAME=? WHERE id = ?";

                PreparedStatement ps1 = conn.prepareStatement(sqlInv);
                ps1.setString(1, category);
                ps1.setInt(2, ProductID);
                ps1.executeUpdate();


                PreparedStatement ps2 = conn.prepareStatement(sqlProduct);
                ps2.setString(1, UPID);
                ps2.setString(2, name);
                ps2.setInt(3, ProductID);
                ps2.executeUpdate();


                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /**
     * Updates the price of a product.
     *
     * @param ProductID the ID of the product to update
     * @param price     the new price of the product
     * @param user_inv  the name of the inventory table
     * @throws Exception if there is an error updating the price
     */
    public static void UpdatePrice(int ProductID, double price, String user_inv) throws Exception {
        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false); // Đảm bảo tính toàn vẹn (Transaction)
            try {
                // Update sell price of Product
                String sqlInv = "UPDATE " + user_inv + " SET price = ? WHERE product_id = ?";
                PreparedStatement ps1 = conn.prepareStatement(sqlInv);
                ps1.setDouble(1, price);
                ps1.setInt(2, ProductID);
                ps1.executeUpdate();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }


    /**
     * Writes a log of an import/export operation to the purchase or sale table.
     *
     * @param conn          the database connection
     * @param ProductID     the ID of the product being imported/exported
     * @param quantity      the quantity being imported/exported
     * @param price         the price of the product being imported/exported
     * @param type          the type of operation ("IN" for import, "OUT" for export)
     * @param user_inv      the name of the inventory table
     * @param user_purchase the name of the purchase table
     * @param user_sale     the name of the sale table
     * @throws Exception if there is an error writing the log
     */
    public static void addPurchase_or_sale(Connection conn, int ProductID, int quantity, double price, String type,
                                           String user_inv, String user_purchase, String user_sale) throws Exception {
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false); // Đảm bảo tính toàn vẹn (Transaction)
            try {

                //Ghi lịch sử Nhập/Xuất để tính lợi nhuận (Write log imports/Exports product to cal profit and revene)
                String sqlLog = type.equals("IN") ?
                        "INSERT INTO " + user_purchase + " (product_id, quantity, import_price, date) VALUES (?, ?, ?, CURDATE())" :
                        "INSERT INTO " + user_sale + " (product_id, quantity, selling_price, date) VALUES (?, ?, ?, CURDATE())";
                PreparedStatement ps2 = conn.prepareStatement(sqlLog);
                ps2.setInt(1, ProductID);
                ps2.setInt(2, quantity);
                ps2.setDouble(3, price);
                ps2.executeUpdate();

                conn.commit();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "import product successfully");
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}