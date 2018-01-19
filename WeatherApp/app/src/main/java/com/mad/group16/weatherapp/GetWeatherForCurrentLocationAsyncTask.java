package com.mad.group16.weatherapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vipul on 4/4/2017.
 */

public class GetWeatherForCurrentLocationAsyncTask extends AsyncTask<String, Void, Weather>{
    private final OkHttpClient client = new OkHttpClient();
    LoadWeather loadWeather;

    public GetWeatherForCurrentLocationAsyncTask(LoadWeather loadWeather) {
        this.loadWeather = loadWeather;
    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        loadWeather.loadWeather(weather);
    }

    @Override
    protected Weather doInBackground(String... params) {
        String key = params[0];

        return getWeather(key);
    }

    Weather getWeather(String key){
        Response response = null;
        JSONArray root = null;
        Weather weather = null;
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=rOm7lXUbAW4fqY5wqXe1K2dcetXmfSg4")
                .build();

        try {
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            try {
                root = new JSONArray(response.body().string());
                weather = parseJsonAndReturnWeather(root);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

    Weather parseJsonAndReturnWeather(JSONArray root) throws JSONException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

        Date localObservationDateTime = simpleDateFormat.parse(root.getJSONObject(0).getString("LocalObservationDateTime"));
        String weatherText = root.getJSONObject(0).getString("WeatherText");
        Integer weatherIconTemp = root.getJSONObject(0).getInt("WeatherIcon");
        String weatherIcon=new String();
        if(weatherIconTemp<10){
            weatherIcon="0"+weatherIconTemp;
        }else{
            weatherIcon = ""+weatherIconTemp;
        }

        Integer value = root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Metric").getInt("Value");
        String unit = root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Metric").getString("Unit");
        Integer unitType = root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Metric").getInt("UnitType");
        MetricTemperature metricTemperature = new MetricTemperature(value, unit, unitType);
        Weather weather = new Weather(localObservationDateTime, weatherText, weatherIcon, metricTemperature);
        return weather;
    }

    interface LoadWeather{
        void loadWeather(Weather weather);
    }
}
