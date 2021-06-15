package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pedroapp.noteApplication.util.CategoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    ArrayList<String> loadedCategories;
    ListView listViewCategories;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewCategories = findViewById(R.id.categories_lv);

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");
        loadedCategories = new ArrayList<String>(Arrays.asList(categories.split(",")));
        categoryAdapter = new CategoryAdapter(this,R.layout.category_list_item,loadedCategories);

        listViewCategories.setAdapter(categoryAdapter);

    }

    public void goToNewCategory(View view) {
        Intent i = new Intent(MainActivity.this, AddCategory.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoryAdapter.notifyDataSetChanged();

    }

}