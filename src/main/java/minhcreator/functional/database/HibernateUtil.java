package minhcreator.functional.database;

import minhcreator.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DEFAULT_DB_USER = "postgres";
    private static final String DEFAULT_DB_PASS = "postgres";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found on classpath", e);
        }
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties dbProps = loadDbConfig();

            return new Configuration()
                    .configure()
                    .setProperty("hibernate.connection.url",
                            dbProps.getProperty("db.url", DEFAULT_DB_URL))
                    .setProperty("hibernate.connection.username",
                            dbProps.getProperty("db.user", DEFAULT_DB_USER))
                    .setProperty("hibernate.connection.password",
                            dbProps.getProperty("db.password", DEFAULT_DB_PASS))
                    .addAnnotatedClass(UserEntity.class)
                    .addAnnotatedClass(ProductEntity.class)
                    .addAnnotatedClass(InventoryEntity.class)
                    .addAnnotatedClass(PurchaseOrderEntity.class)
                    .addAnnotatedClass(SalesOrderEntity.class)
                    .addAnnotatedClass(InvoiceEntity.class)
                    .addAnnotatedClass(InvoiceDetailEntity.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Hibernate initialization failed: " + ex);
        }
    }

    private static Properties loadDbConfig() {
        Properties props = new Properties();
        try (InputStream is = HibernateUtil.class.getResourceAsStream(
                "/minhcreator/config/appConfig.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            System.err.println("Warning: could not load appConfig.properties: " + e.getMessage());
        }
        return props;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
