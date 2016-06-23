package org.hsrt.mc.emergency.utils;

/**
 * Created by KA on 21.06.2016.
 */
public class Verifier {

    public static boolean isStringEmptyOrNull(String string) {
        boolean isEmptyOrNull;
        if(string == null || string.isEmpty()) {
            isEmptyOrNull =  true;
        } else {
            isEmptyOrNull = false;
        }

        return isEmptyOrNull;
    }

    public static boolean isIntZero(int amountPerDay) {
        boolean isIntZero;

        if(amountPerDay == 0) {
            isIntZero = true;
        } else {
            isIntZero = false;
        }

        return isIntZero;
    }
}
