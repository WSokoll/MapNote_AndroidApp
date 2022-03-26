package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //get current location from MainActivity
        Bundle extras = getIntent().getExtras();
        currentLocation = (Location) extras.get("CURRENT_LOCATION");

        //just for test
        Toast.makeText(AddNoteActivity.this, String.valueOf(currentLocation.getLatitude()), Toast.LENGTH_SHORT).show();
    }
}