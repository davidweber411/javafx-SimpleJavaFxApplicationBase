package com.wedasoft.simpleJavaFxApplicationBase.commonJfxDialog;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wedasoft.simpleJavaFxApplicationBase.commonJfxDialog.CommonJfxDialogs.*;
import static org.assertj.core.api.Assertions.assertThat;

class CommonJfxDialogsTest extends SimpleJavaFxTestBase {

    private final String loremIpsumText = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ";

    @Nested
    class InformationDialogTest {
        private Alert informationDialog;

        @Test
        void createInformationDialog_withoutHeader() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                informationDialog = createInformationDialog(loremIpsumText);
                informationDialog.show();
                informationDialog.close();
            });
            assertThat(informationDialog.getTitle()).isEqualTo("Information");
            assertThat(informationDialog.getHeaderText()).isNull();
            assertThat(informationDialog.getContentText()).isEqualTo(loremIpsumText);
        }

        @Test
        void createInformationDialog_withHeader() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                informationDialog = createInformationDialog(loremIpsumText, "HeaderText");
                informationDialog.show();
                informationDialog.close();
            });
            assertThat(informationDialog.getTitle()).isEqualTo("Information");
            assertThat(informationDialog.getHeaderText()).isEqualTo("HeaderText");
            assertThat(informationDialog.getContentText()).isEqualTo(loremIpsumText);

            runOnJavaFxThreadAndJoin(() -> {
                informationDialog = createInformationDialog(loremIpsumText, null);
                informationDialog.show();
                informationDialog.close();
            });
            assertThat(informationDialog.getTitle()).isEqualTo("Information");
            assertThat(informationDialog.getHeaderText()).isNull();
            assertThat(informationDialog.getContentText()).isEqualTo(loremIpsumText);
        }
    }

    @Nested
    class WarningDialogTest {
        private Alert warningDialog;

        @Test
        void createWarningDialog_withoutHeader() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                warningDialog = createWarningDialog(loremIpsumText);
                warningDialog.show();
                warningDialog.close();
            });
            assertThat(warningDialog.getTitle()).isEqualTo("Warning");
            assertThat(warningDialog.getHeaderText()).isNull();
            assertThat(warningDialog.getContentText()).isEqualTo(loremIpsumText);
        }

        @Test
        void createWarningDialog_withHeader() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                warningDialog = createWarningDialog(loremIpsumText, "HeaderText");
                warningDialog.show();
                warningDialog.close();
            });
            assertThat(warningDialog.getTitle()).isEqualTo("Warning");
            assertThat(warningDialog.getHeaderText()).isEqualTo("HeaderText");
            assertThat(warningDialog.getContentText()).isEqualTo(loremIpsumText);
        }

    }

    @Nested
    class ErrorDialogTest {
        private Alert errorDialog;

        @Test
        void createErrorDialog_withoutStacktrace() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                errorDialog = createErrorDialog(loremIpsumText);
                errorDialog.show();
                errorDialog.close();
            });
            assertThat(errorDialog.getTitle()).isEqualTo("Error");
            assertThat(errorDialog.getHeaderText()).isNull();
            assertThat(errorDialog.getContentText()).isEqualTo(loremIpsumText);
        }

        @Test
        void createErrorDialog_withException() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                errorDialog = createErrorDialog(loremIpsumText, new NullPointerException(loremIpsumText));
                errorDialog.show();
                errorDialog.close();
            });
            assertThat(errorDialog.getTitle()).isEqualTo("Error");
            assertThat(errorDialog.getHeaderText()).isEqualTo(loremIpsumText);
            assertThat(((TextArea) errorDialog.getDialogPane().getContent()).getText()).isNotEmpty();
        }

    }

    @Nested
    class ConfirmDialogTest {

        private boolean result;

        @Timeout(10)
        @Test
        void createConfirmDialog_test() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                typeKeysAfterSeconds(List.of(KeyCode.ESCAPE), 2);
                result = displayConfirmDialogAndGetResult("headerText", "contentTest");
            });
            assertThat(result).isFalse();

            runOnJavaFxThreadAndJoin(() -> {
                typeKeysAfterSeconds(List.of(KeyCode.ENTER), 2);
                result = displayConfirmDialogAndGetResult("headerText", "contentTest");
            });
            assertThat(result).isTrue();
        }

    }

    @Nested
    class InputDialogTest {
        private String result;

        @Timeout(10)
        @Test
        void createInputDialog_withInput() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                typeKeysAfterSeconds(List.of(KeyCode.A, KeyCode.B, KeyCode.C), 1);
                typeKeysAfterSeconds(List.of(KeyCode.ENTER), 2);
                result = displayInputDialogAndGetResult("dummy text");
            });
            assertThat(result).isEqualTo("abc");

            runOnJavaFxThreadAndJoin(() -> {
                typeKeysAfterSeconds(List.of(KeyCode.A, KeyCode.B, KeyCode.C), 1);
                typeKeysAfterSeconds(List.of(KeyCode.ESCAPE), 2);
                result = displayInputDialogAndGetResult("dummy text");
            });
            assertThat(result).isEqualTo("");

            runOnJavaFxThreadAndJoin(() -> {
                typeKeysAfterSeconds(List.of(KeyCode.ESCAPE), 1);
                result = displayInputDialogAndGetResult("dummy text");
            });
            assertThat(result).isEqualTo("");
        }

    }

    @SuppressWarnings("SameParameterValue")
    private static void typeKeysAfterSeconds(List<KeyCode> keys, int seconds) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
                for (KeyCode key : keys) {
                    new Robot().keyPress(key.getCode());
                    new Robot().keyRelease(key.getCode());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}