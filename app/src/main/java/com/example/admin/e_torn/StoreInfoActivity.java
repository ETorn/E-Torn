package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StoreInfoActivity extends AppCompatActivity {

    String storeId;
    int storeTurn;
    int usersTurn;
    int queue;

    TextView actualTurnText;
    TextView disponibleTurnText;
    TextView queueText;
    TextView aproxTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        storeId = getIntent().getStringExtra("id");
        //fer peticio a retrofit amb aquesta id

        actualTurnText = (TextView) findViewById(R.id.actualTurn);
        actualTurnText.setText(String.valueOf(storeTurn));

        disponibleTurnText = (TextView) findViewById(R.id.disponibleTurn);
        disponibleTurnText.setText(String.valueOf(usersTurn));

        queueText = (TextView) findViewById(R.id.queue);
        queueText.setText(String.valueOf(queue));
        aproxTimeText = (TextView) findViewById(R.id.aproxTime);



    }


}
