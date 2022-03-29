package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class EditDeleteNoteActivity extends AppCompatActivity {

    private int noteId;
    private DbHelper dbHelper;

    TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_note);
        dbHelper = new DbHelper(this);

        tv_content = findViewById(R.id.tv_content);

        //get note id from ViewNotesActivity
        Bundle extras = getIntent().getExtras();
        noteId = (int) extras.get("NOTE_ID");

        //tymczasowo. docelowo zrobie metode w dbHelper getNote(int id)
        List<Note> list = dbHelper.getAllNotes();
        for(Note note : list){
            if(note.getId() == noteId){
                tv_content.setText(note.getNoteContent());
            }
        }
    }
}