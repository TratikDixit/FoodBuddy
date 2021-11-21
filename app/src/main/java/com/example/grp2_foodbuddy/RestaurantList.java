package com.example.grp2_foodbuddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RestaurantList extends ArrayAdapter<Restaurant> {

    private Activity context;
    private List<Restaurant> restaurantList;

    public RestaurantList(Activity context, List<Restaurant> restaurantList){
        super(context, R.layout.restaurant_card, restaurantList);
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View restaurantItem = inflater.inflate(R.layout.restaurant_card, null, true);

        TextView name = (TextView) restaurantItem.findViewById(R.id.restaurant_name);
        TextView time_remaining = (TextView) restaurantItem.findViewById(R.id.time_remaining);
        TextView users_in_group = (TextView) restaurantItem.findViewById(R.id.users_in_group);
        TextView restaurant_distance = (TextView) restaurantItem.findViewById(R.id.restaurant_distance);
        TextView rating = (TextView) restaurantItem.findViewById(R.id.restaurant_trust);
        ImageView img = (ImageView) restaurantItem.findViewById(R.id.restaurant_logo);

        Restaurant restaurant = restaurantList.get(position);

        List<Groups> groups = restaurant.getOngoing();

        for(Groups group: groups){
            name.setText(restaurant.getName());
            rating.setText(String.valueOf(group.getThreshold_rating()));
            users_in_group.setText(String.valueOf(group.getNumParticipants()) + "/" + String.valueOf(group.getMaxParticipants()));
            time_remaining.setText(String.valueOf(group.getDuration()));
            int drawableResourceId = this.context.getResources().getIdentifier("r" + String.valueOf(restaurant.getImageID()), "drawable", this.context.getPackageName());
            img.setImageResource(drawableResourceId);
        }

        return restaurantItem;
    }
}
