package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class PurposeActivity extends AppCompatActivity {

    public static String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose);
        final EditText text = findViewById(R.id.editText);

        Intent intent = getIntent();
        final HashMap<String,Integer> hashmap = (HashMap<String, Integer>) intent.getSerializableExtra("history");
        Button purposeBtn = findViewById(R.id.purposeConfirm);
        purposeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = text.getText().toString();
                Intent intent = new Intent(PurposeActivity.this,TransactionHistory.class)
                        .putExtra("history",hashmap);
                startActivity(intent);
            }
        });
    }
}