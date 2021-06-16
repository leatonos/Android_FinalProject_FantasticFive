package com.pedroapp.noteApplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pedroapp.noteApplication.database.Note;
import com.pedroapp.noteApplication.database.NoteRoomDb;
import com.pedroapp.noteApplication.util.DatabaseHelper;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

    DatabaseHelper sqLiteDatabase;
    private NoteRoomDb noteRoomDb;

    Spinner categorySelectSpin;
    SharedPreferences sharedpreferences;
    ArrayList<String> loadedCategories;
    ArrayAdapter<String> spinnerArrayAdapter;

    EditText newTitle,newDescription;

    //Location Code Started

    private static final int REQUEST_CODE = 1;

    // Location with FUSED LOCATION PROVIDER CLIENT

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int FASTEST_INTERVAL = 3000; // 3 seconds

    private List<String> permissionsToRequest;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();

    // set initial variables for the location
    double noteLatitute;
    double noteLongitude;


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

        // instantiate the fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // add permissions

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0) {
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE);
        }

    }

    private List<String> permissionsToRequest(List<String> permissions) {
        ArrayList<String> results = new ArrayList<>();
        for (String perm: permissions) {
            if (!hasPermission(perm))
                results.add(perm);
        }

        return results;
    }

    private boolean hasPermission(String perm) {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // this is a proper place to check the google play services availability

        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(AddNote.this, "No Services", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
        } else {
            Log.i("NOTE", "onPostResume: ");
            findLocation();
        }
    }

    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        noteLatitute = location.getLatitude();
                        noteLongitude = location.getLongitude();
                    }
                }
            });
        }
        startUpdateLocation();
    }

    private void startUpdateLocation() {
        Log.d("TEST", "startUpdateLocation: ");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();

                    noteLatitute = location.getLatitude();
                    noteLongitude = location.getLongitude();
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            for (String perm: permissions) {
                if (!hasPermission(perm))
                    permissionsRejected.add(perm);
            }

            if (permissionsRejected.size() > 0 ) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(AddNote.this)
                            .setMessage("The location permission is mandatory")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), REQUEST_CODE);
                                    }

                                }
                            }).setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
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

        Note note = new Note(textNewTitle,textNewDescription,selectedCategory,"","",currentDate,noteLatitute,noteLongitude);
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