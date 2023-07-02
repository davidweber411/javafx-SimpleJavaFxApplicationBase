package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil.conditions;

public abstract class Condition<VALUE_TYPE> {

    protected String attributeName;

    protected String databaseColumnName;

    protected VALUE_TYPE valueToCheck;

    public Condition(VALUE_TYPE valueToCheck) {
        this.valueToCheck = valueToCheck;
    }

    /**
     * Matches everything, that is exactly equal to the value, example given "David" or 5.
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> EqualsCondition<VALUE_TYPE> isEqualTo(VALUE_TYPE value) {
        return new EqualsCondition<>(value);
    }

    /**
     * Matches everything, that is lower than the value, example given 5.
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> LowerThanCondition<VALUE_TYPE> isLowerThan(VALUE_TYPE value) {
        return new LowerThanCondition<>(value);
    }

    /**
     * Matches everything, that is lower than or equal to the value, example given 5.
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> LowerEqualsThanCondition<VALUE_TYPE> isLowerThanOrEqualTo(VALUE_TYPE value) {
        return new LowerEqualsThanCondition<>(value);
    }

    /**
     * Matches everything, that is greater than the value, example given 5.
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> GreaterThanCondition<VALUE_TYPE> isGreaterThan(VALUE_TYPE value) {
        return new GreaterThanCondition<>(value);
    }

    /**
     * Matches everything, that is greater than or equal to the value, example given 5.
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> GreaterEqualsThanCondition<VALUE_TYPE> isGreaterThanOrEqualTo(VALUE_TYPE value) {
        return new GreaterEqualsThanCondition<>(value);
    }

    /**
     * Matches everything, that is not equal to the value, example given "David" or 5.
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> NotEqualsCondition<VALUE_TYPE> isNotEqualTo(VALUE_TYPE value) {
        return new NotEqualsCondition<>(value);
    }

    /**
     * This condition matches any attribute values, which contain the given value in several circumstances. <br>
     * Wildcards can be used. <br><br>
     * Examples:
     * <ul>
     *     <li>Matches everything, that starts exactly with "Dav":<br>
     *    <code>isLikeCaseSensitive("Dav%")</code>
     *     </li>
     *     <li>Matches everything, that ends exactly with "vid":<br>
     *    <code>isLikeCaseSensitive("%vid")</code>
     *     </li>
     *     <li>Matches everything, that contains exactly "avi":<br>
     *    <code>isLikeCaseSensitive("%avi%")</code>
     *     </li>
     *     <li>Matches everything, that has the exact value "David":<br>
     *    <code>isLikeCaseSensitive("David")</code>
     *     </li>
     * </ul>
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> LikeCondition<VALUE_TYPE> isLikeCaseSensitive(VALUE_TYPE value) {
        return new LikeCondition<>(value);
    }

    /**
     * This condition matches any attribute values, which does not contain the given value in several circumstances. <br>
     * Wildcards can be used. <br><br>
     * Examples:
     * <ul>
     *     <li>Matches everything, that does not start with exactly "Dav":<br>
     *    <code>isNotLikeCaseSensitive("Dav%")</code>
     *     </li>
     *     <li>Matches everything, that does not contain exactly "avi":<br>
     *    <code>isNotLikeCaseSensitive("%avi%")</code>
     *     </li>
     *     <li>...</li>
     * </ul>
     *
     * @param value        The value to not match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> NotLikeCondition<VALUE_TYPE> isNotLikeCaseSensitive(VALUE_TYPE value) {
        return new NotLikeCondition<>(value);
    }

    /**
     * This condition matches any attribute values, which contain the given value in several circumstances. <br>
     * Has the same functionality as {@link #isLikeCaseSensitive <code>isLikeCaseSensitive()</code>}, but with the additional effect, that the case of the value is ignored.<br>
     * Wildcards can be used. <br><br>
     * Examples:
     * <ul>
     *     <li>Matches everything, that contains "avi" in every lower- and uppercase combination ("avi", "Avi", "AVi", "aVI", ...):<br>
     *     The following methods will all match the same results: <br>
     *    <code>isLikeInCaseSensitive("%avi%")</code><br>
     *    <code>isLikeInCaseSensitive("%aVI%")</code><br>
     *    <code>isLikeInCaseSensitive("%AVI%")</code><br>
     *    <code>isLikeInCaseSensitive(...)</code>
     *     </li>
     * </ul>
     *
     * @param value        The value to match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> LikeInCaseSensitiveCondition<VALUE_TYPE> isLikeInCaseSensitive(VALUE_TYPE value) {
        return new LikeInCaseSensitiveCondition<>(value);
    }

    /**
     * This condition matches any attribute values, which does not contain the given value in several circumstances. <br>
     * The case of all characters is ignored.<br>
     * Wildcards can be used. <br><br>
     * Examples:
     * <ul>
     *     <li>Matches everything, that does not start with "dav" in every lower- and uppercase combination ("dav", "Dav", "DAV", "dAv", ...):<br>
     *    <code>isNotLikeInCaseSensitive("dav%")</code>
     *     </li>
     *     <li>Matches everything, that does not contain "avi" in every lower- and uppercase combination ("avi", "Avi", "AVi", "aVI", ...):<br>
     *    <code>isNotLikeInCaseSensitive("%avi%")</code>
     *     </li>
     *     <li>...</li>
     * </ul>
     *
     * @param value        The value to not match.
     * @param <VALUE_TYPE> The type of the value.
     * @return The condition.
     */
    public static <VALUE_TYPE> NotLikeInCaseSensitiveCondition<VALUE_TYPE> isNotLikeInCaseSensitive(VALUE_TYPE value) {
        return new NotLikeInCaseSensitiveCondition<>(value);
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
