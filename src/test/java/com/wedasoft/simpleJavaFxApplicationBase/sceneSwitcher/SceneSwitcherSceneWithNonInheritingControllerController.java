package com.wedasoft.simpleJavaFxApplicationBase.sceneSwitcher;

import javafx.event.ActionEvent;

import java.util.Map;

public class SceneSwitcherSceneWithNonInheritingControllerController {

    @SuppressWarnings("unused")
    public void switchToScene1Action(ActionEvent event) throws SceneSwitcherException {
        SceneSwitcher.createFxmlSceneSwitcher(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneSwitcher/sceneSwitcherScene1.fxml"), null)
                .passArgumentsToControllerOfNewScene(Map.ofEntries(Map.entry("name", "Wilhelm")))
                .switchScene();
    }
}
