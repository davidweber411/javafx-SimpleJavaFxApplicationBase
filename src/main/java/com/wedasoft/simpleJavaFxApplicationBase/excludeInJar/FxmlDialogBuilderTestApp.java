package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * IMPORTANT: Start the application with the gradle task "run".
 */
public class FxmlDialogBuilderTestApp extends Application {

    // Start me with the gradle task "run"!
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FxmlDialogBuilderTestApp.class.getResource("" +
                "/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/fxml-dialog-with-controller-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}