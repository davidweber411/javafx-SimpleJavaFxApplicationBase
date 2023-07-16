package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SceneSwitcherExceptionTest {

    @Test
    void sceneSwitcherExceptionTest1() {
        SceneSwitcherException e = new SceneSwitcherException();
        assertThat(e).isNotNull();
        assertThat(e.getMessage()).isNull();
        assertThat(e.getCause()).isNull();
    }

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
    }

}