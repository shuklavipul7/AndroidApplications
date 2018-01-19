package com.mad.group16.chitchat;

import android.os.AsyncTask;
import android.widget.Toast;

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

public class SubscribeToChannelAsyncTask extends AsyncTask<Request, Void, Void>{

    private final OkHttpClient client = new OkHttpClient();
    Subscribed Subscribed;

    public SubscribeToChannelAsyncTask(SubscribeToChannelAsyncTask.Subscribed subscribed) {
        Subscribed = subscribed;
    }

    @Override
    protected Void doInBackground(Request... params) {
        try {
            Response response = client.newCall(params[0]).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    interface Subscribed{
        void subscribed();
    }
}
