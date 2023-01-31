package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class HibernateQueryUtil {

    public static class Count {
        public static synchronized <T> Long countAll(Class<T> entityClass) throws HibernateQueryUtilException {
            Long count = null;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                count = session.createNativeQuery("select Count(*) from " + entityClass.getSimpleName(), Long.class).getSingleResult();
                transaction.commit();
            } catch (Exception e) {
                handleCatch(nonNull(transaction), transaction, "countAll", e);
            }
            return count;
        }
    }

    public static class Insert {
        public static synchronized <T> boolean insertOne(T entity) throws HibernateQueryUtilException {
            boolean isInserted = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(entity);
                transaction.commit();
                isInserted = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "insertOne", e);
            }
            return isInserted;
        }

        public static synchronized <T> boolean insertMany(List<T> entities) throws HibernateQueryUtilException {
            boolean isInserted = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities.forEach(session::persist);
                transaction.commit();
                isInserted = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "insertMany", e);
            }
            return isInserted;
        }

    }

    public static class Find {
        public static synchronized <T> T findById(Class<T> classOfEntity, long id) throws HibernateQueryUtilException {
            T entity = null;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entity = session.createQuery("from " + classOfEntity.getSimpleName() + " where id = :id", classOfEntity).setParameter("id", id).getSingleResult();
                transaction.commit();
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "findById", e);
            }
            return entity;
        }

        public static synchronized <T> List<T> findAll(Class<T> classOfEntity) throws HibernateQueryUtilException {
            List<T> entities = null;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities = session.createQuery("from " + classOfEntity.getSimpleName(), classOfEntity).getResultList();
                transaction.commit();
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "findAll", e);
            }
            return entities;
        }

        /**
         * @param classOfEntity The entity class.
         * @param fieldName     The name of the class field, NOT the column name.
         * @param fieldValue    The value to search for.
         * @param <T>           Type of the entity class.
         * @param <V>           Type of the value to search.
         * @return A list of entities.
         */
        public static synchronized <T, V> List<T> findByFieldValue(Class<T> classOfEntity, String fieldName, V fieldValue) throws HibernateQueryUtilException {
            List<T> entities = null;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                Query<T> query = session.createQuery(" FROM " + classOfEntity.getSimpleName() + " WHERE " + fieldName + " = :fieldValueParam", classOfEntity);
                query.setParameter("fieldValueParam", fieldValue);
                entities = query.getResultList();
                transaction.commit();
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "findByFieldValue", e);
            } finally {
                if (requireNonNull(transaction).isActive()) {
                    transaction.rollback();
                }
            }
            return entities;
        }
    }

    public static class Update {
        public static synchronized <T> boolean updateOne(T entity) throws HibernateQueryUtilException {
            boolean isUpdated = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.merge(entity);
                transaction.commit();
                isUpdated = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "updateOne", e);
            }
            return isUpdated;
        }

        public static synchronized <T> boolean updateMany(List<T> entities) throws HibernateQueryUtilException {
            boolean isUpdated = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities.forEach(session::merge);
                transaction.commit();
                isUpdated = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "updateMany", e);
            }
            return isUpdated;
        }
    }

    public static class Delete {
        public static synchronized <T> boolean deleteOne(T entity) throws HibernateQueryUtilException {
            boolean isDeleted = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.remove(entity);
                transaction.commit();
                isDeleted = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "deleteOne", e);
            }
            return isDeleted;
        }

        public static synchronized <T> boolean deleteMany(List<T> entities) throws HibernateQueryUtilException {
            boolean isDeleted = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities.forEach(session::remove);
                transaction.commit();
                isDeleted = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "deleteMany", e);
            }
            return isDeleted;
        }

        public static synchronized <T> boolean deleteAll(Class<T> entityClass) throws HibernateQueryUtilException {
            boolean isDeleted = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.createNativeQuery(String.format("delete from %s", entityClass.getSimpleName()), entityClass).executeUpdate();
                transaction.commit();
                isDeleted = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "deleteAll", e);
            }
            return isDeleted;
        }

    }

    private static void handleCatch(boolean transactionIsNotNull, Transaction transaction, String methodName, Exception e) throws HibernateQueryUtilException {
        if (transactionIsNotNull) {
            transaction.rollback();
        }
        throw new HibernateQueryUtilException("An exception occured while executing HibernateQueryUtil.*." + methodName + "().", e);
    }
}
