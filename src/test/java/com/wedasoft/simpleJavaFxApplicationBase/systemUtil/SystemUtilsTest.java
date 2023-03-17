package com.wedasoft.simpleJavaFxApplicationBase.systemUtil;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.scene.input.Clipboard;
import org.junit.jupiter.api.Test;

import static com.wedasoft.simpleJavaFxApplicationBase.systemUtil.SystemUtils.getMainClassName;
import static org.assertj.core.api.Assertions.assertThat;

class SystemUtilsTest extends SimpleJavaFxTestBase {

    private String clipboardValue;

    @Test
    void copyToClipBoard_test() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            SystemUtils.copyToClipBoard("helloWorld!", false);
            clipboardValue = Clipboard.getSystemClipboard().getString();
        });
        assertThat(clipboardValue).isEqualTo("helloWorld!");

        runOnJavaFxThreadAndJoin(() -> {
            SystemUtils.copyToClipBoard("    helloWorld!       ", true);
            clipboardValue = Clipboard.getSystemClipboard().getString();
        });
        assertThat(clipboardValue).isEqualTo("helloWorld!");

        runOnJavaFxThreadAndJoin(() -> {
            SystemUtils.copyToClipBoard("    helloWorld!       ", true);
            clipboardValue = Clipboard.getSystemClipboard().getString();
        });
        assertThat(clipboardValue).isEqualTo("helloWorld!");
    }

    @Test
    void getMainClassName_test() {
        String mainClassName = getMainClassName();
        assertThat(mainClassName).isEqualTo("worker.org.gradle.process.internal.worker.GradleWorkerMain");
    }

}