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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pedroapp.noteApplication.NoteActivity;
import com.pedroapp.noteApplication.R;
import com.pedroapp.noteApplication.database.Note;
import com.pedroapp.noteApplication.database.NoteDao;
import com.pedroapp.noteApplication.database.NoteRoomDb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteAdapter extends ArrayAdapter implements Filterable {

    private static final String TAG = "NoteAdapter";

    Context context;
    int layoutRes;
    List<Note> noteList = new ArrayList<>();
    List<Note> noteListFiltered = new ArrayList<>();
    DatabaseHelper sqLiteDatabase;
    NoteRoomDb noteRoomDb;


    public NoteAdapter(@NonNull Context context, int resource, List<Note> noteList){
        super(context, resource, noteList);
        this.noteList = noteList;
        this.noteListFiltered = noteList;
        this.context = context;
        this.layoutRes = resource;
        noteRoomDb = NoteRoomDb.getInstance(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        TextView noteTitleTv = v.findViewById(R.id.noteTitle);

        final Note note = noteList.get(position);
        noteTitleTv.setText(note.getTitle());

        //Activates when you click on a Note
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChosenOptions.chosenNote = note;
                Intent i = new Intent(context, NoteActivity.class);
                context.startActivity(i);
            }
        });

                v.findViewById(R.id.editNoteBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateNote(note);
                    }

                    private void updateNote(final Note note) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View view = layoutInflater.inflate(R.layout.dialog_update_note, null);
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        SharedPreferences sharedpreferences;
                        ArrayList<String> loadedCategories;
                        ArrayAdapter<String> spinnerArrayAdapter;

                        sharedpreferences = context.getSharedPreferences("Categories", Context.MODE_PRIVATE);
                        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");
                        loadedCategories = new ArrayList<String>(Arrays.asList(categories.split(",")));

                        final EditText et_noteTitle = view.findViewById(R.id.editTextNoteTitle);
                        final EditText et_noteDescription = view.findViewById(R.id.editTextNoteDescription);
                        final Spinner categorySelectSpin = view.findViewById(R.id.spinnerEditCategory);

                        spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,loadedCategories);
                        categorySelectSpin.setAdapter(spinnerArrayAdapter);

                        et_noteTitle.setText(note.getTitle());
                        et_noteDescription.setText(note.getDescription());


                        view.findViewById(R.id.confirmEditBtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String title = et_noteTitle.getText().toString().trim();
                                String description = et_noteDescription.getText().toString().trim();
                                String category = categorySelectSpin.getSelectedItem().toString().trim();

                                if (title.isEmpty()) {
                                    et_noteTitle.setError("This field cannot be empty");
                                    et_noteTitle.requestFocus();
                                    return;
                                }
                                if (description.isEmpty()) {
                                    et_noteDescription.setError("This field cannot be empty");
                                    et_noteDescription.requestFocus();
                                    return;
                                }

                                // Room
                                noteRoomDb.noteDao().updateNote(note.getId(),
                                        title, description, category);
                                loadNotes();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

        v.findViewById(R.id.deleteNoteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(note);
            }

            private void deleteNote(final Note note) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Deletes the Contact
                        noteRoomDb.noteDao().deleteNote(note.getId());
                        loadNotes();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "The Note (" + note.getTitle() + ") has not been deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Log.d(TAG, "getView: " + getCount());
        return v;
    }

    @Override
    public int getCount() {
     return noteList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    noteListFiltered = noteList;
                } else {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note row : noteList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getCategory().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    noteListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = noteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                noteListFiltered = (List<Note>) filterResults.values;

                // refresh the list with filtered data
                Log.e( "publishResults: ","called" );
//                Log.e( "publishResults: ",new GsonBuilder().create().toJson(contactListFiltered));
                notifyDataSetChanged();
            }
        };
    }


    private void loadNotes() {

        // check hre for filtered list
        String chosen = ChosenOptions.chosenCategory;
        noteList = noteRoomDb.noteDao().getAllNotes(chosen);
        notifyDataSetChanged();

    }






}
