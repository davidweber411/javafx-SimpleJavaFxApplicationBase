package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Map;

public class SceneSwitcherScene1Controller extends FxmlSceneControllerBase {

    @FXML
    Label scene1Label;
    @FXML
    TextField passedParameterTf;
    @FXML
    Button switchToScene2WithStageIsNullButton;
    @FXML
    Button switchToScene2AndGetStageFromEventButton;
    @FXML
    Button switchToScene2AndGetStageFromAnyComponentButton;
    @FXML
    Button switchToScene2ViaSceneSwitcherUtilViaActionEventButton;
    @FXML
    Button switchToScene2ViaSceneSwitcherUtilViaAnyComponentOfStageButton;

    @Override
    public void onFxmlSceneReady() {
        System.out.println("onFxmlSceneReady() 1");
        System.out.println("SceneSwitcherScene1Controller() 1");
        if (passedArgumentsAreAvailable()) {
            System.out.println("getPassedArguments().get(\"name\")=" + getPassedArguments().get("name"));
            passedParameterTf.setText(getPassedArguments().get("name"));
        }
        scene1Label.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.B) {
                switchToScene2AndGetStageFromEventButton.fire();
            }
            if (keyEvent.getCode() == KeyCode.C) {
                switchToScene2AndGetStageFromAnyComponentButton.fire();
            }
            if (keyEvent.getCode() == KeyCode.D) {
                switchToScene2ViaSceneSwitcherUtilViaActionEventButton.fire();
            }
            if (keyEvent.getCode() == KeyCode.E) {
                switchToScene2ViaSceneSwitcherUtilViaAnyComponentOfStageButton.fire();
            }
        });
    }

    public void switchToScene2WithStageIsNullAction(@SuppressWarnings("unused") ActionEvent event) throws SceneUtilException {
        SceneSwitcher.createFxmlSceneSwitcher(
                        getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"),
                        null)
                .passArgumentsToControllerOfNewScene(Map.ofEntries(Map.entry("name", "Harald")))
                .switchScene();
    }

    public void switchToScene2AndGetStageFromEventAction(ActionEvent event) throws SceneUtilException {
        SceneSwitcher.createFxmlSceneSwitcher(
                        getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"),
                        (Stage) ((Node) event.getSource()).getScene().getWindow())
                .passArgumentsToControllerOfNewScene(Map.ofEntries(Map.entry("name", "Harald")))
                .switchScene();
    }

    public void switchToScene2AndGetStageFromAnyComponentAction(@SuppressWarnings("unused") ActionEvent event) throws SceneUtilException {
        SceneSwitcher.createFxmlSceneSwitcher(
                        getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"),
                        (Stage) scene1Label.getScene().getWindow())
                .passArgumentsToControllerOfNewScene(Map.ofEntries(Map.entry("name", "Harald")))
                .switchScene();
    }

    public void doSwitchToScene2ViaSceneSwitcherUtilViaActionEventButtonClick(ActionEvent event) throws SceneUtilException {
        SceneUtil.switchSceneOfStageViaActionEvent(
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"), null, event);
    }

    public void doSwitchToScene2ViaSceneSwitcherUtilViaAnyComponentOfStageButtonClick(@SuppressWarnings("unused") ActionEvent event) throws SceneUtilException {
        SceneUtil.switchSceneOfStageViaAnyComponent(
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"), null, scene1Label);
    }

}
