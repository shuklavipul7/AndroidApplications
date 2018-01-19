package com.mad.group16.weatherapp;

import java.util.Date;

/**
 * Created by Vipul.Shukla on 4/7/2017.
 */

public class OneDayWeather {
    private Date date;
    private MetricTemperature maximumTemperature;
    private MetricTemperature minimumTemperature;
    private String dayIcon;
    private String nightIcon;
    private String dayPhase;
    private String nightphase;
    private String dailyWeatherForecastUrl;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDailyWeatherForecastUrl() {
        return dailyWeatherForecastUrl;
    }

    public void setDailyWeatherForecastUrl(String dailyWeatherForecastUrl) {
        this.dailyWeatherForecastUrl = dailyWeatherForecastUrl;
    }

    public MetricTemperature getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(MetricTemperature maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public MetricTemperature getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(MetricTemperature minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public String getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(String dayIcon) {
        this.dayIcon = dayIcon;
    }

    public String getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(String nightIcon) {
        this.nightIcon = nightIcon;
    }

    public String getDayPhase() {
        return dayPhase;
    }

    public void setDayPhase(String dayPhase) {
        this.dayPhase = dayPhase;
    }

    public String getNightphase() {
        return nightphase;
    }

    public void setNightphase(String nightphase) {
        this.nightphase = nightphase;
    }
}
