package com.example.admin.e_torn;

import android.Manifest;
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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    double userLatitude;
    double userLongitude;
    PermissionManager permissionManager;
    private AppCompatActivity self;
    private List<Super> supers;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    SuperAdapter superAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_supers);

        Log.d(TAG, "onCreate");

        self = this;

        //(findViewById(R.id.loading_layout)).setVisibility(View.VISIBLE);
        this.context = getApplicationContext();

        superAdapter  = new SuperAdapter(context);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(self);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(superAdapter); // S'ha de setejar abans el adapter del recyclerView al crearse (encara que estigui buit) per evitar errors

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                                self.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                                Toast.makeText(self, ":(", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
            }
        };

        permissionManager = new PermissionManager(this);
        permissionManager.addPermission(Manifest.permission.ACCESS_FINE_LOCATION, "Necessitem permis per fer servir el GPS per mostrat-te els supermercats més propers. Sense aquest permís la app no funcinarà correctament.");
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
                            .setPositiveButton(R.string.ok, null)
                            .create()
                            .show();

                    ((TextView)findViewById(R.id.loading_textView)).setText(R.string.no_location_permission);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");

        // Començar el proces de demanar permisos
        permissionManager.requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateData() {
        //swipeRefreshLayout
        supers = new ArrayList<>();


        SuperService superService = RetrofitManager.getInstance(Constants.serverURL).create(SuperService.class);
        final Call<List<Super>> call = superService.getSupers(userLatitude, userLongitude, Constants.DEFAULT_DISTANCE);

        call.enqueue(new Callback<List<Super>>() {
            @Override
            public void onResponse(Call<List<Super>> call, Response<List<Super>> response) {
                (findViewById(R.id.loading_layout)).setVisibility(View.GONE);
                Log.d(TAG + " Response", response.body().toString());

                if (response.body().size() != 0) {
                    for (Super superM : response.body()) {
                        supers.add(new Super(superM.getId(), superM.getCity(), superM.getAddress(), superM.getPhone(), superM.getFax(), superM.getStores(), superM.getDistance()));
                        /*supers.add(new Super("Caprabo3", "Caprabo2 address", "111111", "22222", R.drawable.capraboicon));
                        supers.add(new Super("Caprabo4", "Caprabo3 address", "111111", "22222", R.drawable.capraboicon));
                        supers.add(new Super("Caprabo5", "Caprabo4 address", "111111", "22222", R.drawable.capraboicon));*/
                    }

                    if (response.body().size() == 1) {
                        startStoreIntent(0);
                        return;  //TODO: Hack
                    }

                    superAdapter = new SuperAdapter(context, supers);
                    recyclerView.setAdapter(superAdapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    startStoreIntent(position);
                                }
                            })
                    );
                }
                else {
                    ((TextView)findViewById(R.id.loading_textView)).setText(R.string.info_no_close_super);
                    (findViewById(R.id.loading_layout)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.progressBar)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Super>> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

    }

    public void startStoreIntent (int position){
        List<Store> stores = supers.get(position).getStores();
        Intent intent = new Intent(context, StoreActivity.class);
        intent.putParcelableArrayListExtra("stores", (ArrayList<? extends Parcelable>) stores); // Pasem a StoreActivity la array de Stores a carregar
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Peta si no es pasa aquesta flag
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Impedeix el retorn a aquesta activitat amb back button
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(locationListener);
    }
}
