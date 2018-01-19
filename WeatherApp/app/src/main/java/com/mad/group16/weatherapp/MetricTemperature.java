package com.mad.group16.weatherapp;

/**
 * Created by vipul on 4/4/2017.
 */

public class MetricTemperature {
    private Integer value;
    private String unit;
    private Integer unitType;

    public MetricTemperature(Integer value, String unit, Integer unitType) {
        this.value = value;
        this.unit = unit;
        this.unitType = unitType;
    }

    public MetricTemperature() {
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }
}
