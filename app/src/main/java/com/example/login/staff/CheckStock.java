package com.example.login.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.view.ViewGroup;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.R;
import com.google.firebase.database.ValueEventListener;
import com.levitnudi.legacytableview.LegacyTableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.levitnudi.legacytableview.LegacyTableView.MAASAI;


public class CheckStock extends AppCompatActivity {

    //initialize firebase
    public static final FirebaseDatabase fablabStock = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference totalStock = fablabStock.getReference("totalStock");
    public static DatabaseReference transactionHistory = fablabStock.getReference("transactionHistory");
    public static int dateSelected;
    public static final String LGcontent = "lgcontent";
    public static final String LGtitle = "lgtitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stock);

        Intent intentTo = getIntent();
        System.out.println();

        String[] LegacyTitle = intentTo.getStringArrayExtra(CheckStock.LGtitle);
        String[] LegacyContent = intentTo.getStringArrayExtra(CheckStock.LGcontent);


        if (LegacyContent!=null) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
            LegacyTableView.insertLegacyTitle(LegacyTitle);
            LegacyTableView.insertLegacyContent(LegacyContent);

            LegacyTableView legacyTableView = (LegacyTableView) findViewById(R.id.legacy_table_view);
            legacyTableView.setContent(LegacyTableView.readLegacyContent());
            legacyTableView.setTitle(LegacyTableView.readLegacyTitle());

            legacyTableView.setTablePadding(7);

            //to enable users to zoom in and out:
            legacyTableView.setZoomEnabled(true);
            legacyTableView.setShowZoomControls(false);
            legacyTableView.setTheme(MAASAI);

            //remember to build your table as the last step
            legacyTableView.build();
        }

        final Spinner items = findViewById(R.id.items);
        final Spinner date = findViewById(R.id.date);
        final List<String> itemSpinner = new ArrayList<>();
        final List<String> dateSpinner = new ArrayList<>();

        dateSpinner.add("Please Select Month 1st!");
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
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
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

        date.setAdapter(dateAdapter);
        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateSelected = position;
                if (dateSelected > 0) {
                    Toast.makeText(CheckStock.this, "Date Selected: " + date.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //items dropdownlist
        totalStock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemSpinner.add("All");
                for (DataSnapshot items : dataSnapshot.getChildren()) {
                    itemSpinner.add(items.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        itemSpinner.add("Please Select Item 2nd");
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


        items.setAdapter(itemAdapter);
        LegacyTableView.insertLegacyTitle("Date","Name","Student ID","Item","Quantity","Purpose","Pillar");

        items.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(
                        AdapterView<?> parent, View view, final int position, long id) {
                    //Toast.makeText(CheckStock.this,"Selected Item: " + items.getItemAtPosition(position)
                    //+ " id=" + id,Toast.LENGTH_LONG).show();
                    if (position > 0) {
                        transactionHistory.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    String[] value = new String[7];
                                    HashMap<String, Object> hmap = (HashMap) d.getValue();
                                    //System.out.println(d.getKey().substring(4,6));
                                    //System.out.println(d.getKey().substring(4,7));
                                    //System.out.println(dateSelected);
                                    if (hmap.get("item").toString().equals(items.getItemAtPosition(position))
                                            && d.getKey().substring(4, 7).equals(date.getItemAtPosition(dateSelected))) {
                                        value[0] = d.getKey().substring(4, 10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                        System.out.println("ABCCCCCCCCCC");
                                    } else if (items.getItemAtPosition(position).equals("All")
                                            && d.getKey().substring(4, 7).equals(date.getItemAtPosition(dateSelected))) {
                                        value[0] = d.getKey().substring(4, 10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                    } else if (items.getItemAtPosition(position).equals("All")
                                            && date.getItemAtPosition(dateSelected).equals("All")) {
                                        value[0] = d.getKey().substring(4, 10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                    }

                                    if (hmap.get("item").toString().equals(items.getItemAtPosition(position))
                                            && date.getItemAtPosition(dateSelected).equals("All")) {
                                        value[0] = d.getKey().substring(4, 10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                        System.out.println("ABCCCCCCCCCC");
                                    }


                                    if (value[0] != null) {
                                        LegacyTableView.insertLegacyContent(value);
                                    }

                                };
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("CANCELLED");
                            }
                        });
                    }
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        Button search = findViewById(R.id.search);

        final Intent intentFROM = new Intent(CheckStock.this, CheckStock.class);

        intentFROM.putExtra(CheckStock.LGcontent, LegacyTableView.readLegacyContent());
        for (String i : LegacyTableView.readLegacyContent()){
            System.out.println(i);
        }
        intentFROM.putExtra(CheckStock.LGtitle, LegacyTableView.readLegacyTitle());
        for (String i : LegacyTableView.readLegacyTitle()){
            System.out.println(i);
        };



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                overridePendingTransition(0,0);
                startActivity(intentFROM);
                overridePendingTransition(0,0);

            }
        });
    }
}
