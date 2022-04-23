package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        //open edit/delete activity after clicking on specific note
        lv_Notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(view.getContext(), EditDeleteNoteActivity.class);

                //pass note id
                intent.putExtra("NOTE_ID", getNoteId(values[position]));

                startActivity(intent);
            }
        });
    }

    //separate and convert note id from note's toString
    private int getNoteId(String noteString){
        String id = "";

        int i = 0;
        while (noteString.charAt(i) != '.'){
            id += noteString.charAt(i);
            i++;
        }

        return Integer.parseInt(id);
    }

    //override back button so that we always go back to the main window after pressing back
    @Override
    public void onBackPressed () {
        //open main activity
        Intent intent = new Intent(ViewNotesActivity.this, MainActivity.class);
        startActivity(intent);
    }
}