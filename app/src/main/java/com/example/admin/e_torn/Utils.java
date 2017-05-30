package com.example.admin.e_torn;

import java.text.DecimalFormat;

/**
 * Created by gcqui on 17/03/2017.
 */

/**
 * Utilitats varies
 *
 * @author Guillem Cruz
 */
public class Utils {
    private static DecimalFormat decimalFormat = new DecimalFormat("0.0");

    /**
     * Rep un nombre de metres i els transforma a kilometres si es possible
     * @param meters long metres a transformar
     * @return String amb el valor convertit a km o m
     */
    public static String humanizeMeters(long meters) {
        if (meters > 1000) {
            return decimalFormat.format(meters / 1000.0f) + "Km";
        }

        return meters + "m";
    };

    /**
     * @see Utils#humanizeMeters(long)
     */
    public static String humanizeMeters(int meters) {
        return humanizeMeters((long) meters);
    }
}
