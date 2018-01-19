package com.mad.group16.chitchat;
/*
* Vipul Shukla
* Shanmukh Anand*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements APICallingAsyncTask.LoadData {
    SharedPreferences sharedPreferences;
    public static final String SIGNUP = "signup";
    public static final String LOGIN = "login";
    public static final String TOKEN = "token";
    public static final String GET_ALL_CHANNELS = "getAllChannels";
    public static final String GET_SUBSCRIBED_CHANNELS = "getSubscribedChannels";
    public static final String SUBSCRIBED_CHENNELS_LIST = "subscribedChannelsList";
    ArrayList<Channel> listOfChannels = new ArrayList<Channel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        if (sharedPreferences.contains(TOKEN)) {
            //start the chats screen from here
            Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void loadData(String token) {
        if (token != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TOKEN, token);
            editor.commit();
            Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
            startActivity(intent);
        }
    }

    public void login(View view) {
        EditText emailLogin = (EditText) findViewById(R.id.emaiIdLogin);
        EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        APICallingAsyncTask apiCallingAsyncTask = new APICallingAsyncTask(this);
        String[] credentials = {LOGIN, emailLogin.getText().toString(), passwordLogin.getText().toString()};
        apiCallingAsyncTask.execute(credentials);
    }

    public void signup(View view) {
        EditText emailSignup = (EditText) findViewById(R.id.emaiIdSignup);
        EditText passwordSignup = (EditText) findViewById(R.id.passwordSignUp);
        EditText fName = (EditText) findViewById(R.id.firstNameId);
        EditText lName = (EditText) findViewById(R.id.lastNameId);

        APICallingAsyncTask apiCallingAsyncTask = new APICallingAsyncTask(this);
        String[] credentials = {SIGNUP, emailSignup.getText().toString(), passwordSignup.getText().toString(), fName.getText().toString(), lName.getText().toString()};
        apiCallingAsyncTask.execute(credentials);
    }

}
