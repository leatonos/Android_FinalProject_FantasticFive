package com.pedroapp.noteApplication.model;

public class Note {

    int id;
    String title,description,category,image,audio;
    String time;
    Double latitude,longitude;

    public Note(int id, String title,String description, String category, String image, String audio,String time, double latitude, double longitude){

        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.image = image;
        this.audio = audio;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public int getId(){ return id;}
    public String getTitle(){ return title;}
    public String getDescription(){ return description;}
    public String getCategory(){ return category;}
    public String getImage(){ return image;}
    public String getAudio(){ return audio;}
    public String getTime() { return time; }
    public Double getLatitude(){ return latitude;}
    public Double getLongitude(){ return longitude;}

}
