package com.mad.group16.weatherapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Vipul.Shukla on 4/7/2017.
 */

public class GetFiveDayForecastAsyncTask extends AsyncTask<String, Void, FiveDayWeather> {
    private final OkHttpClient client = new OkHttpClient();
    LoadFiveDayWeather loadFiveDayWeather;

    public GetFiveDayForecastAsyncTask(LoadFiveDayWeather loadFiveDayWeather) {
        this.loadFiveDayWeather = loadFiveDayWeather;
    }

    @Override
    protected void onPostExecute(FiveDayWeather fiveDayWeather) {
        super.onPostExecute(fiveDayWeather);
        loadFiveDayWeather.loadFiveDayWeather(fiveDayWeather);
    }

    @Override
    protected FiveDayWeather doInBackground(String... params) {
        String cityKey = params[0];
        return getFiveDayWeather(cityKey);

    }

    FiveDayWeather getFiveDayWeather(String cityKey) {
        FiveDayWeather fiveDayWeather = null;
        Response response = null;
        JSONObject root;

        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + cityKey + "?apikey=rOm7lXUbAW4fqY5wqXe1K2dcetXmfSg4&q")
                .build();

        try {
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            try {
                root = new JSONObject(response.body().string());
                if (root.length() > 0) {
                    fiveDayWeather = parseJsonAndReturnFiveDayWeather(root);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fiveDayWeather;
    }

    FiveDayWeather parseJsonAndReturnFiveDayWeather(JSONObject root) throws JSONException, ParseException {
        FiveDayWeather fiveDayWeather = new FiveDayWeather();
        ArrayList<OneDayWeather> dailyWeatherList = new ArrayList<OneDayWeather>();


        fiveDayWeather.setHeadline(root.getJSONObject("Headline").getString("Text"));
        fiveDayWeather.setExtendedWeatherForecastUrl(root.getJSONObject("Headline").getString("MobileLink"));
        JSONArray dailyForecast = root.getJSONArray("DailyForecasts");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

        for (int i = 0; i < dailyForecast.length(); i++) {
            OneDayWeather oneDayWeather = new OneDayWeather();
            MetricTemperature minimumTemperature = new MetricTemperature();
            MetricTemperature maximumTemperature = new MetricTemperature();
            Integer dayIcon = 0;
            Integer nightIcon = 0;

            oneDayWeather.setDate(sd.parse(dailyForecast.getJSONObject(i).getString("Date")));

            minimumTemperature.setValue(dailyForecast.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum").getInt("Value"));
            minimumTemperature.setUnit(dailyForecast.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum").getString("Unit"));
            minimumTemperature.setUnitType(dailyForecast.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum").getInt("UnitType"));
            oneDayWeather.setMinimumTemperature(minimumTemperature);

            maximumTemperature.setValue(dailyForecast.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum").getInt("Value"));
            maximumTemperature.setUnit(dailyForecast.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum").getString("Unit"));
            maximumTemperature.setUnitType(dailyForecast.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum").getInt("UnitType"));
            oneDayWeather.setMaximumTemperature(maximumTemperature);

            dayIcon = dailyForecast.getJSONObject(i).getJSONObject("Day").getInt("Icon");
            if (dayIcon < 10) {
                oneDayWeather.setDayIcon("0" + dayIcon);
            } else {
                oneDayWeather.setDayIcon("" + dayIcon);
            }

            oneDayWeather.setDayPhase(dailyForecast.getJSONObject(i).getJSONObject("Day").getString("IconPhrase"));

            nightIcon = dailyForecast.getJSONObject(i).getJSONObject("Night").getInt("Icon");
            if (nightIcon < 10) {
                oneDayWeather.setNightIcon("0" + nightIcon);
            } else {
                oneDayWeather.setNightIcon("" + nightIcon);
            }

            oneDayWeather.setNightphase(dailyForecast.getJSONObject(i).getJSONObject("Night").getString("IconPhrase"));
            dailyWeatherList.add(oneDayWeather);

            oneDayWeather.setDailyWeatherForecastUrl(dailyForecast.getJSONObject(i).getString("MobileLink"));
        }
        fiveDayWeather.setDailyWeather(dailyWeatherList);
        return fiveDayWeather;
    }

    interface LoadFiveDayWeather{
        void loadFiveDayWeather(FiveDayWeather fiveDayWeather);
    }
}
