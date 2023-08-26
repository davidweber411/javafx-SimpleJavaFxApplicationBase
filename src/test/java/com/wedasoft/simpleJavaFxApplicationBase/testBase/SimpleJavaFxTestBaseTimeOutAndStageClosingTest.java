package com.wedasoft.simpleJavaFxApplicationBase.testBase;


import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.TestController;
import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.FxmlDialog;
import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.JfxDialogUtil;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Set;

import static com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBaseImpl.PRL_TIMEOUT_SECONDS_TO_WAIT;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleJavaFxTestBaseTimeOutAndStageClosingTest extends SimpleJavaFxTestBase {

    private FxmlDialog.Builder<TestController> builder;
    private int valueChangedByCallback;

    @Test
    @Order(1)
    void openAndCloseStage_oneTime_shallCheckForChangedValue() throws Exception {
        valueChangedByCallback = 0;
        runOnJavaFxThreadAndJoin(() -> {
            builder = JfxDialogUtil.createFxmlDialogBuilder(TestController.class, getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"));
            builder.setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE, KeyCode.K));
            builder.setCallbackOnDialogClose(() -> valueChangedByCallback = 52);
        });
        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> builder.get().showAndWait());

        assertNotNull(builder.get());
        assertEquals(52, valueChangedByCallback);
    }

    @Test
    @Order(2)
    void openAndCloseStage_multipleTimes_shallCheckForChangedValue() throws Exception {
        valueChangedByCallback = 0;
        runOnJavaFxThreadAndJoin(() -> {
            builder = JfxDialogUtil.createFxmlDialogBuilder(TestController.class, getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/fxmlDialog/fxml-dialog-with-controller-view.fxml"));
            builder.setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE, KeyCode.K));
            builder.setCallbackOnDialogClose(() -> valueChangedByCallback = 52);
        });
        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> builder.get().showAndWait());

        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> builder.get().showAndWait());

        pressKeyAsyncInOtherThread(1000, KeyCode.ESCAPE);
        runOnJavaFxThreadAndJoin(() -> builder.get().showAndWait());

        assertNotNull(builder.get());
        assertEquals(52, valueChangedByCallback);
    }

    @Test
    @Order(3)
    void runLater_dontRunIntoTimeout_shallNotThrow() {
        assertDoesNotThrow(() -> runOnJavaFxThreadAndJoin(() -> sleep(100)));
    }

    @Test
    @Order(4)
    void runLater_runIntoTimeout_shallThrow() {
        // sleep 2 seconds longer than the timeout setting.
        assertThrows(SimpleJavaFxTestBaseException.class, () -> runOnJavaFxThreadAndJoin(() -> sleep((PRL_TIMEOUT_SECONDS_TO_WAIT * 1000) + 2000)));
    }
}
