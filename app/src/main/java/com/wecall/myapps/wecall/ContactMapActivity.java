package com.wecall.myapps.wecall;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String contactName;
    private String contactAddress;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent dogNumber = getIntent();



        //  Test is the variable name 0 is the value if we dont get any sent value with us
        //contactNumber = dogNumber.getIntExtra("Test", 0);

        contactName = dogNumber.getStringExtra("Name");
        contactAddress = dogNumber.getStringExtra("Address");
        lat =dogNumber.getDoubleExtra("Lat",0);
        lng=dogNumber.getDoubleExtra("Lng",0);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng place = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(place).title(contactName+" lives here")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));

    }
}
