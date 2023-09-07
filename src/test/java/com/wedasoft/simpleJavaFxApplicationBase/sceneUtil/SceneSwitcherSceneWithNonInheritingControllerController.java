package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import javafx.event.ActionEvent;

import java.util.Map;

public class SceneSwitcherSceneWithNonInheritingControllerController {

    @SuppressWarnings("unused")
    public void switchToScene1Action(ActionEvent event) throws SceneUtilException {
        SceneSwitcher.createFxmlSceneSwitcher(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene1.fxml"), null)
                .passArgumentsToControllerOfNewScene(Map.ofEntries(Map.entry("name", "Wilhelm")))
                .switchScene();
    }
}
