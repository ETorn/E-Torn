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

interface CallBack {
    void call();
}

interface PermissionRationale {
    void onShowPermissionRationale(CallBack cb);
}

class PermissionManager {

    private static final String TAG = "Permission Manager";

    private AppCompatActivity ctx;

    private List<String> requestedPermissions;

    private PermissionRequestResultListerner listener;
    private PermissionRationale rationale;

    private int requestCode;

    private boolean forceRetry;

    PermissionManager(AppCompatActivity activity, boolean forceRetry) {
        ctx = activity;
        this.forceRetry = forceRetry;

        requestCode = 1;
        requestedPermissions = new ArrayList<>();
        listener = new PermissionRequestResultListerner() {
            @Override
            public void onPermissionRequestDone(boolean successAll, ArrayList<String> grantedPermissions) {
                Log.w(TAG, "Funcio onPermissionRequestDone() per defecte cridada");
                Log.w(TAG, "Fes un override d'aquesta funcio!");
            }
        };
        rationale = new PermissionRationale() {
            @Override
            public void onShowPermissionRationale(CallBack cb) {
                cb.call();
            }
        };
    }

    PermissionManager(AppCompatActivity a) {
        this(a, false);
    }

    void setPermissionRequestResultListener(PermissionRequestResultListerner l) {
        listener = l;
    }

    void setPermissionRationale(PermissionRationale r) {
        rationale = r;
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
                Log.d(TAG, "Mostrant explicació");

                rationale.onShowPermissionRationale(new CallBack() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Demanant permisos");

                        ActivityCompat.requestPermissions(ctx, new String[]{requestedPermissions.get(0)}, requestCode);
                    }
                });

            } else {
                Log.d(TAG, "Primera vegada que demanem permisos, o l'usuari no vol saber res de nosltres. Demanant permisos");

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

        Log.d(TAG, "successAll = " + successAll);

        if (!successAll && forceRetry) {
            Log.d(TAG, "Reintentant (forceRetry = true)");
            requestPermissions();
        }

        listener.onPermissionRequestDone(successAll, result);
    }
}
