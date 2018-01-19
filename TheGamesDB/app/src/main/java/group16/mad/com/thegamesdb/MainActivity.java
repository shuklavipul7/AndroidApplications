package group16.mad.com.thegamesdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/*
Assignment #Homework 5.
MainActivity.java
Vipul Shukla, Shanmukh Anand*/

public class MainActivity extends AppCompatActivity implements GetGamesListAsync.GameListInterface {

    List<Game> gameListToDisplay;
    ProgressDialog loading;
    RadioGroup gameRadioGroup;
    RadioButton selectedRadioButton;
    public static String GAME_ID = "gameID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void searchGames(View view) {
        EditText gameToSearch = (EditText) findViewById(R.id.gameNameId);
        if (gameToSearch != null && gameToSearch.length() > 0) {

            URLGenerator urlGenerator = new URLGenerator("http://thegamesdb.net/api/GetGamesList.php");
            urlGenerator.addParams("name", gameToSearch.getText().toString());
            String url = urlGenerator.makeUrl();

            //String url = makeUrl(gameToSearch.getText().toString());
            loading = new ProgressDialog(this);
            loading.setCancelable(false);
            loading.show();
            new GetGamesListAsync(this).execute(url);
        } else {
            Toast.makeText(this, "Enter Game Name to Search", Toast.LENGTH_SHORT).show();
        }
    }

    String makeUrl(String name) {
        String baseUrl = "http://thegamesdb.net/api/GetGamesList.php";
        String urlToReturn = "";
        String encodedName = "";
        try {
            encodedName = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlToReturn = baseUrl + "?name=" + encodedName;
        return urlToReturn;
    }

    @Override
    public void loadData(List<Game> gameList) {
        this.gameListToDisplay = null;
        this.gameListToDisplay = gameList;

        gameRadioGroup = (RadioGroup) findViewById(R.id.gameRadioGroupId);
        gameRadioGroup.removeAllViews();

        if (gameList != null && gameList.size() > 0) {
            for (Game game : gameList) {
                RadioButton gameRadioButton = new RadioButton(this);
                gameRadioButton.setId(game.getId());
                gameRadioButton.setText(game.getTitle() + ". Released in " + game.getReleaseYear() + ". Platform: " + game.getPlatform());
                gameRadioGroup.addView(gameRadioButton);
            }
        } else {
            Toast.makeText(this, "No Games Found", Toast.LENGTH_SHORT).show();
        }

        loading.dismiss();

        gameRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Button goButton = (Button) findViewById(R.id.goButtonId);
                if (goButton.isEnabled() == false) {
                    goButton.setEnabled(true);
                    goButton.setBackgroundColor(Color.CYAN);
                }
                selectedRadioButton = (RadioButton) findViewById(gameRadioGroup.getCheckedRadioButtonId());
            }
        });
    }

    void showGameDetails(View view) {
        Intent intent = new Intent(this, GameDetails.class);
        intent.putExtra(GAME_ID, selectedRadioButton.getId());
        startActivity(intent);
    }

}
