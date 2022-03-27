package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ViewNotesActivity extends AppCompatActivity {

    ListView lv_Notes;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        dbHelper = new DbHelper(this);

        //get list of notes from the database
        List<Note> noteList = dbHelper.getAllNotes();

        //create String table with notes
        String[] values = new String[noteList.size()];
        int i = 0;
        for(Note note : noteList){
            values[i] = note.toString();
            i += 1;
        }

        lv_Notes = findViewById(R.id.lv_Notes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                values);
        lv_Notes.setAdapter(adapter);
    }
}