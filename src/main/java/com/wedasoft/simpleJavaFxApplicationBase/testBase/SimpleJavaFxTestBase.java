package com.wedasoft.simpleJavaFxApplicationBase.testBase;

/**
 * This is the only class, that shall be used by developers for interacting with this framework.<br>
 * All other classes of this framework are not meant to be used directly.
 *
 * @author davidweber411
 */
public abstract class SimpleJavaFxTestBase {

    /**
     * This method runs the passed code on the JavaFX thread.<br>
     * The code to run is passed with the CodeRunner Interface.<br>
     * The main thread is forced to wait until the code is executed.<br>
     * <b>Do not put assertions in the CodeRunner. This will not work properly.</b>
     *
     * @param codeRunner Passes code to the JavaFX thread.
     * @throws Exception If an error occurs.
     */
    public synchronized void runOnJavaFxThreadAndJoin(CodeRunner codeRunner) throws Exception {
        SimpleJavaFxTestBaseImpl.runAndWaitForPlatformStartup();
        SimpleJavaFxTestBaseImpl.runAndWaitForPlatformRunLater(codeRunner);
    }

}
