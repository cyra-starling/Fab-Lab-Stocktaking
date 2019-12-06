package com.example.login.staff;

import com.example.login.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionHistoryStaff extends AppCompatActivity {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static DatabaseReference transactionHistoryRef = database.getReference("transactionHistory");
    public static DatabaseReference totalStock = database.getReference("totalStock");

    public static final HashMap<String,String> monthNum = new HashMap<String, String>();

    final List<String> dateSpinner = new ArrayList<>();
    final List<String> itemSpinner = new ArrayList<>();

    public static String monthSelected;
    public static String itemSelected;

    ListView listView;
    ArrayAdapter<String> ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_staff);

        final Spinner month = findViewById(R.id.monthSpinner);
        final Spinner item = findViewById(R.id.itemSpinner);

        monthNum.put("01","Jan");
        monthNum.put("02","Feb");
        monthNum.put("03","Mar");
        monthNum.put("04","Apr");
        monthNum.put("05","May");
        monthNum.put("06","Jun");
        monthNum.put("07","Jul");
        monthNum.put("08","Aug");
        monthNum.put("09","Sep");
        monthNum.put("10","Oct");
        monthNum.put("11","Nov");
        monthNum.put("12","Dec");

        dateSpinner.add("Please select a month");
        dateSpinner.add("All");
        dateSpinner.add("Jan");
        dateSpinner.add("Feb");
        dateSpinner.add("Mar");
        dateSpinner.add("Apr");
        dateSpinner.add("May");
        dateSpinner.add("Jun");
        dateSpinner.add("Jul");
        dateSpinner.add("Aug");
        dateSpinner.add("Sep");
        dateSpinner.add("Oct");
        dateSpinner.add("Nov");
        dateSpinner.add("Dec");

        itemSpinner.add("Please select an item");
        itemSpinner.add("All");

        totalStock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    for(DataSnapshot detail: item.getChildren()){
                        itemSpinner.add(item.getKey()+" "+detail.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("CANCELLED");
            }
        });

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dateSpinner){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemSpinner){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        month.setAdapter(dateAdapter);
        item.setAdapter(itemAdapter);

        monthSelected = (String)month.getSelectedItem();
        itemSelected = (String)item.getSelectedItem();

        generateList(monthSelected, itemSelected);

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = dateSpinner.get(position);
                generateList(monthSelected, itemSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = itemSpinner.get(position);
                generateList(monthSelected, itemSelected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void generateList(final String month, final String item){
        listView = (ListView) findViewById(R.id.TransactionHistoryList);
        final ArrayList<String> a = new ArrayList<>();
        ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,a);
        listView.setAdapter(ad);
        transactionHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    String value = "";
                    HashMap<String,Object> hmap = (HashMap) d.getValue();

                    String checkItem = (String)hmap.get("item");
                    String checkMonth = ((String)hmap.get("YYMMDD")).substring(2,4);
                    System.out.println(checkItem + "        asdfasdfasfasdf     " + checkMonth);
                    System.out.println(item + "        asdfasdfasfasdf     " + month);
                    if(month == "All" && item == "All") {

                        value += d.getKey() + "\n\n";
                        value += "Student ID\t: " + hmap.get("studentId") + "\n";
                        value += "Item\t\t\t\t\t\t\t\t: " + hmap.get("item") + "\n";
                        value += "Quantity\t\t\t\t: " + hmap.get("quantity") + "\n";
                        value += "Purpose\t\t\t\t: " + hmap.get("purpose") + "\n";
                        a.add(value);

                    }
                    else if(month == "All" && item != "All")
                    {
                        if (item.contains(checkItem)) {
                            value += d.getKey() + "\n\n";
                            value += "Student ID\t: " + hmap.get("studentId") + "\n";
                            value += "Item\t\t\t\t\t\t\t\t: " + hmap.get("item") + "\n";
                            value += "Quantity\t\t\t\t: " + hmap.get("quantity") + "\n";
                            value += "Purpose\t\t\t\t: " + hmap.get("purpose") + "\n";
                            a.add(value);
                        }
                    }
                    else if(month != "All" && item == "All"){
                        if (month.contains(monthNum.get(checkMonth))) {
                            value += d.getKey() + "\n\n";
                            value += "Student ID\t: " + hmap.get("studentId") + "\n";
                            value += "Item\t\t\t\t\t\t\t\t: " + hmap.get("item") + "\n";
                            value += "Quantity\t\t\t\t: " + hmap.get("quantity") + "\n";
                            value += "Purpose\t\t\t\t: " + hmap.get("purpose") + "\n";
                            a.add(value);
                        }
                    }
                    else{
                        if (item.contains(checkItem) && month.contains(monthNum.get(checkMonth))) {
                            value += d.getKey() + "\n\n";
                            value += "Student ID\t: " + hmap.get("studentId") + "\n";
                            value += "Item\t\t\t\t\t\t\t\t: " + hmap.get("item") + "\n";
                            value += "Quantity\t\t\t\t: " + hmap.get("quantity") + "\n";
                            value += "Purpose\t\t\t\t: " + hmap.get("purpose") + "\n";
                            a.add(value);
                        }
                    }
                }
                ad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("CANCELLED");
            }
        });
    }
}
