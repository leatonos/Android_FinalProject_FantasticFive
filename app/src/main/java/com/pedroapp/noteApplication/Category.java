package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.chooser.ChooserTargetService;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pedroapp.noteApplication.database.Note;
import com.pedroapp.noteApplication.database.NoteRoomDb;
import com.pedroapp.noteApplication.util.CategoryChosen;
import com.pedroapp.noteApplication.util.DatabaseHelper;
import com.pedroapp.noteApplication.util.NoteAdapter;

import java.util.List;

public class Category extends AppCompatActivity {

    TextView pageTitle;
    ListView noteListView;

    DatabaseHelper sqLiteDatabase;
    private NoteRoomDb noteRoomDb;

    List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        pageTitle = findViewById(R.id.categoryTitle);
        pageTitle.setText(CategoryChosen.chosenCategory);

        //define listView id
        noteListView = findViewById(R.id.notes_lv);

        noteRoomDb = NoteRoomDb.getInstance(this);

        loadNotes();

    }

    public void goToAddNote(View view) {

        Intent i = new Intent(Category.this, AddNote.class);
        startActivity(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes(){

        String chosenCat = CategoryChosen.chosenCategory;

        noteList = noteRoomDb.noteDao().getAllNotes(chosenCat);

        NoteAdapter noteAdapter = new NoteAdapter(this,R.layout.note_list_item,noteList);
        noteListView.setAdapter(noteAdapter);

    }
}