package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pedroapp.noteApplication.database.Note;
import com.pedroapp.noteApplication.database.NoteRoomDb;
import com.pedroapp.noteApplication.util.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

    DatabaseHelper sqLiteDatabase;
    private NoteRoomDb noteRoomDb;

    Spinner categorySelectSpin;
    SharedPreferences sharedpreferences;
    ArrayList<String> loadedCategories;
    ArrayAdapter<String> spinnerArrayAdapter;

    EditText newTitle,newDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //Load the categories from the SharedPreferences
        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");
        loadedCategories = new ArrayList<String>(Arrays.asList(categories.split(",")));

        //Put ids on the TextsFields and Spinner
        categorySelectSpin = findViewById(R.id.newCategorySpinner);
        newTitle = findViewById(R.id.editTextNewNoteTitle);
        newDescription = findViewById(R.id.editTextNewNoteDescription);

        //Insert the Categories in the Spinner
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,loadedCategories);
        categorySelectSpin.setAdapter(spinnerArrayAdapter);

        noteRoomDb = NoteRoomDb.getInstance(this);


    }


    public void createNewNote(View view) {

        String textNewTitle = newTitle.getText().toString().trim();
        String textNewDescription = newDescription.getText().toString().trim();
        String selectedCategory = categorySelectSpin.getSelectedItem().toString().trim();

        if (textNewTitle.isEmpty()) {
            newTitle.setError("This field cannot be empty");
            newTitle.requestFocus();
            return;
        }
        if (textNewDescription.isEmpty()) {
            newDescription.setError("This field cannot be empty");
            newDescription.requestFocus();
            return;
        }

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Note note = new Note(textNewTitle,textNewDescription,selectedCategory,"","",currentDate,0.0,0.0);
        noteRoomDb.noteDao().insertNote(note);

        Toast.makeText(AddNote.this,"Note Added",Toast.LENGTH_SHORT).show();
        clearFields();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clearFields();
    }

    private void clearFields() {
        newTitle.setText("");
        newDescription.setText("");
    }

}