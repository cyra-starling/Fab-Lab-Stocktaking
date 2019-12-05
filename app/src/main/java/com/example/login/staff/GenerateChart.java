package com.example.login.staff;

import com.example.login.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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


    public static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference transactionHistoryTestRef = database.getReference("transactionHistoryTest");
//    public static final DatabaseReference totalStock = database.getReference("totalStock");

    /*
    //method to get Number of Specified Item taken
    public int getNumberOfItemTaken(final String item){
        final List<Long> numbers = new ArrayList<>();
        transactionHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot transactions : dataSnapshot.getChildren()) {
                    String c = transactions.child("item").getValue().toString();
                    if (c.equals(item)) {
                        System.out.println(transactions.child("item").getValue().toString() +" "+ transactions.child("quantity").getValue());
                        numbers.add((Long) transactions.child("quantity").getValue());
                    }
                    System.out.println("???");
                    //System.out.println("...");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("CANCELLED");
            }
        });
        System.out.println("GETTIN NUMBERR");
        int result = 0;

        for (Long i : numbers){
            result+=i;
        };
        return result;
    }
    */

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_chart);

        final AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        Button buildChart = findViewById(R.id.BuildChart);

        final Cartesian barChart = AnyChart.bar();
        final List<DataEntry> data = new ArrayList<>();




//        totalStock.addListenerForSingleValueEvent(new ValueEventListener() {
        transactionHistoryTestRef.orderByChild("date").startAt(1911).endAt(1914).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap itemList = new HashMap<String, Integer>();

                for (DataSnapshot items : dataSnapshot.getChildren()) {
                    HashMap<String,Object> hmap = (HashMap) items.getValue();
                    String currentItem = hmap.get("item").toString();
                    int itemQuantity = (int)(long)hmap.get("quantity");

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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        buildChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}