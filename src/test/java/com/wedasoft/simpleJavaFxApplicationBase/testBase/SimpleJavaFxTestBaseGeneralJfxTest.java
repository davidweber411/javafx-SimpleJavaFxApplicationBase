package com.wedasoft.simpleJavaFxApplicationBase.testBase;

import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.JfxDialogUtil;
import com.wedasoft.simpleJavaFxApplicationBase.sceneUtil.Scene1Controller;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleJavaFxTestBaseGeneralJfxTest extends SimpleJavaFxTestBase {

    private Stage stage;

    private Button dummyButton;

    @Test
    void generalTest() throws Exception {
        runOnJavaFxThreadAndJoin(() -> {
            stage = JfxDialogUtil.createFxmlDialog(
                    "xxxxxxxx",
                    true,
                    true,
                    getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/sceneUtil/scene1.fxml"),
                    new Dimension2D(600, 500),
                    (Consumer<Scene1Controller>) consumer -> consumer.init("myparamter1"),
                    null);
            stage.setTitle("My Dialog");
            stage.show();
        });
        assertThat(stage).isNotNull();
        assertThat(stage.getTitle()).isEqualTo("My Dialog");

        runOnJavaFxThreadAndJoin(() -> stage.setTitle("New StageTitle"));
        assertThat(stage.getScene().getWidth()).isEqualTo(600);
        assertThat(stage.getScene().getHeight()).isEqualTo(500);
        assertThat(stage.getTitle()).isEqualTo("New StageTitle");

        runOnJavaFxThreadAndJoin(() -> dummyButton = new Button("my dummy button"));
        assertThat(dummyButton).isNotNull();
        assertThat(dummyButton.getText()).isEqualTo("my dummy button");
    }

    @Test
    void generalTest_shallThrow() {
        assertThrows(Exception.class, () -> runOnJavaFxThreadAndJoin(() -> stage = JfxDialogUtil.createFxmlDialog(
                "My Dialog",
                true,
                true,
                getClass().getResource("/this/paht/does/not/exist.fxml"),
                null,
                null,
                null)));
    }

}

