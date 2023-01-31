package com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog;

import com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.fxmlDialog.TestController;
import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BuilderSetterMethodsTest extends SimpleJavaFxTestBase {

    FxmlDialog.Builder<TestController> builder;

    private int intChangedByCallback = 0;

    @Test
    void setStageTitleTest_shallCheckForStageTitle() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
        assertThat(builder).isNotNull();
        assertThat(builder.get()).isNotNull();
        assertThat(builder.get().getController()).isNotNull();
        assertThat(builder.get().getStage().getTitle()).isNull();
        runOnJavaFxThreadAndJoin(() -> builder.setStageTitle("New Title"));
        assertThat(builder.get().getStage().getTitle()).isEqualTo("New Title");
    }

    @Test
    void setStageResizableTest_shallCheckForBoolean() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
        assertThat(builder).isNotNull();
        assertThat(builder.get()).isNotNull();
        assertThat(builder.get().getController()).isNotNull();
        assertThat(builder.get().getStage().isResizable()).isTrue();
        runOnJavaFxThreadAndJoin(() -> builder.setStageResizable(false));
        assertThat(builder.get().getStage().isResizable()).isFalse();
    }

    @Test
    void setModalTest_shallCheckForBoolean() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
        assertThat(builder).isNotNull();
        assertThat(builder.get()).isNotNull();
        assertThat(builder.get().getController()).isNotNull();
        assertThat(builder.get().getStage().getModality()).isEqualTo(Modality.NONE);
        runOnJavaFxThreadAndJoin(() -> builder.setModal(true));
        assertThat(builder.get().getStage().getModality()).isEqualTo(Modality.APPLICATION_MODAL);
        runOnJavaFxThreadAndJoin(() -> builder.setModal(false));
        assertThat(builder.get().getStage().getModality()).isEqualTo(Modality.NONE);
    }

    @Test
    void setCallbackOnDialogCloseTest_setKeySetToCloseDialogTest_shallCheckForCallbackExecution() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
        assertThat(builder).isNotNull();
        assertThat(builder.get()).isNotNull();
        assertThat(builder.get().getController()).isNotNull();
        runOnJavaFxThreadAndJoin(() -> {
            builder.setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE));
            builder.setCallbackOnDialogClose(() -> intChangedByCallback = 42);
            builder.get().getStage().setOnShown(event -> new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    new Robot().keyRelease(KeyCode.ESCAPE.getCode());
                    new Robot().keyRelease(KeyCode.ESCAPE.getCode());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start());
            builder.get().showAndWait();
        });
        assertThat(intChangedByCallback).isEqualTo(42);
        intChangedByCallback = 0;
    }

    @Test
    void passArgumentsToControllerTest_shallCheckForPassedArgumentsExistsAndSizeAndValue() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
        assertThat(builder).isNotNull();
        assertThat(builder.get()).isNotNull();
        assertThat(builder.get().getController()).isNotNull();
        builder.passArgumentsToController(new HashMap<>() {{
            put("argument1", "1");
            put("argument2", "2");
        }});
        assertThat(builder.get().getController().getPassedArguments().size()).isEqualTo(2);
        assertThat(builder.get().getController().getPassedArguments().get("argument1")).isEqualTo("1");
        assertThat(builder.get().getController().getPassedArguments().get("argument2")).isEqualTo("2");

        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-without-controller.fxml"), null));
        assertThatThrownBy(() -> builder.passArgumentsToController(new HashMap<>()));
    }

    @Test
    void getTest_shallCheckForDialogIsNotNull() throws Exception {
        runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
        assertThat(builder).isNotNull();
        assertThat(builder.get()).isNotNull();
    }

}
