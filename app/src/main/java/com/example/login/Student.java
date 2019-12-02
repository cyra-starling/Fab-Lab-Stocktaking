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
//metakeitem thingyyyy. add transaction history, update current stock
public class Student extends AppCompatActivity {
    ListView CheckoutList, PopupList, MainCart;

    ArrayList<String> arrayList = new ArrayList<>(); //import from firebase
    ArrayList<Long> arrayList2 =  new ArrayList<>();
    ArrayList<String> cartList = new ArrayList<>();
    public static HashMap<String,Integer> hashmap = new HashMap<String,Integer>();


    public static final FirebaseDatabase fablabStock = FirebaseDatabase.getInstance("https://fablabstock.firebaseio.com/");
    public static final DatabaseReference totalStock = fablabStock.getReference("totalStock");

    Button PopupButton, CheckoutButton, ConfirmButton;
    Dialog popupDialog, checkoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        totalStock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<>();
                arrayList2 = new ArrayList<>();
                for(DataSnapshot item: dataSnapshot.getChildren()) {
                    arrayList.add(item.getKey());
                    arrayList2.add((Long)item.getValue());
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
                if (arrayList.size() == 0){
                    Toast.makeText(Student.this, "Fetching data. Please wait.", Toast.LENGTH_SHORT).show();
                }
                else{showPopup();}
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
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        PopupList.setAdapter(arrayAdapter2);
        PopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList2.set(position, arrayList2.get(position)-1);
                cartList.add(arrayList.get(position));
                //added
                if (hashmap.containsKey(arrayList.get(position))){
                    int count = hashmap.get(arrayList.get(position));
                    hashmap.remove(arrayList.get(position));
                    hashmap.put(arrayList.get(position),count + 1);
                }

                else if (!hashmap.containsKey(arrayList.get(position))){
                    hashmap.put(arrayList.get(position), 1);
                }


                //
                updateCart();
                Toast.makeText(Student.this, arrayList.get(position) +" added to cart(" + arrayList2.get(position) + " left)", Toast.LENGTH_SHORT);
                popupDialog.dismiss();

                /* not used toaster with predefined time
                final Toast t = Toast.makeText(MainActivity.this, arrayList.get(position) +" added to cart(" + arrayList2.get(position) + " left)", Toast.LENGTH_SHORT);
                    t.show();
                    Handler handler = new Handler(); // toast time setter
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            t.cancel();
                        }
                    }, 500);
                 */
            }
        });

        popupDialog.show();
    }


    public void updateCart(){
        TextView textView = findViewById(R.id.bottomBar);
        textView.setText("   "+ cartList.size()+ " items");
        MainCart = findViewById(R.id.mainCart);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cartList);
        MainCart.setAdapter(arrayAdapter3);

    }

    public void cfmCheckout(){
        cartList = new ArrayList<>();
        updateCart();
    }
}