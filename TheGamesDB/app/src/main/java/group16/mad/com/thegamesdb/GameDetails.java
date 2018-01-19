package group16.mad.com.thegamesdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
Assignment #Homework 5.
GameDetails.java
Vipul Shukla, Shanmukh Anand*/

public class GameDetails extends AppCompatActivity implements GameDetailsAsync.GameDetailsInterface {
    Integer gameId = 0;
    ProgressDialog loading;
    public static String GAME_VIDEO_URL = "gameVideoUrl";
    public static String SIMILAR_GAMES = "similarGames";
    String videoUrl = "";
    List<Integer> similarGamesId = new ArrayList<>();
    Game thisGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        gameId = (Integer) getIntent().getExtras().get(MainActivity.GAME_ID);

        URLGenerator urlGenerator = new URLGenerator("http://thegamesdb.net/api/GetGame.php");
        urlGenerator.addParams("id", gameId.toString());
        String url = urlGenerator.makeUrl();

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.show();

        GameDetailsAsync gameDetailsAsync = new GameDetailsAsync(this);
        gameDetailsAsync.execute(url);
    }

    @Override
    public void loadData(Game game) {
        if (game != null) {
            this.thisGame = game;
            TextView gameName = (TextView) findViewById(R.id.gameNameId);
            ImageView imageView = (ImageView) findViewById(R.id.gameImage);
            LinearLayout overviewLayout = (LinearLayout) findViewById(R.id.overviewLayout);
            LinearLayout genrePublisherLayout = (LinearLayout) findViewById(R.id.genrePublisherLayout);

            TextView overviewDesc = new TextView(this);
            TextView genre = new TextView(this);
            TextView publisher = new TextView(this);


            gameName.setText(game.getTitle());
            Picasso.with(this).load(game.getImageUrl()).into(imageView);
            overviewDesc.setText(game.getOverview());
            genre.setText("Genre: " + game.getGenre());
            publisher.setText("Publisher: " + game.getPublisher());

            overviewLayout.addView(overviewDesc);
            genrePublisherLayout.addView(genre);
            genrePublisherLayout.addView(publisher);

            genre.setTextColor(Color.BLACK);
            publisher.setTextColor(Color.BLACK);

            videoUrl = game.getYoutubeUrl();
            similarGamesId = game.getSimilarGamesId();

            loading.dismiss();
        } else {
            loading.dismiss();
            Toast.makeText(this, "Unable to load game details. Please try again.", Toast.LENGTH_SHORT);
        }
    }

    void finishActivity(View view) {
        finish();
    }

    void playGameTrailer(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(GAME_VIDEO_URL, videoUrl);
        startActivity(intent);
    }

    void displaySimilarGames(View view) {
        Intent intent = new Intent(this, SimilarGames.class);
        String gameIds = "";


        if (thisGame.getSimilarGamesId().size()>0) {
            intent.putExtra(SIMILAR_GAMES, thisGame);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Similar games not found", Toast.LENGTH_SHORT).show();
        }
    }
}
