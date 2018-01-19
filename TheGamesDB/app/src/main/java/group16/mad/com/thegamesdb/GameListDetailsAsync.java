package group16.mad.com.thegamesdb;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Vipul.Shukla on 2/19/2017.
 */

/*
Assignment #Homework 5.
GameListDetailsAsync.java
Vipul Shukla, Shanmukh Anand*/

public class GameListDetailsAsync extends AsyncTask<String, Void, List<Game>> {

    GameDetailsInterface activity;

    public GameListDetailsAsync(GameDetailsInterface activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<Game> gameList) {
        super.onPostExecute(gameList);
        this.activity.loadData(gameList);
    }

    @Override
    protected List<Game> doInBackground(String... params) {
        List<Game> gameList = new ArrayList<>();
        List<InputStream> inputStreamList= new ArrayList<>();
        try {
            for (String param : params) {
                URL url = new URL(param);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int statusCode = conn.getResponseCode();
                if (statusCode == HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    inputStreamList.add(in);
                }
            }
            gameList = GamesUtil.GamePullParser.parseMultipleGames(inputStreamList);
            return gameList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    interface GameDetailsInterface {
        void loadData(List<Game> game);
    }
}

