package com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


public class JfxDialogUtil {

    /*
     * ******************************************************************
     * ********************** FXML dialogs ******************************
     * ******************************************************************
     */

    public static void createAndShowFxmlDialog(
            String title,
            boolean dialogIsModal,
            boolean dialogIsResizeable,
            URL absoluteFxmlFileUrl,
            Dimension2D sceneSize,
            @SuppressWarnings("rawtypes") Consumer initMethodOfController,
            Runnable callbackOnDialogClose)
            throws IOException {

        createFxmlDialog(title,
                dialogIsModal,
                dialogIsResizeable,
                absoluteFxmlFileUrl,
                sceneSize,
                initMethodOfController,
                callbackOnDialogClose)
                .showAndWait();
    }

    public static Stage createFxmlDialog(
            String title,
            boolean dialogIsModal,
            boolean dialogIsResizeable,
            URL absoluteFxmlFileUrl,
            Dimension2D sceneSize,
            @SuppressWarnings("rawtypes") Consumer initMethodOfController,
            Runnable callbackOnDialogClose)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(absoluteFxmlFileUrl);
        Parent root = loader.load(); // this calls the constructor and after that initialize from jfx()
        Object viewController = loader.getController();
        Scene scene = sceneSize == null ? new Scene(root) : new Scene(root, sceneSize.getWidth(), sceneSize.getHeight());

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(dialogIsModal ? Modality.APPLICATION_MODAL : Modality.NONE);
        stage.setResizable(dialogIsResizeable);
        stage.setScene(scene);
        stage.setOnHidden(event -> {
            event.consume();
            if (callbackOnDialogClose != null) {
                callbackOnDialogClose.run();
            }
            stage.close();
        });

