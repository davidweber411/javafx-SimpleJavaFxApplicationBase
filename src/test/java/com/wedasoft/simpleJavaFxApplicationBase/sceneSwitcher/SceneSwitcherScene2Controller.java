package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.Map;

public class SceneSwitcherScene2Controller extends FxmlSceneControllerBase {

    @FXML
    Label scene2Label;

    @FXML
    TextField passedParameterTf;

    @FXML
    Button switchToScene1Button;

    @Override
    public void onFxmlSceneReady() {
        System.out.println("onFxmlSceneReady() 2");
        System.out.println("SceneSwitcherScene2Controller() 2");
        if (passedArgumentsAreAvailable()) {
            passedParameterTf.setText(getPassedArguments().get("name"));
        }
        scene2Label.getScene().setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.X) {
                switchToScene1Button.fire();
            }
        });
    }

    @SuppressWarnings("unused")
    public void switchToScene1Action(ActionEvent event) throws SceneSwitcherException {
        SceneSwitcher.createFxmlSceneSwitcher(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"), null)
                .passArgumentsToControllerOfNewScene(Map.ofEntries(Map.entry("name", "Wilhelm")))
                .switchScene();
    }
}
