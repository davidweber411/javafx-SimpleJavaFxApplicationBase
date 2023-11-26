package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions;

import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.HibernateQueryUtil;

/**
 * Represents a condition for querying a database with the {@link HibernateQueryUtil.Finder}.
 */
public class LowerEqualsThanCondition<VALUE_TYPE> extends Condition<VALUE_TYPE> {

    public LowerEqualsThanCondition(VALUE_TYPE value) {
        super(value);
    }

}
