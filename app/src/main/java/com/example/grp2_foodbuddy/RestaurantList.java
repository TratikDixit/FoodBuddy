package com.example.grp2_foodbuddy;

import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class RestaurantList extends ArrayAdapter<Restaurant> {

    private Activity context;
    private List<Restaurant> restaurantList;
    private Dashboard dashboard;
    private GeoPoint userGeoPoint;

    public RestaurantList(Activity context, List<Restaurant> restaurantList, GeoPoint userGeoPoint){
        super(context, R.layout.restaurant_card, restaurantList);
        this.context = context;
        this.restaurantList = restaurantList;
        this.userGeoPoint = userGeoPoint;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        dashboard = new Dashboard();
        LayoutInflater inflater = context.getLayoutInflater();

        View restaurantItem = inflater.inflate(R.layout.restaurant_card, null);

        TextView name = (TextView) restaurantItem.findViewById(R.id.restaurant_name);
        TextView time_remaining = (TextView) restaurantItem.findViewById(R.id.time_remaining);
        TextView users_in_group = (TextView) restaurantItem.findViewById(R.id.users_in_group);
        TextView restaurant_distance = (TextView) restaurantItem.findViewById(R.id.restaurant_distance);
        TextView rating = (TextView) restaurantItem.findViewById(R.id.restaurant_trust);
        ImageView img = (ImageView) restaurantItem.findViewById(R.id.restaurant_logo);

        Restaurant restaurant = restaurantList.get(position);

        List<Groups> groups = restaurant.getOngoing();

        System.out.println(restaurant.getName());
        for(Groups group: groups){
            name.setText(restaurant.getName());
            rating.setText(String.valueOf(group.getThreshold_rating()));
            users_in_group.setText("Users: " + String.valueOf(group.getNumParticipants()) + "/" + String.valueOf(group.getMaxParticipants()));
            time_remaining.setText(String.valueOf(group.getDuration()) + " minutes left");
            int drawableResourceId = this.context.getResources().getIdentifier("r" + String.valueOf(restaurant.getImageID()) +"_logo", "drawable", this.context.getPackageName());
            img.setImageResource(drawableResourceId);
            group.setDistance_to_pickup(this.getDistanceToPickup(group.getPickup_location()));
            restaurant_distance.setText(String.valueOf(group.getDistance_to_pickup()) + " km away");
        }

        return restaurantItem;
    }

    public double getDistanceToPickup(GeoPoint geo){
        Location loc1 = new Location("");
        loc1.setLatitude(geo.getLatitude());
        loc1.setLongitude(geo.getLongitude());
        System.out.println(loc1.getLatitude());
        System.out.println(loc1.getLongitude());


        Location loc2 = new Location("");
        loc2.setLatitude(userGeoPoint.getLatitude());
        loc2.setLongitude(userGeoPoint.getLongitude());

        float distanceInKm = (loc1.distanceTo(loc2)) / 1000;

        double dist = (double)Math.round(distanceInKm * 100d) / 100d;


        return dist;

    }
}
