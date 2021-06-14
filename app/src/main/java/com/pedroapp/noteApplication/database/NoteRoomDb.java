package com.pedroapp.noteApplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteRoomDb extends RoomDatabase{

    private static final String DB_NAME = "note_room_db";

    public abstract NoteDao noteDao();

    private static volatile NoteRoomDb INSTANCE;

    public static NoteRoomDb getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        return INSTANCE;
    }

}
