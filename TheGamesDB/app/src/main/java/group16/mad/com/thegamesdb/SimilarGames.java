package group16.mad.com.thegamesdb;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

/*
Assignment #Homework 5.
SimilarGames.java
Vipul Shukla, Shanmukh Anand*/

public class SimilarGames extends AppCompatActivity implements GameListDetailsAsync.GameDetailsInterface {
    ProgressDialog progressDialog;
    List<Game> gamesToDisplay = new ArrayList<>();
    Game similarGame;
    Game thisGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);

        similarGame = (Game) getIntent().getExtras().get(GameDetails.SIMILAR_GAMES);

        List<Integer> similarIds = similarGame.getSimilarGamesId();
        similarGame = similarGame;

        URLGenerator urlGenerator = new URLGenerator("http://thegamesdb.net/api/GetGame.php");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        String[] urlList = new String[similarIds.size()];

        for (int i = 0; i < similarIds.size(); i++) {
            urlGenerator.addParams("id", similarIds.get(i).toString());
            String url = urlGenerator.makeUrl();
            urlList[i] = url;
        }
        GameListDetailsAsync gameListDetailsAsync = new GameListDetailsAsync(this);
        gameListDetailsAsync.execute(urlList);
    }

    @Override
    public void loadData(List<Game> gameList) {
        TextView gameName = (TextView) findViewById(R.id.gameName);
        gameName.setText("Similar games to "+thisGame.getTitle());
        gamesToDisplay = gameList;
        ListView similarGamesList = (ListView) findViewById(R.id.similarGamesList);
        ArrayAdapter<Game> adapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_1, gamesToDisplay);
        similarGamesList.setAdapter(adapter);
        progressDialog.dismiss();
    }

    void finishActivity(View view) {
        finish();
    }
}
