package com.example.grp2_foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Form extends AppCompatActivity {

    ImageView image;
    private Button create_group_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        create_group_button = (Button) findViewById(R.id.create_group_button);

        Log.d("Atiab ", "" + id);

        image = findViewById(R.id.imageView2);

        image.setImageResource(id);

        create_group_button.setOnClickListener(view -> openEmptyChat());
    }

    private void openEmptyChat(){
        Intent intent = new Intent(Form.this, EmptyChatScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}