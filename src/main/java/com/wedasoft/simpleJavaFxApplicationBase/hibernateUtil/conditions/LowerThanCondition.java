package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions;

import com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.HibernateQueryUtil;

/**
 * Represents a condition for querying a database with the {@link HibernateQueryUtil.Finder}.
 */
public class LowerThanCondition<VALUE_TYPE> extends Condition<VALUE_TYPE> {

    public LowerThanCondition(VALUE_TYPE value) {
        super(value);
    }

}
