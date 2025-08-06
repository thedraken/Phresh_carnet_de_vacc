package com.phresh;

import com.google.inject.Inject;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionHolder<T> {

    private final static Logger logger = Logger.getLogger(SessionHolder.class.getName());

    private final SQLLiteConfigure sqlLiteConfigure;
    private final Object lock = new Object();
    private final Class<T> clazz;
    private Session session;
    private SessionFactory sessionFactory;

    @Inject
    public SessionHolder(SQLLiteConfigure sqlLiteConfigure, Class<T> clazz) {
        this.sqlLiteConfigure = sqlLiteConfigure;
        this.clazz = clazz;
    }

    private Session getSession() {
        if (this.session == null || !this.session.isOpen()) {
            openSession();
        }
        return session;
    }

    public void clearSession() {
        if (this.session != null && session.isOpen()) {
            try {
                this.session.clear();
                this.session.close();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Error closing session, at the moment the session is " + (session.isOpen() ? "open" : "closed"));
                logger.log(Level.WARNING, ex.getMessage());
            } finally {
                this.session = null;
            }
        } else if (this.session != null)
            this.session = null;
    }

    private void openSession() {
        clearSession();
        synchronized (lock) {
            if (sessionFactory == null || sessionFactory.isClosed()) {
                sessionFactory = sqlLiteConfigure.buildSessionFactory();
            }
        }
        if (sessionFactory != null) {
            this.session = sessionFactory.openSession();
            this.session.setHibernateFlushMode(FlushMode.MANUAL);
            logger.info("Session opened");
        } else {
            logger.info("Session Factory is null, can't open session");
            throw new RuntimeException("Session factory is null, unable to open a session");
        }
    }

    public T get(Serializable id) {
        return getSession().get(clazz, id);
    }

    public void refresh(Object object) {
        getSession().refresh(object);
    }

    public Transaction beginTransaction() {
        if (isNestedTransaction()) {
            return getSession().getTransaction();
        }
        return getSession().beginTransaction();
    }

    public boolean isNestedTransaction() {
        return getSession().getTransaction() != null && getSession().getTransaction().isActive();
    }

    public void persist(Object object) {
        getSession().persist(object);
    }

    public Object merge(Object object) {
        return getSession().merge(object);
    }

    protected void flush() {
        getSession().flush();
    }

    protected void delete(Object object) {
        getSession().delete(object);
    }

    public void evict(Object object) {
        getSession().evict(object);
    }

    public boolean isNotPersisted(Object object) {
        boolean isPersisted = getSession().contains(object);
        if (isPersisted) {
            logger.log(Level.INFO, "Object is persisted");
        }
        return !isPersisted;
    }

    public Query<T> createQuery(String query) {
        return getSession().createNamedQuery(query, clazz);
    }

    public Query<Number> createCountQuery(String query) {
        return getSession().createNamedQuery(query, Number.class);
    }

    public void save(T object) {
        boolean isNestedTransaction = isNestedTransaction();
        Transaction transaction = beginTransaction();
        try {
            persist(object);
            flush();
            if (!isNestedTransaction) {
                transaction.commit();
            }
        } finally {
            if (!isNestedTransaction && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void saveAll(List<T> objects) {
        boolean isNestedTransaction = isNestedTransaction();
        Transaction transaction = beginTransaction();
        try {
            objects.forEach(this::save);
            if (!isNestedTransaction) {
                transaction.commit();
            }
        } finally {
            if (!isNestedTransaction && transaction.isActive()) {
                transaction.rollback();
            }
        }

    }

    public List<T> findAll() {
        String hql = "select t from " + clazz.getSimpleName() + " t";
        Query<T> query = createQuery(hql);
        return query.list();
    }
}
