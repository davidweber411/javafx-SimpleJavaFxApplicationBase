package com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog;

import javafx.fxml.Initializable;

import java.util.Map;

/**
 * @author davidweber411
 */
@SuppressWarnings("unused")
public abstract class FxmlDialogControllerBase implements Initializable {

    private Map<String, String> passedArguments;

    /**
     * This method is called just prior to the window (dialog) being shown. <br>
     * In this method, you can e.g. load data from the database and fill textfields.
     */
    public abstract void onFxmlDialogReady();

    /**
     * This method gets the arguments that were passed to the controller.
     *
     * @return Map of passed arguments.
     */
    public Map<String, String> getPassedArguments() {
        return passedArguments;
    }

    /**
     * NOT TESTED
     *
     * @param mapKey Map of parameters and associated values.
     */
    public int getPassedArgumentsAsInt(String mapKey) {
        try {
            return Integer.parseInt(passedArguments.get(mapKey));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setPassedArguments(Map<String, String> passedArguments) {
        this.passedArguments = passedArguments;
    }
}