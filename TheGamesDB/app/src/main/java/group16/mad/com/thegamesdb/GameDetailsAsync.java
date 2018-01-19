package group16.mad.com.thegamesdb;


import android.os.AsyncTask;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Vipul.Shukla on 2/16/2017.
 */

/*
Assignment #Homework 5.
GameDetailsAsync.java
Vipul Shukla, Shanmukh Anand*/

public class GameDetailsAsync extends AsyncTask<String, Void, Game> {

    GameDetailsInterface activity;

    public GameDetailsAsync(GameDetailsInterface activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Game game) {
        super.onPostExecute(game);
        this.activity.loadData(game);
    }

    @Override
    protected Game doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int statusCode = conn.getResponseCode();
            if (statusCode == HTTP_OK) {
                InputStream in = conn.getInputStream();
                return GamesUtil.GamePullParser.parseGame(in);
            }else{
                return null;
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

    interface GameDetailsInterface {
        void loadData(Game game);
    }
}
