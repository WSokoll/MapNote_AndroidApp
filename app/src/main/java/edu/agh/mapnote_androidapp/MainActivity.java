package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
//elements from GUI
    TextView tv_latitude, tv_longitude, tv_address, tv_updates, tv_accuracy;
    Switch sw_updates, sw_accuracy;
//client Google API
    private FusedLocationProviderClient fusedLocationClient;
//location request
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);
        tv_address = findViewById(R.id.tv_address);
        tv_updates = findViewById(R.id.tv_localisationStatus);
        tv_accuracy = findViewById(R.id.tv_accuracyStatus);
        sw_updates = findViewById(R.id.sw_updates);
        sw_accuracy = findViewById(R.id.sw_accuracy);
//zalążek clienta lokalizacji
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    }
}