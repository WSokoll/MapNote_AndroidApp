package edu.agh.mapnote_androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String NOTES_TABLE = "Notes";
    public static final String ID_COLUMN = "NoteId";
    public static final String LATITUDE_COLUMN = "NoteLatitude";
    public static final String LONGITUDE_COLUMN = "NoteLongitude";
    public static final String ADDRESS_COLUMN = "NoteAddress";
    public static final String DATE_COLUMN = "NoteDate";
    public static final String CONTENT_COLUMN = "NoteContent";

    public DbHelper(@Nullable Context context) {
        super(context, "mapNote.db", null, 2);
    }

    //create new database (called the first time database is accessed)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE TABLE " + NOTES_TABLE + " (" +
                ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LATITUDE_COLUMN + " REAL, " +
                LONGITUDE_COLUMN + " REAL, " +
                ADDRESS_COLUMN + " TEXT, " +
                DATE_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                CONTENT_COLUMN + " TEXT)";

        db.execSQL(createStatement);
    }

    //called when database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //not really right way to do it, because we will loose all the data

        //drop the table
        String statement = "DROP TABLE IF EXISTS " + NOTES_TABLE;
        db.execSQL(statement);

        //create new table
        onCreate(db);
    }

    //add note to database
    public boolean addNote(Note note){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LATITUDE_COLUMN, note.getLatitude());
        cv.put(LONGITUDE_COLUMN, note.getLongitude());
        cv.put(ADDRESS_COLUMN, note.getAddress());
        cv.put(CONTENT_COLUMN, note.getNoteContent());

        //insert content to database
        long insert = db.insert(NOTES_TABLE, null, cv);

        //close db
        db.close();

        //returns true if insert was successful or false if insert was unsuccessful
        return insert != -1;
    }

    //get list of all notes
    public List<Note> getAllNotes(){
        List<Note> resultList = new ArrayList<>();

        //get data from the database
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE;

        //result set
        Cursor cursor = db.rawQuery(query, null);

        //check if there are any results
        if(cursor.moveToFirst()){
            //create new Note object for every row and put it into resultList
            do{
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setLatitude(cursor.getDouble(1));
                note.setLongitude(cursor.getDouble(2));
                note.setAddress(cursor.getString(3));
                note.setDate(cursor.getString(4));
                note.setNoteContent(cursor.getString(5));

                resultList.add(note);
            }while(cursor.moveToNext());

        }
        //close the cursor and the db
        cursor.close();
        db.close();
        return resultList;
    }

    //get note with specified id
    public Note getNoteById(int id) {
        Note note = new Note();

        //get data from the database
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE + " WHERE " + ID_COLUMN + " = " + id;

        //result set
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setLatitude(cursor.getDouble(1));
            note.setLongitude(cursor.getDouble(2));
            note.setAddress(cursor.getString(3));
            note.setDate(cursor.getString(4));
            note.setNoteContent(cursor.getString(5));
        }

        return note;
    }

    //delete note with specified id
    public boolean deleteNoteById(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete row from database where id column match given id
        long delete = db.delete(NOTES_TABLE, ID_COLUMN + "=?", new String[]{String.valueOf(id)});

        //close db
        db.close();

        //returns true if delete was successful or false if delete was unsuccessful
        return delete != -1;
    }

    //edit specific note's content
    public boolean editNote(Note editedNote){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CONTENT_COLUMN, editedNote.getNoteContent());

        //update content
        long update = db.update(NOTES_TABLE, cv, ID_COLUMN + " = ?", new String[]{String.valueOf(editedNote.getId())});

        //close db
        db.close();

        //returns true if update was successful or false if update was unsuccessful
        return update != -1;
    }
}
