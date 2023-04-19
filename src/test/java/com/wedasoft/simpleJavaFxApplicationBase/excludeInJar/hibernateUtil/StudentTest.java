package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.hibernateUtil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTest {

    @Test
    void constructor_test() {
        Student s = new Student();
        assertThat(s).isNotNull();
        assertThat(s.getFirstName()).isNull();
        assertThat(s.getLastName()).isNull();
        assertThat(s.getEmail()).isNull();
        assertThat(s.getId()).isEqualTo(0);

        s = new Student("Harald", "Braun", "harald.braun@abc.com");
        assertThat(s).isNotNull();
        assertThat(s.getFirstName()).isEqualTo("Harald");
        assertThat(s.getLastName()).isEqualTo("Braun");
        assertThat(s.getEmail()).isEqualTo("harald.braun@abc.com");
        assertThat(s.getId()).isEqualTo(0);

        s = new Student("Harald", "Braun", "harald.braun@abc.com", 97);
        assertThat(s).isNotNull();
        assertThat(s.getFirstName()).isEqualTo("Harald");
        assertThat(s.getLastName()).isEqualTo("Braun");
        assertThat(s.getEmail()).isEqualTo("harald.braun@abc.com");
        assertThat(s.getId()).isEqualTo(0);
        assertThat(s.getAge()).isEqualTo(97);
    }

    @Test
    void allSetters_test() {
        Student s = new Student();
        s.setId(24);
        s.setFirstName("Harald");
        s.setLastName("Braun");
        s.setEmail("harald.braun@abc.com");
        s.setAge(97);
        assertThat(s).isNotNull();
        assertThat(s.getId()).isEqualTo(24);
        assertThat(s.getFirstName()).isEqualTo("Harald");
        assertThat(s.getLastName()).isEqualTo("Braun");
        assertThat(s.getEmail()).isEqualTo("harald.braun@abc.com");
        assertThat(s.getAge()).isEqualTo(97);
    }

    @Test
    void toString_test() {
        Student s = new Student("Harald", "Braun", "harald.braun@abc.com");
        assertThat(s.toString()).isEqualTo("Student [id=0, firstName=Harald, lastName=Braun, email=harald.braun@abc.com]");
    }

    @Test
    void fieldNameConstants_test() {
        assertThat(Student.Fields.id).isEqualTo("id");
        assertThat(Student.Fields.firstName).isEqualTo("firstName");
        assertThat(Student.Fields.lastName).isEqualTo("lastName");
        assertThat(Student.Fields.email).isEqualTo("email");
        assertThat(Student.Fields.age).isEqualTo("age");
    }

}