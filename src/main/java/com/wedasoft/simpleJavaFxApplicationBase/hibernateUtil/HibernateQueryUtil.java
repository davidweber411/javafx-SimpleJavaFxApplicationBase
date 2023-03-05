package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions.Condition;
import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions.EqualsCondition;
import jakarta.persistence.Column;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.*;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class HibernateQueryUtil {

    public static class Insert {
        /**
         * Inserts one entity into the database.
         *
         * @param entity Entity to insert.
         * @param <T>    Type of the entity.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean insertOne(T entity) throws HibernateQueryUtilException {
            return insertMany(List.of(entity));
        }

        /**
         * Inserts many entities into the database.
         *
         * @param entities Entities to insert.
         * @param <T>      Type of the entities.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean insertMany(List<T> entities) throws HibernateQueryUtilException {
            boolean isInserted = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities.forEach(session::persist);
                transaction.commit();
                isInserted = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "insertOne or insertMany", e);
            }
            return isInserted;
        }
    }

    public static class Update {
        /**
         * Updates one entity in the database.
         *
         * @param entity Entity to update.
         * @param <T>    Type of the entity.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean updateOne(T entity) throws HibernateQueryUtilException {
            return updateMany(List.of(entity));
        }

        /**
         * Updates many entities in the database.
         *
         * @param entities Entities to update.
         * @param <T>      Type of the entities.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean updateMany(List<T> entities) throws HibernateQueryUtilException {
            boolean executedSuccessfully = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities.forEach(session::merge);
                transaction.commit();
                executedSuccessfully = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "updateOne or updateMany", e);
            }
            return executedSuccessfully;
        }
    }

    public static class Delete {
        /**
         * Deletes one entity from the database.
         *
         * @param entity Entity to delete.
         * @param <T>    Type of the entity.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean deleteOne(T entity) throws HibernateQueryUtilException {
            return deleteMany(List.of(entity));
        }

        /**
         * Deletes many entities from the database.
         *
         * @param entities Entities to delete.
         * @param <T>      Type of the entities.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean deleteMany(List<T> entities) throws HibernateQueryUtilException {
            boolean executedSuccessfully = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                entities.forEach(session::remove);
                transaction.commit();
                executedSuccessfully = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "deleteOne or deleteMany", e);
            }
            return executedSuccessfully;
        }

        @Deprecated
        /**
         * Deletes all entities of the given type from the database.
         *
         * @param entityClass Entity type as class.
         * @param areYouSure  Security flag. Must be set to true.
         * @param <T>         Type of the entities.
         * @return True, if there were no errors while executing.
         */
        public static synchronized <T> boolean deleteAll(Class<T> entityClass, boolean areYouSure) throws HibernateQueryUtilException {
            if (!areYouSure) {
                throw new HibernateQueryUtilException("The security flag 'areYouSure' is not set to 'true'.", null);
            }
            boolean executedSuccessfully = false;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.createNativeQuery(String.format("delete from %s", entityClass.getSimpleName()), entityClass).executeUpdate();
                transaction.commit();
                executedSuccessfully = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "deleteAll", e);
            }
            return executedSuccessfully;
        }
    }

    @Deprecated
    public static class Aggregate {
        /**
         * Counts the amount of datasets of the given type in the database.
         *
         * @param entityClass Entity to count.
         * @param <T>         Type of the entity.
         * @return The number of datasets.
         */
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

    public static class Find {
        public static class Builder<ENTITY> {

            private final Class<ENTITY> entityClass;

            @SuppressWarnings("FieldMayBeFinal")
            private List<Condition<?>> conditions;

            private long offset;

            private long limit;

            @SuppressWarnings("FieldMayBeFinal")
            private List<Order> orderBys;

            private Builder(Class<ENTITY> entity) {
                this.entityClass = entity;
                this.conditions = new ArrayList<>();
                this.offset = -1;
                this.limit = -1;
                this.orderBys = new ArrayList<>();
            }

            public Builder<ENTITY> addCondition(String fieldNameOfClass, Condition<?> condition) throws NoSuchFieldException {
                if (condition == null) {
                    throw new NullPointerException("Condition must not be null.");
                }
                Field field = entityClass.getDeclaredField(fieldNameOfClass);
                condition.setAttributeName(field.getName());
                Column annotation = field.getAnnotation(Column.class);
                String columnName = isNull(annotation) ? field.getName() : (annotation.name().isEmpty() ? field.getName() : annotation.name());
                condition.setDatabaseColumnName(columnName);
                conditions.add(condition);
                return this;
            }

            public Builder<ENTITY> offset(long beginWithIndex) {
                this.offset = beginWithIndex;
                return this;
            }

            public Builder<ENTITY> limit(long amountOfDatasetsToLoad) {
                this.limit = amountOfDatasetsToLoad;
                return this;
            }

            public Builder<ENTITY> orderByInOrderOfList(List<Order> orderings) {
                this.orderBys.addAll(orderings);
                return this;
            }

            public List<ENTITY> findAll() throws HibernateQueryUtilException {
                List<ENTITY> entities = null;
                Transaction transaction = null;
                Query<ENTITY> query;
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    transaction = session.beginTransaction();

                    // step #1: initialize objects
                    HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
                    JpaCriteriaQuery<ENTITY> criteriaQuery = cb.createQuery(entityClass);
                    JpaRoot<ENTITY> root = criteriaQuery.from(entityClass);

                    // step #2: build sql query
                    criteriaQuery = criteriaQuery.select(root);
                    for (Condition<?> condition : conditions) {
                        if (condition instanceof EqualsCondition<?>) {
                            criteriaQuery.where(cb.equal(root.get(condition.getAttributeName()), condition.getValueToCheck()));
                        }
                    }
                    if (offset > -1) {
                        criteriaQuery.offset(offset);
                    }
                    if (limit > -1) {
                        criteriaQuery.fetch(limit);
                    }
                    if (!orderBys.isEmpty()) {
                        ArrayList<jakarta.persistence.criteria.Order> orderings = new ArrayList<>();
                        for (Order order : orderBys) {
                            if (order.asc()) {
                                orderings.add(cb.asc(root.get(order.attributeName())));
                            } else {
                                orderings.add(cb.desc(root.get(order.attributeName())));
                            }
                        }
                        criteriaQuery.orderBy(orderings);
                    }

                    // step #3: execute and get result
                    query = session.createQuery(criteriaQuery);
                    entities = query.getResultList();
                    transaction.commit();
                } catch (Exception e) {
                    handleCatch(transaction != null, transaction, "findWithBuilder", e);
                } finally {
                    if (requireNonNull(transaction).isActive()) {
                        transaction.rollback();
                    }
                }
                return entities;
            }

            public Class<ENTITY> getEntityClass() {
                return entityClass;
            }

            public List<Condition<?>> getConditions() {
                return conditions;
            }

            public long getOffset() {
                return offset;
            }

            public long getLimit() {
                return limit;
            }

            public List<Order> getOrderBys() {
                return orderBys;
            }
        }

        public static synchronized <ENTITY> Builder<ENTITY> findWithBuilder(Class<ENTITY> entity) {
            return new Builder<>(entity);
        }

    }

    private static void handleCatch(boolean transactionIsNotNull, Transaction transaction, String methodName, Exception e) throws HibernateQueryUtilException {
        if (transactionIsNotNull) {
            transaction.rollback();
        }
        throw new HibernateQueryUtilException("An exception occured while executing the method HibernateQueryUtil.*." + methodName + "().", e);
    }
}
