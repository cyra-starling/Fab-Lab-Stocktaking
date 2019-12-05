package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

//metakeitem thingyyyy. add transaction history, update current stock
public class Student extends AppCompatActivity {
    ListView CheckoutList, PopupList, PopupList2, MainCart;

    ArrayList<String> categoryList = new ArrayList<>(); //import from firebase
    ArrayList<String> itemList =  new ArrayList<>();
    ArrayList<Long> stockList = new ArrayList<>();
    ArrayList<String> cartList = new ArrayList<>();
    public static HashMap<String,Integer> hashmap = new HashMap<String,Integer>();


    public static final FirebaseDatabase fablabStock = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference totalStock = fablabStock.getReference("totalStock");

    Button PopupButton, CheckoutButton, ConfirmButton;
    Dialog popupDialog, popupDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hashmap = new HashMap<>(); //reset hashmap
        totalStock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList = new ArrayList<>();
                for(DataSnapshot category: dataSnapshot.getChildren()){
                    categoryList.add(category.getKey());
                    final ProgressBar spinner;
                    spinner = findViewById(R.id.progressBar);
                    spinner.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        PopupButton = findViewById(R.id.PopupButton);
        PopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryList.size() == 0){
                    Toast.makeText(Student.this, "Fetching data. Please wait.", Toast.LENGTH_SHORT).show();
                }
                else{
                    showPopup();
                }
            }
        });

        CheckoutButton = findViewById(R.id.CheckoutButton);
        CheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student.this,ShoppingActivity.class)
                        .putExtra("count",hashmap);
                startActivity(intent);
            }
        });

        MainCart = findViewById(R.id.mainCart);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cartList);
        MainCart.setAdapter(arrayAdapter3);


    }
    public void showPopup(){
        popupDialog = new Dialog(Student.this);
        popupDialog.setContentView(R.layout.popup);
        PopupList = popupDialog.findViewById(R.id.popupList);
        ArrayAdapter arrayAdapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,categoryList);
        PopupList.setAdapter(arrayAdapter4);
        PopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String category = categoryList.get(position);
                final ProgressBar spinner;
                spinner = findViewById(R.id.progressBar);
                spinner.setVisibility(View.VISIBLE);
                totalStock.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemList = new ArrayList<>();
                        stockList = new ArrayList<>();
                        DataSnapshot item = dataSnapshot.child(category);
                        for (DataSnapshot thing: item.getChildren()) {
                            itemList.add(thing.getKey());
                            stockList.add((Long) thing.getValue());
                            final ProgressBar spinner;
                            spinner = findViewById(R.id.progressBar);
                            spinner.setVisibility(View.GONE);
                        }
                        showPopup2();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                popupDialog.dismiss();
            }
        });
        popupDialog.show();
    }


    public void showPopup2(){
        popupDialog2 = new Dialog(Student.this);
        popupDialog2.setContentView(R.layout.popup2);

        PopupList2 = popupDialog2.findViewById(R.id.popupList2);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,itemList);
        PopupList2.setAdapter(arrayAdapter2);
        PopupList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cartList.contains(itemList.get(position))){
                    Toast.makeText(Student.this, "Item already exists!",Toast.LENGTH_SHORT).show();
                }
                else {
                    stockList.set(position, stockList.get(position) - 1);
                    cartList.add(itemList.get(position));

                    if (hashmap.containsKey(itemList.get(position))) {
                        int count = hashmap.get(itemList.get(position));
                        hashmap.remove(itemList.get(position));
                        hashmap.put(itemList.get(position), count + 1);
                    } else if (!hashmap.containsKey(itemList.get(position))) {
                        hashmap.put(itemList.get(position), 1);
                    }
                    Toast.makeText(Student.this, itemList.get(position) +" added to cart", Toast.LENGTH_SHORT).show();

                }

                updateCart();
                popupDialog2.dismiss();

            }
        });
        popupDialog2.show();

    }


    public void updateCart(){
        TextView textView = findViewById(R.id.bottomBar);
        textView.setText("   "+ cartList.size()+ " items");
        MainCart = findViewById(R.id.mainCart);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cartList);
        MainCart.setAdapter(arrayAdapter3);

    }

}