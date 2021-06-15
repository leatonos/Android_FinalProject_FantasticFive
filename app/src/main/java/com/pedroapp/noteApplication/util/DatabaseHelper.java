package com.pedroapp.noteApplication.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper{

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "note_database";

        private static final String TABLE_NAME = "note";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_TITLE = "title";
        private static final String COLUMN_DESCRIPTION = "description";
        private static final String COLUMN_CATEGORY = "category";
        private static final String COLUMN_IMAGE = "image";
        private static final String COLUMN_AUDIO = "audio";
        private static final String COLUMN_TIME = "time";
        private static final String COLUMN_LATITUDE = "latitude";
        private static final String COLUMN_LONGITUDE = "longitude";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER NOT NULL CONSTRAINT note_pk PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " VARCHAR(50) NOT NULL, " +
                    COLUMN_DESCRIPTION + " VARCHAR(150) NOT NULL, " +
                    COLUMN_CATEGORY + " VARCHAR(45) NOT NULL, " +
                    COLUMN_IMAGE + " VARCHAR(50) NOT NULL, " +
                    COLUMN_AUDIO + " VARCHAR(50) NOT NULL, " +
                    COLUMN_TIME + " DATETIME NOT NULL, " +
                    COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                    COLUMN_LONGITUDE + " DOUBLE NOT NULL);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // drop table and then create it
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
            db.execSQL(sql);
            onCreate(db);
        }

        // insert
        /**
         * add employee - insert employee into employee table
         * @param title
         * @param category
         * @param description
         * @param image
         * @param audio
         * @param time
         * @param latitude
         * @param longitude
         * @return boolean value - true (inserted) false (not inserted)
         * */
        public boolean addNote( String title,String description, String category, String image, String audio,String time, double latitude, double longitude) {
            // we need a writeable instance of SQLite database
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();

            // we need to define a content values in order to insert data into our database
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TITLE, title);
            contentValues.put(COLUMN_DESCRIPTION, description);
            contentValues.put(COLUMN_CATEGORY, category);
            contentValues.put(COLUMN_IMAGE, image);
            contentValues.put(COLUMN_AUDIO, audio);
            contentValues.put(COLUMN_TIME, time);
            contentValues.put(COLUMN_IMAGE, image);
            contentValues.put(COLUMN_LATITUDE, String.valueOf(latitude));
            contentValues.put(COLUMN_LONGITUDE, String.valueOf(longitude));

            // the insert method associated to SQLite database instance returns -1 if nothing is inserted
            return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) != -1;
        }

        /**
         * Query database - select all the employees
         * @return cursor
         * */
        public Cursor getAllNotes() {
            // we need a readable instance of database
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        }

        /**
         * Update note in database
         * @param id
         * @param title
         * @param description
         * @param category
         * @return boolean value - true (successful)
         * */
        public boolean updateNote(int id, String title, String description, String category) {
            // we need a writeable instance of database
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TITLE, title);
            contentValues.put(COLUMN_DESCRIPTION, description);
            contentValues.put(COLUMN_CATEGORY, category);

            // update method associated to SQLite database instance returns number of rows affected
            return sqLiteDatabase.update(TABLE_NAME,
                    contentValues,
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}) > 0;
        }

        /**
         * Delete employee from database table
         * @param id
         * @return true if is successful
         * */
        public boolean deleteNote(int id) {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            // the delete method associated to the SQLite database instance returns the number of rows affected
            return sqLiteDatabase.delete(TABLE_NAME,
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}) > 0;
        }



    }
