package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.hibernateUtil.Student;
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
                .orderByInOrderOfList(List.of(new Order(Student.Fields.firstName, false), new Order(Student.Fields.lastName, true)));
        assertThat(builder.getOrderBys().size()).isEqualTo(2);
        assertThat(builder.getOrderBys().get(0).attributeName()).isEqualTo(Student.Fields.firstName);
        assertThat(builder.getOrderBys().get(0).asc()).isEqualTo(false);
        assertThat(builder.getOrderBys().get(1).attributeName()).isEqualTo(Student.Fields.lastName);
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
                .addCondition(Student.Fields.firstName, new NotLikeInCaseSensitiveCondition<>("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeInCaseSensitive("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeInCaseSensitiveCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeInCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeInCaseSensitiveCondition<>("%aVI%"))
                .addCondition(Student.Fields.lastName, new NotLikeInCaseSensitiveCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeInCaseSensitive("%aVI%"))
                .addCondition(Student.Fields.lastName, isNotLikeInCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeInCaseSensitiveCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new NotLikeInCaseSensitiveCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeInCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isNotLikeInCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeInCaseSensitiveCondition<>("%xxxxxxxxxxxxx%"))
                .addCondition(Student.Fields.lastName, new NotLikeInCaseSensitiveCondition<>("yyyyyyyyyyyyyy%"))
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(6);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeInCaseSensitive("%xxxxxxxxxxxxx%"))
                .addCondition(Student.Fields.lastName, isNotLikeInCaseSensitive("yyyyyyyyyyyyyy%"))
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(3))
                .findAll();
        assertThat(students.size()).isEqualTo(6);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.lastName, new NotLikeInCaseSensitiveCondition<>("%eBEr%"))
                .addCondition(Student.Fields.age, new GreaterEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.lastName, isNotLikeInCaseSensitive("%eBEr%"))
                .addCondition(Student.Fields.age, isGreaterThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isNotLikeCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeCondition<>("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeCaseSensitive("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeCondition<>("%aVI%"))
                .addCondition(Student.Fields.lastName, new NotLikeCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeCaseSensitive("%aVI%"))
                .addCondition(Student.Fields.lastName, isNotLikeCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(9);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new NotLikeCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isNotLikeCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new NotLikeCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new NotLikeCondition<>("Web%"))
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isNotLikeCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isNotLikeCaseSensitive("Web%"))
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(3))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
    }

    private void findWithBuilder_findByAddedConditions_isLikeInCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeInCaseSensitiveCondition<>("%AVi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeInCaseSensitive("%AVi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeInCaseSensitiveCondition<>("%AvI%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeInCaseSensitive("%AvI%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeInCaseSensitiveCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeInCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeInCaseSensitiveCondition<>("%aVI%"))
                .addCondition(Student.Fields.lastName, new LikeInCaseSensitiveCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeInCaseSensitive("%aVI%"))
                .addCondition(Student.Fields.lastName, isLikeInCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeInCaseSensitiveCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new LikeInCaseSensitiveCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeInCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isLikeInCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeInCaseSensitiveCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new LikeInCaseSensitiveCondition<>("Web%"))
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeInCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isLikeInCaseSensitive("Web%"))
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isLikeCaseSensitive() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeCondition<>("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeCaseSensitive("%avi"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeCondition<>("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeCaseSensitive("%avi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeCondition<>("%aVi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeCaseSensitive("%aVi%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new LikeCondition<>("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isLikeCaseSensitive("web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new LikeCondition<>("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isLikeCaseSensitive("Web%"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new LikeCondition<>("%avi%"))
                .addCondition(Student.Fields.lastName, new LikeCondition<>("Web%"))
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isLikeCaseSensitive("%avi%"))
                .addCondition(Student.Fields.lastName, isLikeCaseSensitive("Web%"))
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isNotEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new NotEqualsCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(7);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isNotEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(7);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new NotEqualsCondition<>(4))
                .addCondition(Student.Fields.firstName, new NotEqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isNotEqualTo(4))
                .addCondition(Student.Fields.firstName, isNotEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(4);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(4))
                .addCondition(Student.Fields.firstName, new NotEqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(4))
                .addCondition(Student.Fields.firstName, isNotEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
    }

    private void findWithBuilder_findByAddedConditions_isGreaterThanOrEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new GreaterEqualsThanCondition<>(5))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isGreaterThanOrEqualTo(5))
                .findAll();
        assertThat(students.size()).isEqualTo(1);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new GreaterEqualsThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(5);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isGreaterThanOrEqualTo(3))
                .findAll();
        assertThat(students.size()).isEqualTo(5);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new GreaterEqualsThanCondition<>(3))
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(3);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isGreaterThanOrEqualTo(3))
                .addCondition(Student.Fields.firstName, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(3);
    }

    private void findWithBuilder_findByAddedConditions_isGreaterThan() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new GreaterThanCondition<>(5))
                .findAll();
        assertThat(students.size()).isEqualTo(0);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isGreaterThan(5))
                .findAll();
        assertThat(students.size()).isEqualTo(0);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new GreaterThanCondition<>(3))
                .findAll();
        assertThat(students.size()).isEqualTo(3);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isGreaterThan(3))
                .findAll();
        assertThat(students.size()).isEqualTo(3);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new GreaterThanCondition<>(3))
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isGreaterThan(3))
                .addCondition(Student.Fields.firstName, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
    }

    private void findWithBuilder_findByAddedConditions_isLowerThanOrEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(8);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(4))
                .findAll();
        assertThat(students.size()).isEqualTo(8);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(4))
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(4))
                .addCondition(Student.Fields.firstName, isEqualTo("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerEqualsThanCondition<>(3))
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThanOrEqualTo(3))
                .addCondition(Student.Fields.firstName, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(2);
    }

    private void findWithBuilder_findByAddedConditions_isLowerThan() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerThanCondition<>(4))
                .findAll();
        assertThat(students.size()).isEqualTo(6);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThan(4))
                .findAll();
        assertThat(students.size()).isEqualTo(6);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerThanCondition<>(4))
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThan(4))
                .addCondition(Student.Fields.firstName, isEqualTo("Celina"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, new LowerThanCondition<>(3))
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.age, isLowerThan(3))
                .addCondition(Student.Fields.firstName, isEqualTo("David"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_findByAddedConditions_isEqualTo() throws Exception {
        List<Student> students;

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .addCondition(Student.Fields.lastName, new EqualsCondition<>("Weber"))
                .addCondition(Student.Fields.email, new EqualsCondition<>("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("David");
        assertThat(students.get(0).getLastName()).isEqualTo("Weber");
        assertThat(students.get(0).getEmail()).isEqualTo("david.weber.original@abc.com");
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isEqualTo("David"))
                .addCondition(Student.Fields.lastName, isEqualTo("Weber"))
                .addCondition(Student.Fields.email, isEqualTo("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("David");
        assertThat(students.get(0).getLastName()).isEqualTo("Weber");
        assertThat(students.get(0).getEmail()).isEqualTo("david.weber.original@abc.com");

        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .addCondition(Student.Fields.age, new EqualsCondition<>(4))
                .addCondition(Student.Fields.email, new EqualsCondition<>("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        students = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, isEqualTo("David"))
                .addCondition(Student.Fields.age, isEqualTo(4))
                .addCondition(Student.Fields.email, isEqualTo("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void find_findWithBuilder_addConditions() throws NoSuchFieldException {
        Finder.Builder<Student> findBuilder = Finder.findWithBuilder(Student.class);
        assertThat(findBuilder.getConditions().size()).isEqualTo(0);
        assertThat(findBuilder.getConditions()).isNotNull();

        assertThrows(Exception.class, () -> Finder.findWithBuilder(Student.class).addCondition(Student.Fields.firstName, null));

        findBuilder = Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .addCondition(Student.Fields.id, new EqualsCondition<>(27));
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
        Student student = Finder.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
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
        Student student = Finder.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        student.setFirstName("AAA");
        student.setLastName("BBB");
        student.setEmail("CCC");
        Updater.updateOne(student);
        student = Finder.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
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
                .addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(0)))
                .findAll().get(0).getFirstName()).isEqualTo("Anna");
        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1)))
                .findAll().get(0).getFirstName()).isEqualTo("Berta");
        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(2)))
                .findAll().get(0).getFirstName()).isEqualTo("Celina");

        @SuppressWarnings("all")
        List<Student> modifiedStudents = Finder.findWithBuilder(Student.class).findAll().stream().limit(2).map(e -> {
            e.setFirstName("JOHN");
            return e;
        }).toList();
        Updater.updateMany(modifiedStudents);

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("JOHN"))
                .findAll().size()).isEqualTo(2);

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("JOHN"))
                .findAll().get(0).getLastName()).isEqualTo("Angus");

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.lastName, new EqualsCondition<>("Barbossa"))
                .findAll().get(0).getFirstName()).isEqualTo("JOHN");

        assertThat(Finder.findWithBuilder(Student.class)
                .addCondition(Student.Fields.lastName, new EqualsCondition<>("Calensika"))
                .findAll().get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> Updater.updateMany(List.of(new Object(), new Object())));
    }

    private void insert_insertOne() throws Exception {
        Deleter.deleteAll(Student.class, true);
        Inserter.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Finder.countAll(Student.class);
        assertThat(count).isEqualTo(1);
        Student student = Finder.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(0))).findAll().get(0);
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

        Student student = Finder.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(0))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> Inserter.insertMany(List.of(new Object())));
    }

    private long getIdOfEntityWithDbIndex(int index) throws Exception {
        return Finder.findWithBuilder(Student.class).findAll().get(index).getId();
    }

}