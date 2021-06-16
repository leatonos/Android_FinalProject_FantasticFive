package com.pedroapp.noteApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 3;
    DatabaseHelper sqLiteDatabase;
    private NoteRoomDb noteRoomDb;


    //

    Spinner categorySelectSpin;
    SharedPreferences sharedpreferences;
    ArrayList<String> loadedCategories;
    ArrayAdapter<String> spinnerArrayAdapter;
    ImageView notePhoto;

    //Variables to store the photoPath on the
    OutputStream outputStream;
    String notePhotoPath;

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

    //Audio Recording initial variables
    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;

    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static String audioFilePath;
    private Button stopButton, playButton, recordButton;

    private boolean isRecording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //Load the categories from the SharedPreferences
        sharedpreferences = getSharedPreferences("Categories", Context.MODE_PRIVATE);
        String categories = sharedpreferences.getString("Cat_list", "My Dreams,My Memories,Events");
        loadedCategories = new ArrayList<String>(Arrays.asList(categories.split(",")));

        //Put ids on the TextsFields, Spinner and ImageView
        categorySelectSpin = findViewById(R.id.newCategorySpinner);
        newTitle = findViewById(R.id.editTextNewNoteTitle);
        newDescription = findViewById(R.id.editTextNewNoteDescription);
        notePhoto = findViewById(R.id.imageNewNote);

        //Insert the Categories in the Spinner
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,loadedCategories);
        categorySelectSpin.setAdapter(spinnerArrayAdapter);

        noteRoomDb = NoteRoomDb.getInstance(this);

        // instantiate the fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // add permissions for Location

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);


        //asks permitions for Location
        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0) {
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE);
        }

        //Ask permition for Camera

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},2);
        }

        //Ask permition for Internal Storage

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4);
        }


        audioSetup();

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


    //Taking Photos

    public void takePhoto(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            notePhoto.setVisibility(View.VISIBLE);
            openCamera();
        }

    }

    private void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
          Bundle extras = data.getExtras();
            Bitmap imageTaken = (Bitmap) extras.get("data");
            notePhoto.setVisibility(View.VISIBLE);
            notePhoto.setImageBitmap(imageTaken);
        }else{
            notePhoto.setVisibility(View.GONE);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public void savePhoto() {

        File dir = new File(Environment.getExternalStorageDirectory(),"SaveImage");

        if (!dir.exists()){
            dir.mkdir();
        }


        BitmapDrawable drawable = (BitmapDrawable) notePhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File file = new File(dir,System.currentTimeMillis()+".png");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);



        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        notePhotoPath = file.getAbsolutePath();

    }


    public void createNewNote(View view){

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

        savePhoto();

        Note note = new Note(textNewTitle,textNewDescription,selectedCategory,notePhotoPath,"",currentDate,noteLatitute,noteLongitude);
        noteRoomDb.noteDao().insertNote(note);

        clearFields();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //clearFields();
    }

    private void clearFields() {
        newTitle.setText("");
        newDescription.setText("");
    }


}