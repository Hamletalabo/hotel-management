package com.hamlet.HamletHotel.utils;

import java.security.SecureRandom;

public class Utils {
    private static final String APHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomAlphanumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(APHANUMERIC_STRING.length());
            char randomChar = APHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);

        }
        return stringBuilder.toString();
    }
}
