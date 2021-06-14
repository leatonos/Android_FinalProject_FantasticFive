package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pedroapp.noteApplication.util.CategoryChosen;

public class Category extends AppCompatActivity {

    TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        pageTitle = findViewById(R.id.categoryTitle);
        pageTitle.setText(CategoryChosen.chosenCategory);

    }

    public void goToAddNote(View view) {
    }
}