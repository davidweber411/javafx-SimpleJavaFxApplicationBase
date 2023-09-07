package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class SceneUtilTest extends SimpleJavaFxTestBase {

    Stage stage;
    Scene scene;

    @Test
    void unitTestsForSceneContentSwitcher() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene1.fxml"))));
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

        SceneUtil.switchSceneContent(
                scene,
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/sceneSwitcherScene2.fxml"),
                null);
        Thread.sleep(500);

        runOnJavaFxThreadAndJoin(() -> {
            stage.hide();
            stage.close();
        });
    }

}