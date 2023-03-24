package com.wedasoft.simpleJavaFxApplicationBase.testBase;

import javafx.scene.control.Button;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SimpleJavaFxTestBaseExecutionOrderTest extends SimpleJavaFxTestBase {

    private final static String EXPECTED_TEST_CLASS_EXECUTION_ORDER_CHECKSUM = "ba_be12ae_be12ae_be12ae_aa";
    private static String actualTestClassExecutionOrderChecksum = "";

    private final static String EXPECTED_TEST_METHOD_EXECUTION_ORDER_CHECKSUM = "12";
    private static String actualTestMethodExecutionOrderChecksum = "";

    private Button button;

    @BeforeAll
    static void beforeAll() {
        actualTestClassExecutionOrderChecksum = "";
        actualTestClassExecutionOrderChecksum += "ba";
    }

    @BeforeEach
    void beforeEach() {
        actualTestMethodExecutionOrderChecksum = "";
        actualTestClassExecutionOrderChecksum += "_be";
    }

    @AfterAll
    static void afterAll() {
        actualTestClassExecutionOrderChecksum += "_aa";
        assertEquals(EXPECTED_TEST_CLASS_EXECUTION_ORDER_CHECKSUM, actualTestClassExecutionOrderChecksum);
    }

    @AfterEach
    void afterEach() {
        actualTestClassExecutionOrderChecksum += "ae";
        assertEquals(EXPECTED_TEST_METHOD_EXECUTION_ORDER_CHECKSUM, actualTestMethodExecutionOrderChecksum);
    }

    @Test
    void generateTestMethodChecksum1() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            actualTestMethodExecutionOrderChecksum += "1";
            button = new Button("buttonLabel");
        });
        assertEquals("buttonLabel", button.getText());
        assertNotEquals("wrongLabel", button.getText());
        actualTestMethodExecutionOrderChecksum += "2";
        actualTestClassExecutionOrderChecksum += actualTestMethodExecutionOrderChecksum;
    }

    @Test
    void generateTestMethodChecksum2() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            actualTestMethodExecutionOrderChecksum += "1";
            button = new Button("buttonLabel");
        });
        assertEquals("buttonLabel", button.getText());
        assertNotEquals("wrongLabel", button.getText());
        actualTestMethodExecutionOrderChecksum += "2";
        actualTestClassExecutionOrderChecksum += actualTestMethodExecutionOrderChecksum;
    }

    @Test
    void generateTestMethodChecksum3() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            actualTestMethodExecutionOrderChecksum += "1";
            button = new Button("buttonLabel");
        });
        assertEquals("buttonLabel", button.getText());
        assertNotEquals("wrongLabel", button.getText());
        actualTestMethodExecutionOrderChecksum += "2";
        actualTestClassExecutionOrderChecksum += actualTestMethodExecutionOrderChecksum;
    }


}

