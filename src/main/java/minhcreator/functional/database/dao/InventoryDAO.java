package minhcreator.functional.database.dao;

import minhcreator.entity.InventoryEntity;
import minhcreator.functional.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class InventoryDAO {

    public void save(InventoryEntity inv) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(inv);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void update(InventoryEntity inv) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(inv);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(int productId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            InventoryEntity inv = session.get(InventoryEntity.class, productId);
            if (inv != null) session.remove(inv);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public InventoryEntity findByProductId(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(InventoryEntity.class, productId);
        }
    }

    public List<InventoryEntity> findByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<InventoryEntity> q = session.createQuery(
                    "FROM InventoryEntity WHERE userId = :uid", InventoryEntity.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public void updateQuantity(int productId, int quantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "UPDATE InventoryEntity SET quantity = quantity + :qty WHERE productId = :pid");
            q.setParameter("qty", quantity);
            q.setParameter("pid", productId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void setQuantity(int productId, int quantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "UPDATE InventoryEntity SET quantity = quantity - :qty WHERE productId = :pid AND quantity >= :qty");
            q.setParameter("qty", quantity);
            q.setParameter("pid", productId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void updateSellingPrice(int productId, double sellingPrice) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "UPDATE InventoryEntity SET sellingPrice = :sp WHERE productId = :pid");
            q.setParameter("sp", sellingPrice);
            q.setParameter("pid", productId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void updatePrice(int productId, double price) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "UPDATE InventoryEntity SET price = :p WHERE productId = :pid");
            q.setParameter("p", price);
            q.setParameter("pid", productId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void updateCategory(int productId, String category) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "UPDATE InventoryEntity SET category = :cat WHERE productId = :pid");
            q.setParameter("cat", category);
            q.setParameter("pid", productId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<Object[]> getProductsWithInventory(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT p.id, p.UPID, p.name, i.category, i.price, i.sellingPrice, i.quantity " +
                    "FROM ProductEntity p LEFT JOIN InventoryEntity i ON p.id = i.productId " +
                    "WHERE p.userId = :uid ORDER BY p.id", Object[].class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> getProductsWithInventorySorted(int userId, String sortField, String sortOrder) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String validSort = switch (sortField) {
                case "name" -> "p.name";
                case "category" -> "i.category";
                case "price" -> "i.price";
                case "quantity" -> "i.quantity";
                default -> "p.UPID";
            };
            String direction = "ASC".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC";
            String hql = "SELECT p.id, p.UPID, p.name, i.category, i.price, i.sellingPrice, i.quantity " +
                    "FROM ProductEntity p LEFT JOIN InventoryEntity i ON p.id = i.productId " +
                    "WHERE p.userId = :uid ORDER BY " + validSort + " " + direction;
            Query<Object[]> q = session.createQuery(hql, Object[].class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> searchProductsWithInventory(int userId, String searchText) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String pat = "%" + searchText + "%";
            Query<Object[]> q = session.createQuery(
                    "SELECT p.id, p.UPID, p.name, i.category, i.price, i.sellingPrice, i.quantity " +
                    "FROM ProductEntity p LEFT JOIN InventoryEntity i ON p.id = i.productId " +
                    "WHERE p.userId = :uid AND (p.name LIKE :pat OR i.category LIKE :pat OR p.UPID LIKE :pat) " +
                    "ORDER BY p.id", Object[].class);
            q.setParameter("uid", userId);
            q.setParameter("pat", pat);
            return q.list();
        }
    }
}
