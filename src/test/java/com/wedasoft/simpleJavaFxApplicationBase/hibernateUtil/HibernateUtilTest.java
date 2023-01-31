package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateUtilTest {

    @Test
    void getSessionFactory_checkForNotNull() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        assertThat(sessionFactory).isNotNull();
        sessionFactory = HibernateUtil.getSessionFactory();
        assertThat(sessionFactory).isNotNull();
    }

    @Test
    void shutdown_checkForIsNull() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        assertThat(sessionFactory).isNotNull();
        HibernateUtil.shutdown();
    }

}