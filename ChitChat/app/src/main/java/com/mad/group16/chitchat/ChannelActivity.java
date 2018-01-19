package com.mad.group16.chitchat;
/*
* Vipul Shukla
* Shanmukh Anand*/

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class ChannelActivity extends AppCompatActivity implements GetAllChannelsAsyncTask.LoadAllChannelData, GetSubscribedChannelsAsyncTask.LoadSubscribedChannelData, SubscribeToChannelAsyncTask.Subscribed {
    private final OkHttpClient client = new OkHttpClient();
    SharedPreferences sharedPreferences;
    ArrayList<Channel> listOfAllChannels = new ArrayList<Channel>();
    ArrayList<Channel> listOfSubscribedChannels = new ArrayList<Channel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        sharedPreferences = getSharedPreferences("chitchatApp", MODE_PRIVATE);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ImageButton logout = (ImageButton) mCustomView.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });

        getListOfSubscribedChannels();
    }

    void getListOfSubscribedChannels() {
        String token = sharedPreferences.getString(MainActivity.TOKEN, null);
        if (token != null) {
            GetSubscribedChannelsAsyncTask getChannelsAsyncTask = new GetSubscribedChannelsAsyncTask(this);
            getChannelsAsyncTask.execute(token);
        }
    }

    void getAllChannels() {
        sharedPreferences = getSharedPreferences("chitchatApp", MODE_PRIVATE);
        String token = sharedPreferences.getString(MainActivity.TOKEN, null);

        GetAllChannelsAsyncTask getChannelsAsyncTask = new GetAllChannelsAsyncTask(this);
        getChannelsAsyncTask.execute(token);
    }

    void addMore(View view) {
        Button addMoreButton = (Button) findViewById(R.id.addMoreButton);
        Button doneButton = (Button) findViewById(R.id.DoneButton);


        addMoreButton.setVisibility(View.INVISIBLE);
        doneButton.setVisibility(View.VISIBLE);

        getAllChannels();
    }

    @Override
    public void loadAllChannelData(ArrayList<Channel> channelList) {
        listOfAllChannels = channelList;
        CustomAdapter2 noteRowAdapter2 = new CustomAdapter2(this, R.layout.layout_row, listOfAllChannels);
        ListView listView = (ListView) findViewById(R.id.listViewChannels);
        listView.setAdapter(noteRowAdapter2);
    }

    @Override
    public void loadSubscribedChannelData(ArrayList<Channel> channelList) {
        listOfSubscribedChannels = channelList;
        CustomAdapter noteRowAdapter = new CustomAdapter(this, R.layout.layout_row, listOfSubscribedChannels);
        ListView listView = (ListView) findViewById(R.id.listViewChannels);
        listView.setAdapter(noteRowAdapter);
    }

    @Override
    public void subscribed() {
        Toast.makeText(this, "Subscription Complete", Toast.LENGTH_SHORT);
    }
}
