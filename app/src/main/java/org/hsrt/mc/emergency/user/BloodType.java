package org.hsrt.mc.emergency.user;

/**
 * Created by KA on 14.05.2016.
 */
public class BloodType {

    public static final String ZERO_NEG = "0-";
    public static final String ZERO_POS= "0+";
    public static final String A_NEG = "A-";
    public static final String A_POS = "A+";
    public static final String B_NEG = "B-";
    public static final String B_POS = "B+";
    public static final String AB_NEG = "AB-";
    public static final String AB_POS = "AB+";

    private String bloodType;

    public static String[] bloodtypes = new String[]{ZERO_NEG, ZERO_POS, A_NEG, A_POS, B_NEG, B_POS, AB_NEG, AB_POS};

    public BloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
