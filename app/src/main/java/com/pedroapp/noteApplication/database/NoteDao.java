package com.pedroapp.noteApplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface NoteDao {

    @Insert
    void insertEmployee(Note note);

    @Query("DELETE FROM note")
    void deleteAllNotes();

    @Query("DELETE FROM note WHERE id = :id" )
    int deleteNote(int id);

    /*
    @Query("UPDATE note SET name = :name, department = :department, salary = :salary WHERE id = :id")
    int updateEmployee(int id, String name, String department, double salary);
    */

    @Query("SELECT * FROM note ORDER BY title")
    List<Note> getAllNotes();

}
