package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddNoteActivity extends AppCompatActivity {

    private Note note;
    Button btn_addNoteInput;
    TextView tv_currentAddress;
    TextInputEditText tied_noteTextInput;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //get current location from MainActivity
        Bundle extras = getIntent().getExtras();
        currentLocation = (Location) extras.get("CURRENT_LOCATION");

        tv_currentAddress = findViewById(R.id.tv_currentAddress);
        //tv_currentAddress.setText((Integer) extras.get("CURRENT_ADDRESS"));

        tied_noteTextInput = findViewById(R.id.tied_noteTextInput);
        btn_addNoteInput = findViewById(R.id.btn_addNoteInput);

        btn_addNoteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddNoteActivity.this, tied_noteTextInput.getText(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //just for test
        Toast.makeText(AddNoteActivity.this, String.valueOf(currentLocation.getLatitude()), Toast.LENGTH_SHORT).show();
    }
}