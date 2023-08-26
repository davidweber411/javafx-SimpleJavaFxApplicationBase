package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.HibernateQueryUtil.Deleter;
import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.HibernateQueryUtil.Finder;
import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.HibernateQueryUtil.Inserter;
import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.HibernateQueryUtil.Updater;
import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HibernateQueryUtilTest {

    @SuppressWarnings("unused")
    @Test
    void hibernateQueryUtilTest() throws Exception {
        insert_insertOne();
        insert_insertMany();

        update_updateOne();
        update_updateMany();

        delete_deleteOne();
        delete_deleteMany();
        delete_deleteAll();

        find_findWithBuilder_addConditions();
        find_findWithBuilder_findByAddedConditions();
        find_findWithBuilder_offsetAndLimitAndEntityClass();
        find_findWithBuilder_orderBy();
        find_countAll();
    }

    void find_findWithBuilder_orderBy() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Dora", "Diablo", "dora.diablo@abc.com"));
        Inserter.insertOne(new Student("David", "Weber", "david.weber.original@abc.com"));
        Inserter.insertOne(new Student("David", "Weber", "david.weber.double@abc.com"));
        Inserter.insertOne(new Student("David", "Diablo", "david.diablo@abc.com"));
        Inserter.insertOne(new Student("Eva", "Entity", "eva.entity@abc.com"));

        Finder.Builder<Student> builder = Finder.findWithBuilder(Student.class)
                .orderByInOrderOfList(List.of(new Order(Student_.FIRST_NAME, false), new Order(Student_.LAST_NAME, true)));
        assertThat(builder.getOrderBys().size()).isEqualTo(2);
        assertThat(builder.getOrderBys().get(0).attributeName()).isEqualTo(Student_.FIRST_NAME);
        assertThat(builder.getOrderBys().get(0).asc()).isEqualTo(false);
        assertThat(builder.getOrderBys().get(1).attributeName()).isEqualTo(Student_.LAST_NAME);
        assertThat(builder.getOrderBys().get(1).asc()).isEqualTo(true);

        List<Student> students = builder.findAll();
        assertThat(students.get(0).getFirstName()).isEqualTo("Eva");
        assertThat(students.get(students.size() - 1).getFirstName()).isEqualTo("Anna");
        assertThat(students.get(2).getLastName()).isEqualTo("Diablo");
        assertThat(students.get(3).getLastName()).isEqualTo("Weber");
        assertThat(students.get(4).getLastName()).isEqualTo("Weber");
    }

    private void find_findWithBuilder_offsetAndLimitAndEntityClass() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Dora", "Diablo", "dora.diablo@abc.com"));
        Inserter.insertOne(new Student("David", "Weber", "david.weber.original@abc.com"));
        Inserter.insertOne(new Student("David", "Weber", "david.weber.double@abc.com"));
        Inserter.insertOne(new Student("David", "Diablo", "david.diablo@abc.com"));
        Inserter.insertOne(new Student("Eva", "Entity", "eva.entity@abc.com"));

        Finder.Builder<Student> builder = Finder.findWithBuilder(Student.class)
                .offset(6)
                .limit(34);
        List<Student> students = builder.findAll();
        assertThat(students.size()).isEqualTo(2);
        assertThat(builder.getOffset()).isEqualTo(6);
        assertThat(builder.getLimit()).isEqualTo(34);
        assertThat(builder.getEntityClass()).isEqualTo(Student.class);

        students = Finder.findWithBuilder(Student.class)
                .offset(2)
                .limit(3)
                .findAll();
        assertThat(students.size()).isEqualTo(3);
        assertThat(students.get(0).getFirstName()).isEqualTo("Celina");
        assertThat(students.get(1).getFirstName()).isEqualTo("Dora");
        assertThat(students.get(2).getFirstName()).isEqualTo("David");
    }

    private void find_findWithBuilder_findByAddedConditions() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com", 1));
        Inserter.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com", 2));
        Inserter.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com", 3));
        Inserter.insertOne(new Student("Dora", "Diablo", "dora.diablo@abc.com", 4));
        Inserter.insertOne(new Student("David", "Weber", "david.weber.original@abc.com", 4));
        Inserter.insertOne(new Student("David", "Weber", "david.weber.double@abc.com", 5));
        Inserter.insertOne(new Student("David", "Diablo", "david.diablo@abc.com", 1));
        Inserter.insertOne(new Student("David", "Dumbo", "david.dumbo@abc.com", 3));
        Inserter.insertOne(new Student("Eva", "Entity", "eva.entity@abc.com", 2));

        findWithBuilder_findByAddedConditions_isEqualTo();
        findWithBuilder_findByAddedConditions_isLowerThan();
        findWithBuilder_findByAddedConditions_isLowerThanOrEqualTo();
        findWithBuilder_findByAddedConditions_isGreaterThan();
        findWithBuilder_findByAddedConditions_isGreaterThanOrEqualTo();
        findWithBuilder_findByAddedConditions_isNotEqualTo();
        findWithBuilder_findByAddedConditions_isLikeCaseSensitive();
        findWithBuilder_findByAddedConditions_isLikeInCaseSensitive();
        findWithBuilder_findByAddedConditions_isNotLikeCaseSensitive();
        findWithBuilder_findByAddedConditions_isNotLikeInCaseSensitive();
    }

    private void findWithBuilder_findByAddedConditions_isNotLikeInCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeInCaseSensitiveCondition<>("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeInCaseSensitive("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeInCaseSensitiveCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeInCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeInCaseSensitiveCondition<>("%aVI%"))
                .addCondition(Student_.LAST_NAME, new NotLikeInCaseSensitiveCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeInCaseSensitive("%aVI%"))
                .addCondition(Student_.LAST_NAME, isNotLikeInCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeInCaseSensitiveCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new NotLikeInCaseSensitiveCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeInCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isNotLikeInCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeInCaseSensitiveCondition<>("%xxxxxxxxxxxxx%"))
                .addCondition(Student_.LAST_NAME, new NotLikeInCaseSensitiveCondition<>("yyyyyyyyyyyyyy%"))
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(6);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeInCaseSensitive("%xxxxxxxxxxxxx%"))
                .addCondition(Student_.LAST_NAME, isNotLikeInCaseSensitive("yyyyyyyyyyyyyy%"))
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(3))
                .findAll();
        assertThat(students.size()).isEqualTo(6);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.LAST_NAME, new NotLikeInCaseSensitiveCondition<>("%eBEr%"))
                .addCondition(Student_.AGE, new GreaterEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.LAST_NAME, isNotLikeInCaseSensitive("%eBEr%"))
                .addCondition(Student_.AGE, isGreaterThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isNotLikeCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeCondition<>("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeCaseSensitive("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeCondition<>("%aVI%"))
                .addCondition(Student_.LAST_NAME, new NotLikeCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeCaseSensitive("%aVI%"))
                .addCondition(Student_.LAST_NAME, isNotLikeCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new NotLikeCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isNotLikeCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new NotLikeCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new NotLikeCondition<>("Web%"))
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isNotLikeCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isNotLikeCaseSensitive("Web%"))
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(3))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
    }

    private void findWithBuilder_findByAddedConditions_isLikeInCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeInCaseSensitiveCondition<>("%AVi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeInCaseSensitive("%AVi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeInCaseSensitiveCondition<>("%AvI%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeInCaseSensitive("%AvI%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeInCaseSensitiveCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeInCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeInCaseSensitiveCondition<>("%aVI%"))
                .addCondition(Student_.LAST_NAME, new LikeInCaseSensitiveCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeInCaseSensitive("%aVI%"))
                .addCondition(Student_.LAST_NAME, isLikeInCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeInCaseSensitiveCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new LikeInCaseSensitiveCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeInCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isLikeInCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeInCaseSensitiveCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new LikeInCaseSensitiveCondition<>("Web%"))
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeInCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isLikeInCaseSensitive("Web%"))
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isLikeCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeCondition<>("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeCaseSensitive("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeCondition<>("%aVi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeCaseSensitive("%aVi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new LikeCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isLikeCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new LikeCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isLikeCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new LikeCondition<>("%avi%"))
                .addCondition(Student_.LAST_NAME, new LikeCondition<>("Web%"))
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isLikeCaseSensitive("%avi%"))
                .addCondition(Student_.LAST_NAME, isLikeCaseSensitive("Web%"))
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isNotEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new NotEqualsCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(7);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isNotEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(7);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new NotEqualsCondition<>(4))
                .addCondition(Student_.FIRST_NAME, new NotEqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isNotEqualTo(4))
                .addCondition(Student_.FIRST_NAME, isNotEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(4))
                .addCondition(Student_.FIRST_NAME, new NotEqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(4))
                .addCondition(Student_.FIRST_NAME, isNotEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
    }

    private void findWithBuilder_findByAddedConditions_isGreaterThanOrEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new GreaterEqualsThanCondition<>(5))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isGreaterThanOrEqualTo(5))
                .findAll();
        assertThat(students.size()).isEqualTo(1);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new GreaterEqualsThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isGreaterThanOrEqualTo(3))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new GreaterEqualsThanCondition<>(3))
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(3);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isGreaterThanOrEqualTo(3))
                .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(3);
    }

    private void findWithBuilder_findByAddedConditions_isGreaterThan() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new GreaterThanCondition<>(5))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isGreaterThan(5))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new GreaterThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(3);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isGreaterThan(3))
                .findAll();
        assertThat(students.size()).isEqualTo(3);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new GreaterThanCondition<>(3))
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isGreaterThan(3))
                .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
    }

    private void findWithBuilder_findByAddedConditions_isLowerThanOrEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(8);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(8);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(4))
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(4))
                .addCondition(Student_.FIRST_NAME, isEqualTo("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerEqualsThanCondition<>(3))
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThanOrEqualTo(3))
                .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
    }

    private void findWithBuilder_findByAddedConditions_isLowerThan() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(6);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThan(4))
                .findAll();
        assertThat(students.size()).isEqualTo(6);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerThanCondition<>(4))
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThan(4))
                .addCondition(Student_.FIRST_NAME, isEqualTo("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, new LowerThanCondition<>(3))
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.AGE, isLowerThan(3))
                .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .addCondition(Student_.LAST_NAME, new EqualsCondition<>("Weber"))
                .addCondition(Student_.EMAIL, new EqualsCondition<>("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("David");
        assertThat(students.get(0).getLastName()).isEqualTo("Weber");
        assertThat(students.get(0).getEmail()).isEqualTo("david.weber.original@abc.com");
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
                .addCondition(Student_.LAST_NAME, isEqualTo("Weber"))
                .addCondition(Student_.EMAIL, isEqualTo("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("David");
        assertThat(students.get(0).getLastName()).isEqualTo("Weber");
        assertThat(students.get(0).getEmail()).isEqualTo("david.weber.original@abc.com");

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .addCondition(Student_.AGE, new EqualsCondition<>(4))
                .addCondition(Student_.EMAIL, new EqualsCondition<>("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, isEqualTo("David"))
                .addCondition(Student_.AGE, isEqualTo(4))
                .addCondition(Student_.EMAIL, isEqualTo("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void find_findWithBuilder_addConditions() throws NoSuchFieldException {
        Finder.Builder<Student> findBuilder = Finder.findWithBuilder(Student.class);
        assertThat(findBuilder.getConditions().size()).isEqualTo(0);
        assertThat(findBuilder.getConditions()).isNotNull();

        assertThrows(Exception.class, () -> Finder.findWithBuilder(Student.class).addCondition(Student_.FIRST_NAME, null));

        findBuilder = Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("David"))
                .addCondition(Student_.ID, new EqualsCondition<>(27));
        assertThat(findBuilder.getConditions().size()).isEqualTo(2);
        assertThat(findBuilder.getConditions().get(0)).isNotNull();
        assertThat(findBuilder.getConditions().get(0).getAttributeName()).isEqualTo("firstName");
        assertThat(findBuilder.getConditions().get(0).getDatabaseColumnName()).isEqualTo("first_name");
        assertThat(findBuilder.getConditions().get(1).getAttributeName()).isEqualTo("id");
    }

    private void find_countAll() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Finder.countAll(Student.class);
        assertThat(count).isEqualTo(3);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Finder.countAll(Object.class));
    }

    private void delete_deleteOne() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        Inserter.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = Finder.findWithBuilder(Student.class).addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        Deleter.deleteOne(student);
        List<Student> students = Finder.findWithBuilder(Student.class).findAll();
        assertThat(students.size()).isEqualTo(2);

        assertThrows(Exception.class, () -> Deleter.deleteOne(new Object()));
    }

    private void delete_deleteMany() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "berta.barbossa@abc.com"), new Student("Celina", "Calensika", "celina.calensika@abc.com")));
        assertThat(Finder.findWithBuilder(Student.class).findAll().size()).isEqualTo(3);

        List<Student> threeStudents = Finder.findWithBuilder(Student.class).findAll();
        List<Student> twoStudents = List.of(threeStudents.get(0), threeStudents.get(1));

        Deleter.deleteMany(twoStudents);
        assertThat(Finder.findWithBuilder(Student.class).findAll().size()).isEqualTo(1);
        assertThat(Finder.findWithBuilder(Student.class).findAll().get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> Deleter.deleteMany(List.of(new Object())));
    }

    private void delete_deleteAll() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "berta.barbossa@abc.com"), new Student("Celina", "Calensika", "celina.calensika@abc.com")));
        assertThat(Finder.findWithBuilder(Student.class).findAll().size()).isEqualTo(3);
        Deleter.deleteAll(Student.class, true);
        assertThat(Finder.findWithBuilder(Student.class).findAll().size()).isEqualTo(0);
        assertThrows(Exception.class, () -> Deleter.deleteAll(Object.class, true));
        assertThrows(Exception.class, () -> Deleter.deleteAll(Student.class, false));
    }

    private void update_updateOne() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        Inserter.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = Finder.findWithBuilder(Student.class).addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        student.setFirstName("AAA");
        student.setLastName("BBB");
        student.setEmail("CCC");
        Updater.updateOne(student);
        student = Finder.findWithBuilder(Student.class).addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("AAA");
        assertThat(student.getLastName()).isEqualTo("BBB");
        assertThat(student.getEmail()).isEqualTo("CCC");

        assertThrows(Exception.class, () -> Updater.updateOne(new Object()));
    }

    private void update_updateMany() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        Inserter.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        Inserter.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(0)))
                .findAll().get(0).getFirstName()).isEqualTo("Anna");
        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(1)))
                .findAll().get(0).getFirstName()).isEqualTo("Berta");
        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(2)))
                .findAll().get(0).getFirstName()).isEqualTo("Celina");

        @SuppressWarnings("all")
        List<Student> modifiedStudents = Finder.findWithBuilder(Student.class).findAll().stream().limit(2).map(e -> {
            e.setFirstName("JOHN");
            return e;
        }).toList();
        Updater.updateMany(modifiedStudents);

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("JOHN"))
                .findAll().size()).isEqualTo(2);

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.FIRST_NAME, new EqualsCondition<>("JOHN"))
                .findAll().get(0).getLastName()).isEqualTo("Angus");

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.LAST_NAME, new EqualsCondition<>("Barbossa"))
                .findAll().get(0).getFirstName()).isEqualTo("JOHN");

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student_.LAST_NAME, new EqualsCondition<>("Calensika"))
                .findAll().get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> Updater.updateMany(List.of(new Object(), new Object())));
    }

    private void insert_insertOne() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Finder.countAll(Student.class);
        assertThat(count).isEqualTo(1);
        Student student = Finder.findWithBuilder(Student.class).addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(0))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> Inserter.insertOne(new Object()));
    }

    private void insert_insertMany() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "anton.angus@abc.com"), new Student("Celina", "Calensika", "anton.angus@abc.com")));
        long count = HibernateQueryUtil.Finder.countAll(Student.class);
        assertThat(count).isEqualTo(3);

        Student student = Finder.findWithBuilder(Student.class).addCondition(Student_.ID, new EqualsCondition<>(getIdOfEntityWithDbIndex(0))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> Inserter.insertMany(List.of(new Object())));
    }

    private long getIdOfEntityWithDbIndex(int index) throws Exception {
        return Finder.findWithBuilder(Student.class).findAll().get(index).getId();
    }

}