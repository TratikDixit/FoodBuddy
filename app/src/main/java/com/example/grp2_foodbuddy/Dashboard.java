package com.example.grp2_foodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class Dashboard extends AppCompatActivity {

    private ListView container;
    private FirebaseFirestore database;
    private ArrayList restaurantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        database = FirebaseFirestore.getInstance();
        container = (ListView) findViewById(R.id.restaurant_container);
        restaurantList = new ArrayList<>();
        fetchData();



    }
    //Method to fetch restaurant data along with groups
    private void fetchData() {
        CollectionReference collection = database.collection("Restaurants");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Restaurant restaurant = new Restaurant();
                        restaurant.setImageID(doc.getId());
                        restaurant.setName((String) doc.get("name"));
                        restaurant.setGeopoint((GeoPoint) doc.get("loc"));


                        List<Long> ongoingOrders = (List<Long>) doc.get("ongoing");
                        ArrayList<Groups> restaurantGroup = new ArrayList<Groups>();
                        if(ongoingOrders.size() > 0) {
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

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("Query failed");
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        System.out.println(restaurantList);
                                        RestaurantList adapter = new RestaurantList(Dashboard.this, restaurantList);
                                        container.setAdapter(adapter);
                                    }
                                });

                            }
                        }

                    }



                }
                else{
                    System.out.println(task.getException());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}