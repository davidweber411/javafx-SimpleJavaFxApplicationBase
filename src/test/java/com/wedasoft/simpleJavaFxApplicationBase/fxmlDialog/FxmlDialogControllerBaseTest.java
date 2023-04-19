package com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FxmlDialogControllerBaseTest extends SimpleJavaFxTestBase {

    private FxmlDialog.Builder builder;

    @Test
    void getPassedArgumentsAsIntTest() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null).passArgumentsToController(Map.ofEntries(Map.entry("firstname", "Harald"), Map.entry("age", "25"))));
        assertNotNull(builder);
        assertNotNull(builder.get());
        assertThat(builder.get().getController().getPassedArgumentsAsInt("age")).isEqualTo(25);
        assertThrows(Exception.class, () -> builder.get().getController().getPassedArgumentsAsInt("firstname"));
    }

}
