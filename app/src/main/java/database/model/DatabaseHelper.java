package database.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME ="notes_db";

//create database
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

//create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
    }
//upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //dropping older table and creating table again
        db.execSQL(" DROP TABLE IF EXISTS " + Note.TABLE_NAME );
        onCreate(db);
    }




//inserting note


     public long insertNote(String note){

        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //we need not add id and timestamp they will be auto
        values.put(Note.COLUMN_NOTE,note);

        //inserting row
        long id =db.insert(Note.TABLE_NAME,null,values) ;
        db.close();
        return id;
     }
//Reading notes


     public Note getNote(long id){
        SQLiteDatabase db =this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID,Note.COLUMN_NOTE,Note.COLUMN_TIMESTAMP},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);


        if(cursor!=null)
            cursor.moveToFirst();

//prepare note object
                Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

        cursor.close();
        return note;

    }

    public List<Note> getAllNotes(){
          List<Note> notes = new ArrayList<>();


          //select query

        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }
            db.close();
            return  notes;
//return notes list

    }

    public int getNotesCount(){

        String countQuery ="SELECT * FROM " +Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return  count;
    }



    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE,note.getNote());

        return db.update(Note.TABLE_NAME,values, Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }


    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME , Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(note.getId())});


    }
































































}
