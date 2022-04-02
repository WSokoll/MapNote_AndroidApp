package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditDeleteNoteActivity extends AppCompatActivity {

    private int noteId;
    private DbHelper dbHelper;
    private Note note;

    TextView tv_editNoteContent, tv_editNoteAddress;
    Button btn_deleteNote, btn_editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_note);
        dbHelper = new DbHelper(this);

        tv_editNoteContent = findViewById(R.id.tv_editNoteContent);
        tv_editNoteAddress = findViewById(R.id.tv_editNoteAddress);
        btn_editNote = findViewById(R.id.btn_editNote);
        btn_deleteNote = findViewById(R.id.btn_deleteNote);

        //get note id from ViewNotesActivity
        Bundle extras = getIntent().getExtras();
        noteId = (int) extras.get("NOTE_ID");

        //get note from the database
        note = dbHelper.getNoteById(noteId);

        //set textViews
        tv_editNoteContent.setText(note.getNoteContent());
        tv_editNoteAddress.setText(note.getAddress());

        //button listeners
        btn_deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.deleteNoteById(noteId)) {
                    Toast.makeText(EditDeleteNoteActivity.this, "Notatka została usunięta", Toast.LENGTH_SHORT).show();

                    //Tu może coś wymyślić, żeby inaczej wrócić do tego widoku bo teraz wraca, ale jak sie kliknie
                    //strzałke w tył to wraca do okna usuwania i edytowania już usuniętej notatki
                    //open view notes activity
                    Intent intent = new Intent(EditDeleteNoteActivity.this, ViewNotesActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditDeleteNoteActivity.this, "Wystąpił błąd podczas usuwania notatki", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}