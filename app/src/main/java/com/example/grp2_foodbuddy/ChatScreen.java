package com.example.grp2_foodbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.grp2_foodbuddy.databinding.CompleteConversationBinding;

import java.util.ArrayList;

public class ChatScreen extends AppCompatActivity {

    private ArrayList<String> items;

    private ListView listView;
    private FrameLayout sendButton;
    private AppCompatImageView homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_conversation);

        listView = findViewById((R.id.listView));
        sendButton = (FrameLayout) findViewById((R.id.layoutSend));
        homebutton = (AppCompatImageView) findViewById(R.id.imageInfo);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMessage(view);
            }
        });

        items = new ArrayList<>();
        MessageAdapter itemsAdapter = new MessageAdapter(this,items);
        listView.setAdapter(itemsAdapter);

        homebutton.setOnClickListener(v -> goHome());
    }

    private void addMessage(View view){
        EditText input = findViewById(R.id.inputMessage);
        String itemText = input.getText().toString();

        if (!(itemText.equals(""))){
            items.add(itemText);
            input.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"Please enter text..", Toast.LENGTH_LONG).show();
        }

    }

    private void goHome(){
        Intent intent = new Intent(ChatScreen.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username", "");
        startActivity(intent);
    }

}
