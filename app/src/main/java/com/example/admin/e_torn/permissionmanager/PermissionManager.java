package com.example.admin.e_torn.permissionmanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

    private static final String TAG = "Permission Manager";

    private AppCompatActivity ctx;

    private List<String> permissions;
    private List<String> rationales;

    private PermissionRequestResultListerner listener;

    private int currentRationaleIndex;

    private int requestCode;

    public PermissionManager(AppCompatActivity activity) {
        ctx = activity;

        requestCode = 1;
        permissions = new ArrayList<>();
        rationales = new ArrayList<>();
    }

    public void setPermissionRequestResultListener(PermissionRequestResultListerner l) {
        listener = l;
    }

    public void addPermission(String permission, String rationale) {
        permissions.add(permission);
        rationales.add(rationale);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showNextRationale() {
        Log.d(TAG, "showNextRationale()");

        if (currentRationaleIndex >= permissions.size()) {
            Log.d(TAG, "Acabats els rationales");
            doneShowingRationales();
            return;
        }

        String perm = permissions.get(currentRationaleIndex);

        Log.d(TAG, "Permission: " + perm);
        if (ContextCompat.checkSelfPermission(ctx, perm) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "No tenim aquest permis");

            if (ActivityCompat.shouldShowRequestPermissionRationale(ctx, perm)) {

                if (rationales.get(currentRationaleIndex) != null) {
                    Log.d(TAG, "Mostrant explicació");

                    new AlertDialog.Builder(ctx)
                            .setMessage(rationales.get(currentRationaleIndex))
                            .setCancelable(true)
                            .setPositiveButton("D'acord", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Log.d(TAG, "Rationale dismissed");
                                    showNextRationale();
                                }
                            })
                            .create()
                            .show();
                } else
                    Log.d(TAG, "No explicació especificada. Saltant");
            }
        } else
            Log.d(TAG, "Ja tenim el permis no cal mostrar rationale");

        currentRationaleIndex++;
        showNextRationale();
    }

    private void doneShowingRationales() {
        Log.d(TAG, "doneShowingRationales()");

        Log.d(TAG, "Demanant permisos");
        ActivityCompat.requestPermissions(ctx, permissions.toArray(new String[0]), requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void requestPermissions() {
        Log.d(TAG, ".requestPermissions() cridat");

        if (listener == null)
            throw new ResultListenerNotSpecified("Listener no especificat");

        currentRationaleIndex = 0;

        showNextRationale();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

        listener.onPermissionRequestDone(successAll, result);
    }
}
