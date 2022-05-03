package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class ViewNotesActivity extends AppCompatActivity {

    ListView lv_Notes;
    Button btn_viewNotesRefresh;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        btn_viewNotesRefresh = findViewById(R.id.btn_viewNotesRefresh);

        dbHelper = new DbHelper(this);

        //fill list of notes and get string table with notes
        String[] values = fillNoteListView();

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

        //refresh button listener
        btn_viewNotesRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillNoteListView();
            }
        });
    }

    //fill or update list of notes, return string table with notes
    private String[] fillNoteListView(){
        //get list of notes from the database
        List<Note> noteList = dbHelper.getAllNotes();

        //create String table with notes
        String[] values = new String[noteList.size()];
        int i = 0;
        for(Note note : noteList){
            values[i] = note.toString();
            i += 1;
        }

        //fill listView with values from values array
        lv_Notes = findViewById(R.id.lv_Notes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                values);
        lv_Notes.setAdapter(adapter);

        return values;
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
}