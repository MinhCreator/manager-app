package minhcreator.functional.database.dao;

import minhcreator.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import minhcreator.functional.database.HibernateUtil;

import java.util.List;

public class UserDAO {

    public void save(UserEntity user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void update(UserEntity user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
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
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) session.remove(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public UserEntity findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(UserEntity.class, id);
        }
    }

    public UserEntity findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserEntity> q = session.createQuery(
                    "FROM UserEntity WHERE email = :email", UserEntity.class);
            q.setParameter("email", email);
            return q.uniqueResult();
        }
    }

    public UserEntity findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserEntity> q = session.createQuery(
                    "FROM UserEntity WHERE username = :username", UserEntity.class);
            q.setParameter("username", username);
            return q.uniqueResult();
        }
    }

    public UserEntity findByUsernameOrEmail(String credential) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserEntity> q = session.createQuery(
                    "FROM UserEntity WHERE username = :cred OR email = :cred", UserEntity.class);
            q.setParameter("cred", credential);
            return q.uniqueResult();
        }
    }

    public List<UserEntity> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserEntity", UserEntity.class).list();
        }
    }
}
