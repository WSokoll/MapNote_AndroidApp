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

    Button btn_addNoteInput;
    TextView tv_currentAddress;
    TextInputEditText tied_noteTextInput;

    private Location currentLocation;
    private String currentAddress;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dbHelper = new DbHelper(this);

        //get current address and current location from MainActivity
        Bundle extras = getIntent().getExtras();
        currentAddress = (String) extras.get("CURRENT_ADDRESS");
        currentLocation = (Location) extras.get("CURRENT_LOCATION");

        //update current address textView
        tv_currentAddress = findViewById(R.id.tv_currentAddress);
        tv_currentAddress.setText(currentAddress);

        tied_noteTextInput = findViewById(R.id.tied_noteTextInput);
        btn_addNoteInput = findViewById(R.id.btn_addNoteInput);

        btn_addNoteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tied_noteTextInput.getText().length() == 0){
                    Toast.makeText(AddNoteActivity.this, "Wprowadź treść notatki", Toast.LENGTH_SHORT).show();
                }else {

                    //create Note object and fill it with data
                    Note note = new Note();
                    note.setLatitude(AddNoteActivity.this.currentLocation.getLatitude());
                    note.setLongitude(AddNoteActivity.this.currentLocation.getLongitude());
                    note.setAddress(AddNoteActivity.this.currentAddress);
                    note.setNoteContent(String.valueOf(tied_noteTextInput.getText()));

                    //if the note is not empty -> try to insert it to the database
                    if (dbHelper.addNote(note)) {
                        //successful insert
                        Toast.makeText(AddNoteActivity.this, "Notatka została dodana pomyślnie", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        //insert unsuccessful
                        Toast.makeText(AddNoteActivity.this, "Podczas zapisywania wystąpił błąd", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}