package com.wedasoft.simpleJavaFxApplicationBase.sceneUtil;

import java.util.Map;

/**
 * @author davidweber411
 */
public abstract class FxmlSceneControllerBase {

    /**
     * This map contains all passed attributes.
     */
    private Map<String, String> passedArguments;

    /**
     * This method is called after setting the new scene into the stage. <br>
     * The execution order of the initialization methods is:
     * <ol>
     *     <li><b>Constructor() of the controller is called first.</b><br>
     *     You can do constructor things here, but it is recommend to do nearly everything in onFxmlSceneReady().</li>
     *     <li><b>initialize() of the controller is called second.</b><br>
     *     You can load JavaFX specific things here.</li>
     *     <li><b>onFxmlSceneReady() of the controller is called third.</b><br>
     *     You can load data from the database and fill textfields here.</li>
     * </ol>
     */
    public abstract void onFxmlSceneReady();

    /**
     * Checks if there are passed arguments available.
     *
     * @return False if passedArguments is null or empty, otherwise true.
     */
    public boolean passedArgumentsAreAvailable() {
        return passedArguments != null && !passedArguments.isEmpty();
    }

    /**
     * This method gets the arguments that were passed to the controller.
     *
     * @return Map of passed arguments.
     */
    public Map<String, String> getPassedArguments() {
        return passedArguments;
    }

    /**
     * This method sets the map of passed arguments. Beware: This method replaces the complete map with a new one!
     *
     * @param passedArguments The passed attributes.
     */
    public void setPassedArguments(Map<String, String> passedArguments) {
        this.passedArguments = passedArguments;
    }

}
