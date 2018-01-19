package group16.mad.com.triviaapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
* Assignment #04
* MainActivity.java
* Vipul Shukla, Shanmukh Anand
*
* */

public class MainActivity extends AppCompatActivity {
    ArrayList<Question> questionBank;
    public static final String QUESTION_BANK = "questionBank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean status = isOnline();
        if (status==true) {
            TriviaDataLoader triviaDataLoader = new TriviaDataLoader(this);
            triviaDataLoader.execute("http://dev.theappsdr.com/apis/trivia_json/index.php");
        } else {
            Toast toast = Toast.makeText(this,"Internet not available",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    void exitApp(View view) {
        finish();
    }

    void applicationReady(ArrayList<Question> questionList) {
        questionBank = questionList;
        ImageView triviaLogo = (ImageView) findViewById(R.id.triviaLogoId);
        TextView triviaReadyText = (TextView) findViewById(R.id.triviaReadyTextId);
        Button startTriviaButton = (Button) findViewById(R.id.startTriviaButtonID);
        startTriviaButton.setBackgroundColor(getResources().getColor(R.color.aqua));
        triviaLogo.setVisibility(View.VISIBLE);
        triviaReadyText.setVisibility(View.VISIBLE);
    }

    void startTrivia(View view) {
        Intent intent = new Intent(this, TriviaActivity.class);
        intent.putParcelableArrayListExtra(QUESTION_BANK, questionBank);
        startActivity(intent);
    }
}
