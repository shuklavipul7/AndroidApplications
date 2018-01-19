package com.mad.group16.chitchat;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

/**
 * Created by vipul on 3/27/2017.
 */
/*
* Vipul Shukla
* Shanmukh Anand*/

public class APICallingAsyncTask extends AsyncTask<String, Void, String> {
    LoadData loadData;

    public APICallingAsyncTask(LoadData loadData) {
        this.loadData = loadData;
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);
        loadData.loadData(token);
    }

    @Override
    protected String doInBackground(String... params) {
        if (params[0].equals(MainActivity.SIGNUP)) {
            APICallUsingHttpOK apiCallUsingHttpOK = new APICallUsingHttpOK();
            User user = new User();
            user.setEmail(params[1]);
            user.setPassword(params[2]);
            user.setfName(params[3]);
            user.setlName(params[4]);

            try {
                SignupResponse response = apiCallUsingHttpOK.signup(user);
                if(response.getStatus()==1){
                    return response.getData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(params[0].equals(MainActivity.LOGIN)){
            APICallUsingHttpOK apiCallUsingHttpOK = new APICallUsingHttpOK();
            User user = new User();
            user.setEmail(params[1]);
            user.setPassword(params[2]);

            try {
                SignupResponse response = apiCallUsingHttpOK.login(user);
                if(response.getStatus()==1){
                    return response.getData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public interface LoadData{
        public void loadData(String token);
    }
}
