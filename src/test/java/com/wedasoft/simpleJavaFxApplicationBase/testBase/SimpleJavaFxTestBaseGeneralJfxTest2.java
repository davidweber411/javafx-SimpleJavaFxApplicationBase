package com.wedasoft.simpleJavaFxApplicationBase.testBase;

import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.FxmlDialog;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
This is a copied test of <code>SimpleJavaFxTestBaseGeneralJfxTest</code>.<br>
This duplicated tests are used to test the JavaFX toolkit initialization.
 */
@SuppressWarnings("NewClassNamingConvention")
class SimpleJavaFxTestBaseGeneralJfxTest2 extends SimpleJavaFxTestBase {

    private FxmlDialog.Builder builder;
    private Button button;
    private Button button2;

    @Test
    void testFxmlLoader() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/testBase/test-woc.fxml"), new Dimension2D(600, 500));
            builder.setStageTitle("Old StageTitle");
        });
        assertEquals("Old StageTitle", builder.get().getStage().getTitle());
        assertNotNull(builder.get());
        builder.setStageTitle("New StageTitle");
        assertEquals(600, builder.get().getStage().getScene().getWidth());
        assertEquals(500, builder.get().getStage().getScene().getHeight());
        assertEquals("New StageTitle", builder.get().getStage().getTitle());

        assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder(getClass().getResource("/doesnotexistse/test-woc.fxml"), new Dimension2D(600, 500))));
        runOnJavaFxThreadAndJoin(() -> {
            builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/testBase/test-woc.fxml"), new Dimension2D(1000, 1000));
            builder.setStageTitle("Another StageTitle");
            button2 = new Button("second init button");
        });
        assertEquals(1000, builder.get().getStage().getScene().getWidth());
        assertEquals(1000, builder.get().getStage().getScene().getHeight());
        assertEquals("Another StageTitle", builder.get().getStage().getTitle());
        assertNotNull(button2);
        assertEquals("second init button", button2.getText());
        assertTrue(true);
    }

    @Test
    void testButton() throws Exception {
        runOnJavaFxThreadAndJoin(() -> button = new Button("buttonLabel"));
        assertEquals("buttonLabel", button.getText());
        assertNotEquals("wrongLabel", button.getText());
    }

    @Test
    void exceptionOnJavaFxThread_shallThrow() {
        assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> builder = new FxmlDialog.Builder(getClass().getResource("/this/path/does/not/exist/file.fxml"), null)));
    }

}

