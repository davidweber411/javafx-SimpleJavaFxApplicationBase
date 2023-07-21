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

        switchToScene1WithStageIsNull();
        switchToScene2WithStageViaActionEvent();
        switchToScene2WithStageViaAnyComponent();
        switchToScene2ViaSceneSwitcherUtilViaActionEvent();
        switchToScene2ViaSceneSwitcherUtilViaAnyComponent();

        testGettersOfSceneSwitcher();
        testSceneSwitcherConstructionErrors();
    }


    private void switchToScene1WithStageIsNull() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            sceneSwitcherForScene1 = SceneSwitcher.createFxmlSceneSwitcher(
                    getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"),
                    null);
            sceneSwitcherForScene1.switchScene();
        });
        SceneSwitcherScene1Controller controllerOfScene1 = (SceneSwitcherScene1Controller) sceneSwitcherForScene1.getControllerOfNewScene();
        assertEquals("This is scene1.", controllerOfScene1.scene1Label.getText());
    }

    private void switchToScene2WithStageViaActionEvent() throws Exception {
        switchSceneViaButtonClickViaKeyboardClick(KeyCode.B, "#scene2Label", "This is scene2.");
        switchFromScene2ToScene1();
    }

    private void switchToScene2WithStageViaAnyComponent() throws Exception {
        switchSceneViaButtonClickViaKeyboardClick(KeyCode.C, "#scene2Label", "This is scene2.");
        switchFromScene2ToScene1();
    }

    private void switchToScene2ViaSceneSwitcherUtilViaActionEvent() throws Exception {
        switchSceneViaButtonClickViaKeyboardClick(KeyCode.D, "#scene2Label", "This is scene2.");
        switchFromScene2ToScene1();
    }

    private void switchToScene2ViaSceneSwitcherUtilViaAnyComponent() throws Exception {
        switchSceneViaButtonClickViaKeyboardClick(KeyCode.E, "#scene2Label", "This is scene2.");
        switchFromScene2ToScene1();
    }

    private void switchFromScene2ToScene1() throws Exception {
        switchSceneViaButtonClickViaKeyboardClick(KeyCode.X, "#scene1Label", "This is scene1.");
    }

    private void switchSceneViaButtonClickViaKeyboardClick(KeyCode e, String s, String expected) throws Exception {
        // switch the stage via a button click
        runOnJavaFxThreadAndJoin(() -> pressKeyAsyncInOtherThread(0, e));

        // check if the stage was switched
        runOnJavaFxThreadAndJoin(() -> {
            Window window = Stage.getWindows().stream().findFirst().orElse(null);
            if (window == null) {
                throw new Exception("Window mustn't be null.");
            }
            lookedUpNode = window.getScene().lookup(s);
        });
        assertEquals(expected, ((Label) lookedUpNode).getText());
    }

    private void testGettersOfSceneSwitcher() {
        assertNotNull(sceneSwitcherForScene1.getNewScene());
        assertNotNull(sceneSwitcherForScene1.getStageToSwitchScene());
        assertNotNull(sceneSwitcherForScene1.getControllerOfNewScene());
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
}