package com.wedasoft.simpleJavaFxApplicationBase.testBase;

/**
 * This class represnts an exception for the {@link SimpleJavaFxTestBaseException}.
 *
 * @author davidweber411
 */
public class SimpleJavaFxTestBaseException extends Exception {

    /**
     * Constructs an exception.
     *
     * @param message The message.
     */
    public SimpleJavaFxTestBaseException(
            String message) {

        super(message);
    }

    /**
     * Constructs an exception.
     *
     * @param message The message.
     * @param cause   The cause.
     */
    public SimpleJavaFxTestBaseException(
            String message,
            Throwable cause) {

        super(message, cause);
    }

}
