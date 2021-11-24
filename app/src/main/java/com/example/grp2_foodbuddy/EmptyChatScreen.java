package com.example.grp2_foodbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmptyChatScreen extends AppCompatActivity {
    private ArrayList<String> items;

    private ListView listView;
    private FrameLayout sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_chat);

        listView = findViewById((R.id.listView));
        sendButton = (FrameLayout) findViewById((R.id.layoutSend));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMessage(view);
            }
        });

        items = new ArrayList<>();
        MessageAdapter itemsAdapter = new MessageAdapter(this,items);
        listView.setAdapter(itemsAdapter);
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

}
