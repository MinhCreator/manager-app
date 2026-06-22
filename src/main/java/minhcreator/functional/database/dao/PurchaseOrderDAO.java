package minhcreator.functional.database.dao;

import minhcreator.entity.PurchaseOrderEntity;
import minhcreator.functional.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PurchaseOrderDAO {

    public void save(PurchaseOrderEntity po) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(po);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void deleteByProductId(int productId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "DELETE FROM PurchaseOrderEntity WHERE productId = :pid");
            q.setParameter("pid", productId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<PurchaseOrderEntity> findByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PurchaseOrderEntity> q = session.createQuery(
                    "FROM PurchaseOrderEntity WHERE userId = :uid ORDER BY date DESC", PurchaseOrderEntity.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> getCostByCategory(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT i.category, SUM(po.quantity * po.importPrice) " +
                    "FROM PurchaseOrderEntity po JOIN InventoryEntity i ON po.productId = i.productId " +
                    "WHERE po.userId = :uid GROUP BY i.category", Object[].class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<Object[]> getMonthlyExpense(int userId, String startMonth) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT FUNCTION('DATE_FORMAT', po.date, '%Y-%m'), SUM(po.quantity * po.importPrice) " +
                    "FROM PurchaseOrderEntity po WHERE po.userId = :uid AND po.date >= :start " +
                    "GROUP BY FUNCTION('DATE_FORMAT', po.date, '%Y-%m') ORDER BY 1 DESC", Object[].class);
            q.setParameter("uid", userId);
            q.setParameter("start", LocalDate.parse(startMonth + "-01"));
            return q.list();
        }
    }

    public List<Object[]> getDailyExpenseBetween(int userId, String start, String end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> q = session.createQuery(
                    "SELECT po.date, SUM(po.quantity * po.importPrice) " +
                    "FROM PurchaseOrderEntity po WHERE po.userId = :uid AND po.date BETWEEN :start AND :end " +
                    "GROUP BY po.date ORDER BY po.date", Object[].class);
            q.setParameter("uid", userId);
            q.setParameter("start", LocalDate.parse(start));
            q.setParameter("end", LocalDate.parse(end));
            return q.list();
        }
    }
}
