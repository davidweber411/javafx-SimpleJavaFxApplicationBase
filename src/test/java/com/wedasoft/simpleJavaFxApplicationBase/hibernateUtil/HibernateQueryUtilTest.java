package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.hibernateUtil.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HibernateQueryUtilTest {

    @SuppressWarnings("unused")
    @Test
    void hibernateQueryUtilTest() throws HibernateQueryUtilException {
        count_countAll();

        insert_insertOne();
        insert_insertMany();

        find_findById();
        find_findAll();
        find_findByFieldValue();

        update_updateOne();
        update_updateMany();

        delete_deleteOne();
        delete_deleteMany();
        delete_deleteAll();
    }

    private void delete_deleteOne() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(1));
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        HibernateQueryUtil.Delete.deleteOne(student);
        List<Student> students = HibernateQueryUtil.Find.findAll(Student.class);
        assertThat(students.size()).isEqualTo(2);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteOne(new Object()));
    }

    private void delete_deleteMany() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertMany(List.of(
                new Student("Anna", "Angus", "anton.angus@abc.com"),
                new Student("Berta", "Barbossa", "berta.barbossa@abc.com"),
                new Student("Celina", "Calensika", "celina.calensika@abc.com")));
        assertThat(HibernateQueryUtil.Find.findAll(Student.class).size()).isEqualTo(3);

        List<Student> threeStudents = HibernateQueryUtil.Find.findAll(Student.class);
        List<Student> twoStudents = List.of(threeStudents.get(0), threeStudents.get(1));

        HibernateQueryUtil.Delete.deleteMany(twoStudents);
        assertThat(HibernateQueryUtil.Find.findAll(Student.class).size()).isEqualTo(1);
        assertThat(HibernateQueryUtil.Find.findAll(Student.class).get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteMany(List.of(new Object())));
    }

    private void delete_deleteAll() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertMany(List.of(
                new Student("Anna", "Angus", "anton.angus@abc.com"),
                new Student("Berta", "Barbossa", "berta.barbossa@abc.com"),
                new Student("Celina", "Calensika", "celina.calensika@abc.com")));
        assertThat(HibernateQueryUtil.Find.findAll(Student.class).size()).isEqualTo(3);
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        assertThat(HibernateQueryUtil.Find.findAll(Student.class).size()).isEqualTo(0);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Delete.deleteAll(Object.class));
    }

    private void update_updateOne() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(1));
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");
        student.setFirstName("AAA");
        student.setLastName("BBB");
        student.setEmail("CCC");
        HibernateQueryUtil.Update.updateOne(student);
        student = HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(1));
        assertThat(student.getFirstName()).isEqualTo("AAA");
        assertThat(student.getLastName()).isEqualTo("BBB");
        assertThat(student.getEmail()).isEqualTo("CCC");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Update.updateOne(new Object()));
    }

    private void update_updateMany() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        assertThat(HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(0)).getFirstName()).isEqualTo("Anna");
        assertThat(HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(1)).getFirstName()).isEqualTo("Berta");
        assertThat(HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(2)).getFirstName()).isEqualTo("Celina");

        List<Student> modifiedStudents = HibernateQueryUtil.Find.findAll(Student.class).stream()
                .limit(2)
                .map(e -> {
                    e.setFirstName("JOHN");
                    return e;
                })
                .toList();
        HibernateQueryUtil.Update.updateMany(modifiedStudents);
        assertThat(HibernateQueryUtil.Find.findByFieldValue(Student.class, "firstName", "JOHN").size()).isEqualTo(2);
        assertThat(HibernateQueryUtil.Find.findByFieldValue(Student.class, "lastName", "Angus").get(0).getFirstName()).isEqualTo("JOHN");
        assertThat(HibernateQueryUtil.Find.findByFieldValue(Student.class, "lastName", "Barbossa").get(0).getFirstName()).isEqualTo("JOHN");
        assertThat(HibernateQueryUtil.Find.findByFieldValue(Student.class, "lastName", "Calensika").get(0).getFirstName()).isEqualTo("Celina");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Update.updateMany(List.of(new Object(), new Object())));
    }

    private void find_findByFieldValue() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Barbossa", "anna.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        List<Student> student = HibernateQueryUtil.Find.findByFieldValue(Student.class, "firstName", "Anna");
        assertThat(student.size()).isEqualTo(2);
        assertThat(student.get(0).getFirstName()).isEqualTo("Anna");
        assertThat(student.get(0).getLastName()).isEqualTo("Angus");
        assertThat(student.get(0).getEmail()).isEqualTo("anton.angus@abc.com");
        assertThat(student.get(1).getFirstName()).isEqualTo("Anna");
        assertThat(student.get(1).getLastName()).isEqualTo("Barbossa");
        assertThat(student.get(1).getEmail()).isEqualTo("anna.barbossa@abc.com");
        student = HibernateQueryUtil.Find.findByFieldValue(Student.class, "lastName", "Barbossa");
        assertThat(student.size()).isEqualTo(1);
        assertThat(student.get(0).getFirstName()).isEqualTo("Anna");
        assertThat(student.get(0).getLastName()).isEqualTo("Barbossa");
        assertThat(student.get(0).getEmail()).isEqualTo("anna.barbossa@abc.com");
        student = HibernateQueryUtil.Find.findByFieldValue(Student.class, "lastName", "DoesNotExist");
        assertThat(student.size()).isEqualTo(0);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Find.findByFieldValue(Student.class, "doesNotExist", "test"));
    }

    private void find_findAll() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        List<Student> student = HibernateQueryUtil.Find.findAll(Student.class);
        assertThat(student.size()).isEqualTo(3);
        assertThat(student.get(0).getFirstName()).isEqualTo("Anna");
        assertThat(student.get(0).getLastName()).isEqualTo("Angus");
        assertThat(student.get(0).getEmail()).isEqualTo("anton.angus@abc.com");
        assertThat(student.get(1).getFirstName()).isEqualTo("Berta");
        assertThat(student.get(1).getLastName()).isEqualTo("Barbossa");
        assertThat(student.get(1).getEmail()).isEqualTo("berta.barbossa@abc.com");
        assertThat(student.get(2).getFirstName()).isEqualTo("Celina");
        assertThat(student.get(2).getLastName()).isEqualTo("Calensika");
        assertThat(student.get(2).getEmail()).isEqualTo("celina.calensika@abc.com");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Find.findAll(Object.class));
    }

    private void find_findById() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "berta.barbossa@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "celina.calensika@abc.com"));
        Student student = HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(1));
        assertThat(student.getFirstName()).isEqualTo("Berta");
        assertThat(student.getLastName()).isEqualTo("Barbossa");
        assertThat(student.getEmail()).isEqualTo("berta.barbossa@abc.com");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Find.findById(Student.class, 666));
    }

    private void insert_insertOne() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Count.countAll(Student.class);
        assertThat(count).isEqualTo(1);
        Student student = HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(0));
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Insert.insertOne(new Object()));
    }

    private void insert_insertMany() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertMany(List.of(new Student("Anna", "Angus", "anton.angus@abc.com"), new Student("Berta", "Barbossa", "anton.angus@abc.com"), new Student("Celina", "Calensika", "anton.angus@abc.com")));
        long count = HibernateQueryUtil.Count.countAll(Student.class);
        assertThat(count).isEqualTo(3);
        Student student = HibernateQueryUtil.Find.findById(Student.class, getIdOfEntityWithDbIndex(0));
        assertThat(student.getFirstName()).isEqualTo("Anna");
        assertThat(student.getLastName()).isEqualTo("Angus");
        assertThat(student.getEmail()).isEqualTo("anton.angus@abc.com");

        assertThrows(Exception.class, () -> HibernateQueryUtil.Insert.insertMany(List.of(new Object())));
    }

    private void count_countAll() throws HibernateQueryUtilException {
        HibernateQueryUtil.Delete.deleteAll(Student.class);
        HibernateQueryUtil.Insert.insertOne(new Student("Anna", "Angus", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Berta", "Barbossa", "anton.angus@abc.com"));
        HibernateQueryUtil.Insert.insertOne(new Student("Celina", "Calensika", "anton.angus@abc.com"));
        long count = HibernateQueryUtil.Count.countAll(Student.class);
        assertThat(count).isEqualTo(3);

        assertThrows(Exception.class, () -> HibernateQueryUtil.Count.countAll(Object.class));
    }

    public long getIdOfEntityWithDbIndex(int index) throws HibernateQueryUtilException {
        return HibernateQueryUtil.Find.findAll(Student.class).get(index).getId();
    }

}