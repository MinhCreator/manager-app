package minhcreator.service;

import minhcreator.component.model.Product;
import minhcreator.entity.InventoryEntity;
import minhcreator.entity.ProductEntity;
import minhcreator.entity.PurchaseOrderEntity;
import minhcreator.entity.SalesOrderEntity;
import minhcreator.functional.database.dao.InventoryDAO;
import minhcreator.functional.database.dao.ProductDAO;
import minhcreator.functional.database.dao.PurchaseOrderDAO;
import minhcreator.functional.database.dao.SalesOrderDAO;
import minhcreator.util.AppLogger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import raven.toast.Notifications;
import minhcreator.functional.database.HibernateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarehouseService {

    private static final ProductDAO productDAO = new ProductDAO();
    private static final InventoryDAO inventoryDAO = new InventoryDAO();
    private static final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
    private static final SalesOrderDAO salesOrderDAO = new SalesOrderDAO();

    public static void processStock(int ProductID, int quantity, double price, String type,
                                    String user_inv, String user_purchase, String user_sale) throws Exception {
        int userId = getCurrentUserId();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            if (type.equals("IN")) {
                session.createQuery(
                        "UPDATE InventoryEntity SET quantity = quantity + :qty WHERE productId = :pid")
                        .setParameter("qty", quantity)
                        .setParameter("pid", ProductID)
                        .executeUpdate();
            } else {
                session.createQuery(
                        "UPDATE InventoryEntity SET quantity = quantity - :qty WHERE productId = :pid AND quantity >= :qty")
                        .setParameter("qty", quantity)
                        .setParameter("pid", ProductID)
                        .executeUpdate();
            }

            session.persist(type.equals("IN")
                    ? new PurchaseOrderEntity(userId, ProductID, quantity, price, LocalDate.now())
                    : new SalesOrderEntity(userId, ProductID, quantity, price, LocalDate.now()));

            tx.commit();
            AppLogger.info("Warehouse", "Stock " + type + " for product " + ProductID + " qty=" + quantity);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Product updated successfully");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            AppLogger.error("Warehouse", "Stock operation failed: " + e.getMessage());
            throw e;
        }
    }

    public static int addNewProduct(String upid, String name, String category,
                                     double price, double sellingPrice, int initialQuantity,
                                     String user_inv, String user_product, String user_purchase, String user_sale) throws Exception {
        int userId = getCurrentUserId();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            ProductEntity product = new ProductEntity(userId, upid, name);
            session.persist(product);

            InventoryEntity inv = new InventoryEntity(product.getId(), userId, category, price, sellingPrice, initialQuantity);
            session.persist(inv);

            if (initialQuantity > 0) {
                session.persist(new PurchaseOrderEntity(userId, product.getId(), initialQuantity, price, LocalDate.now()));
            }

            tx.commit();
            AppLogger.info("Warehouse", "New product added: " + name + " (UPID=" + upid + ") id=" + product.getId());
            return product.getId();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            AppLogger.error("Warehouse", "Failed to add product: " + e.getMessage());
            throw e;
        }
    }

    public static List<Product> getAllProducts(String user_product, String user_inv) {
        int userId = getCurrentUserId();
        List<Product> list = new ArrayList<>();
        List<Object[]> rows = inventoryDAO.getProductsWithInventory(userId);
        for (Object[] row : rows) {
            list.add(new Product(
                    row[0] != null ? (int) row[0] : 0,
                    row[1] != null ? (String) row[1] : "",
                    row[2] != null ? (String) row[2] : "",
                    row[3] != null ? (String) row[3] : "",
                    row[4] != null ? (double) row[4] : 0.0,
                    row[5] != null ? (double) row[5] : 0.0,
                    row[6] != null ? (int) row[6] : 0
            ));
        }
        return list;
    }

    public static boolean deleteProduct(int productId, String user_inv, String user_purchase, String user_sale, String user_product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.createMutationQuery("DELETE FROM SalesOrderEntity WHERE productId = :pid")
                    .setParameter("pid", productId).executeUpdate();
            session.createMutationQuery("DELETE FROM PurchaseOrderEntity WHERE productId = :pid")
                    .setParameter("pid", productId).executeUpdate();
            session.createMutationQuery("DELETE FROM InvoiceDetailEntity WHERE productId = :pid")
                    .setParameter("pid", productId).executeUpdate();
            InventoryEntity inv = session.get(InventoryEntity.class, productId);
            if (inv != null) session.remove(inv);
            ProductEntity prod = session.get(ProductEntity.class, productId);
            if (prod != null) {
                session.remove(prod);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public static void UpdateSellPrice(int ProductID, double sell_price, String user_inv) throws Exception {
        inventoryDAO.updateSellingPrice(ProductID, sell_price);
    }

    public static void UpdateName_Category_UPID(int ProductID, String UPID, String name, String category, String user_inv, String user_product) throws Exception {
        productDAO.updateNameAndUPID(ProductID, UPID, name);
        inventoryDAO.updateCategory(ProductID, category);
    }

    public static void UpdatePrice(int ProductID, double price, String user_inv) throws Exception {
        inventoryDAO.updatePrice(ProductID, price);
    }

    public static void addPurchase_or_sale(org.hibernate.Session session, int ProductID, int quantity, double price, String type,
                                            String user_inv, String user_purchase, String user_sale) throws Exception {
        int userId = getCurrentUserId();
        session.persist(type.equals("IN")
                ? new PurchaseOrderEntity(userId, ProductID, quantity, price, LocalDate.now())
                : new SalesOrderEntity(userId, ProductID, quantity, price, LocalDate.now()));
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "import product successfully");
    }

    private static int getCurrentUserId() {
        return minhcreator.component.page.Login.session.getUserId();
    }
}
