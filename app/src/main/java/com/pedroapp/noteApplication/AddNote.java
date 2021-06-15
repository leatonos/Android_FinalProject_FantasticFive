package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class AddNote extends AppCompatActivity {

    Spinner categorySelectSpin;
    SharedPreferences sharedpreferences;
    ArrayList<String> loadedCategories;
    ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");
        loadedCategories = new ArrayList<String>(Arrays.asList(categories.split(",")));

        categorySelectSpin = findViewById(R.id.newCategorySpinner);
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,loadedCategories);
        categorySelectSpin.setAdapter(spinnerArrayAdapter);

    }

}