package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher<ControllerClassT extends FxmlSceneControllerBase> {

    private final Stage stageToSwitchScene;
    private final Scene newScene;
    private final ControllerClassT controllerOfNewScene;

    /**
     * Creates a FxmlSceneSwitcher object which is used to switch scenes of a stage.
     *
     * @param fxmlFilePathUrl    The path to the fxml file of the new scene.
     * @param stageToSwitchScene The stage which shall switch its scene.
     * @param <ControllerClassT> The Class of the controller of the new scene.
     * @return The SceneSwitcher object.
     * @throws SceneSwitcherException If an error occurs.
     */
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
            throw new SceneSwitcherException("The value of this.stageToSwitchScene is null. The default stage could not be loaded.");
        }

        // check the controller of the scene
        try {
            controllerOfNewScene = fxmlLoader.getController();
        } catch (Exception e) {
            throw new SceneSwitcherException("The controller of the new scene is not null, but it could not be loaded and assigned. Does the controller extend from the class FxmlSceneControllerBase?", e);
        }
        if (controllerOfNewScene == null) {
            throw new SceneSwitcherException("The controller of the new scene is null. Please add a controller to the scene which extends from the class FxmlSceneControllerBase.");
        }

        // init arguments map with default values
        controllerOfNewScene.setPassedArguments(new HashMap<>());
    }

    /**
     * This method is used to pass arguments to the controller of the new scene.
     *
     * @param argumentsToPass The arguments to pass.
     * @return This instance of the SceneSwitcher object.
     */
    public SceneSwitcher<ControllerClassT> passArgumentsToControllerOfNewScene(Map<String, String> argumentsToPass) {
        controllerOfNewScene.setPassedArguments(argumentsToPass);
        return this;
    }

    /**
     * This method is used to switch the scene.
     */
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
