package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SceneUtilExceptionTest {

    @Test
    void test1() {
        SceneUtilException e = new SceneUtilException("Hello");
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Hello");
        assertThat(e.getCause()).isNull();
    }

    @Test
    void test2() {
        SceneUtilException e = new SceneUtilException("Hello", null);
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isEqualTo("Hello");
        assertThat(e.getCause()).isNull();

        try {
            throw new Exception("Test");
        } catch (Exception ex) {
            SceneUtilException sse = new SceneUtilException("Hello", ex);
            assertThat(sse).isNotNull();
            assertThat(sse.getMessage()).isEqualTo("Hello");
            assertThat(sse.getCause()).isNotNull();
        }
    }

}