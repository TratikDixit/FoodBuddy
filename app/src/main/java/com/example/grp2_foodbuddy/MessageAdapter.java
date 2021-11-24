package com.example.grp2_foodbuddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> messages;

    public MessageAdapter(Activity context, ArrayList<String> messages){
        super(context,R.layout.item_container_sent_message,messages);
        this.context = context;
        this.messages = messages;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("EEEE HH:mm");
        LocalDateTime now = LocalDateTime.now();
        LayoutInflater inflater = context.getLayoutInflater();
        View sentMessageLayout = inflater.inflate(R.layout.item_container_sent_message,null);
        TextView sentMessage = (TextView) sentMessageLayout.findViewById(R.id.textMessage);
        TextView currentDate = (TextView) sentMessageLayout.findViewById(R.id.textDateTime);;
        String message = messages.get(position);
        sentMessage.setText(message);
        currentDate.setText(dateTimeFormat.format(now));
        return sentMessageLayout;
    }
}
