package com.mad.group16.weatherapp;

/**
 * Created by Vipul.Shukla on 4/8/2017.
 */

public class TemperatureConvertor {

    public MetricTemperature convertFromCelsiusToFahrenheit(MetricTemperature metricTemperature){
        MetricTemperature temperatureInFahrenheit = new MetricTemperature();
        double f=0;
        double c= metricTemperature.getValue();
        f = (9 * (c / 5)) + 32;
        temperatureInFahrenheit.setUnit("F");
        temperatureInFahrenheit.setValue((int)f);
        return temperatureInFahrenheit;
    }


    public MetricTemperature convertFromFahrenheitToCelsius(MetricTemperature metricTemperature){
        MetricTemperature temperatureInCelsius = new MetricTemperature();
        double c=0;
        double f= metricTemperature.getValue();
        c= ((f- 32) * 5) / 9;
        temperatureInCelsius.setUnit("F");
        temperatureInCelsius.setValue((int)c);
        return temperatureInCelsius;
    }
}
