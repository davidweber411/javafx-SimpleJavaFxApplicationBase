package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import com.wedasoft.simpleJavaFxApplicationBase.testBase.SimpleJavaFxTestBase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class SceneUtilTest extends SimpleJavaFxTestBase {

    Stage stage;
    Scene scene;

    @Test
    void switchSceneRootTest() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/scene1.fxml"))));
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
        Thread.sleep(1000);

        SceneUtil.switchSceneRoot(
                stage,
                getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/scene2.fxml"),
                (Consumer<Scene2Controller>) controller -> controller.init("xxx"));
        assertThat(((TextField) stage.getScene().lookup("#passedParameterTf")).getText()).isEqualTo("xxx");
        Thread.sleep(1000);

        runOnJavaFxThreadAndJoin(() -> pressKeyAsyncInOtherThread(0, KeyCode.A));
        assertThat(((TextField) stage.getScene().lookup("#passedParameterTf")).getText()).isEqualTo("PassedParamToScene1");
        Thread.sleep(1000);

        runOnJavaFxThreadAndJoin(() -> pressKeyAsyncInOtherThread(0, KeyCode.A));
        assertThat(((TextField) stage.getScene().lookup("#passedParameterTf")).getText()).isEqualTo("PassedParamToScene2");
        Thread.sleep(1000);

        runOnJavaFxThreadAndJoin(() -> {
            stage.hide();
            stage.close();
        });
    }

}