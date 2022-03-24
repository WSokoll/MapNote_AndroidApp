package edu.agh.mapnote_androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import javax.xml.transform.Source;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_FINE_LOCCATION = 10;
    //elements from GUI
    TextView tv_latitude, tv_longitude, tv_address, tv_updates, tv_accuracy;
    Switch sw_updates, sw_accuracy;

    //Google's API client
    private FusedLocationProviderClient fusedLocationClient;

    //location request
    private LocationRequest locationRequest;

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

        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //testowo
        updateGPS();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_FINE_LOCCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS();
            } else {
                //jakaś wiadomość, że potrzeba pozwolenia...
                System.out.println("brak pozwolenia");
                finish();
            }
        }
    }

    public void updateGPS(){

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        //get permission if it's not already granted
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //permission granted
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //tutaj wywołanie metody co będzie odświerzać pola
                    tv_latitude.setText(String.valueOf(location.getLatitude()));
                }
            });
        }else{
            //permission not granted yet

            //request permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCCATION);
            }
        }

    }
}