package com.example.admin.e_torn;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,SuperActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
                //falta  posar xml de recycler viewcorrecte
            }
        }, Constants.SPLASH_DISPLAY_LENGTH);
    }
}
