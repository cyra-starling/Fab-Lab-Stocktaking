package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class TransactionHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactionhistory);

        Button backbutton = findViewById(R.id.BackButton);
        //retrieve intent
        Intent intent = getIntent();
        HashMap<String,Integer> hashmap = (HashMap<String, Integer>) intent.getSerializableExtra("history");
        TextView transaction_list = findViewById(R.id.transaction_list);
        transaction_list.setText(hashmap.toString());

        //goes back to previous activity, do i need to clear data?????
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistory.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
