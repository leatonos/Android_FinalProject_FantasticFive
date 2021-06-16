package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pedroapp.noteApplication.util.ChosenOptions;

public class NoteActivity extends AppCompatActivity {

    TextView TVtitle,TVdate,TVFinalDescription,TVnoteAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        TVtitle = findViewById(R.id.NoteTitleFinal);
        TVdate = findViewById(R.id.noteDate);
        TVFinalDescription = findViewById(R.id.noteFinalDescription);
        TVnoteAddress = findViewById(R.id.noteAddress);

        String noteTitle = ChosenOptions.chosenNote.getTitle();
        String noteDate = ChosenOptions.chosenNote.getTime();
        String noteDescription = ChosenOptions.chosenNote.getDescription();

        TVtitle.setText(noteTitle);
        TVdate.setText(noteDate);
        TVFinalDescription.setText(noteDescription);

    }

    public void goToMap(View view) {
        Intent i = new Intent(NoteActivity.this, MapsActivity.class);
        startActivity(i);
    }
}