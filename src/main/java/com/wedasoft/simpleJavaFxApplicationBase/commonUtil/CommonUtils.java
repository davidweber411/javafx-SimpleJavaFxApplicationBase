package com.wedasoft.simpleJavaFxApplicationBase.commonUtil;

public class CommonUtils {

    /**
     * Checks if a string is empty, blank or null.
     *
     * @param string The string to check.
     * @return True if the string is empty, blank or null.
     */
    public static boolean isEmptyBlankOrNull(
            String string) {

        return string == null || string.trim().isEmpty();
    }

    /**
     * Rounds a float on the given amount of commaspaces.
     *
     * @param numberToRound         The number to round.
     * @param numberOfDecimalPlaces The number of commaspaces.
     * @return The rounded float.
     */
    public static float roundNumberOnCommaspace(
            float numberToRound,
            int numberOfDecimalPlaces) {

        return (float) roundNumberOnCommaspace((double) numberToRound, numberOfDecimalPlaces);
    }

    /**
     * Rounds a double on the given amount of commaspaces.
     *
     * @param numberToRound         The number to round.
     * @param numberOfDecimalPlaces The number of commaspaces.
     * @return The rounded double.
     */
    public static double roundNumberOnCommaspace(
            double numberToRound,
            int numberOfDecimalPlaces) {

        double x = 1;
        for (int i = 0; i < numberOfDecimalPlaces; i++) {
            x *= 10;
        }
        return Math.round(numberToRound * x) / x;
    }

}
