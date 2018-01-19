package com.mad.group16.weatherapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vipul on 4/4/2017.
 */

public class GetCurrentCityAsyncTask extends AsyncTask<String, Void, Location> {
    private final OkHttpClient client = new OkHttpClient();
    LoadLocation loadLocation;

    public GetCurrentCityAsyncTask(LoadLocation loadLocation) {
        this.loadLocation = loadLocation;
    }

    @Override
    protected void onPostExecute(Location location) {
        super.onPostExecute(location);
        loadLocation.loadLocation(location);
    }

    @Override
    protected Location doInBackground(String... params) {
        String city = params[0];
        String country = params[1];
        return getLocation(city, country);
    }

    Location getLocation(String city, String country) {
        Response response = null;
        JSONArray root = null;
        Location location = null;
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/locations/v1/" + country + "/search?apikey=rOm7lXUbAW4fqY5wqXe1K2dcetXmfSg4&q=" + city)
                .build();

        try {
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            try {
                root = new JSONArray(response.body().string());
                if(root.length()>0) {
                    location = parseJsonAndReturnLocation(root);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    Location parseJsonAndReturnLocation(JSONArray root) throws JSONException {
        String city = root.getJSONObject(0).getString("LocalizedName");
        String countryId = root.getJSONObject(0).getJSONObject("Country").getString("ID");
        String key = root.getJSONObject(0).getString("Key");
        Location location = new Location(city, countryId, key);
        return location;
    }

    interface LoadLocation {
        void loadLocation(Location location);
    }
}
