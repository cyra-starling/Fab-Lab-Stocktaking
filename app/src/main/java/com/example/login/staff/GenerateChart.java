package com.example.login.staff;

import com.example.login.R;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GenerateChart extends AppCompatActivity {

    public static final FirebaseDatabase fablabStock = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference transactionHistoryRef = fablabStock.getReference("transactionHistory");

//    public static final DatabaseReference totalStock = database.getReference("totalStock");
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView1;
    private TextView dateView2;
    private int year, month, day;

    private static <K extends Comparable, V extends Comparable> Map<K, V> getSortedMapByValues(final Map<K, V> map){

        Map<K, V> mapSortedByValues = new LinkedHashMap<K, V>();

        //get all the entries from the original map and put it in a List
        List<Entry<K, V>> list = new ArrayList<Entry<K, V>>(map.entrySet());

        //sort the entries based on the value by custom Comparator
        Collections.sort(list, new Comparator<Entry<K, V>>(){
            public int compare(Entry<K, V> entry1, Entry<K, V> entry2) {
                return -(entry1.getValue().compareTo( entry2.getValue() ));
            }
        });

        //put all sorted entries in LinkedHashMap
        for( Entry<K, V> entry : list  )
            mapSortedByValues.put(entry.getKey(), entry.getValue());

        //return Map sorted by values
        return mapSortedByValues;
    }


    public void generateChart(List<DataEntry> data) {
        final AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        final Cartesian barChart = AnyChart.bar();
        Bar column = barChart.bar(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        barChart.barGroupsPadding(0);
        barChart.animation(false);
        barChart.title("Amount of Item Taken ");
        barChart.yScale().minimum(0d);
        barChart.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        barChart.tooltip().positionMode(TooltipPositionMode.POINT);
        barChart.interactivity().hoverMode(HoverMode.BY_X);
        barChart.xAxis(0).title("Items");
        barChart.yAxis(0).title("Number");
        anyChartView.setChart(barChart);
    }

    public void generateChartPrep(String dateFrom, String dateTo){
        System.out.println(dateFrom);
        System.out.println(dateTo);
//        dd1 = dateFrom.substring(0, 7);
//        mm1 = dateFrom.substring()

        final List<DataEntry> data = new ArrayList<>();


        final ProgressBar spinner;
        spinner = findViewById(R.id.progressBar3);
        spinner.setVisibility(View.VISIBLE);

        fablabStock.getReference("transactionHistory").orderByChild("YYMMDD").startAt(dateFrom).endAt(dateTo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap itemList = new HashMap<String, Integer>();
                for (DataSnapshot items : dataSnapshot.getChildren()) {
                    HashMap<String,Object> hmap = (HashMap) items.getValue();
                    String currentItem = hmap.get("item").toString();
                    int itemQuantity = (int)(long) Integer.parseInt((String) hmap.get("quantity"));

                    if(itemList.containsKey(currentItem)){
                        itemList.put(currentItem, (int) itemList.get(currentItem) + itemQuantity);
                    }
                    else {
                        itemList.put(currentItem, itemQuantity);
                    }
                }
                // loop through newly created Hash Map
                Iterator<Entry<String, Integer>> it = getSortedMapByValues(itemList).entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Integer> pair = (Entry<String, Integer>) it.next();
                    data.add(new ValueDataEntry(pair.getKey(), (int)(long)pair.getValue()));
                }
                spinner.setVisibility(View.GONE);

                generateChart(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_chart);

        Button refresh = findViewById(R.id.refresh);

        dateView1 = (TextView) findViewById(R.id.dateSelected1);
        dateView2 = (TextView) findViewById(R.id.dateSelected2);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
//        showDate1(year, month+1, day);
//        showDate2(year, month+1, day);
        showDate1(2019, 01, 01);
        showDate2(2019, 12, 31);

        // generate chart on start
        generateChartPrep(dateView1.getText().toString(), dateView2.getText().toString());

        //generate chart upon refresh
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateChartPrep(dateView1.getText().toString(), dateView2.getText().toString());
            }
        });
    }


    @SuppressWarnings("deprecation")
    public void setDate1(View view) {
        showDialog(998);
    }

    @SuppressWarnings("deprecation")
    public void setDate2(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 998) {
            return new DatePickerDialog(this, myDateListener1, year, month, day);
        }
        else if (id == 999){
            return new DatePickerDialog(this, myDateListener2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // year, month, day
            showDate1(arg1, arg2+1, arg3);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // year, month, day
            showDate2(arg1, arg2+1, arg3);
        }
    };

    private void showDate1(int year, int month, int day) {
        dateView1.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showDate2(int year, int month, int day) {
        dateView2.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

}