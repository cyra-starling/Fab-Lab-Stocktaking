package com.example.login.staff;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.login.R;
<<<<<<< Updated upstream
import androidx.appcompat.app.AppCompatActivity;
=======
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference fablabstockRef = database.getReference("fablabstock");

    public static final String CHANNEL_ID = "channel";
    private NotificationManagerCompat notificationManager;
=======
    //firebase references
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference threshold = database.getReference("Threshold");
    public static final DatabaseReference totalStockRef = database.getReference("totalStock");

    //notification initialization
    public static final String CHANNEL_ID = "channel";
    private NotificationManagerCompat notificationManager;
    private String notificationContent = "";
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

<<<<<<< Updated upstream
        notificationManager = NotificationManagerCompat.from(this);
=======
        //create notiication channel
        createNotificationchannel();
        notificationManager = NotificationManagerCompat.from(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
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
=======

        //NOTIFY STAFF IF ITEM FALLS BELOW THRESHOLD
        //It goes through 2 datasnapshot : Threshold and totalStock
        threshold.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final DataSnapshot thresholdd = dataSnapshot;


                totalStockRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            for (DataSnapshot id : item.getChildren()) {



                                // if quantity of item in totalstock is lower than in threshold (int)
                                if ( (int) (long) thresholdd.child(item.getKey().toString()).getValue()  > (int) (long) id.getValue()) {

                                    notificationContent += item.getKey() + " " + id.getKey() + " is low on stock: " + id.getValue() + "\n";


                                }

                                // if notificationConent is not Empty ( there are some items that are below threshold )
                                if (!notificationContent.isEmpty()) {
                                    System.out.println(notificationContent);

                                    String notificationTitle = "LOW STOCK";
                                    Notification notification = new NotificationCompat.Builder(Staff.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_warning_black_24dp)
                                            .setContentTitle(notificationTitle)
                                            .setContentText(notificationContent)
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText(notificationContent))
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .build();

                                    notificationManager.notify(1, notification);
                                }

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("CANCELLED");

                    }

                });




            }

>>>>>>> Stashed changes
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("CANCELLED");
            }
        });
<<<<<<< Updated upstream
=======
    }


    //method to create a channel for notification
    public void createNotificationchannel(){

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel", importance);
            channel.setDescription("this is notification");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

>>>>>>> Stashed changes
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

