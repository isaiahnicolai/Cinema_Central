package it.cinema.cinemaCentral;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Isaiah Nicolai on 11/26/2015.
 */
public class Movie implements Parcelable {

    private String title;
    private String year;

    public Movie(){

    }

    public Movie(String title, String year){
        this.title = title;
        this.year = year;
    }

    public String getTitle(){
        return title;
    }

    public String getYear(){
        return year;
    }

    public void setTitle(String newTitle){
        this.title=newTitle;
    }

    public void setYear(String newYear){
        this.year=newYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
