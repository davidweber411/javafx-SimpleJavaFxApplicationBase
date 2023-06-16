package com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

public class JfxDialogUtil {

    /*
     * ******************************************************************
     * ********************** FXML Dialogs ******************************
     * ******************************************************************
     */

    /**
     * This method builds an initial {@link FxmlDialog.Builder} object. The scene size is computed automatically.
     *
     * @param fxmlFileUrl Examples: <br>getClass().getResource("test.fxml"), null)
     *                    <br>getClass().getResource("/com/wedasoft/jfxdialoghelpers/test.fxml"), null)
     * @throws Exception Throws if an error occurs.
     */
    public static <CONTROLLER_CLASS extends FxmlDialogControllerBase> FxmlDialog.Builder<CONTROLLER_CLASS> createFxmlDialogBuilder(
            @SuppressWarnings("unused") Class<CONTROLLER_CLASS> controllerClass, URL fxmlFileUrl)
            throws Exception {
        return createFxmlDialogBuilder(controllerClass, fxmlFileUrl, null);
    }

    /**
     * This method builds an initial {@link FxmlDialog.Builder} object.
     *
     * @param fxmlFileUrl Examples: <br>getClass().getResource("test.fxml"), null)
     *                    <br>getClass().getResource("/com/wedasoft/jfxdialoghelpers/test.fxml"), null)
     * @param sceneSize   The size of the {@link Scene}. Null is allowed and computes the scene size automatically.
     * @throws Exception Throws if an error occurs.
     */
    public static <CONTROLLER_CLASS extends FxmlDialogControllerBase> FxmlDialog.Builder<CONTROLLER_CLASS> createFxmlDialogBuilder(
            @SuppressWarnings("unused") Class<CONTROLLER_CLASS> controllerClass, URL fxmlFileUrl, Dimension2D sceneSize)
            throws Exception {
        return new FxmlDialog.Builder<>(fxmlFileUrl, sceneSize);
    }

    /*
     * ******************************************************************
     * ************* Information / Warning / Error dialogs **************
     * ******************************************************************
     */

    private static Alert createAlertDialog(AlertType alertType, String titleBarText, String headerText, String contentText) {
        Alert alert;
        if (alertType == AlertType.WARNING || alertType == AlertType.ERROR) {
            alert = new Alert(alertType);
        } else {
            alert = new Alert(AlertType.INFORMATION);
        }
        alert.setTitle(titleBarText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

    /**
     * Creates an information dialog.
     *
     * @param message The message to display.
     * @return The information dialog.
     */
    public static Alert createInformationDialog(String message) {
        return createAlertDialog(AlertType.INFORMATION, "Information", null, message);
    }

    /**
     * Creates an information dialog.
     *
     * @param message       The message to display.
     * @param messageHeader The header of the message.
     * @return The information dialog.
     */
    public static Alert createInformationDialog(String message, String messageHeader) {
        return createAlertDialog(AlertType.INFORMATION, "Information", messageHeader, message);
    }

    /**
     * Creates a warning dialog.
     *
     * @param message The message to display.
     * @return The warning dialog.
     */
    public static Alert createWarningDialog(String message) {
        return createAlertDialog(AlertType.WARNING, "Warning", null, message);
    }

    /**
     * Creates a warning dialog.
     *
     * @param message       The message to display.
     * @param messageHeader The header of the message.
     * @return The warning dialog.
     */
    public static Alert createWarningDialog(String message, String messageHeader) {
        return createAlertDialog(AlertType.WARNING, "Warning", messageHeader, message);
    }

    /**
     * Creates an simple error dialog.
     *
     * @param message The message to display.
     * @return The error dialog.
     */
    public static Alert createErrorDialog(String message) {
        return createErrorDialog(message, null);
    }

    /**
     * Creates an error dialog containing the thrown exception inclusive its stacktrace, if the given exception is not
     * null.
     *
     * @param message                The message to display.
     * @param exceptionForStacktrace The thrown exception. The stack trace of this exception will be displayed.
     */
    public static Alert createErrorDialog(String message, Exception exceptionForStacktrace) {
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
     * ******************** Confirm dialogs *****************************
     * ******************************************************************
     */

    private static Alert createConfirmDialog(String titleBarText, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titleBarText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }

    public static boolean displayConfirmDialogAndGetResult(String headerText, String contentText) {
        Alert alert = createConfirmDialog("Confirm", headerText, contentText);
        alert.showAndWait();
        return alert.getResult().getButtonData() == ButtonData.OK_DONE;
    }

    @SuppressWarnings("unused")
    public static void displayCloseStageDialog(Stage stageToClose) {
        Alert alert = createConfirmDialog("Close window", null, "Do you really want to close this window?");
        if (alert.getResult().getButtonData() == ButtonData.OK_DONE) {
            stageToClose.close();
        }
        alert.showAndWait();
    }

    @SuppressWarnings("unused")
    public static void displayExitProgramDialog() {
        Alert alert = createConfirmDialog("Exit program", null, "Do you really want to exit the program?");
        if (alert.getResult().getButtonData() == ButtonData.OK_DONE) {
            Platform.exit();
            System.exit(0);
        }
        alert.showAndWait();
    }

    /*
     * ******************************************************************
     * ******************** Input dialogs *******************************
     * ******************************************************************
     */

    public static TextInputDialog createInputDialog(String titleText, String headerText, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titleText);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        return dialog;
    }

    public static String displayInputDialogAndGetResult(String dialogText) {
        TextInputDialog dialog = createInputDialog("Input", dialogText, null);
        dialog.showAndWait();
        String result = dialog.getResult();
        return result != null ? result : "";
    }

}
