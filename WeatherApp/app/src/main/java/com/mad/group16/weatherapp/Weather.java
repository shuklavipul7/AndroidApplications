package com.mad.group16.weatherapp;

import java.util.Date;

/**
 * Created by vipul on 4/4/2017.
 */

public class Weather {
    private Date localObservationDateTime;
    private String weatherText;
    private String weatherIcon;
    private MetricTemperature metricTemperature;

    public Weather(Date localObservationDateTime, String weatherText, String weatherIcon, MetricTemperature metricTemperature) {
        this.localObservationDateTime = localObservationDateTime;
        this.weatherText = weatherText;
        this.weatherIcon = weatherIcon;
        this.metricTemperature = metricTemperature;
    }

    public Date getLocalObservationDateTime() {
        return localObservationDateTime;
    }

    public void setLocalObservationDateTime(Date localObservationDateTime) {
        this.localObservationDateTime = localObservationDateTime;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public MetricTemperature getMetricTemperature() {
        return metricTemperature;
    }

    public void setMetricTemperature(MetricTemperature metricTemperature) {
        this.metricTemperature = metricTemperature;
    }
}
