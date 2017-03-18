package com.example.admin.e_torn.asynctasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Patango on 18/03/2017.
 */

public class GetGpsTask extends AsyncTask<LocationManager, Void, Void> {
    private static final String TAG = "GetGpsAsyncTask";

    AppCompatActivity self;
    final AlertDialog.Builder builder;
    public GetGpsTask(AppCompatActivity self) {
        this.self = self;
        builder = new AlertDialog.Builder(self);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        builder.setMessage("Sembla que el teu GPS está desactivat, vols activar-lo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        self.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected Void doInBackground(LocationManager... params) {
        while (!params[0].isProviderEnabled(LocationManager.GPS_PROVIDER))
            Log.d(TAG, "Waiting untill gps is active");


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
