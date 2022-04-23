package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class EditDeleteNoteActivity extends AppCompatActivity {

    private int noteId;
    private DbHelper dbHelper;
    private Note note;

    TextView tv_editNoteAddress;
    Button btn_deleteNote, btn_editNote;
    TextInputEditText tn_EditDeleteNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_note);
        dbHelper = new DbHelper(this);

        tv_editNoteAddress = findViewById(R.id.tv_editNoteAddress);
        btn_editNote = findViewById(R.id.btn_editNote);
        btn_deleteNote = findViewById(R.id.btn_deleteNote);
        tn_EditDeleteNoteContent = findViewById(R.id.tn_EditDeleteNoteContent);

        //get note id from ViewNotesActivity
        Bundle extras = getIntent().getExtras();
        noteId = (int) extras.get("NOTE_ID");

        //get note from the database
        note = dbHelper.getNoteById(noteId);

        //set textViews
        tv_editNoteAddress.setText(note.getAddress());
        tn_EditDeleteNoteContent.setText(note.getNoteContent());

        //delete button listener
        btn_deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.deleteNoteById(noteId)) {
                    Toast.makeText(EditDeleteNoteActivity.this, R.string.toastNoteDeleted, Toast.LENGTH_SHORT).show();

                    //open view notes activity
                    Intent intent = new Intent(EditDeleteNoteActivity.this, ViewNotesActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditDeleteNoteActivity.this, R.string.toastErrorWhileDeleting, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //edit button listener
        btn_editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if content isn't blank
                if(tn_EditDeleteNoteContent.getText().length() == 0) {
                    Toast.makeText(EditDeleteNoteActivity.this, R.string.toastEnterNoteContent, Toast.LENGTH_SHORT).show();
                }
                //check if any changes have been made
                else if(String.valueOf(tn_EditDeleteNoteContent.getText()).equals(note.getNoteContent())){
                    Toast.makeText(EditDeleteNoteActivity.this, R.string.toastNoChangesHaveBeenMade, Toast.LENGTH_SHORT).show();

                } else{
                    note.setNoteContent(String.valueOf(tn_EditDeleteNoteContent.getText()));

                    //try to update
                    if(dbHelper.editNote(note)){
                        Toast.makeText(EditDeleteNoteActivity.this, R.string.toastChangedContentSaved, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditDeleteNoteActivity.this, ViewNotesActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(EditDeleteNoteActivity.this, R.string.toastErrorWhileSavingChangedContent, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}