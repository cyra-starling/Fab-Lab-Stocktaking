package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

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
            final TextView count = view.findViewById(R.id.count);
            Button add = view.findViewById(R.id.add);
            Button minus = view.findViewById(R.id.minus);

            text.setText(item);
            count.setText(hashmap.get(item).toString());

            //increment number
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(count.getText().toString());
                    number += 1;
                    count.setText(Integer.toString(number));
                    hashmap.remove(text.toString());
                    hashmap.put(text.toString(),number); //update hashmap
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
                        //show error message
                        Toast.makeText(ShoppingActivity.this, "Please add items into the cart",Toast.LENGTH_SHORT).show();
                    }
                    count.setText(Integer.toString(number));
                    hashmap.remove(text.toString());
                    hashmap.put(text.toString(),number); //update hashmap
                }
            });
            layout.addView(view);
        }

        //add the submit and back buttons
        View view = layoutInflater.inflate(R.layout.listview,null,false);

        //todo: submit button goes to ncf reader
        //update transaction history
        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingActivity.this,TransactionHistory.class)
                        .putExtra("history",hashmap);
                startActivity(intent);
            }
        });

        //back button
        Button backtoStart = view.findViewById(R.id.BacktoStart);
        backtoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        layout.addView(view);
    }
}
