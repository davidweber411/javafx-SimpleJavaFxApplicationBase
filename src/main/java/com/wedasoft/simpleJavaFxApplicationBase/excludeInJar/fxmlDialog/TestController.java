package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.fxmlDialog;

import com.wedasoft.simpleJavaFxApplicationBase.jfxDialogs.FxmlDialogControllerBase;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal"})
public class TestController extends FxmlDialogControllerBase {

    private int intValue;

    private String stringValue;

    public TestController() {
        intValue = 5;
        stringValue = "coolValue";
    }

    @Override
    public void onFxmlDialogReady() {
        int testInt = 5;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

}
