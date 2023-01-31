package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateQueryUtilExceptionTest {

    @Test
    void hibernateQueryUtilException() {
        HibernateQueryUtilException e = new HibernateQueryUtilException("Hello", null);
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Hello");
        assertThat(e.getCause()).isNull();
    }

}