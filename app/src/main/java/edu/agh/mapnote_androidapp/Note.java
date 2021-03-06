package edu.agh.mapnote_androidapp;

public class Note {

    private int id;
    private double latitude;
    private double longitude;
    private String address;
    private String date;
    private String noteContent;

    public Note() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }


    //needs to start with id followed by dot!!!
    @Override
    public String toString() {
        return id + ". " + address + "\n" + noteContent;
    }
}
