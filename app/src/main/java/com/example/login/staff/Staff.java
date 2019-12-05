package com.example.login.staff;

import android.content.Intent;
import android.os.Bundle;

import com.example.login.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Staff extends AppCompatActivity {
    TextView Header;
    Button CheckStock;
    Button Chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //IDS
        Header = findViewById(R.id.header);
        CheckStock = findViewById(R.id.CheckStockButton);
        Chart = findViewById(R.id.ChartButton);

        //setOnClickListener
        CheckStock.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent toCheckStock = new Intent(Staff.this, CheckStock.class);
                startActivity(toCheckStock);
            }

        });

        Chart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toRestock = new Intent(Staff.this, GenerateChart.class);
                startActivity(toRestock);
            }
        });
    }

}
