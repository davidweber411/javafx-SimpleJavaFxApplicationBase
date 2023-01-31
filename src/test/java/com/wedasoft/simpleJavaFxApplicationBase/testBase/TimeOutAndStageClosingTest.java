package com.wedasoft.simpleJavaFxApplicationBase.testBase;


import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.FxmlDialog;
import javafx.geometry.Dimension2D;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Set;

import static com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBaseImpl.PRL_TIMEOUT_SECONDS_TO_WAIT;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimeOutAndStageClosingTest extends SimpleJavaFxTestBase {

    FxmlDialog.Builder builder;
    int valueChangedByCallback;

    @Test
    @Order(1)
    void openAndCloseStage_oneTime_shallCheckForChangedValue() throws Exception {
        valueChangedByCallback = 0;
        runOnJavaFxThreadAndJoin(() -> {
            builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/testBase/test-woc.fxml"), new Dimension2D(600, 500));
            builder.setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE, KeyCode.K));
            builder.setCallbackOnDialogClose(() -> valueChangedByCallback = 52);
            builder.get().getStage().setOnShown((e) -> {
                new Robot().keyRelease(KeyCode.ESCAPE);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            builder.get().showAndWait();
        });
        assertNotNull(builder.get());
        assertEquals(52, valueChangedByCallback);
        Thread.sleep(500);
    }

    @Test
    @Order(2)
    void openAndCloseStage_multipleTimes_shallCheckForChangedValue() throws Exception {
        valueChangedByCallback = 0;
        runOnJavaFxThreadAndJoin(() -> {
            builder = new FxmlDialog.Builder(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/testBase/test-woc.fxml"), new Dimension2D(600, 500));
            builder.setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE, KeyCode.K));
            builder.setCallbackOnDialogClose(() -> valueChangedByCallback = 52);
            builder.get().getStage().setOnShown((e) -> {
                new Robot().keyRelease(KeyCode.ESCAPE);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            builder.get().showAndWait();
            builder.get().showAndWait();
            builder.get().showAndWait();
        });
        assertNotNull(builder.get());
        assertEquals(52, valueChangedByCallback);
    }

    @Test
    @Order(3)
    void runLater_dontRunIntoTimeout_shallNotThrow() {
        assertDoesNotThrow(() -> runOnJavaFxThreadAndJoin(() -> Thread.sleep(100)));
    }

    @Test
    @Order(4)
    void runLater_runIntoTimeout_shallThrow() {
        // sleep 2 seconds longer than the timeout setting.
        assertThrows(SimpleJavaFxTestBaseException.class, () -> runOnJavaFxThreadAndJoin(() -> Thread.sleep((PRL_TIMEOUT_SECONDS_TO_WAIT * 1000) + 2000)));
    }
}
