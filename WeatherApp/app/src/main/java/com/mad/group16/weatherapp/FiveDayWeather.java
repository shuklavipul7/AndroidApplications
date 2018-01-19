package com.mad.group16.weatherapp;

import java.util.ArrayList;

/**
 * Created by Vipul.Shukla on 4/7/2017.
 */

public class FiveDayWeather {
    private String headline;
    private ArrayList<OneDayWeather> dailyWeatherList;
    private String extendedWeatherForecastUrl;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public ArrayList<OneDayWeather> getDailyWeather() {
        return dailyWeatherList;
    }

    public void setDailyWeather(ArrayList<OneDayWeather> dailyWeather) {
        this.dailyWeatherList = dailyWeather;
    }

    public String getExtendedWeatherForecastUrl() {
        return extendedWeatherForecastUrl;
    }

    public void setExtendedWeatherForecastUrl(String extendedWeatherForecastUrl) {
        this.extendedWeatherForecastUrl = extendedWeatherForecastUrl;
    }
}
