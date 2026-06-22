package minhcreator.functional.database.dao;

import minhcreator.entity.InvoiceDetailEntity;
import minhcreator.entity.InvoiceEntity;
import minhcreator.functional.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class InvoiceDAO {

    public int save(InvoiceEntity invoice) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(invoice);
            tx.commit();
            return invoice.getInvoiceId();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void saveDetail(InvoiceDetailEntity detail) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(detail);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void deleteInvoice(int invoiceId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            InvoiceEntity inv = session.get(InvoiceEntity.class, invoiceId);
            if (inv != null) {
                Query<?> q = session.createQuery(
                        "DELETE FROM InvoiceDetailEntity WHERE invoiceId = :iid");
                q.setParameter("iid", invoiceId);
                q.executeUpdate();
                session.remove(inv);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void deleteDetailsByInvoiceId(int invoiceId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "DELETE FROM InvoiceDetailEntity WHERE invoiceId = :iid");
            q.setParameter("iid", invoiceId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<InvoiceEntity> findByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<InvoiceEntity> q = session.createQuery(
                    "FROM InvoiceEntity WHERE userId = :uid ORDER BY createdAt DESC", InvoiceEntity.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<InvoiceEntity> searchByUserId(int userId, String searchText) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String pat = "%" + searchText + "%";
            Query<InvoiceEntity> q = session.createQuery(
                    "FROM InvoiceEntity WHERE userId = :uid AND CAST(invoiceId AS string) LIKE :pat " +
                    "ORDER BY createdAt DESC", InvoiceEntity.class);
            q.setParameter("uid", userId);
            q.setParameter("pat", pat);
            return q.list();
        }
    }

    public List<Object[]> getInvoiceDetails(int invoiceId, int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT p.name, d.quantity, d.unitPrice, (d.quantity * d.unitPrice) " +
                    "FROM InvoiceDetailEntity d JOIN ProductEntity p ON d.productId = p.id " +
                    "WHERE d.invoiceId = :iid AND d.userId = :uid", Object[].class);
            q.setParameter("iid", invoiceId);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> getIncomeByCategory(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT i.category, SUM(d.quantity * d.unitPrice) " +
                    "FROM InvoiceDetailEntity d JOIN InventoryEntity i ON d.productId = i.productId " +
                    "WHERE d.userId = :uid GROUP BY i.category", Object[].class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> getProfitByCategory(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT i.category, " +
                    "SUM((d.quantity * d.unitPrice) - (po.quantity * po.importPrice)) " +
                    "FROM InvoiceDetailEntity d " +
                    "JOIN PurchaseOrderEntity po ON d.productId = po.productId " +
                    "JOIN InventoryEntity i ON d.productId = i.productId " +
                    "WHERE d.userId = :uid GROUP BY i.category", Object[].class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> getMonthlyIncome(int userId, String startMonth) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT FUNCTION('DATE_FORMAT', createdAt, '%Y-%m'), SUM(totalAmount) " +
                    "FROM InvoiceEntity WHERE userId = :uid AND createdAt >= :start " +
                    "GROUP BY FUNCTION('DATE_FORMAT', createdAt, '%Y-%m') ORDER BY 1 DESC", Object[].class);
            q.setParameter("uid", userId);
            q.setParameter("start", LocalDate.parse(startMonth + "-01"));
            return q.list();
        }
    }

    public List<Object[]> getDailyIncomeBetween(int userId, String start, String end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT createdAt, SUM(totalAmount) FROM InvoiceEntity " +
                    "WHERE userId = :uid AND createdAt BETWEEN :start AND :end " +
                    "GROUP BY createdAt ORDER BY createdAt", Object[].class);
            q.setParameter("uid", userId);
            q.setParameter("start", LocalDate.parse(start));
            q.setParameter("end", LocalDate.parse(end));
            return q.list();
        }
    }
}
