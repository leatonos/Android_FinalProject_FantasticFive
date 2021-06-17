package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pedroapp.noteApplication.database.Note;
import com.pedroapp.noteApplication.database.NoteRoomDb;
import com.pedroapp.noteApplication.util.ChosenOptions;
import com.pedroapp.noteApplication.util.DatabaseHelper;
import com.pedroapp.noteApplication.util.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    TextView pageTitle;
    ListView noteListView;

    DatabaseHelper sqLiteDatabase;
    private NoteRoomDb noteRoomDb;

    List<Note> noteList;
    List<Note> filteredList;
    EditText searchText;

    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        pageTitle = findViewById(R.id.categoryTitle);
        pageTitle.setText(ChosenOptions.chosenCategory);

        //define listView id
        noteListView = findViewById(R.id.notes_lv);

        noteRoomDb = NoteRoomDb.getInstance(this);

        loadNotes();

//        searchText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (noteAdapter != null){
//
//                    noteAdapter.getFilter().filter(s);
//
//                }
//
//            }
//        });

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

    private void loadNotes() {

        String chosenCat = ChosenOptions.chosenCategory;
        noteList = noteRoomDb.noteDao().getAllNotes(chosenCat);


        noteAdapter = new NoteAdapter(this, R.layout.note_list_item, noteList);
        noteListView.setAdapter(noteAdapter);



    }



}