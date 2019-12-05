package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static com.example.login.MainActivity.userId;
import static com.example.login.PurposeActivity.content;

public class TransactionHistory extends AppCompatActivity {
    public static final FirebaseDatabase fablabStock = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference transactionHistory = fablabStock.getReference("transactionHistory");

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
        //update transaction history

        Iterator it = hashmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            updateTransactionHistory(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistory.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void updateTransactionHistory(String item, String quantity) {
        HashMap<String,Object> updates = new HashMap<>();
        String pattern ="yyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        updates.put("YYMMDD",date);
        updates.put("item", item);
        updates.put("purpose", content);
        updates.put("quantity",quantity);
        updates.put("studentId", userId);
        UUID uuid = UUID.randomUUID();
        transactionHistory.child(uuid.toString()).updateChildren(updates);
    }
}
