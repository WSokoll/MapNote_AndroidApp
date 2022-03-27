package edu.agh.mapnote_androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewNotesActivity extends AppCompatActivity {

    ListView lv_Notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        String[] values = new String[]{"abc", "123", "456"};

        lv_Notes = findViewById(R.id.lv_Notes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                values);
        lv_Notes.setAdapter(adapter);
    }
}