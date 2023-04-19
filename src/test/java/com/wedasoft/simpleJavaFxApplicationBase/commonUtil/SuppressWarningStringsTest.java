package com.wedasoft.simpleJavaFxApplicationBase.commonUtil;

import org.junit.jupiter.api.Test;

import static com.wedasoft.simpleJavaFxApplicationBase.commonUtil.SuppressWarningStrings.*;
import static org.assertj.core.api.Assertions.assertThat;

class SuppressWarningStringsTest {

    @Test
    void suppressWarningStringsTest_test() {
        @SuppressWarnings({UNUSED, INSTANTIATION_OF_UTILITY_CLASS})
        SuppressWarningStrings sws = new SuppressWarningStrings();

        assertThat(ALL).isEqualTo("all");
        assertThat(BUSY_WAIT).isEqualTo("BusyWait");
        assertThat(CAST).isEqualTo("cast");
        assertThat(CONSTANT_CONDITIONS).isEqualTo("ConstantConditions");
        assertThat(FIELD_CAN_BE_LOCAL).isEqualTo("FieldCanBeLocal");
        assertThat(FIELD_MAY_BE_FINAL).isEqualTo("FieldMayBeFinal");
        assertThat(FIELD_MAY_BE_STATIC).isEqualTo("FieldMayBeStatic");
        assertThat(INSTANTIATION_OF_UTILITY_CLASS).isEqualTo("InstantiationOfUtilityClass");
        assertThat(RAW_TYPES).isEqualTo("rawtypes");
        assertThat(UNUSED).isEqualTo("unused");
    }

}