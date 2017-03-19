package com.example.admin.e_torn;

import java.text.DecimalFormat;

/**
 * Created by gcqui on 17/03/2017.
 */

public class Utils {
    private static DecimalFormat decimalFormat = new DecimalFormat("0.0");
    public static String humanizeMeters(long meters) {
        if (meters > 1000) {
            return decimalFormat.format(meters / 1000.0f) + "Km";
        }

        return meters + "m";
    };
    public static String humanizeMeters(int meters) {
        return humanizeMeters((long) meters);
    }
}
