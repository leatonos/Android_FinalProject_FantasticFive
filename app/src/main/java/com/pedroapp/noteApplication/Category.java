package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
    boolean sortingByDate;

    Button sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        pageTitle = findViewById(R.id.categoryTitle);
        pageTitle.setText(ChosenOptions.chosenCategory);
        sortBy = findViewById(R.id.sortingBtn);

        //define listView id
        noteListView = findViewById(R.id.notes_lv);

        noteRoomDb = NoteRoomDb.getInstance(this);

        loadNotes();

        sortingByDate = false;

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
//

    }

    public void goToAddNote(View view) {
        Intent i = new Intent(Category.this, AddNote.class);
        startActivity(i);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
        //sortBy.setText("Sort by date");
    }

    private void loadNotes() {

        String chosenCat = ChosenOptions.chosenCategory;
        noteList = noteRoomDb.noteDao().getAllNotes(chosenCat);


        noteAdapter = new NoteAdapter(this, R.layout.note_list_item, noteList);
        noteListView.setAdapter(noteAdapter);

    }

    public void sortBy(View view) {

        if(!sortingByDate){
            String chosenCat = ChosenOptions.chosenCategory;
            noteList = noteRoomDb.noteDao().getAllNotesByDate(chosenCat);

            noteAdapter = new NoteAdapter(this, R.layout.note_list_item, noteList);
            noteListView.setAdapter(noteAdapter);

            sortingByDate = true;

            sortBy.setText("Sort by title");
        }else{
            sortBy.setText("Sort by date");
            sortingByDate = false;
            loadNotes();
        }

    }
}