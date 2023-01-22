package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.fxmlDialog;

import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.FxmlDialogControllerBase;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class TestController extends FxmlDialogControllerBase {

    private int intValue = 5;

    private String stringValue = "coolValue";

    @Override
    public void onFxmlDialogReady() {
        int testInt = 5;
        System.out.println("testInt=" + testInt);
        if (getPassedArguments() != null) {
            String firstname = getPassedArguments().getOrDefault("firstname", "default name");
            int age = Integer.parseInt(getPassedArguments().getOrDefault("age", "-1"));
            boolean isMale = Boolean.parseBoolean(getPassedArguments().getOrDefault("isMale", "false"));
            System.out.println("firstname=" + firstname);
            System.out.println("age=" + age);
            System.out.println("isMale=" + isMale);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
