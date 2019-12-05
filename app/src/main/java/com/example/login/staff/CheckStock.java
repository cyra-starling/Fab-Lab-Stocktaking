package com.example.login.staff;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
    // TODO: Handle refresh case
    // TODO: Account for hierachy of items

    //initialize firebase
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference totalStockRef = database.getReference("totalStock");
    public static DatabaseReference transactionHistoryRef = database.getReference("transactionHistory");
    public static int dateSelected;
    private static ArrayList valueArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stock);

        final Spinner items = findViewById(R.id.items);
        final Spinner date = findViewById(R.id.date);
        final List<String> dateSpinner = new ArrayList<>();
        final List<String> itemSpinner = new ArrayList<>();
        final List<String> specificSpinner = new ArrayList<>();


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

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dateSpinner);
        date.setAdapter(dateAdapter);

        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateSelected = position;
                Toast.makeText(CheckStock.this, "Date Selected: " + date.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //items dropdownlist
        totalStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemSpinner);
        items.setAdapter(itemAdapter);
        LegacyTableView.insertLegacyTitle("Date","Name","Student ID","Item","Quantity","Purpose","Pillar");

        items.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, final int position, long id) {
                        //Toast.makeText(CheckStock.this,"Selected Item: " + items.getItemAtPosition(position)
                                //+ " id=" + id,Toast.LENGTH_LONG).show();

                        transactionHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot d: dataSnapshot.getChildren()){
                                    String[] value = new String[7];
                                    HashMap<String,Object> hmap = (HashMap) d.getValue();
                                    if (hmap.get("item").toString().equals(items.getItemAtPosition(position))
                                    && d.getKey().substring(4,7).equals(date.getItemAtPosition(dateSelected))){
                                        value[0] = d.getKey().substring(4,10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                    }
                                    else if(items.getItemAtPosition(position).equals("All")
                                            && d.getKey().substring(4,7).equals(date.getItemAtPosition(dateSelected))){
                                        value[0] = d.getKey().substring(4,10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                    }
                                    else if (hmap.get("item").toString().equals(items.getItemAtPosition(position))
                                            && date.getItemAtPosition(dateSelected).equals("All")){
                                        value[0] = d.getKey().substring(4,10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                    }
                                    else if(items.getItemAtPosition(position).equals("All")
                                            && date.getItemAtPosition(dateSelected).equals("All")){
                                        value[0] = d.getKey().substring(4,10);
                                        value[1] = hmap.get("name").toString();
                                        value[2] = hmap.get("studentId").toString();
                                        value[3] = hmap.get("item").toString();
                                        value[4] = hmap.get("quantity").toString();
                                        value[5] = hmap.get("purpose").toString();
                                        value[6] = hmap.get("pillar").toString();
                                    }

                                    // if array has value
                                    if (value[0]!=null){
                                         LegacyTableView.insertLegacyContent(value);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("CANCELLED");
                            }
                        });
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        Button search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                overridePendingTransition(0,0);
//                startActivity(getIntent());
//                overridePendingTransition(0,0);

//                LegacyTableView.insertLegacyContent(valueArr.toString());

                LegacyTableView legacyTableView = (LegacyTableView)findViewById(R.id.legacy_table_view);

                //legacyTableView.destroy();

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
        });
    }
}