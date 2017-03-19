package com.example.admin.e_torn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.admin.e_torn.adapters.SuperAdapter;
import com.example.admin.e_torn.listeners.RecyclerItemClickListener;
import com.example.admin.e_torn.models.Store;
import com.example.admin.e_torn.models.Super;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.SuperService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SuperActivity extends AppCompatActivity {
    private static final String TAG = "SuperActivity";
    private AppCompatActivity self;
    double userLatitude;
    double userLongitude;
    private List<Super> supers;
    private RecyclerView recyclerView;
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;

    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        Log.d(TAG, "onCreate");

        self = this;

        //(findViewById(R.id.loading_layout)).setVisibility(View.VISIBLE);
        this.context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(self);
        recyclerView.setLayoutManager(linearLayoutManager);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            GetGpsTask task = new GetGpsTask(this);
//            task.execute(locationManager);
//        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "Location changed " + location.toString());
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();

                //Es van fent peticions a /GET supers cada vegada que s'actualitza la localització
                updateData();

                //recyclerView.setHasFixedSize(true); Per a quan sabem que el tamany del recyclerView no canviara

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "Status changed: " + provider + " " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Provider enabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Provider disabled: " + provider);

                new AlertDialog.Builder(self)
                        .setMessage("Sembla que la teva localització està desactivada. Vols activar-la?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                self.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        };

        permissionManager = new PermissionManager(this);
        permissionManager.addPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionManager.setPermissionRequestResultListener(new PermissionRequestResultListerner() {
            @Override
            public void onPermissionRequestDone(boolean successAll, ArrayList<String> grantedPermissions) {
                Log.d(TAG, "Resultat permisos rebuts");

                if (successAll) {
                    Log.d(TAG, "Ens han donat tots els permisos");

                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    } catch (SecurityException ignored) {
                    }
                } else {
                    Log.d(TAG, "No ens han donat tots els permisos");

                    new AlertDialog.Builder(self)
                            .setMessage("Hmmm. ok.")
                            .setCancelable(true)
                            .setPositiveButton("D'acord", null)
                            .create()
                            .show();

                    ((TextView)findViewById(R.id.loading_textView)).setText("No tenim permisos de localització");
                }
            }
        });

        permissionManager.setPermissionRationale(new PermissionRationale() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onShowPermissionRationale(final CallBack cb) {
                new AlertDialog.Builder(self)
                        .setMessage("Necessitem permis per fer servir el GPS per mostrat-te els supermercats més propers. Sense aquest permís la app no funcinarà correctament.")
                        .setCancelable(true)
                        .setPositiveButton("D'acord", null)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                cb.call();
                            }
                        })
                        .create()
                        .show();
            }
        });

        // Començar el proces de demanar permisos
        permissionManager.requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateData() {

        supers = new ArrayList<>();


        SuperService superService = RetrofitManager.retrofit.create(SuperService.class);
        final Call<List<Super>> call = superService.getSupers(userLatitude, userLongitude, Constants.DEFAULT_DISTANCE);

        call.enqueue(new Callback<List<Super>>() {
            @Override
            public void onResponse(Call<List<Super>> call, Response<List<Super>> response) {
                (findViewById(R.id.loading_layout)).setVisibility(View.GONE);
                Log.d("Response", response.body().toString());
                for (Super superM : response.body()) {
                    supers.add(new Super(superM.getId(), superM.getCity(), superM.getAddress(), superM.getPhone(), superM.getFax(), superM.getStores(), superM.getDistance()));
                /*supers.add(new Super("Caprabo3", "Caprabo2 address", "111111", "22222", R.drawable.capraboicon));
                supers.add(new Super("Caprabo4", "Caprabo3 address", "111111", "22222", R.drawable.capraboicon));
                supers.add(new Super("Caprabo5", "Caprabo4 address", "111111", "22222", R.drawable.capraboicon));*/
                }
                SuperAdapter adapter = new SuperAdapter(context, supers);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            List<Store> stores = supers.get(position).getStores();
                            Intent intent = new Intent(context, StoreActivity.class);
                            intent.putParcelableArrayListExtra("stores", (ArrayList<? extends Parcelable>) stores); // Pasem a StoreActivity la array de Stores a carregar
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    })
                );
            }

            @Override
            public void onFailure(Call<List<Super>> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        locationManager.removeUpdates(locationListener);
    }
}
