package group16.mad.com.itunestoppaidapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Assignment#Homework 06.
File Name: MainActivity.java
Vipul Shukla, Shanmukh Anand
*/

public class MainActivity extends AppCompatActivity implements AppDataDownloadAsync.ActivityDataLoader {
    ProgressDialog loading;
    List<App> listApps;
    public static final String FAVOURITES = "favourites";
    SharedPreferences  sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDataDownloadAsync appDataDownloadAsync = new AppDataDownloadAsync(this);
        appDataDownloadAsync.execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2, menu);
        return true;
    }


    @Override
    public void dataLoader(final List<App> appList) {
        this.listApps = appList;
        ListView appListView = (ListView) findViewById(R.id.appList);
        AppAdapter appAdapter = new AppAdapter(this, R.layout.list_row, appList);
        appListView.setAdapter(appAdapter);
        loading.dismiss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        AppDataDownloadAsync appDataDownloadAsync = new AppDataDownloadAsync(this);
        appDataDownloadAsync.execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");

        loading = new ProgressDialog(this);
        loading.setCancelable(false);

    }

    void refresh(MenuItem item) {
        AppDataDownloadAsync appDataDownloadAsync = new AppDataDownloadAsync(this);
        appDataDownloadAsync.execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
    }

    void sortIncreasingly(MenuItem item) {
        List<App> listOfApps = this.listApps;

        Collections.sort(listOfApps, new Comparator<App>() {
            public int compare(App o1, App o2) {
                Double priceOfApp1 = Double.parseDouble(o1.getPrice().substring(1));
                Double priceOfApp2 = Double.parseDouble(o2.getPrice().substring(1));
                return priceOfApp1.compareTo(priceOfApp2);
            }
        });

        ListView appListView = (ListView) findViewById(R.id.appList);
        AppAdapter appAdapter = new AppAdapter(this, R.layout.list_row, listOfApps);
        appListView.setAdapter(appAdapter);
    }

    void sortDecreasingly(MenuItem item) {
        final List<App> listOfApps = this.listApps;

        Collections.sort(listOfApps, new Comparator<App>() {
            public int compare(App o1, App o2) {
                Double priceOfApp1 = Double.parseDouble(o1.getPrice().substring(1));
                Double priceOfApp2 = Double.parseDouble(o2.getPrice().substring(1));
                return priceOfApp2.compareTo(priceOfApp1);
            }
        });

        ListView appListView = (ListView) findViewById(R.id.appList);
        AppAdapter appAdapter = new AppAdapter(this, R.layout.list_row, listOfApps);
        appListView.setAdapter(appAdapter);
    }

    void showFavourites(MenuItem item){
        sharedPreferences = getSharedPreferences("favouriteApps",MODE_PRIVATE);
        Set<String> appHashSet = sharedPreferences.getStringSet(AppAdapter.APP_FAVOURITES_SHARED_PREFERENCES_SET,new HashSet<String>());
        ArrayList<App> favouriteApps = new ArrayList<>();
        if(appHashSet.size()>0)
        {
            Intent intent = new Intent(this, FavouritesActivity.class);

            for(App app : listApps){
                if(appHashSet.contains(app.appName)){
                    favouriteApps.add(app);
                }
            }

            intent.putExtra(FAVOURITES,favouriteApps);
            startActivity(intent);
        } else{
            Toast.makeText(this,"No Favourite Apps Found",Toast.LENGTH_SHORT).show();
        }
    }
}
