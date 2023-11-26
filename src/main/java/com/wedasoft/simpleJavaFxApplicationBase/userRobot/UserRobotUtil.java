package com.wedasoft.simpleJavaFxApplicationBase.userRobot;

import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserRobotUtil {

    public static void typeKeysAfterSeconds(List<KeyCode> keys, int seconds) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
                for (KeyCode key : keys) {
                    new Robot().keyPress(key.getCode());
                    new Robot().keyRelease(key.getCode());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
