package com.wedasoft.simpleJavaFxApplicationBase.testBase;

import javafx.application.Platform;

import static java.util.Objects.nonNull;

/**
 * This class should not be used directly.<br>
 * There is no guarantee, that this class will not change in future.<br>
 * This class implements the framework functions.
 *
 * @author davidweber411
 */
public class SimpleJavaFxTestBaseImpl {

    /* General */
    private static final boolean DEBUG_MODE = false;

    /* PlatformStartup */
    private static final int PS_TIMEOUT_SECONDS_TO_WAIT = 15; // only change this variable if needed!
    private static final int PS_MILLIS_TO_SLEEP_PER_POLL = 250;
    private static final int PS_TICKS_UNTIL_TIMEOUT = (int) ((1000d / PS_MILLIS_TO_SLEEP_PER_POLL) * PS_TIMEOUT_SECONDS_TO_WAIT);
    private static volatile boolean ps_isTimedOut;

    private static boolean ps_runPlatformStartup = true;
    private static boolean ps_platformStartupIsLoading = true;

    /* PlatformRunLater */
    public static final int PRL_TIMEOUT_SECONDS_TO_WAIT = 15; // only change this variable if needed!
    private static final int PRL_MILLIS_TO_SLEEP_PER_POLL = 250;
    private static final int PRL_TICKS_UNTIL_TIMEOUT = (int) ((1000d / PRL_MILLIS_TO_SLEEP_PER_POLL) * PRL_TIMEOUT_SECONDS_TO_WAIT);

    private static volatile boolean prl_jfxThreadIsLoading;
    private static Exception prl_passedExceptionFromJfxThread;

    static synchronized void runAndWaitForPlatformStartup() throws Exception {
        int ticksCounterForTimeout = 0;
        if (ps_runPlatformStartup) {
            Platform.startup(() -> {
                Platform.setImplicitExit(false);
                ps_platformStartupIsLoading = false;
                ps_runPlatformStartup = false;
            });
            while (ps_platformStartupIsLoading) {
                if (DEBUG_MODE) System.out.println("ps looping...");
                //noinspection BusyWait
                Thread.sleep(PS_MILLIS_TO_SLEEP_PER_POLL);
                ticksCounterForTimeout++;
                if (ticksCounterForTimeout >= PS_TICKS_UNTIL_TIMEOUT) {
                    ps_isTimedOut = true;
                    Platform.exit();
                    throw new SimpleJavaFxTestBaseException("Timeout during the execution of runAndWaitForPlatformStartup()");
                }
            }
        }
    }

    static synchronized void runAndWaitForPlatformRunLater(CodeRunner codeRunner) throws Exception {
        int ticksCounterForTimeout = 0;
        prl_jfxThreadIsLoading = true;

        Platform.runLater(() -> {
            try {
                codeRunner.run();
            } catch (Exception e) {
                prl_passedExceptionFromJfxThread = e;
            } finally {
                prl_jfxThreadIsLoading = false;
            }
        });
        while (prl_jfxThreadIsLoading) {
            if (DEBUG_MODE) System.out.println("prl looping...");
            //noinspection BusyWait
            Thread.sleep(PRL_MILLIS_TO_SLEEP_PER_POLL);
            ticksCounterForTimeout++;
            if (ps_isTimedOut || ticksCounterForTimeout >= PRL_TICKS_UNTIL_TIMEOUT) {
                ps_isTimedOut = false;
                throw new SimpleJavaFxTestBaseException("Timeout during the execution of runAndWaitForPlatformRunLater()");
            }
        }
        Thread.sleep(200); // quick and dirty fix for assuming a wrong test result
        if (nonNull(prl_passedExceptionFromJfxThread)) {
            SimpleJavaFxTestBaseException exception = new SimpleJavaFxTestBaseException("The passed JavaFX code could not be initialized without errors. CodeRunner.initialize() threw an Exception. ", prl_passedExceptionFromJfxThread);
            prl_passedExceptionFromJfxThread = null;
            throw exception;
        }
    }

    //    private static void executeAfter(Runnable execute, long afterMillis) {
    //        Task<Void> task = new Task<Void>() {
    //            @Override
    //            protected Void call() {
    //                try {
    //                    Thread.sleep(afterMillis);
    //                } catch (Exception e) {
    //                }
    //                return null;
    //            }
    //        };
    //        task.setOnSucceeded(event -> execute.run());
    //        new Thread(task).start();
    //    }

}
