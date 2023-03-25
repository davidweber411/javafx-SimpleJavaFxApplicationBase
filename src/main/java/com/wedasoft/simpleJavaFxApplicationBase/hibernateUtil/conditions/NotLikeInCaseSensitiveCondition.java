package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions;

public class NotLikeInCaseSensitiveCondition<VALUE_TYPE> extends Condition<VALUE_TYPE> {

    public NotLikeInCaseSensitiveCondition(VALUE_TYPE value) {
        super(value);
    }

}
