package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SceneContentSwitcherTest extends SimpleJavaFxTestBase {

    Stage stage;
    Scene scene;
    SceneContentSwitcher<FxmlSceneControllerBase> sceneContentSwitcher;

    @Test
    void unitTestsForSceneContentSwitcher() throws Exception {
        constructorTests();
        successfullCreationAndSwitchingAndArgumentPassingTests();
    }

    private void successfullCreationAndSwitchingAndArgumentPassingTests() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene1.fxml"))));
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
        Thread.sleep(500);

        runOnJavaFxThreadAndJoin(() -> {
            stage.setWidth(500);
            stage.setHeight(500);
        });
        Thread.sleep(500);

        sceneContentSwitcher = SceneContentSwitcher
                .createSceneContentSwitcher(
                        scene,
                        getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"))
                .passArgumentsToNewController(Map.ofEntries(Map.entry("name", "garry")));
        sceneContentSwitcher.switchSceneContent();
        Thread.sleep(500);
        SceneSwitcherScene2Controller controllerOfScene2 = (SceneSwitcherScene2Controller) sceneContentSwitcher.getControllerOfNewFxmlFile();
        assertNotNull(sceneContentSwitcher.getSceneToSwitchContent());
        assertEquals("This is scene2.", controllerOfScene2.scene2Label.getText());
        assertEquals("garry", controllerOfScene2.getPassedArguments().get("name"));
        Thread.sleep(500);

        sceneContentSwitcher = SceneContentSwitcher
                .createSceneContentSwitcher(
                        scene,
                        getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene1.fxml"))
                .passArgumentsToNewController(Map.ofEntries(Map.entry("age", "397"), Map.entry("name", "Julia")));
        sceneContentSwitcher.switchSceneContent();
        Thread.sleep(500);
        SceneSwitcherScene1Controller controllerOfScene1 = (SceneSwitcherScene1Controller) sceneContentSwitcher.getControllerOfNewFxmlFile();
        assertNotNull(sceneContentSwitcher.getSceneToSwitchContent());
        assertEquals("This is scene1.", controllerOfScene1.scene1Label.getText());
        assertEquals("397", controllerOfScene1.getPassedArguments().get("age"));
        assertEquals("Julia", controllerOfScene1.getPassedArguments().get("name"));
        Thread.sleep(500);

        runOnJavaFxThreadAndJoin(() -> {
            stage.hide();
            stage.close();
        });
    }

    private void constructorTests() {
        assertThrows(Exception.class, () -> sceneContentSwitcher = SceneContentSwitcher.createSceneContentSwitcher(
                null,
                getClass().getResource("/does/not/exist.fxml")));

        assertThrows(Exception.class, () -> sceneContentSwitcher = SceneContentSwitcher.createSceneContentSwitcher(
                new Scene(new VBox()),
                getClass().getResource("/does/not/exist.fxml")));

        assertThrows(Exception.class, () -> sceneContentSwitcher = SceneContentSwitcher.createSceneContentSwitcher(
                new Scene(new VBox()),
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherSceneWithNonInheritingController.fxml")));

        assertThrows(Exception.class, () -> sceneContentSwitcher = SceneContentSwitcher.createSceneContentSwitcher(
                new Scene(new VBox()),
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherSceneWithoutController.fxml")));
    }

}