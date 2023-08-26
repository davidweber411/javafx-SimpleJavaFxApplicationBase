package com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs;

import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.TestController;
import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.geometry.Dimension2D;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FxmlDialogTest extends SimpleJavaFxTestBase {

    private FxmlDialog.Builder<TestController> builder;

    private FxmlDialog.Builder builderWithoutController;

    @Nested
    class ConstructorGeneral {
        @Test
        void checkForFxmlPathIsNull() {
            assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(null, new Dimension2D(500, 500))));
        }

        @Test
        void checkForFxmlPathNotExists() {
            assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/this/path/does/not/exist/file.fxml"), null)));
        }

        @Test
        void checkForSceneSizeIsNull() {
            assertDoesNotThrow(() -> runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null)));
        }
    }

    @Nested
    class ConstructorWithoutController {
        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeIsNull() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-without-controller-view.fxml"), null));
            assertNotNull(builderWithoutController);
            assertNotNull(builderWithoutController.get());
        }

        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeExists() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-without-controller-view.fxml"), new Dimension2D(600, 500)));
            assertNotNull(builderWithoutController);
            assertNotNull(builderWithoutController.get());
            assertEquals(600, builderWithoutController.get().getStage().getScene().getWidth());
            assertEquals(500, builderWithoutController.get().getStage().getScene().getHeight());
        }

    }

    @Nested
    class ConstructorWithController {
        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeIsNull() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
            assertNotNull(builder);
            assertNotNull(builder.get());
            assertNotNull(builder.get().getController());
            assertEquals(5, builder.get().getController().getIntValue());
            assertEquals("coolValue", builder.get().getController().getStringValue());
        }

        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeExists() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), new Dimension2D(600, 500)));
            assertNotNull(builder);
            assertNotNull(builder.get());
            assertEquals(600, builder.get().getStage().getScene().getWidth());
            assertEquals(500, builder.get().getStage().getScene().getHeight());
            assertNotNull(builder.get().getController());
            assertEquals(5, builder.get().getController().getIntValue());
            assertEquals("coolValue", builder.get().getController().getStringValue());
        }

        @Test
        void fxmlDialog_getController_shallReturnCorrectController() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
            assertNotNull(builder);
            assertNotNull(builder.get());
            assertNotNull(builder.get().getController());
            assertEquals(5, builder.get().getController().getIntValue());
            assertEquals("coolValue", builder.get().getController().getStringValue());
        }

        @Test
        void fxmlDialog_getController_shallReturnNull() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-without-controller-view.fxml"), null));
            assertNotNull(builderWithoutController);
            assertNotNull(builderWithoutController.get());
            assertNull(builderWithoutController.get().getController());
        }
    }

    @Nested
    class SetterMethodsOfBuilder {

        private int intChangedByCallback = 0;

        @Test
        void setStageTitle_shallCheckForStageTitle() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
            assertThat(builder).isNotNull();
            assertThat(builder.get()).isNotNull();
            assertThat(builder.get().getController()).isNotNull();
            assertThat(builder.get().getStage().getTitle()).isNull();
            runOnJavaFxThreadAndJoin(() -> builder.setStageTitle("New Title"));
            assertThat(builder.get().getStage().getTitle()).isEqualTo("New Title");
        }

        @Test
        void setStageResizable_shallCheckForBoolean() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
            assertThat(builder).isNotNull();
            assertThat(builder.get()).isNotNull();
            assertThat(builder.get().getController()).isNotNull();
            assertThat(builder.get().getStage().isResizable()).isTrue();
            runOnJavaFxThreadAndJoin(() -> builder.setStageResizable(false));
            assertThat(builder.get().getStage().isResizable()).isFalse();
        }

        @Test
        void setModal_shallCheckForBoolean() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
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
        void setCallbackOnDialogClose_setKeySetToCloseDialogTest_shallCheckForCallbackExecution() throws Exception {
            runOnJavaFxThreadAndJoin(() -> {
                builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null);
                builder.setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE));
                builder.setCallbackOnDialogClose(() -> intChangedByCallback = 42);
            });
            assertThat(builder).isNotNull();
            assertThat(builder.get()).isNotNull();
            assertThat(builder.get().getController()).isNotNull();

            pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
            runOnJavaFxThreadAndJoin(() -> builder.get().showAndWait());

            assertThat(intChangedByCallback).isEqualTo(42);
            intChangedByCallback = 0;
        }

        @Test
        void passArgumentsToController_shallCheckForPassedArgumentsExistsAndSizeAndValue() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
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

            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-without-controller-view.fxml"), null));
            assertThatThrownBy(() -> builder.passArgumentsToController(new HashMap<>()));
        }

        @Test
        void get_shallCheckForDialogIsNotNull() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null));
            assertThat(builder).isNotNull();
            assertThat(builder.get()).isNotNull();
        }

    }


}


