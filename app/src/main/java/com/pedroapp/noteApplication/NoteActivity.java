package com.pedroapp.noteApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pedroapp.noteApplication.util.ChosenOptions;

import java.io.IOException;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    TextView TVtitle,TVdate,TVFinalDescription,TVnoteAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        TVtitle = findViewById(R.id.NoteTitleFinal);
        TVdate = findViewById(R.id.noteDate);
        TVFinalDescription = findViewById(R.id.noteFinalDescription);
        TVnoteAddress = findViewById(R.id.noteAddress);



        double latitude = ChosenOptions.chosenNote.getLatitude();
        double longitude = ChosenOptions.chosenNote.getLongitude();

        String noteTitle = ChosenOptions.chosenNote.getTitle();
        String noteDate = ChosenOptions.chosenNote.getTime();
        String noteDescription = ChosenOptions.chosenNote.getDescription();

        //set Address on the TextView
        getAddress(latitude,longitude);


        TVtitle.setText(noteTitle);
        TVdate.setText(noteDate);
        TVFinalDescription.setText(noteDescription);

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
}