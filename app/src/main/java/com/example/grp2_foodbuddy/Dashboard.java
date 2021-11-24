package com.example.grp2_foodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_CODE = 0;
    private ListView container;
    private FirebaseFirestore database;
    private ArrayList<Restaurant> restaurantList;
    private LocationManager locationManager;
    private GeoPoint geoPoint;
    private FloatingActionButton fab;
    private Slider slider;
    private Intent intent;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        database = FirebaseFirestore.getInstance();
        container = (ListView) findViewById(R.id.ongoing_orders_restaurant_container);
        restaurantList = new ArrayList<>();
        slider = (Slider) findViewById(R.id.distance_slider);
        fab = (FloatingActionButton) findViewById(R.id.add_group_FAB);
        fab.setOnClickListener(v -> sendUserToAddGroups());
        intent = getIntent();
        userName = intent.getStringExtra("username");
        System.out.println(userName);


        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        updateUserLocation();
        fetchData();
        initSearchBar();
        initSlider();


//        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(Dashboard.this, "clicked", Toast.LENGTH_SHORT).show();
//                // intent -> screen
//                Intent intent = new Intent(Dashboard.this, PreChat.class);
//                startActivity(intent);
//            }
//        });

    }

    //Method to fetch restaurant data along with groups
    private void fetchData() {
        CollectionReference collection = database.collection("Restaurants");
        collection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setImageID(doc.getId());
                    restaurant.setName((String) doc.get("name"));
                    restaurant.setGeopoint((GeoPoint) doc.get("loc"));

                    List<Long> ongoingOrders = (List<Long>) doc.get("ongoing");
                    ArrayList<Groups> restaurantGroup = new ArrayList<Groups>();
                    if (ongoingOrders.size() > 0) {
                        for (Long orderID : ongoingOrders) {
                            CollectionReference groupCollection = database.collection("Groups");
                            DocumentReference groupDoc = groupCollection.document(String.valueOf(orderID));
                            groupDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        Groups group = documentSnapshot.toObject(Groups.class);
                                        List<Long> participants = (List<Long>) documentSnapshot.get("participantID");
                                        group.setNumParticipants(participants.size());
                                        group.setPickup_location((GeoPoint) documentSnapshot.get("pickUpPoint"));
                                        group.setThreshold_rating((Double) documentSnapshot.get("thresholdRating"));
                                        restaurantGroup.add(group);
                                    } else {
                                        System.out.println(String.valueOf(orderID) + " Document does not exist");
                                    }

                                    restaurant.setOngoing(restaurantGroup);
                                    restaurantList.add(restaurant);
                                }

                            }).addOnFailureListener(e -> System.out.println("Query failed")
                            ).addOnCompleteListener(task1 -> {
                                createRestaurantAdapter(restaurantList);
                            });
                        }
                    }

                }

            } else {
                System.out.println(task.getException());
            }
        });

    }

    private void createRestaurantAdapter(ArrayList<Restaurant> restaurantList) {
        RestaurantList adapter = new RestaurantList(Dashboard.this, restaurantList, geoPoint);
        container.setAdapter(adapter);

    }

    private void updateUserLocation() {
        if (ContextCompat.checkSelfPermission(Dashboard.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            requestLocationPermission();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            {
                Location curLocation = location;
                // check if locations has accuracy data
                if (curLocation.hasAccuracy()) {
                    // Accuracy is in rage of 20 meters, stop listening we have a fix
                    if (curLocation.getAccuracy() < 20) {
                        stopGpsListner();
                    }
                }
            }
            geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        }
    };


    private void stopGpsListner() {
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Location services are needed for basic functionality of FoodBuddy")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();


        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateUserLocation();
            }
        }
    }

    private void initSearchBar() {
        SearchView searchView = (SearchView) findViewById(R.id.restaurant_searchBar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();

                for (Restaurant restaurant : restaurantList) {

                    if (restaurant.getName().toLowerCase().contains(newText)) {
                        filteredRestaurants.add(restaurant);
                    }
                }

                createRestaurantAdapter(filteredRestaurants);


                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void sendUserToAddGroups() {
        //fill code here to create intent and send user to add groups page
        Intent intent = new Intent(Dashboard.this, NewGroup.class);
        startActivity(intent);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private void initSlider() {
        slider.setValue(10);
        slider.addOnChangeListener((slider, value, fromUser) -> {

            ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();

            for (Restaurant restaurant : restaurantList) {
                ArrayList<Groups> filteredGroups = new ArrayList<>();

                for (Groups group : restaurant.getOngoing()) {
                    if (group.getDistance_to_pickup() <= value) {
                        filteredGroups.add(group);
                    }
                }
                if (filteredGroups.size() > 0) {
                    restaurant.setOngoing(filteredGroups);
                    filteredRestaurants.add(restaurant);
                }
            }

            createRestaurantAdapter(filteredRestaurants);

        });
    }

    public double getLatitude() {
        return geoPoint.getLatitude();
    }

    public double getLongitude() {
        return geoPoint.getLongitude();
    }


}