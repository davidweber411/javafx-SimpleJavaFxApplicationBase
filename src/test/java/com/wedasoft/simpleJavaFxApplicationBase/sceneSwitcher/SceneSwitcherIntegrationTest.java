package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class SceneSwitcherIntegrationTest extends SimpleJavaFxTestBase {

    Stage stage;
    SceneSwitcher<FxmlSceneControllerBase> sceneSwitcherForScene1;
    Node lookedUpNode;

    @Test
    void integrationTests() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            stage = new Stage();
            stage.show();
        });

        testSceneSwitchingToScene1WithStageIsNull();

        testSceneSwitchingToScene2WithStageGottenOverEvent();
        switchFromScene2ToScene1();

        testSceneSwitchingToScene2WithStageGottenOverAnyComponentOfStage();
        switchFromScene2ToScene1();

        testGettersOfSceneSwitcher();
        testSceneSwitcherConstructionErrors();
    }

    private void testSceneSwitcherConstructionErrors() throws Exception {
        AtomicBoolean exceptionWasThrown = new AtomicBoolean(false);
        runOnJavaFxThreadAndJoin(() -> {
            try {
                sceneSwitcherForScene1 = SceneSwitcher.createFxmlSceneSwitcher(
                        getClass().getResource("/this/scene/does/not/exist.fxml"),
                        null);
            } catch (Exception e) {
                exceptionWasThrown.set(true);
            }
        });
        assertTrue(exceptionWasThrown.get());

        exceptionWasThrown.set(false);
        runOnJavaFxThreadAndJoin(() -> stage.hide());

        runOnJavaFxThreadAndJoin(() -> {
            try {
                sceneSwitcherForScene1 = SceneSwitcher.createFxmlSceneSwitcher(
                        getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"),
                        null);
            } catch (SceneSwitcherException e) {
                exceptionWasThrown.set(true);
            }
        });
    }

    private void testGettersOfSceneSwitcher() {
        assertNotNull(sceneSwitcherForScene1.getNewScene());
        assertNotNull(sceneSwitcherForScene1.getStageToSwitchScene());
        assertNotNull(sceneSwitcherForScene1.getControllerOfNewScene());
    }

    private void testSceneSwitchingToScene1WithStageIsNull() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            sceneSwitcherForScene1 = SceneSwitcher.createFxmlSceneSwitcher(
                    getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"),
                    null);
            sceneSwitcherForScene1.switchScene();
        });
        SceneSwitcherScene1Controller controllerOfScene1 = (SceneSwitcherScene1Controller) sceneSwitcherForScene1.getControllerOfNewScene();
        assertEquals("This is scene1.", controllerOfScene1.scene1Label.getText());
    }

    private void testSceneSwitchingToScene2WithStageGottenOverEvent() throws Exception {
        runOnJavaFxThreadAndJoin(() -> pressKeyAsyncInOtherThread(0, KeyCode.B));
        runOnJavaFxThreadAndJoin(() -> {
            Window window = Stage.getWindows().stream().findFirst().orElse(null);
            if (window == null) {
                throw new Exception("Window mustn't be null.");
            }
            lookedUpNode = window.getScene().lookup("#scene2Label");
        });
        assertEquals("This is scene2.", ((Label) lookedUpNode).getText());
    }

    private void switchFromScene2ToScene1() throws Exception {
        runOnJavaFxThreadAndJoin(() -> pressKeyAsyncInOtherThread(0, KeyCode.X));
        runOnJavaFxThreadAndJoin(() -> {
            Window window = Stage.getWindows().stream().findFirst().orElse(null);
            if (window == null) {
                throw new Exception("Window mustn't be null.");
            }
            lookedUpNode = window.getScene().lookup("#scene1Label");
        });
        assertEquals("This is scene1.", ((Label) lookedUpNode).getText());
    }

    private void testSceneSwitchingToScene2WithStageGottenOverAnyComponentOfStage() throws Exception {
        runOnJavaFxThreadAndJoin(() -> pressKeyAsyncInOtherThread(0, KeyCode.C));
        runOnJavaFxThreadAndJoin(() -> {
            Window window = Stage.getWindows().stream().findFirst().orElse(null);
            if (window == null) {
                throw new Exception("Window mustn't be null.");
            }
            lookedUpNode = window.getScene().lookup("#scene2Label");
        });
        assertEquals("This is scene2.", ((Label) lookedUpNode).getText());
    }

}