package com.example.grp2_foodbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PreChat extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;
    List<Integer> participantIds = new ArrayList<>();
    LatLng pickUpPoint;
    List<TextView> usernames = new ArrayList<>();
    List<TextView> ratings = new ArrayList<>();
    TextView bottom;
    private Button joinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prechat);


        usernames.add(findViewById(R.id.textView));
        usernames.add(findViewById(R.id.textView2));
        usernames.add(findViewById(R.id.textView3));
        ratings.add(findViewById(R.id.textView5));
        ratings.add(findViewById(R.id.textView6));
        ratings.add(findViewById(R.id.textView7));
        bottom = (TextView) findViewById(R.id.textView4);
        joinButton = (Button) findViewById(R.id.joinButton);

        joinButton.setOnClickListener(v -> openChatScreen());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Groups").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        participantIds = (List<Integer>) document.get("participantID");
                        for (int i = 0; i < participantIds.size(); i++) {
                            DocumentReference docRef = db.collection("Users").document(String.valueOf(participantIds.get(i)));
                            int finalI = i;
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists() && finalI < usernames.size()) {
                                            Log.d("Atiab", "DocumentSnapshot data: " + document.getData());
                                            Log.d("finalI", "" + finalI);
                                            usernames.get(finalI).setText("" + document.getString("username"));
                                            ratings.get(finalI).setText("" + document.getDouble("rating"));
                                        } else {
                                            Log.d("Atiab", "No such document");
                                        }
                                    } else {
                                        Log.d("Atiab", "get failed with ", task.getException());
                                    }
                                }
                            });
                        }
                        // Log.d("Atiab",  participantIds.toString());
                        pickUpPoint = new LatLng(
                                document.getGeoPoint("pickUpPoint").getLatitude(),
                                document.getGeoPoint("pickUpPoint").getLongitude()
                        );
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

    }

    private void openChatScreen() {
        Intent intent = new Intent(PreChat.this, ChatScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Pickup Point");
        if (pickUpPoint == null) {
            pickUpPoint = new LatLng(22.2829989,114.1370848);
        }
        markerOptions.position(pickUpPoint);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pickUpPoint, 15);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}