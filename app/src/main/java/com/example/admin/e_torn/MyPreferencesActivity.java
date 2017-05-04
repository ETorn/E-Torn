package com.example.admin.e_torn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.admin.e_torn.models.User;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.UserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patango on 27/04/2017.
 */

public class MyPreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    final static String TAG = "MyPreferencesActivity";

    SharedPreferences sharedPreferences;

    ETornApplication app;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (ETornApplication) getApplication();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "Key: " + key);
        Log.d(TAG, "Value: " + sharedPreferences.getString(key, "5"));

        app.getUser().setNotificationTurns(Integer.valueOf(sharedPreferences.getString(key, "5")));

        if (app.getUserInfo().size() > 0) {
            UserService userService = RetrofitManager.getInstance(Constants.serverURL).create(UserService.class);
            Call<JSONObject> call = userService.updateUserPref(app.getUser().get_id(), app.getUser());

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    Log.d(TAG, "UserResponse: " + response.body().toString());
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
                }
            });
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
