package com.example.login.staff;

import android.content.Intent;
import android.os.Bundle;

import com.example.login.R;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Staff extends AppCompatActivity {

    TextView Header;
    Button CheckStock;
    Button Chart;

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference fablabstockRef = database.getReference("fablabstock");

    public static final String CHANNEL_ID = "channel";
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        notificationManager = NotificationManagerCompat.from(this);

        //IDS
        //Header = findViewById(R.id.header);
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

        //Notification channel
        createNotificationchannel();

        fablabstockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String notificationContent = "";
                for (DataSnapshot item : dataSnapshot.child("totalStock").getChildren()) {
                    for (DataSnapshot id : item.getChildren()) {
                        // if quantity of item in totalstock is lower than in threshold
                        if ((int) dataSnapshot.child("threshold").child(item.getKey()).getValue() > (int) id.getValue()) {

                            notificationContent += item.getKey() + " " + id.getKey() + "is low on stock: " + id.getValue() + "\n";
                        }
                    }
                }
                if (!notificationContent.isEmpty()) {
                    String notificationTitle = "LOW STOCK";
                    Notification notification = new NotificationCompat.Builder(Staff.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_android_black_24dp)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationContent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .build();
                    notificationManager.notify(1, notification);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("CANCELLED");
            }
        });
    }

    public void createNotificationchannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel", importance);
            channel.setDescription("this is notification");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

