package edu.agh.mapnote_androidapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String NOTES_TABLE = "Notes";
    public static final String ID_COLUMN = "NoteId";
    public static final String LATITUDE_COLUMN = "NoteLatitude";
    public static final String LONGITUDE_COLUMN = "NoteLongitude";
    public static final String ADDRESS_COLUMN = "NoteAddress";
    public static final String DATE_COLUMN = "NoteDate";
    public static final String CONTENT_COLUMN = "NoteContent";

    public DbHelper(@Nullable Context context) {
        super(context, "mapNote.db", null, 1);
    }

    //create new database (called the first time database is accessed)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE TABLE " + NOTES_TABLE + " (" +
                ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LATITUDE_COLUMN + " REAL, " +
                LONGITUDE_COLUMN + " REAL, " +
                ADDRESS_COLUMN + " TEXT, " +
                DATE_COLUMN + " TEXT, " +
                CONTENT_COLUMN + " TEXT)";

        db.execSQL(createStatement);
    }

    //called when database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