        if (initMethodOfController != null) {
            //noinspection unchecked
            initMethodOfController.accept(viewController);
        }
        return stage;
    }

    /*
     * ******************************************************************
     * ************* Error dialogs **************************************
     * ******************************************************************
     */

    /**
     * Creates an simple error dialog.
     *
     * @param message The message to display.
     * @return The error dialog.
     */
    public static Alert createErrorDialog(
            String message) {

        return createErrorDialog(message, null);
    }

    /**
     * Creates an error dialog containing the thrown exception inclusive its stacktrace, if the given exception is not
     * null.
     *
     * @param message                The message to display.
     * @param exceptionForStacktrace The thrown exception. The stack trace of this exception will be displayed.
     */
    public static Alert createErrorDialog(
            String message,
            Exception exceptionForStacktrace) {

        Alert alert = createAlertDialog(AlertType.ERROR, "Error", null, message);
        if (exceptionForStacktrace != null) {
            StringWriter sw = new StringWriter();
            exceptionForStacktrace.printStackTrace(new PrintWriter(sw));
            TextArea area = new TextArea(sw.toString());
            area.setWrapText(true);
            area.setEditable(false);
            alert.getDialogPane().setContent(area);

            alert.setHeaderText(message);
            alert.getDialogPane().setMaxWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.8);
            alert.getDialogPane().setMaxHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8);
            alert.setResizable(true);
        }
        return alert;
    }

    /*
     * ******************************************************************
     * ************* Warning dialogs ************************************
     * ******************************************************************
     */

    /**
     * Creates a warning dialog.
     *
     * @param message The message to display.
     * @return The warning dialog.
     */
    public static Alert createWarningDialog(
            String message) {

        return createAlertDialog(AlertType.WARNING, "Warning", null, message);
    }

    /**
     * Creates a warning dialog.
     *
     * @param message       The message to display.
     * @param messageHeader The header of the message.
     * @return The warning dialog.
     */
    public static Alert createWarningDialog(
            String message,
            String messageHeader) {

        return createAlertDialog(AlertType.WARNING, "Warning", messageHeader, message);
    }

    /*
     * ******************************************************************
     * ************* Information dialogs ********************************
     * ******************************************************************
     */

    /**
     * Creates an information dialog.
     *
     * @param message The message to display.
     * @return The information dialog.
     */
    public static Alert createInformationDialog(
            String message) {

        return createAlertDialog(AlertType.INFORMATION, "Information", null, message);
    }

    /**
     * Creates an information dialog.
     *
     * @param message       The message to display.
     * @param messageHeader The header of the message.
     * @return The information dialog.
     */
    public static Alert createInformationDialog(
            String message,
            String messageHeader) {

        return createAlertDialog(AlertType.INFORMATION, "Information", messageHeader, message);
    }

    /*
     * ******************************************************************
     * ******************** Column dialogs ******************************
     * ******************************************************************
     */

    /**
     * Creates a dialog which displays the given node in a grid. All columns will have the same width.
     *
     * @param title           The title of the dialog.
     * @param amountOfColumns Specifies the amount of columns.
     * @param nodes           All nodes that shall be displayed in the grid system.
     */
    public static Alert createDialogWithColumns(
            String title,
            int amountOfColumns,
            int cellGap,
            List<Node> nodes) {

        GridPane grid = new GridPane();
        grid.setHgap(cellGap);
        grid.setVgap(cellGap);
        for (int i = 0; i < amountOfColumns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        int amountOfRows = (int) Math.ceil(nodes.size() / (double) amountOfColumns);
        Iterator<Node> iterator = nodes.iterator();
        for (int rowI = 0; rowI < amountOfRows; rowI++) {
            for (int columnJ = 0; columnJ < amountOfColumns; columnJ++) {
                if (iterator.hasNext()) {
                    grid.add(iterator.next(), columnJ, rowI);
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title != null ? title : "");
        alert.setHeaderText(null);
        // alert.setGraphic(null);
        alert.setResizable(true);
        alert.getDialogPane().setContent(grid);
        return alert;
    }

    /*
     * ******************************************************************
     * ******************** Confirm dialogs *****************************
     * ******************************************************************
     */

    public static boolean displayConfirmDialogAndGetResult(
            String dialogTitle,
            String dialogText) {

        Alert alert = createConfirmDialog("Confirm", dialogTitle, dialogText);
        alert.showAndWait();
        return alert.getResult().getButtonData() == ButtonData.OK_DONE;
    }

    public static void displayCloseStageDialog(
            Stage stageToClose,
            String dialogTitle,
            String dialogText) {

        Alert alert = createConfirmDialog(dialogTitle, null, dialogText);
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonData.OK_DONE) {
            stageToClose.close();
        }
    }

    public static void displayCloseStageDialog(
            Stage stageToClose) {

        displayCloseStageDialog(stageToClose, "Close window", "Do you really want to close this window?");
    }

    public static void displayExitProgramDialog(
            String dialogTitle,
            String dialogText) {

        Alert alert = createConfirmDialog(dialogTitle, null, dialogText);
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonData.OK_DONE) {
            Platform.exit();
            System.exit(0);
        }
    }

    public static void displayExitProgramDialog() {
        displayExitProgramDialog("Exit program", "Do you really want to exit the program?");
    }

    /*
     * ******************************************************************
     * ******************** Input dialogs *******************************
     * ******************************************************************
     */

    public static TextInputDialog createInputDialog(
            String titleText,
            String headerText,
            String contentText) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titleText);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        return dialog;
    }

    public static String displayInputDialogAndGetResult(
            String dialogText) {

        TextInputDialog dialog = createInputDialog("Input", dialogText, null);
        dialog.showAndWait();
        String result = dialog.getResult();
        return result != null ? result : "";
    }

    /*
     * ******************************************************************
     * ******************** Private methods *****************************
     * ******************************************************************
     */
    private static Alert createAlertDialog(
            AlertType alertType,
            String dialogTitleText,
            String dialogBodyHeaderText,
            String dialogContentText) {

        Alert alert;
        if (alertType == AlertType.WARNING || alertType == AlertType.ERROR) {
            alert = new Alert(alertType);
        } else {
            alert = new Alert(AlertType.INFORMATION);
        }
        alert.setTitle(dialogTitleText);
        alert.setHeaderText(dialogBodyHeaderText);
        alert.setContentText(dialogContentText);
        return alert;
    }

    private static Alert createConfirmDialog(
            String titleBarText,
            String headerText,
            String contentText) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titleBarText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

}
