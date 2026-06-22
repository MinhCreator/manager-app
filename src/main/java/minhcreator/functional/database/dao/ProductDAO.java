package minhcreator.functional.database.dao;

import minhcreator.entity.ProductEntity;
import minhcreator.functional.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductDAO {

    public int save(ProductEntity product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(product);
            tx.commit();
            return product.getId();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void update(ProductEntity product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            ProductEntity p = session.get(ProductEntity.class, id);
            if (p != null) session.remove(p);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public ProductEntity findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ProductEntity.class, id);
        }
    }

    public List<ProductEntity> findByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ProductEntity> q = session.createQuery(
                    "FROM ProductEntity WHERE userId = :uid", ProductEntity.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public List<ProductEntity> searchByUserId(int userId, String searchText) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String pattern = "%" + searchText + "%";
            Query<ProductEntity> q = session.createQuery(
                    "FROM ProductEntity WHERE userId = :uid AND (name LIKE :pat OR UPID LIKE :pat)",
                    ProductEntity.class);
            q.setParameter("uid", userId);
            q.setParameter("pat", pattern);
            return q.list();
        }
    }

    public void updateNameAndUPID(int id, String UPID, String name) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<?> q = session.createQuery(
                    "UPDATE ProductEntity SET UPID = :upid, name = :name WHERE id = :id");
            q.setParameter("upid", UPID);
            q.setParameter("name", name);
            q.setParameter("id", id);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
