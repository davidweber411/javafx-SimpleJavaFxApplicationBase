package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions.*;
import jakarta.persistence.Column;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaDelete;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.*;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class HibernateQueryUtil {

    public static class Inserter {

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

    public static class Updater {

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

    public static class DeleterBuilder {
        // deleteOne()
        // deleteMany()
        // deleteByConditions
    }

    public static class Deleter {

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

                // step #1: initialize objects
                HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
                JpaCriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(entityClass);
                Root<T> root = criteriaDelete.from(entityClass);

                // step #2: set where clauses
                //        criteriaDelete.where(cb.lessThanOrEqualTo(e.get("amount"), amount));

                // step #3: execute
                session.createMutationQuery(criteriaDelete).executeUpdate();

                transaction.commit();
                executedSuccessfully = true;
            } catch (Exception e) {
                handleCatch(transaction != null, transaction, "deleteAll", e);
            }
            return executedSuccessfully;
        }

    }

    public static class Finder {

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

            /**
             * Adds a condition to the query. <br> All conditions added with this method are concatenated in the resulting
             * query with a logical AND.
             *
             * @param fieldNameOfClass The field to query.
             * @param condition        The condition that shall match.
             * @return The builder object.
             * @throws NoSuchFieldException If a field does not exist with this name in the entity class.
             */
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

            /**
             * Sets the offset of the matching datasets that shall be returned. <br> Example given: If the offset is "2", then
             * the first 2 elements of the resulting datasets will not be returned by the query.
             *
             * @param beginWithIndex The index of the first element to return by the query.
             * @return The builder object.
             */
            public Builder<ENTITY> offset(long beginWithIndex) {
                this.offset = beginWithIndex;
                return this;
            }

            /**
             * Sets the maximum amount of the matching datasets that shall be returned by the query.
             *
             * @param amountOfDatasetsToLoad The amount of datasets.
             * @return The builder object.
             */
            public Builder<ENTITY> limit(long amountOfDatasetsToLoad) {
                this.limit = amountOfDatasetsToLoad;
                return this;
            }

            /**
             * Sets the ordering of the query. <br> The result is ordered by all order objects in the list. <br> The order of
             * the orderings of the result is equivalent to their position in the list.
             *
             * @param orderings The orderings as a list.
             * @return The builder object.
             */
            public Builder<ENTITY> orderByInOrderOfList(List<Order> orderings) {
                this.orderBys.addAll(orderings);
                return this;
            }

            /**
             * Executes the created query and gets the result.
             *
             * @return A list of the matching entities.
             * @throws Exception If an error occurs.
             */
            public List<ENTITY> findAll() throws Exception {
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
                    List<Predicate> andConcatenatedPredicates = new ArrayList<>();
                    for (Condition<?> condition : conditions) {
                        if (condition instanceof EqualsCondition<?>) {
                            andConcatenatedPredicates.add(cb.equal(root.get(condition.getAttributeName()), condition.getValueToCheck()));
                        }
                        if (condition instanceof NotEqualsCondition<?>) {
                            andConcatenatedPredicates.add(cb.notEqual(root.get(condition.getAttributeName()), condition.getValueToCheck()));
                        }
                        if (condition instanceof LowerThanCondition<?>) {
                            andConcatenatedPredicates.add(cb.lt(root.get(condition.getAttributeName()), (Number) condition.getValueToCheck()));
                        }
                        if (condition instanceof LowerEqualsThanCondition<?>) {
                            andConcatenatedPredicates.add(cb.le(root.get(condition.getAttributeName()), (Number) condition.getValueToCheck()));
                        }
                        if (condition instanceof GreaterThanCondition<?>) {
                            andConcatenatedPredicates.add(cb.gt(root.get(condition.getAttributeName()), (Number) condition.getValueToCheck()));
                        }
                        if (condition instanceof GreaterEqualsThanCondition<?>) {
                            andConcatenatedPredicates.add(cb.ge(root.get(condition.getAttributeName()), (Number) condition.getValueToCheck()));
                        }
                        if (condition instanceof LikeCondition<?>) {
                            andConcatenatedPredicates.add(cb.like(root.get(condition.getAttributeName()), (String) condition.getValueToCheck()));
                        }
                        if (condition instanceof LikeInCaseSensitiveCondition<?>) {
                            andConcatenatedPredicates.add(cb.ilike(root.get(condition.getAttributeName()), (String) condition.getValueToCheck()));
                        }
                        if (condition instanceof NotLikeCondition<?>) {
                            andConcatenatedPredicates.add(cb.notLike(root.get(condition.getAttributeName()), (String) condition.getValueToCheck()));
                        }
                        if (condition instanceof NotLikeInCaseSensitiveCondition<?>) {
                            andConcatenatedPredicates.add(cb.notIlike(root.get(condition.getAttributeName()), (String) condition.getValueToCheck()));
                        }
                    }
                    if (!andConcatenatedPredicates.isEmpty()) {
                        criteriaQuery.where(cb.and(andConcatenatedPredicates.toArray(new Predicate[0])));
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

        /**
         * Creates a builder object which is used for creating queries.
         *
         * @param entity   The entity type to find.
         * @param <ENTITY> The entity class.
         * @return The builder object.
         */
        public static synchronized <ENTITY> Builder<ENTITY> findWithBuilder(Class<ENTITY> entity) {
            return new Builder<>(entity);
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

    private static void handleCatch(boolean transactionIsNotNull, Transaction transaction, String methodName, Exception e) throws HibernateQueryUtilException {
        if (transactionIsNotNull) {
            transaction.rollback();
        }
        throw new HibernateQueryUtilException("An exception occured while executing the method HibernateQueryUtil.*." + methodName + "().", e);
    }
}
