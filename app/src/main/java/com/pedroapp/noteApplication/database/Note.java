package com.pedroapp.noteApplication.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "audio")
    private String audio;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    public Note(@NonNull String title, @NonNull String description, @NonNull String category, String image, String audio, String time, Double latitude, Double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.image = image;
        this.audio = audio;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Gets
    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public String getImage(){return image;}

    public String getAudio(){return audio;}

    public String getTime(){return time;}

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {return longitude;}


    //Sets
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public void setImage(String image){this.image = image;}

    public void setAudio(String audio){this.audio = audio;}

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude){this.longitude = longitude;}



}
