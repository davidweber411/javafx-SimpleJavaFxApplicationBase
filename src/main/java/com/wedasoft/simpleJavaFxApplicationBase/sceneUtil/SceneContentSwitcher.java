package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SceneContentSwitcher<ControllerClassT extends FxmlSceneControllerBase> {

    private final Scene sceneToSwitchContent;
    private final Parent rootNodeOfNewFxmlFile;
    private final ControllerClassT controllerOfNewFxmlFile;

    /**
     * Creates a SceneContentSwitcher which is used to switch the complete content of a scene.
     *
     * @param sceneToSwitchContent The scene which shall switch its content.
     * @param urlToNewFxmlFile     The path to the new fxml file whose content shall be injected into the scene.
     * @param <ControllerClassT>   The Class of the controller of the fxml file of the new content.
     * @return The SceneContentSwitcher object.
     * @throws SceneUtilException If an error occurs.
     */
    public static <ControllerClassT extends FxmlSceneControllerBase> SceneContentSwitcher<ControllerClassT> createSceneContentSwitcher(
            Scene sceneToSwitchContent,
            URL urlToNewFxmlFile)
            throws SceneUtilException {
        return new SceneContentSwitcher<>(sceneToSwitchContent, urlToNewFxmlFile);
    }

    private SceneContentSwitcher(
            Scene sceneToSwitchContent,
            URL urlToNewFxmlFile)
            throws SceneUtilException {
        if (sceneToSwitchContent == null) {
            throw new SceneUtilException("The parameter 'sceneToSwitchContent' mustn't be null.");
        }
        this.sceneToSwitchContent = sceneToSwitchContent;

        FXMLLoader fxmlLoader = new FXMLLoader(urlToNewFxmlFile);
        try {
            rootNodeOfNewFxmlFile = fxmlLoader.load(); // constructs the new fxml and the associated controller
        } catch (Exception e) {
            throw new SceneUtilException(
                    "Couldn't create the SceneContentSwitcher because the FXMLLoader couldn't load the fxml file. Is the 'urlToNewFxmlFile' parameter correct?", e);
        }

        try {
            controllerOfNewFxmlFile = fxmlLoader.getController();
        } catch (Exception e) {
            throw new SceneUtilException(
                    "The controller of the new fxml file couldn't be loaded and assigned. Does the controller class extend from the class 'FxmlSceneControllerBase'?", e);
        }
        if (controllerOfNewFxmlFile == null) {
            throw new SceneUtilException(
                    "The controller of the new fxml file is null. Please add a controller to the new fxml file which extends from the class 'FxmlSceneControllerBase'.");
        }

        controllerOfNewFxmlFile.setPassedArguments(new HashMap<>());
    }

    /**
     * This method is used to pass arguments to the new controller.
     *
     * @param argumentsToPass The arguments to pass.
     * @return This instance of the SceneContentSwitcher.
     */
    public SceneContentSwitcher<ControllerClassT> passArgumentsToNewController(Map<String, String> argumentsToPass) {
        controllerOfNewFxmlFile.setPassedArguments(argumentsToPass);
        return this;
    }

    /**
     * This method is used to switch the content of the scene.
     */
    public void switchSceneContent() {
        sceneToSwitchContent.setRoot(rootNodeOfNewFxmlFile);
        controllerOfNewFxmlFile.onFxmlSceneReady();
    }

    public Scene getSceneToSwitchContent() {
        return sceneToSwitchContent;
    }

    public ControllerClassT getControllerOfNewFxmlFile() {
        return controllerOfNewFxmlFile;
    }

}
