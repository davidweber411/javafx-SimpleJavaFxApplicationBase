package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.Map;

public class SceneSwitcher<ControllerClassT extends FxmlSceneControllerBase> {

    private final Stage stageToSwitchScene;
    private final Scene newScene;
    private final ControllerClassT controllerOfNewScene;

    public static <ControllerClassT extends FxmlSceneControllerBase> SceneSwitcher<ControllerClassT> createFxmlSceneSwitcher(
            URL fxmlFilePathUrl, Stage stageToSwitchScene) throws SceneSwitcherException {
        return new SceneSwitcher<>(fxmlFilePathUrl, stageToSwitchScene);
    }

    private SceneSwitcher(URL fxmlFilePathUrl, Stage stageToSwitchScene) throws SceneSwitcherException {
        // check and load the new scene
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFilePathUrl);
        Parent root;
        try {
            root = fxmlLoader.load(); // this calls the constructor and after that initialize from jfx
        } catch (Exception e) {
            throw new SceneSwitcherException("Could not create SceneSwitcher object because the FXMLLoader could not load the fxml file. Is the URL parameter correct?", e);
        }
        newScene = new Scene(root);

        // check and load the stage
        this.stageToSwitchScene = stageToSwitchScene != null
                ? stageToSwitchScene
                : (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        if (this.stageToSwitchScene == null) {
            throw new SceneSwitcherException("The value of this.stageToSwitchScene is null. The default stage could not be loaded. Please use setStageToSwitchScene(Stage stage).");
        }

        // check the controller of the scene
        controllerOfNewScene = fxmlLoader.getController();
        if (controllerOfNewScene == null) {
            throw new SceneSwitcherException("The controller of the new scene is null. Please add a controller to the scene which extends from the class FxmlSceneControllerBase.");
        }
    }

    public SceneSwitcher<ControllerClassT> passArgumentsToControllerOfNewScene(Map<String, String> argumentsToPass) {
        controllerOfNewScene.setPassedArguments(argumentsToPass);
        return this;
    }

    public void switchScene() {
        stageToSwitchScene.setScene(newScene);
        controllerOfNewScene.onFxmlSceneReady();
    }


    public Stage getStageToSwitchScene() {
        return stageToSwitchScene;
    }

    public Scene getNewScene() {
        return newScene;
    }

    public ControllerClassT getControllerOfNewScene() {
        return controllerOfNewScene;
    }
}
