package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;

public class SceneSwitcherUtil {

    /**
     * Switches the scene of the stage on which the node is located, which fired the ActionEvent.
     *
     * @param fxmlFilePathUrl The path to to fxml file of the new scene.
     * @param argumentsToPass The arguments which shall be passed to the new scene.
     * @param event           The event whose source node is used to determine the stage for the scene switching.
     * @throws SceneSwitcherException If an error occurs.
     */
    public static void switchSceneOfStageViaActionEvent(URL fxmlFilePathUrl, HashMap<String, String> argumentsToPass, ActionEvent event) throws SceneSwitcherException {
        Node node = (Node) event.getSource();
        switchSceneOfStageViaAnyComponent(fxmlFilePathUrl, argumentsToPass, node);
    }

    /**
     * Switches the scene of the stage on which the given node is located.
     *
     * @param fxmlFilePathUrl        The path to to fxml file of the new scene.
     * @param argumentsToPass        The arguments which shall be passed to the new scene.
     * @param anyComponentOfTheStage The node which is used to determine the stage for scene switching.
     * @throws SceneSwitcherException If an error occurs.
     */
    public static void switchSceneOfStageViaAnyComponent(URL fxmlFilePathUrl, HashMap<String, String> argumentsToPass, Node anyComponentOfTheStage) throws SceneSwitcherException {
        Stage stage = (Stage) anyComponentOfTheStage.getScene().getWindow();
        SceneSwitcher.createFxmlSceneSwitcher(fxmlFilePathUrl, stage)
                .passArgumentsToControllerOfNewScene(argumentsToPass == null ? new HashMap<>() : argumentsToPass)
                .switchScene();
    }

}
