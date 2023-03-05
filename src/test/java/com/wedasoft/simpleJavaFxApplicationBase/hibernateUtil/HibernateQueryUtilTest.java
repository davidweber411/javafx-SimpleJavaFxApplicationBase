package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.hibernateUtil.Student;
import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions.EqualsCondition;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        findWithBuilder_addConditions();
        findWithBuilder_findByAndConditions();
        findWithBuilder_offsetAndLimitAndEntityClass();
        findWithBuilder_orderBy();

        count_countAll();
    }

    void findWithBuilder_orderBy() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Dora", "Diablo", "dora.diablo@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Weber", "david.weber.original@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Weber", "david.weber.double@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Diablo", "david.diablo@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Eva", "Entity", "eva.entity@abc.com"));

        HibernateQueryUtil.Find.Builder<Student> builder = HibernateQueryUtil.Find.findWithBuilder(Student.class)
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

    private void findWithBuilder_offsetAndLimitAndEntityClass() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Dora", "Diablo", "dora.diablo@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Weber", "david.weber.original@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Weber", "david.weber.double@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Diablo", "david.diablo@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Eva", "Entity", "eva.entity@abc.com"));

        HibernateQueryUtil.Find.Builder<Student> builder = HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .offset(6)
                .limit(34);
        List<Student> students = builder.findAll();
        assertThat(students.size()).isEqualTo(2);
        assertThat(builder.getOffset()).isEqualTo(6);
        assertThat(builder.getLimit()).isEqualTo(34);
        assertThat(builder.getEntityClass()).isEqualTo(Student.class);

        students = HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .offset(2)
                .limit(3)
                .findAll();
        assertThat(students.size()).isEqualTo(3);
        assertThat(students.get(0).getFirstName()).isEqualTo("Celina");
        assertThat(students.get(1).getFirstName()).isEqualTo("Dora");
        assertThat(students.get(2).getFirstName()).isEqualTo("David");
    }

    private void findWithBuilder_findByAndConditions() throws HibernateQueryUtilException, NoSuchFieldException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Dora", "Diablo", "dora.diablo@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Weber", "david.weber.original@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Weber", "david.weber.double@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("David", "Diablo", "david.diablo@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Eva", "Entity", "eva.entity@abc.com"));

        List<Student> students;
        students = HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .addCondition(Student.Fields.lastName, new EqualsCondition<>("Weber"))
                .addCondition(Student.Fields.email, new EqualsCondition<>("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
        assertThat(students.get(0).getFirstName()).isEqualTo("David");
        assertThat(students.get(0).getLastName()).isEqualTo("Weber");
        assertThat(students.get(0).getEmail()).isEqualTo("david.weber.original@abc.com");

        students = HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .addCondition(Student.Fields.id, new EqualsCondition<>(5))
                .addCondition(Student.Fields.email, new EqualsCondition<>("david.weber.original@abc.com"))
                .findAll();
        assertThat(students.size()).isEqualTo(1);
    }

    private void findWithBuilder_addConditions() throws NoSuchFieldException {
        HibernateQueryUtil.Find.Builder<Student> findBuilder = HibernateQueryUtil.Find.findWithBuilder(Student.class);
        assertThat(findBuilder.getConditions().size()).isEqualTo(0);
        assertThat(findBuilder.getConditions()).isNotNull();

        assertThrows(Exception.class, () -> HibernateQueryUtil.Find.findWithBuilder(Student.class).addCondition(Student.Fields.firstName, null));

        findBuilder = HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("David"))
                .addCondition(Student.Fields.id, new EqualsCondition<>(27));
        assertThat(findBuilder.getConditions().size()).isEqualTo(2);
        assertThat(findBuilder.getConditions().get(0)).isNotNull();
        assertThat(findBuilder.getConditions().get(0).getAttributeName()).isEqualTo("firstName");
        assertThat(findBuilder.getConditions().get(0).getDatabaseColumnName()).isEqualTo("first_name");
        assertThat(findBuilder.getConditions().get(1).getAttributeName()).isEqualTo("id");
    }

    private void delete_deleteOne() throws HibernateQueryUtilException, NoSuchFieldException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = HibernateQueryUtil.Find.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        HibernateQueryUtil.Delete.deleteOne(student);
        List<Student> students = HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll();
        assertThat(students.size()).isEqualTo(2);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteOne(new Object()));
    }

    private void delete_deleteMany() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "berta.barbossa@abc.com"), new Student("Celina", "Calensika", "celina.calensika@abc.com")));
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().size()).isEqualTo(3);

        List<Student> threeStudents = HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll();
        List<Student> twoStudents = List.of(threeStudents.get(0), threeStudents.get(1));

        HibernateQueryUtil.Delete.deleteMany(twoStudents);
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().size()).isEqualTo(1);
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteMany(List.of(new Object())));
    }

    private void delete_deleteAll() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "berta.barbossa@abc.com"), new Student("Celina", "Calensika", "celina.calensika@abc.com")));
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().size()).isEqualTo(3);
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().size()).isEqualTo(0);
        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteAll(Object.class, true));
        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteAll(Student.class, false));
    }

    private void update_updateOne() throws HibernateQueryUtilException, NoSuchFieldException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = HibernateQueryUtil.Find.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        student.setFirstName("AAA");
        student.setLastName("BBB");
        student.setEmail("CCC");
        HibernateQueryUtil.Update.updateOne(student);
        student = HibernateQueryUtil.Find.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("AAA");
        assertThat(student.getLastName()).isEqualTo("BBB");
        assertThat(student.getEmail()).isEqualTo("CCC");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Update.updateOne(new Object()));
    }

    private void update_updateMany() throws HibernateQueryUtilException, NoSuchFieldException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(0)))
                .findAll().get(0).getFirstName()).isEqualTo("Anna");
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(1)))
                .findAll().get(0).getFirstName()).isEqualTo("Berta");
        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(2)))
                .findAll().get(0).getFirstName()).isEqualTo("Celina");

        @SuppressWarnings("all")
        List<Student> modifiedStudents = HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().stream().limit(2).map(e -> {
            e.setFirstName("JOHN");
            return e;
        }).toList();
        HibernateQueryUtil.Update.updateMany(modifiedStudents);

        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("JOHN"))
                .findAll().size()).isEqualTo(2);

        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.firstName, new EqualsCondition<>("JOHN"))
                .findAll().get(0).getLastName()).isEqualTo("Angus");

        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.lastName, new EqualsCondition<>("Barbossa"))
                .findAll().get(0).getFirstName()).isEqualTo("JOHN");

        assertThat(HibernateQueryUtil.Find.findWithBuilder(Student.class)
                .addCondition(Student.Fields.lastName, new EqualsCondition<>("Calensika"))
                .findAll().get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Update.updateMany(List.of(new Object(), new Object())));
    }

    private void insert_insertOne() throws HibernateQueryUtilException, NoSuchFieldException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Aggregate.countAll(Student.class);
        assertThat(count).isEqualTo(1);
        Student student = HibernateQueryUtil.Find.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(0))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Insert.insertOne(new Object()));
    }

    private void insert_insertMany() throws HibernateQueryUtilException, NoSuchFieldException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "anton.angus@abc.com"), new Student("Celina", "Calensika", "anton.angus@abc.com")));
        long count = HibernateQueryUtil.Aggregate.countAll(Student.class);
        assertThat(count).isEqualTo(3);

        Student student = HibernateQueryUtil.Find.findWithBuilder(Student.class).addCondition(Student.Fields.id, new EqualsCondition<>(getIdOfEntityWithDbIndex(0))).findAll().get(0);
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Insert.insertMany(List.of(new Object())));
    }

    private void count_countAll() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class, true);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Aggregate.countAll(Student.class);
        assertThat(count).isEqualTo(3);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Aggregate.countAll(Object.class));
    }

    public long getIdOfEntityWithDbIndex(int index) throws HibernateQueryUtilException {
        return HibernateQueryUtil.Find.findWithBuilder(Student.class).findAll().get(index).getId();
    }

}