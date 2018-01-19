package com.mad.group16.chitchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vipul on 3/27/2017.
 */
/*
* Vipul Shukla
* Shanmukh Anand*/

public class CustomAdapter2 extends ArrayAdapter {
    Context mContext;
    int mResource;
    List mObjects;
    View mConvertView;
    SharedPreferences sharedPreferences;
    private final OkHttpClient client = new OkHttpClient();

    public CustomAdapter2 (Context context, int resource, List objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        sharedPreferences = mContext.getSharedPreferences("chitchatApp", MODE_PRIVATE);
        final String token = sharedPreferences.getString(MainActivity.TOKEN,null);

        Channel channel = (Channel) mObjects.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        mConvertView = convertView;
        final TextView channelName = (TextView) convertView.findViewById(R.id.channelName);
        final Button view = (Button)  convertView.findViewById(R.id.viewButton);
        final Button join = (Button)  convertView.findViewById(R.id.joinButton);

        channelName.setText(channel.getChannelName());

        if(channel.isSubscribed==true){
            view.setVisibility(View.VISIBLE);
            join.setVisibility(View.INVISIBLE);
        }else{
            view.setVisibility(View.INVISIBLE);
            join.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.VISIBLE);
                join.setVisibility(View.INVISIBLE);

                RequestBody formBody = new FormBody.Builder()
                        .add("channel_id", ((Channel) mObjects.get(position)).getChannelID().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://localhost:8080/controller/api/subscribe/channel")
                        .header("Authorization", "BEARER " + token)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();

                SubscribeToChannelAsyncTask subscribeToChannelAsyncTask = new SubscribeToChannelAsyncTask((ChannelActivity)mContext);
                subscribeToChannelAsyncTask.execute(request);
            }
        });
        return convertView;


    }

}
