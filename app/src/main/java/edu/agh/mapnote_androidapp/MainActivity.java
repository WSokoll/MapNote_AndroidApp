package edu.agh.mapnote_androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_FINE_LOCCATION = 10;
    private Location currentLocation;
    private String currentAddress = "Nie udało się pobrać adresu";

    //elements from GUI
    TextView tv_latitude, tv_longitude, tv_address, tv_updates, tv_accuracy;
    Switch sw_updates, sw_accuracy;
    Button btn_viewNotes, btn_viewMap, btn_addNote;

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

        btn_viewNotes = findViewById(R.id.btn_notes);
        btn_viewMap = findViewById(R.id.btn_map);
        btn_addNote = findViewById(R.id.btn_addNote);

        //button listeners

        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "view notes pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ViewNotesActivity.class);
                startActivity(intent);
            }
        });

        btn_viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "view map pressed", Toast.LENGTH_SHORT).show();
            }
        });

        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);

                //pass current address and current location to the new activity
                intent.putExtra("CURRENT_ADDRESS", MainActivity.this.currentAddress);
                intent.putExtra("CURRENT_LOCATION", MainActivity.this.currentLocation);

                startActivity(intent);
            }
        });
        //(trzeba będzie jeszcze dodać do listenera przełącznika)
        //w onCreate też musi zostać
        updateGPS();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_FINE_LOCCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS();
            } else {
                Toast.makeText(MainActivity.this, "Aplikacja potrzebuje uprawnień do poprawnego działania.", Toast.LENGTH_SHORT).show();
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
                    updateValues(location);
                    MainActivity.this.currentLocation = location;
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

    //update location text views and currentAddress
    public void updateValues(Location location){

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        //GeoCoder for the address
        Geocoder geocoder = new Geocoder(MainActivity.this);

        //update text views
        tv_latitude.setText(String.valueOf(lat));
        tv_longitude.setText(String.valueOf(lon));

        try {
            List<Address> address = geocoder.getFromLocation(lat, lon, 1);
            tv_address.setText(address.get(0).getAddressLine(0));

            //update currentAddress
            this.currentAddress = address.get(0).getAddressLine(0);

        } catch (Exception e) {
            tv_address.setText("Nie udało się pobrać adresu");
            Toast.makeText(MainActivity.this, "Nie udało się pobrać adresu.", Toast.LENGTH_SHORT).show();
        }

    }
}