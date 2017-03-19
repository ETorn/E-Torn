package com.example.admin.e_torn;


import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

interface PermissionRequestResultListerner {
    void onPermissionRequestDone(boolean successAll, ArrayList<String> grantedPermissions);
}

class PermissionManager {

    private static final String TAG = "Permission Manager";

    private AppCompatActivity ctx;

    private List<String> requestedPermissions;

    PermissionRequestResultListerner listener;

    int requestCode;

    public PermissionManager(AppCompatActivity activity) {
        ctx = activity;

        requestCode = 1;
        requestedPermissions = new ArrayList<>();
        listener = new PermissionRequestResultListerner() {
            @Override
            public void onPermissionRequestDone(boolean successAll, ArrayList<String> grantedPermissions) {
                Log.w(TAG, "Funcio onPermissionRequestDone() per defecte cridada");
                Log.w(TAG, "Fes un override d'aquesta funcio!");
            }
        };
    }

    void setPermissionRequestResultListener(PermissionRequestResultListerner l) {
        listener = l;
    }

    void addPermission(String permission) {
        requestedPermissions.add(permission);
    }

    void requestPermissions() {
        Log.d(TAG, ".requestPermissions() cridat");

        Log.d(TAG, "Permission: " + requestedPermissions.get(0));
        if (ContextCompat.checkSelfPermission(ctx, requestedPermissions.get(0)) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "No tenim aquest permis");

            if (ActivityCompat.shouldShowRequestPermissionRationale(ctx, requestedPermissions.get(0))) {
                Log.d(TAG, "Demanant permisos");

                ActivityCompat.requestPermissions(ctx, new String[]{requestedPermissions.get(0)}, requestCode);
            } else {
                Log.d(TAG, "L'usuari ha denegat el permis, i no vol que el demanem mes");

                //TODO: Aixo no hauria d'estar aqui
                ActivityCompat.requestPermissions(ctx, new String[]{requestedPermissions.get(0)}, requestCode);
            }
        } else {
            Log.d(TAG, "Ja tenim aquest permis");

            listener.onPermissionRequestDone(true, new ArrayList<String>());
        }
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult cridat");

        ArrayList<String> result = new ArrayList<>();

        if (requestCode != this.requestCode) {
            Log.d(TAG, "No es el nostre callback");
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            Log.d(TAG, "Permission:" + permissions[i] + "/Result:" + grantResults[i]);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                result.add(permissions[i]);
            }
        }

        boolean successAll = permissions.length == result.size();

        listener.onPermissionRequestDone(successAll, result);
    }
}
