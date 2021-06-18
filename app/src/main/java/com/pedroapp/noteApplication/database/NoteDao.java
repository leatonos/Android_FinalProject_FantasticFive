package com.pedroapp.noteApplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Query("DELETE FROM note")
    void deleteAllNotes();

    @Query("DELETE FROM note WHERE id = :id" )
    int deleteNote(int id);

    @Query("DELETE FROM note WHERE category = :category" )
    void deleteNotesFromCategory(String category);

    @Query("UPDATE note SET title = :title, description = :description, category = :category  WHERE id = :id")
    int updateNote(int id, String title, String description, String category);

    @Query("SELECT * FROM note WHERE category =:chosenCategory  ORDER BY title")
    List<Note> getAllNotes(String chosenCategory);


}
