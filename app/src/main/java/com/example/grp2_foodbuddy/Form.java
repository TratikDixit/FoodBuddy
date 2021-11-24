package com.example.grp2_foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class Form extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        Log.d("Atiab ", "" + id);

        image = findViewById(R.id.imageView2);

        image.setImageResource(id);
    }
}