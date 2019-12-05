package com.example.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import static com.example.login.Student.hashmap;

public class ShoppingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselayout);

        Intent intent = getIntent();
        final HashMap<String,Integer> hashmap = (HashMap<String,Integer>) intent.getSerializableExtra("count");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = findViewById(R.id.base_shopping_cart);

        for (String item: hashmap.keySet()){
            View view = layoutInflater.inflate(R.layout.activity_shopping,null,false);
            final TextView text = view.findViewById(R.id.itemname);
            final EditText count = view.findViewById(R.id.count);
            ImageButton add = view.findViewById(R.id.add);
            ImageButton minus = view.findViewById(R.id.minus);

            text.setText(item);
            count.setText(hashmap.get(item).toString());

            count.setOnKeyListener(new View.OnKeyListener(){
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                        int number = Integer.parseInt(count.getText().toString());
                        count.setText(Integer.toString(number));
                        hashmap.remove(text.getText().toString());
                        hashmap.put(text.getText().toString(),number); //update hashmap
                    }
                    return false;
                }
            });

            //increment number
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(count.getText().toString());
                    number += 1;
                    count.setText(Integer.toString(number));
                    hashmap.remove(text.getText().toString());
                    hashmap.put(text.getText().toString(),number); //update hashmap
                }
            });

            //decrement number
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(count.getText().toString());
                    if (number > 0) {
                        number -= 1;
                    }
                    else {
                        Toast.makeText(ShoppingActivity.this, "You cannot delete on this page. Choose cancel to restart.",Toast.LENGTH_SHORT).show();
                    }
                    count.setText(Integer.toString(number));
                    hashmap.remove(text.getText().toString());
                    hashmap.put(text.getText().toString(),number); //update hashmap
                }
            });
            layout.addView(view);
        }

        //add the submit and back buttons

        //update transaction history
        Button submit = findViewById(R.id.checkoutSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hashmap.containsValue(0)){
                    Toast.makeText(ShoppingActivity.this, "You cannot submit a 0 value", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(ShoppingActivity.this, PurposeActivity.class)
                            .putExtra("history", hashmap);
                    startActivity(intent);
                }
            }
        });

        //back button
        Button backtoStart = findViewById(R.id.checkoutCancel);
        backtoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
