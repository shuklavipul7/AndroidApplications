package com.mad.group16.chitchat;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vipul on 3/27/2017.
 */
/*
* Vipul Shukla
* Shanmukh Anand*/

public class GetSubscribedChannelsAsyncTask extends AsyncTask<String, Void, ArrayList<Channel>> {
    private final OkHttpClient client = new OkHttpClient();
    SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();
    LoadSubscribedChannelData loadSubscribedChannelData;


    public GetSubscribedChannelsAsyncTask(LoadSubscribedChannelData loadChannelData) {
        this.loadSubscribedChannelData = loadChannelData;
    }

    @Override
    protected void onPostExecute(ArrayList<Channel> channels) {
        super.onPostExecute(channels);
        this.loadSubscribedChannelData.loadSubscribedChannelData(channels);
    }

    @Override
    protected ArrayList<Channel> doInBackground(String... params) {
        ArrayList<Channel> listOfChannels = new ArrayList<Channel>();

        String token = params[0];
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/subscriptions")
                .header("Authorization", "BEARER " + token)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            //JSONObject jsonObject = response.body().string();
            JSONObject obj = new JSONObject(response.body().string());
            JSONArray arr = obj.getJSONArray("data");

            for (int i = 0; i < arr.length(); i++) {
                Channel channel = new Channel();
                JSONObject obj1 = arr.getJSONObject(i);
                JSONObject obj2 = obj1.getJSONObject("channel");

                channel.setChannelID(obj2.getInt("channel_id"));
                channel.setChannelName(obj2.getString("channel_name"));
                listOfChannels.add(channel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listOfChannels;
    }

    public interface LoadSubscribedChannelData {
        public void loadSubscribedChannelData(ArrayList<Channel> channelList);
    }
}
