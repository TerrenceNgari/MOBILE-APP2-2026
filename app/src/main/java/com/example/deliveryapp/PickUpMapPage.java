package com.example.deliveryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PickUpMapPage extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_up_map_page);

        // Retrieve restaurant ID from CartPage
        restaurantId = getIntent().getStringExtra("restaurantId");

        // Back button
        findViewById(R.id.btnMapBack).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        // Check restaurant ID
        if (restaurantId == null || restaurantId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Restaurant not selected",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Check Google Play Services
        if (isGooglePlayServicesAvailable()) {

            SupportMapFragment mapFragment =
                    (SupportMapFragment)
                            getSupportFragmentManager()
                                    .findFragmentById(R.id.map);

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    // Check whether Google Play Services is available
    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability availability =
                GoogleApiAvailability.getInstance();

        int result = availability
                .isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        }

        Toast.makeText(
                this,
                "Google Play Services is unavailable",
                Toast.LENGTH_LONG
        ).show();

        return false;
    }

    // Called when Google Maps is ready
    @Override
    public void onMapReady(@NonNull GoogleMap map) {

        googleMap = map;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.getUiSettings()
                .setZoomControlsEnabled(true);

        // Retrieve restaurant coordinates
        loadRestaurantLocation();
    }

    private void loadRestaurantLocation() {

        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("restaurants")
                        .child(restaurantId);

        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot
                    ) {

                        Double latitude = snapshot
                                .child("latitude")
                                .getValue(Double.class);

                        Double longitude = snapshot
                                .child("longitude")
                                .getValue(Double.class);

                        // Check whether coordinates exist
                        if (latitude == null || longitude == null) {

                            Toast.makeText(
                                    PickUpMapPage.this,
                                    "Restaurant location not available",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        // Create restaurant location
                        LatLng restaurantLocation =
                                new LatLng(latitude, longitude);

                        // Add marker
                        googleMap.addMarker(
                                new MarkerOptions()
                                        .position(restaurantLocation)
                                        .title("Pick Up Order")
                        );

                        // Move camera to restaurant
                        googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        restaurantLocation,
                                        16f
                                )
                        );
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error
                    ) {

                        Toast.makeText(
                                PickUpMapPage.this,
                                "Failed to load restaurant location",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}
