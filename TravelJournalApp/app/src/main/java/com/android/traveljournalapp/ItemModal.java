package com.android.traveljournalapp;

import android.net.Uri;

public class ItemModal {

    // variables for our course
    // name and description.
    private String cityName;
    private String cityDescription;
    private String cityFeedback;
    //private Uri cityImg;

    // creating constructor for our variables.


    public ItemModal(String cityName, String cityDescription, String cityFeedback) {
        this.cityName = cityName;
        this.cityDescription=cityDescription;
        this.cityFeedback=cityFeedback;
      //  this.cityImg=cityImg;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public String getCityFeedback() {
        return cityFeedback;
    }

    public void setCityFeedback(String cityFeedback) {
        this.cityFeedback = cityFeedback;
    }



}
