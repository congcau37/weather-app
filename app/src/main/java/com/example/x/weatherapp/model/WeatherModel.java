package com.example.x.weatherapp.model;

/* Created by X on 12/9/2017.
* */

public class WeatherModel {
    private String description;
    private String image;
    private String date;
    private String maxTemp;
    private String minTemp;

    public WeatherModel(String description, String image, String date, String maxTemp, String minTemp) {
        this.description = description;
        this.image = image;
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }
}
