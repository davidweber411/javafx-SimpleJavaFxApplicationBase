package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.fxmlDialog;

import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.FxmlDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("all")
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws Exception {
        welcomeText.setText("Welcome to JavaFX Application!");
        openDialog();
    }

    public void openDialog() throws Exception {
        FxmlDialog.Builder<TestController> dialogBuilder = new FxmlDialog.Builder<TestController>(getClass().getResource("/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/fxml-dialog-with-controller-view.fxml"), null)
                .setStageTitle("My stage title")
                .setStageResizable(true)
                .setModal(false)
                .setCallbackOnDialogClose(() -> System.out.println("CALLBACK ON DIALOG CLOSE!"))
                .setKeySetToCloseDialog(Set.of(KeyCode.ESCAPE))
                .passArgumentsToController(new HashMap<>() {{
                    put("firstname", "David");
                    put("age", "31");
                    put("isMale", "true");
                }});
        FxmlDialog<TestController> dialog = dialogBuilder.get();
        dialog.showAndWait();
    }
}