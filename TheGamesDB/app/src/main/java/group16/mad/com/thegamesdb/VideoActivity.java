package group16.mad.com.thegamesdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import static group16.mad.com.thegamesdb.GameDetails.GAME_VIDEO_URL;

/*
Assignment #Homework 5.
VideoActivity.java
Vipul Shukla, Shanmukh Anand*/

public class VideoActivity extends AppCompatActivity {
    String gameVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        TextView videoTrailerName = (TextView) findViewById(R.id.videoTrailerName);
        videoTrailerName.setText("Trailer");

        gameVideoUrl = (String) getIntent().getExtras().get(GAME_VIDEO_URL);

        WebView webview = (WebView) findViewById(R.id.gameVideo);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(gameVideoUrl);

    }

    void finishActivity(View view){
        finish();
    }
}
