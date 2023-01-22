package com.wedasoft.simpleJavaFxApplicationBase.testBase.fxmlDialog;

import com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.fxmlDialog.TestController;
import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.FxmlDialog;
import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.geometry.Dimension2D;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderConstructorTests extends SimpleJavaFxTestBase {

    FxmlDialog.Builder<TestController> builder;

    FxmlDialog.Builder builderWithoutController;

    @Test
    void constructor_checkForFxmlPathIsNullAndSceneSizeIsNull() {
        assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(null, null)));
    }

    @Test
    void constructor_checkForFxmlPathNotExists() {
        assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/this/path/does/not/exist/file.fxml"), null)));
    }

    @Nested
    class ConstructorWithoutController {
        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeIsNull() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-without-controller.fxml"), null));
            assertNotNull(builderWithoutController);
            assertNotNull(builderWithoutController.get());
        }

        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeExists() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-without-controller.fxml"), new Dimension2D(600, 500)));
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
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
            assertNotNull(builder);
            assertNotNull(builder.get());
            assertNotNull(builder.get().getController());
            assertEquals(5, builder.get().getController().getIntValue());
            assertEquals("coolValue", builder.get().getController().getStringValue());
        }

        @Test
        void constructor_checkForFxmlPathExistsAndSceneSizeExists() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), new Dimension2D(600, 500)));
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
            runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-with-controller.fxml"), null));
            assertNotNull(builder);
            assertNotNull(builder.get());
            assertNotNull(builder.get().getController());
            assertEquals(5, builder.get().getController().getIntValue());
            assertEquals("coolValue", builder.get().getController().getStringValue());
        }

        @Test
        void fxmlDialog_getController_shallReturnNull() throws Exception {
            runOnJavaFxThreadAndJoin(() -> builderWithoutController = new FxmlDialog.Builder<>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/test-without-controller.fxml"), null));
            assertNotNull(builderWithoutController);
            assertNotNull(builderWithoutController.get());
            assertNull(builderWithoutController.get().getController());
        }
    }
}


