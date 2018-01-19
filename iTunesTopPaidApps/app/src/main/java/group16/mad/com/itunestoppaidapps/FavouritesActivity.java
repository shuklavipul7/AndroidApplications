package group16.mad.com.itunestoppaidapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/*
Assignment#Homework 06.
File Name: FavouritesActivity.java
Vipul Shukla, Shanmukh Anand
*/

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ArrayList<App> listOfFavouriteApps = (ArrayList<App>) getIntent().getExtras().get(MainActivity.FAVOURITES);

        ListView appListView = (ListView) findViewById(R.id.appList);
        FavouriteAppsAdapter favouriteAppsAdapter = new FavouriteAppsAdapter(this, R.layout.list_row, listOfFavouriteApps);
        appListView.setAdapter(favouriteAppsAdapter);
    }

}
