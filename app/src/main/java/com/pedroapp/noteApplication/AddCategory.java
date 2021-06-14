package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddCategory extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    EditText newCategoryName;
    List<String> loadedCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        newCategoryName = findViewById(R.id.editTextCatName);

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");
        loadedCategories = new ArrayList<String>(Arrays.asList(categories.split(",")));

        Log.d("Test",categories);

    }

    public void createNewCategory(View view) {

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");

        String userCategoryText = newCategoryName.getText().toString();

        if (userCategoryText.isEmpty()) {
            newCategoryName.setError("This field cannot be empty");
            newCategoryName.requestFocus();
            return;
        }

        //Add new Category to the SharedPreferences
        loadedCategories.add(userCategoryText);



            editor.putString("Cat_list",addResult());
            editor.commit();


        Toast.makeText(AddCategory.this,"Category created",Toast.LENGTH_SHORT);
        Intent i = new Intent(AddCategory.this, MainActivity.class);
        startActivity(i);

    }

    public void deleteAll(View view) {

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");

        editor.putString("Cat_list","My Dreams,My Memories,Events");
        editor.commit();

        Intent i = new Intent(AddCategory.this, MainActivity.class);
        startActivity(i);

    }

    String addResult(){
        String finalResult = "";
        for (String cat : loadedCategories){
            finalResult += ","+cat;
        }

        Log.i("RESULT", "addResult: "+finalResult);
        return finalResult.substring(1);
    }


}