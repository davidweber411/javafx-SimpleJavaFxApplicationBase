package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SceneSwitcherTest extends SimpleJavaFxTestBase {

    Stage stage;
    SceneSwitcher<FxmlSceneControllerBase> sceneSwitcher;
    Label label;

    @Test
    void unitTestsForSceneSwitcher() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            label = new Label();
            stage = new Stage();
            stage.show();
        });

        constructorTest();
        passArgumentsToControllerOfNewSceneTest();

        runOnJavaFxThreadAndJoin(() -> {
            stage.hide();
            stage.close();
        });
    }

    private void constructorTest() {
        assertThrows(Exception.class, () -> sceneSwitcher = SceneSwitcher.createFxmlSceneSwitcher(
                getClass().getResource("/does/not/exist.fxml"), null));

        assertThrows(Exception.class, () -> sceneSwitcher = SceneSwitcher.createFxmlSceneSwitcher(
                getClass().getResource("/does/not/exist.fxml"), (Stage) label.getScene().getWindow()));

        assertThrows(Exception.class, () -> sceneSwitcher = SceneSwitcher.createFxmlSceneSwitcher(
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherSceneWithoutController.fxml"), (Stage) label.getScene().getWindow()));

        assertDoesNotThrow(() -> sceneSwitcher = SceneSwitcher.createFxmlSceneSwitcher(
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"), null));
    }

    private void passArgumentsToControllerOfNewSceneTest() {
        assertDoesNotThrow(() -> sceneSwitcher = SceneSwitcher.createFxmlSceneSwitcher(
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"), null));

        sceneSwitcher.passArgumentsToControllerOfNewScene(new HashMap<>() {{
            put("name", "Rambo");
        }});

        assertEquals("Rambo", sceneSwitcher.getControllerOfNewScene().getPassedArguments().get("name"));
    }

}