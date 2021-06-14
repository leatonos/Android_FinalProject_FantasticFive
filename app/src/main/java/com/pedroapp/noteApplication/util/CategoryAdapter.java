package com.pedroapp.noteApplication.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pedroapp.noteApplication.Category;
import com.pedroapp.noteApplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter {
    private static final String TAG = "CategoryAdapter";

    Context context;
    int layoutRes;

    SharedPreferences sharedpreferences;
    List<String> loadedCategories;


    public CategoryAdapter(@NonNull Context context, int resource, List<String> loadedCategories) {

        super(context, resource, loadedCategories);
        this.loadedCategories = loadedCategories;
        this.context = context;
        this.layoutRes = resource;

    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        TextView catNameTV = v.findViewById(R.id.CatName);
        catNameTV.setText(loadedCategories.get(position));

        //Activates when you click on the Category
       v.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d("Category Clicked!!!", "onClick: "+loadedCategories.get(position));
           }
       });
        v.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(position);
            }

            private void deleteCategory(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        loadedCategories.remove(position);
                        String finalresult = loadedCategories.toString();

                        SharedPreferences sharedpreferences;
                        sharedpreferences = context.getSharedPreferences("Categories", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("Cat_list",finalresult);
                        editor.commit();

                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "The Category has not been deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        Log.d(TAG, "getView: " + getCount());
        return v;
    }



}
