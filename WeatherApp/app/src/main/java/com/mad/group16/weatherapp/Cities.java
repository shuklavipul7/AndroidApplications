package com.mad.group16.weatherapp;

import java.util.Date;

/**
 * Created by vipul on 4/4/2017.
 */

public class Cities {
    private String cityKey;
    private String cityName;
    private String country;
    private MetricTemperature metricTemperature;
    private Boolean favourite;
    private String lastUpdated;

    public Cities(String cityKey, String cityName, String country, MetricTemperature metricTemperature, Boolean favourite, String lastUpdated) {
        this.cityKey = cityKey;
        this.cityName = cityName;
        this.country = country;
        this.metricTemperature = metricTemperature;
        this.favourite = favourite;
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Cities() {
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public MetricTemperature getMetricTemperature() {
        return metricTemperature;
    }

    public void setMetricTemperature(MetricTemperature metricTemperature) {
        this.metricTemperature = metricTemperature;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }
}
