package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SceneSwitcherExceptionTest {

    @Test
    void sceneSwitcherExceptionTest2() {
        SceneSwitcherException e = new SceneSwitcherException("Hello");
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Hello");
        assertThat(e.getCause()).isNull();
    }

    @Test
    void sceneSwitcherExceptionTest3() {
        SceneSwitcherException e = new SceneSwitcherException("Hello", null);
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Hello");
        assertThat(e.getCause()).isNull();

        try {
            throw new Exception("Test");
        } catch (Exception ex) {
            SceneSwitcherException sse = new SceneSwitcherException("Hello", ex);
            assertThat(sse).isNotNull();
            assertThat(sse.getMessage()).isEqualTo("Hello");
            assertThat(sse.getCause()).isNotNull();
        }
    }

}