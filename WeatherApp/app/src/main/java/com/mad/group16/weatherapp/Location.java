package com.mad.group16.weatherapp;

/**
 * Created by vipul on 4/4/2017.
 */

public class Location {
    private String city;
    private String countryId;
    private String key;

    public Location(String city, String countryId, String key) {
        this.city = city;
        this.countryId = countryId;
        this.key = key;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
