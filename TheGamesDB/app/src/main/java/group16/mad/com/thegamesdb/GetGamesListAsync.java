package group16.mad.com.thegamesdb;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Vipul.Shukla on 2/16/2017.
 */

/*
Assignment #Homework 5.
GetGamesListAsync.java
Vipul Shukla, Shanmukh Anand*/

public class GetGamesListAsync extends AsyncTask<String, Void, List<Game>> {

    GameListInterface activity;

    public GetGamesListAsync(GameListInterface activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<Game> games) {
        super.onPostExecute(games);
        this.activity.loadData(games);
    }

    @Override
    protected List<Game> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int statusCode = conn.getResponseCode();
            if(statusCode==HTTP_OK){
                InputStream in = conn.getInputStream();
                return GamesUtil.GamePullParser.parseGameList(in);
            }
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

    interface GameListInterface{
        void loadData(List<Game> gameList);
    }
}
