package com.wedasoft.simpleJavaFxApplicationBase.systemUtil;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author davidweber411
 */
public class SystemUtils {

    /**
     * Copies the given value to the system mouse clipboard.
     *
     * @param stringValueToCopy The value to copy.
     * @param trimValue         Indicates if the value should be trimmed.
     */
    public static void copyToClipBoard(String stringValueToCopy, boolean trimValue) {
        if (stringValueToCopy == null || stringValueToCopy.isEmpty()) {
            return;
        }

        ClipboardContent content = new ClipboardContent();
        content.putString(trimValue ? stringValueToCopy.trim() : stringValueToCopy);

        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Returns the name of the main class of the started/invoked application.
     *
     * @return The name of the main class, example given
     * "com.wedasoft.jpackageExecutor.Java2nativeConverterMain". If this
     * operation goes wrong, the return value will be null.
     * @author David Weber
     */
    public static String getMainClassName() {
        Class<?> mainClass = null;
        for (Map.Entry<Thread, StackTraceElement[]> threadToStackTraceEntry : Thread.getAllStackTraces().entrySet()) {
            for (StackTraceElement stackTrace : threadToStackTraceEntry.getValue()) {
                try {
                    String stackClass = stackTrace.getClassName();
                    if (stackClass.indexOf("$") > 0) {
                        stackClass = stackClass.substring(0, stackClass.lastIndexOf("$"));
                    }
                    Class<?> instance = Class.forName(stackClass);
                    Method method = instance.getDeclaredMethod("main", String[].class);
                    if (Modifier.isStatic(method.getModifiers())) {
                        mainClass = instance;
                        break;
                    }
                } catch (Exception ignored) {
                    // is ignored
                }
            }
        }
        return mainClass != null ? mainClass.getName() : null;
    }

}
