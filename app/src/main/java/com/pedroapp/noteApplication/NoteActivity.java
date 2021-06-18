package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroapp.noteApplication.util.ChosenOptions;

import java.io.IOException;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    TextView TVtitle,TVdate,TVFinalDescription,TVnoteAddress;
    ImageView notePhoto;
    Button playBtn, stopBtn, locationBtn;

    //variables for the Audio
    private static MediaPlayer mediaPlayer;
    private static String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Set Textviews Ids
        TVtitle = findViewById(R.id.NoteTitleFinal);
        TVdate = findViewById(R.id.noteDate);
        TVFinalDescription = findViewById(R.id.noteFinalDescription);
        TVnoteAddress = findViewById(R.id.noteAddress);
        notePhoto = findViewById(R.id.noteImageView);
        playBtn = findViewById(R.id.playbtnNote);
        stopBtn = findViewById(R.id.stopBtnNote);
        locationBtn = findViewById(R.id.locationBtn);

        double latitude = ChosenOptions.chosenNote.getLatitude();
        double longitude = ChosenOptions.chosenNote.getLongitude();

        String noteTitle = ChosenOptions.chosenNote.getTitle();
        String noteDate = ChosenOptions.chosenNote.getTime();
        String noteDescription = ChosenOptions.chosenNote.getDescription();
        String notePhotoPath = ChosenOptions.chosenNote.getImage();
        audioFilePath = ChosenOptions.chosenNote.getAudio();

        //set Address on the TextView
        getAddress(latitude,longitude);


        TVtitle.setText(noteTitle);
        TVdate.setText(noteDate);
        TVFinalDescription.setText(noteDescription);
        stopBtn.setEnabled(false);

        //finds the image on the InternalStorage and put on the ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(notePhotoPath);
        notePhoto.setImageBitmap(bitmap);

        //checking for empty collums
        if(ChosenOptions.chosenNote.getAudio().equals("") || ChosenOptions.chosenNote.getAudio().isEmpty()){
            Log.d("TESTING", "NO AUDIO FOUND");
            playBtn.setVisibility(View.GONE);
            stopBtn.setVisibility(View.GONE);
        }

        if(ChosenOptions.chosenNote.getLatitude() == 0.0 && ChosenOptions.chosenNote.getLatitude() == 0.0){
            locationBtn.setVisibility(View.GONE);
        }


    }

    public void goToMap(View view) {
        Intent i = new Intent(NoteActivity.this, MapsActivity.class);
        startActivity(i);
    }


    void getAddress(double latitude,double longitude){

        Geocoder geocoder = new Geocoder(NoteActivity.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            Address address = addresses.get(0);

            String Street = address.getThoroughfare();
            String AdNumber = address.getFeatureName();
            String postalCode = address.getPostalCode();
            String city = address.getSubAdminArea();
            String province = address.getAdminArea();

                    /*
                    Log.d("Testing", "Street: "+AdNumber+","+Street);
                    Log.d("Testing", "Postalcode: "+postalCode);
                    Log.d("Testing", "City "+city);
                    Log.d("Testing", "Province "+province);
                    */

            //Toast.makeText(MapsActivity.this,AdNumber+", "+Street+", "+postalCode+", "+city+", "+province,Toast.LENGTH_LONG).show();

            String finaladdress = AdNumber+", "+Street+", "+postalCode+", "+city+", "+province;

            TVnoteAddress.setText(finaladdress);

        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void playAudioNote(View view) throws IOException {

        playBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    public void stopAudioNote(View view) {
        stopBtn.setEnabled(false);
        playBtn.setEnabled(true);

            mediaPlayer.release();
            mediaPlayer = null;

    }





}