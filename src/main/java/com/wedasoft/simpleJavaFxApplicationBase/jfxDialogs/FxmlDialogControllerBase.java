package com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs;

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
     * This method gets the passed argument as int.
     *
     * @param mapKey Key of the argument.
     */
    public int getPassedArgumentsAsInt(String mapKey) throws Exception {
        try {
            return Integer.parseInt(passedArguments.get(mapKey));
        } catch (Exception e) {
            throw new Exception("The value '" + passedArguments.get(mapKey) + "' can not be parsed into an int.");
        }
    }

    public void setPassedArguments(Map<String, String> passedArguments) {
        this.passedArguments = passedArguments;
    }
}