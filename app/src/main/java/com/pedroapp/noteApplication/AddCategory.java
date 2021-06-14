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


public class AddCategory extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    EditText newCategoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        newCategoryName = findViewById(R.id.editTextCatName);

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String categories = sharedpreferences.getString("Cat_list", "");

        Log.d("Test",categories);

    }

    public void createNewCategory(View view) {

        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String categories = sharedpreferences.getString("Cat_list", "");



        String userCategoryText = newCategoryName.getText().toString();

        if (userCategoryText.isEmpty()) {
            newCategoryName.setError("This field cannot be empty");
            newCategoryName.requestFocus();
            return;
        }


        if(categories == "" || categories == "[]") {
            editor.putString("Cat_list",userCategoryText);
            editor.commit();
        }else{
            String addResult = categories+","+userCategoryText;
            editor.putString("Cat_list",addResult);
            editor.commit();
        }





        Toast.makeText(AddCategory.this,"Category created",Toast.LENGTH_SHORT);
        Intent i = new Intent(AddCategory.this, MainActivity.class);
        startActivity(i);

    }
}