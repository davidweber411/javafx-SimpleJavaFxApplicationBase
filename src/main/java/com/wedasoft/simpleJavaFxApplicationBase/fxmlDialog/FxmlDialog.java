package com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @param <CONTROLLER_CLASS> The type of the controller class.
 * @author davidweber411
 */
@SuppressWarnings({"unused"})
public class FxmlDialog<CONTROLLER_CLASS extends FxmlDialogControllerBase> {

    private final Stage stage;

    private final CONTROLLER_CLASS controller;

    private FxmlDialog(URL fxmlFileUrl, Dimension2D sceneSize) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFileUrl);
        Parent root = fxmlLoader.load(); // this calls the constructor and after that initialize from jfx()
        Scene scene = sceneSize == null ? new Scene(root) : new Scene(root, sceneSize.getWidth(), sceneSize.getHeight());
        stage = new Stage();
        stage.setScene(scene);
        controller = fxmlLoader.getController();
        if (nonNull(controller)) {
            stage.setOnShowing(e -> controller.onFxmlDialogReady());
        }
    }

    private void setCallbackOnDialogClose(CallbackOnDialogClose callback) {
        stage.setOnHidden(event -> {
            event.consume();
            callback.execute();
            stage.close();
        });
    }

    private void setKeySetToCloseDialog(Set<KeyCode> keySet) {
        if (keySet != null) {
            stage.getScene().setOnKeyReleased(event -> {
                for (KeyCode keyCode : keySet)
                    if (event.getCode() == keyCode) {
                        stage.close();
                        break;
                    }
            });
        }
    }

    private void passArgumentsToController(HashMap<String, String> argumentsToPass) throws Exception {
        if (isNull(controller)) {
            throw new Exception("Can not execute 'passArgumentsToController()' because the controller of the fxml file is null.");
        }
        this.controller.setPassedArguments(argumentsToPass);
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public CONTROLLER_CLASS getController() {
        if (isNull(controller)) {
            return null;
        }
        return controller;
    }


    /********************************************
     * ******************************************
     * Builder
     * ******************************************
     *******************************************/
    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public static class Builder<CONTROLLER_CLASS extends FxmlDialogControllerBase> {

        private final FxmlDialog<CONTROLLER_CLASS> fxmlDialog;

        /**
         * This method/constructor builds an initial {@link FxmlDialog} object.
         *
         * @param fxmlFileUrl Examples: <br>getClass().getResource("test.fxml"), null)
         *                    <br>getClass().getResource("/com/wedasoft/jfxdialoghelpers/test.fxml"), null)
         * @param sceneSize   The size of the {@link Scene}. Null is allowed and computes the scene size automatically.
         * @throws Exception Throws if an error occurs.
         */
        public Builder(URL fxmlFileUrl, Dimension2D sceneSize) throws Exception {
            this.fxmlDialog = new FxmlDialog<>(fxmlFileUrl, sceneSize);
        }

        /**
         * This method sets the title of the {@link Stage}.
         *
         * @param stageTitle The title of the stage.
         * @implNote Is an empty string by default.
         */
        public Builder<CONTROLLER_CLASS> setStageTitle(String stageTitle) {
            this.fxmlDialog.getStage().setTitle(stageTitle);
            return this;
        }

        /**
         * This method makes the {@link Stage} resizable or not.
         *
         * @param isResizable The ability to be resizable.
         * @implNote Is resizable by default.
         */
        public Builder<CONTROLLER_CLASS> setStageResizable(boolean isResizable) {
            this.fxmlDialog.getStage().setResizable(isResizable);
            return this;
        }

        /**
         * This method makes the dialog modal or not.
         *
         * @param isModal The ability to be modal.
         * @implNote Is not modal by default.
         */
        public Builder<CONTROLLER_CLASS> setModal(boolean isModal) {
            if (isModal) {
                this.fxmlDialog.getStage().initModality(Modality.APPLICATION_MODAL);
            } else {
                this.fxmlDialog.getStage().initModality(Modality.NONE);
            }
            return this;
        }

        /**
         * This method adds a {@link CallbackOnDialogClose} function to the dialog, which is executed, when the dialog is closed.
         *
         * @param callback The callback function.
         */
        public Builder<CONTROLLER_CLASS> setCallbackOnDialogClose(CallbackOnDialogClose callback) {
            this.fxmlDialog.setCallbackOnDialogClose(callback);
            return this;
        }

        /**
         * This method adds a set of keys to the dialog, which can be pressed to close the dialog.
         *
         * @param keySet The key containing set.
         * @implNote Example given: .setKeySetToCloseDialog(() -> new KeyCode[]{KeyCode.ESCAPE})
         */
        public Builder<CONTROLLER_CLASS> setKeySetToCloseDialog(Set<KeyCode> keySet) {
            this.fxmlDialog.setKeySetToCloseDialog(keySet);
            return this;
        }

        /**
         * This method is used to pass arguments to the controller.
         * <br> All arguments are stored as Strings.
         * <br> All arguments can be retrieved in the controller by executing getPassedArguments().
         *
         * @param argumentsToPass A HashMap of arguments to pass.
         * @throws Exception If an error occurs.
         * @see FxmlDialogControllerBase#getPassedArguments()
         */
        public Builder<CONTROLLER_CLASS> passArgumentsToController(HashMap<String, String> argumentsToPass) throws Exception {
            this.fxmlDialog.passArgumentsToController(argumentsToPass);
            return this;
        }

        /**
         * This method returns the built {@link FxmlDialog} object.
         */
        public FxmlDialog<CONTROLLER_CLASS> get() {
            return this.fxmlDialog;
        }

    }

    /********************************************
     * ******************************************
     * Interfaces
     * ******************************************
     *******************************************/
    public interface CallbackOnDialogClose {
        void execute();
    }

    public interface KeySetToCloseDialog {
        KeyCode[] getKeys();
    }

}
