package com.example.login.staff;

import com.example.login.R;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionHistoryStaff extends AppCompatActivity {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static DatabaseReference totalStockRef =  database.getReference("totalStock");
    public static DatabaseReference transactionHistoryRef = database.getReference("transactionHistory");
    public static DatabaseReference cardListRef = database.getReference("cardList");

    ListView listView;
    ArrayList<String> a = new ArrayList<>();
    ArrayAdapter<String> ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_staff);

        listView = (ListView) findViewById(R.id.TransactionHistoryList);
        ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,a);
        listView.setAdapter(ad);
        transactionHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    String value = "";
                    HashMap<String,Object> hmap = (HashMap) d.getValue();
                    value += d.getKey().substring(4,10) + "\n\n";
                    value += "Name\t\t\t\t\t\t: " + hmap.get("name") + "\n";
                    value += "Student ID\t: " + hmap.get("studentId") + "\n";
                    value += "Item\t\t\t\t\t\t\t\t: " + hmap.get("item") + "\n";
                    value += "Quantity\t\t\t\t: " + hmap.get("quantity") + "\n";
                    value += "Purpose\t\t\t\t: " + hmap.get("purpose") + "\n";
                    value += "Pillar\t\t\t\t\t\t\t: " + hmap.get("pillar");
                    a.add(value);
                }
                ad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("CANCELLED");
            }
        });
        System.out.println(a);
    }
}
