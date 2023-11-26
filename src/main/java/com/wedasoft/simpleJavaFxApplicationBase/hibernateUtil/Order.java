package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

/**
 * This record represents an {@link Order} object for sorting the result of the {@link HibernateQueryUtil.Finder}.
 *
 * @param attributeName The attribute name.
 * @param asc           Ascending or not.
 */
public record Order(String attributeName, boolean asc) {

}
