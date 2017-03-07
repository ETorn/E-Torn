package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StoreInfoActivity extends AppCompatActivity {

    String storeId;
    int storeTurn;
    int usersTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        storeId = getIntent().getStringExtra("id");
        storeTurn = getIntent().getIntExtra("storeTurn", 1);
        usersTurn = getIntent().getIntExtra("usersTurn", 1);

    }
}
