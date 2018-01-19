package com.mad.group16.chitchat;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;

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

public class APICallUsingHttpOK {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        APICallUsingHttpOK a = new APICallUsingHttpOK();
        User user = new User();
        user.setEmail("user1@test.net");
        user.setPassword("123456");
        a.login(user);
    }


    public SignupResponse signup(User user) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("email", user.getEmail())
                .add("password", user.getPassword())
                .add("fname", user.getfName())
                .add("lname", user.getlName())
                .build();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/signUp")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        SignupResponse signupResponse = gson.fromJson(response.body().charStream(), SignupResponse.class);

        return signupResponse;
    }

    public SignupResponse login(User user) throws IOException{
        RequestBody formBody = new FormBody.Builder()
                .add("email", user.getEmail())
                .add("password", user.getPassword())
                .build();
        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/login")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        SignupResponse signupResponse = gson.fromJson(response.body().charStream(), SignupResponse.class);

        return signupResponse;
    }
}
