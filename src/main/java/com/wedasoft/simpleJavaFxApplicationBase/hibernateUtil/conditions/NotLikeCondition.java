package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions;

public class NotLikeCondition<VALUE_TYPE> extends Condition<VALUE_TYPE> {

    public NotLikeCondition(VALUE_TYPE value) {
        super(value);
    }

}
