package com.wedasoft.simpleJavaFxApplicationBase.commonUtil;

import org.junit.jupiter.api.Test;

import static com.wedasoft.simpleJavaFxApplicationBase.commonUtil.CommonUtils.isEmptyBlankOrNull;
import static com.wedasoft.simpleJavaFxApplicationBase.commonUtil.CommonUtils.roundNumberOnCommaspace;
import static org.assertj.core.api.Assertions.assertThat;

class CommonUtilsTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void isEmptyBlankOrNull_test() {
        String testString = "";
        assertThat(isEmptyBlankOrNull(testString)).isTrue();
        testString = "   ";
        assertThat(isEmptyBlankOrNull(testString)).isTrue();
        testString = null;
        assertThat(isEmptyBlankOrNull(testString)).isTrue();
        testString = "Hello";
        assertThat(isEmptyBlankOrNull(testString)).isFalse();
    }

    @Test
    void roundNumberOnCommaspace_double_test() {
        double testDouble = 937456.2083652567988769;
        assertThat(roundNumberOnCommaspace(testDouble, 1)).isEqualTo(937456.2);
        assertThat(roundNumberOnCommaspace(testDouble, 2)).isEqualTo(937456.21);
        assertThat(roundNumberOnCommaspace(testDouble, 3)).isEqualTo(937456.208);
        assertThat(roundNumberOnCommaspace(testDouble, 4)).isEqualTo(937456.2084);
        assertThat(roundNumberOnCommaspace(testDouble, 5)).isEqualTo(937456.20837);
    }

    @Test
    void roundNumberOnCommaspace_float_test() {
        float testFloat = 937.208365256132429f;
        assertThat(roundNumberOnCommaspace(testFloat, 1)).isEqualTo(937.2f);
        assertThat(roundNumberOnCommaspace(testFloat, 2)).isEqualTo(937.21f);
        assertThat(roundNumberOnCommaspace(testFloat, 3)).isEqualTo(937.208f);
        assertThat(roundNumberOnCommaspace(testFloat, 4)).isEqualTo(937.2084f);
        assertThat(roundNumberOnCommaspace(testFloat, 5)).isEqualTo(937.20837f);
    }


}