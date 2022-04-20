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
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_FINE_LOCCATION = 10;
    private Location currentLocation;
    private String currentAddress = String.valueOf(R.string.mainActivityFailToRetrieveAddress);
    boolean requestingLocationUpdates; //idk boolean for the switches start/stop?

    //elements from GUI
    TextView tv_latitude, tv_longitude, tv_address;
    Switch sw_updates, sw_accuracy;
    Button btn_viewNotes, btn_viewMap, btn_addNote;

    //Google's API client
    private FusedLocationProviderClient fusedLocationClient;

    //location request
    LocationRequest locationRequest;

    //location callback
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);
        tv_address = findViewById(R.id.tv_address);
        sw_updates = findViewById(R.id.sw_updates);
        sw_accuracy = findViewById(R.id.sw_accuracy);


        locationRequest = LocationRequest.create()
                .setInterval(15000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // locationRequest = new LocationRequest(); DEPRECATED

        //locationCallback triggered by intervals(?)
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateValues(locationResult.getLastLocation()); //updates the values without updateGPS doing same thing(?)
            }
        };


        btn_viewNotes = findViewById(R.id.btn_notes);
        btn_viewMap = findViewById(R.id.btn_map);
        btn_addNote = findViewById(R.id.btn_addNote);

        //button listeners

        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewNotesActivity.class);
                startActivity(intent);
            }
        });

        btn_viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);

                //pass current latitude and longitude to map activity
                intent.putExtra("CURRENT_LATITUDE", MainActivity.this.currentLocation.getLatitude());
                intent.putExtra("CURRENT_LONGITUDE", MainActivity.this.currentLocation.getLongitude());

                startActivity(intent);
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

        //listener of Updates switch

        sw_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_updates.isChecked()){
                //    startLocationUpdates();
                //    requestingLocationUpdates = true;
                }
                //start updating in intervals
            }
        });

        //listener of Accuracy switch
        sw_accuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_accuracy.isChecked()){
                //    stopLocationUpdates();
                }
                //get more accurate location <-> stay with less accurate
            }
        });


        updateGPS();

    }

    //FUNNY
    //https://developer.android.com/training/location/request-updates
    //docs^

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    //if you remove the checkSelf... and try to use requestLocationUpdates it might cause and Exception if no permission
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()); //Looper can't be null :D
        updateGPS();
    }

    //END OF FUNNY

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_FINE_LOCCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS();
            } else {
                Toast.makeText(MainActivity.this, R.string.toastPermissionNeeded, Toast.LENGTH_SHORT).show();
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
            this.currentAddress = address.get(0).getAddressLine(0);
            tv_address.setText(address.get(0).getAddressLine(0));

            //update currentAddress


        } catch (Exception e) {
            tv_address.setText(R.string.mainActivityFailToRetrieveAddress);
            Toast.makeText(MainActivity.this, R.string.mainActivityFailToRetrieveAddress, Toast.LENGTH_SHORT).show();
        }

    }
}