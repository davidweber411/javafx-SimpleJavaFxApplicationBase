package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions;

public abstract class Condition<VALUE_TYPE> {

    protected String attributeName;

    protected String databaseColumnName;

    protected VALUE_TYPE valueToCheck;

    public Condition(VALUE_TYPE valueToCheck) {
        this.valueToCheck = valueToCheck;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public VALUE_TYPE getValueToCheck() {
        return valueToCheck;
    }

    public String getDatabaseColumnName() {
        return databaseColumnName;
    }

    public void setDatabaseColumnName(String databaseColumnName) {
        this.databaseColumnName = databaseColumnName;
    }
}
