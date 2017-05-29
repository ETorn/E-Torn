package com.example.admin.e_torn;

/**
 * Created by Admin on 06/02/2017.
 */

public class Constants {

    /**
     * Duració de la splash screen
     */
    static final int SPLASH_DISPLAY_LENGTH = 3000;

    /**
     * Etiqueta de failure de retrofit
     */
    static final String RETROFIT_FAILURE_TAG = "RETROFIT_FAILURE";

    /**
     * Adreça del servidor principal
     */
    public static final String serverURL = "http://192.168.137.1:8080";

    /**
     * Adreça del servidor d'estadístiques
     */
    public static final String caesarURL = "http://192.168.137.1:8081";

    /**
     * Nom del paquet. Ha de concordar amb el de veritat
     */
    public static final String packageName = "com.example.admin.e_torn";

    /**
     * Etiqueta sota la que es guarden les perferencies de la app
     */
    public static final String PREFERENCES_NAME = packageName + "UserPreferences";

    /**
     * Nom de la propietat de preferencies on es guarda el token de fce
     */
    public static final String FCE_TOKEN_NAME = "FCEToken";

    /**
     * Nom de la propietat de preferencies on es guarda la id de user
     */
    public static final String USER_ID = "userId";

    /**
     * Distància maxima en la que es buscaràn supers
     */
    public static int DEFAULT_DISTANCE = 3000;
}
