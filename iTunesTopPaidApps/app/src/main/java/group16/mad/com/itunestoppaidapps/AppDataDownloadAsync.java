package group16.mad.com.itunestoppaidapps;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Vipul.Shukla on 2/22/2017.
 */
/*
Assignment#Homework 06.
File Name: AppDataDownloadAsync.java
Vipul Shukla, Shanmukh Anand
*/

public class AppDataDownloadAsync extends AsyncTask<String, Void, List<App>> {

    ActivityDataLoader activity;

    public AppDataDownloadAsync(ActivityDataLoader activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<App> apps) {
        super.onPostExecute(apps);
        activity.dataLoader(apps);
    }

    @Override
    protected List<App> doInBackground(String... params) {
        String urlString = params[0];
        List<App> appList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            }

            try {
                JSONObject root = new JSONObject(sb.toString());
                JSONArray entry = root.getJSONObject("feed").getJSONArray("entry");
                String appName = "";
                String appImageUrl;
                String appPrice;

                for (int i = 0; i < entry.length(); i++) {
                    App app = new App();
                    appName = entry.getJSONObject(i).getJSONObject("im:name").getString("label");
                    appImageUrl = entry.getJSONObject(i).getJSONArray("im:image").getJSONObject(0).getString("label");
                    appPrice = entry.getJSONObject(i).getJSONObject("im:price").getString("label");
                    app.setAppName(appName);
                    app.setImageUrl(appImageUrl);
                    app.setPrice(appPrice);

                    appList.add(app);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appList;
    }

    interface ActivityDataLoader{
        void dataLoader(List<App> appList);
    }
}
