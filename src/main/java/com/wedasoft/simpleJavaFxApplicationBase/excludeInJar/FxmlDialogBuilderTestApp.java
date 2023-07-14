package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar;

import com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.hibernateUtil.Student;
import com.wedasoft.simpleJavaFxApplicationBase.jfxComponents.JfxComponentUtil;
import com.wedasoft.simpleJavaFxApplicationBase.jfxComponents.diagram.DiagramType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IMPORTANT: Start the application with the gradle task "run".
 */
@SuppressWarnings("unused")
public class FxmlDialogBuilderTestApp extends Application {

    // Start me with the gradle task "run"!
    @Override
    public void start(Stage stage) {
//        testFxmlDialog(stage);
        testDiagrams(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    public void testFxmlDialog(Stage stage) throws IOException {
        System.out.println("Starting testFxmlDialog() ...");
        FXMLLoader fxmlLoader = new FXMLLoader(FxmlDialogBuilderTestApp.class.getResource("" + "/com/wedasoft/simpleJavaFxApplicationBase/excludeInJar/fxmlDialog/fxml-dialog-with-controller-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("testFxmlDialog()");
        stage.setScene(scene);
        stage.show();
        System.out.println("Stopping testFxmlDialog() ...");
    }

    public void testDiagrams(Stage stage) {
        System.out.println("Starting testDiagrams() ...");
        var elements = new ArrayList<>(List.of(
                new Student("Sarah", "Anker", null, 18),
                new Student("Markus", "Bond", null, 25),
                new Student("Markus", "Bond", null, 25),
                new Student("David", "Causalis", null, 31),
                new Student("Julia", "Causalis", null, 31),
                new Student("Ming", "Causalis", null, 31),
                new Student("Anna", "Deutsch", null, 42),
                new Student("Hermann", "Deutsch", null, 42),
                new Student("Hans", "Deutsch", null, 42),
                new Student("Otto", "Deutsch", null, 42)
        ));

        stage.setTitle("testDiagrams()");

//      JfxComponentUtil.createDiagramScene(elements, Student::getAge, DiagramType.BAR_DIAGRAM, "Ages of employees:");// nach age -> 18:1, 25:2, 31:3, 42:4

//        stage.setScene(JfxComponentUtil.createDiagramSceneComponent(elements, Student::getLastName, DiagramType.BAR_DIAGRAM_SIMPLE, "Names of employees:")); // nach lastname -> Anker:1, Bond:2, Causalis:3, Deutsch:4

        stage.setScene(new Scene(JfxComponentUtil.createDiagramSceneComponent(
                elements, Student::getLastName, DiagramType.BAR_DIAGRAM,
                "Names of employees:"))); // nach lastname -> Anker:1, Bond:2, Causalis:3, Deutsch:4


        stage.show();

        System.out.println("Stopping testDiagrams() ...");
    }

}