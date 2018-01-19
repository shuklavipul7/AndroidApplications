package com.mad.group16.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class CityWeatherActivity extends AppCompatActivity implements GetFiveDayForecastAsyncTask.LoadFiveDayWeather {
    String key;
    ProgressDialog loading;
    String city;
    String country;
    SharedPreferences sharedPreferences;
    FiveDayWeather fiveDayWeather;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        sharedPreferences = getSharedPreferences("weatherapp", MODE_PRIVATE);
        key = (String) getIntent().getExtras().get(MainActivity.KEY);
        city = (String) getIntent().getExtras().get(MainActivity.CITY);
        country = (String) getIntent().getExtras().get(MainActivity.COUNTRY);
        loading = new ProgressDialog(CityWeatherActivity.this);
        loading.show();
        loadFiveDayWeather(key);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cityweathermenu, menu);
        return true;
    }

    void loadFiveDayWeather(String key) {
        GetFiveDayForecastAsyncTask getFiveDayForecastAsyncTask = new GetFiveDayForecastAsyncTask(this);
        getFiveDayForecastAsyncTask.execute(key);
    }

    @Override
    public void loadFiveDayWeather(final FiveDayWeather fiveDayWeather) {
        this.fiveDayWeather = fiveDayWeather;
        TextView dailyForecastForText = (TextView) findViewById(R.id.dailyForecastForText);
        TextView headLineText = (TextView) findViewById(R.id.headLineTextId);
        TextView forecastText = (TextView) findViewById(R.id.forecastTextId);
        TextView temperatureText = (TextView) findViewById(R.id.temperatureText);
        ImageView dayImage = (ImageView) findViewById(R.id.dayWeatherImageId);
        ImageView nightImage = (ImageView) findViewById(R.id.nightWeatherImageId);
        TextView dayWeatherPredictionText = (TextView) findViewById(R.id.dayWeatherPredictionText);
        TextView nightWeatherPredictionText = (TextView) findViewById(R.id.nightWeatherPredictionText);
        TextView clickHereForMoreDetailsText = (TextView) findViewById(R.id.clickHereForMoreDetailsText);
        TextView clickHereForExtendedForecastText = (TextView) findViewById(R.id.clickHereForExtendedForecastText);
        RecyclerView gridView = (RecyclerView) findViewById(R.id.recyclerViewForFiveDayForecast);


        SimpleDateFormat sd = new SimpleDateFormat("MMM d, yyyy");
        char degreeSymbol = (char) 0x00B0;


        dailyForecastForText.setText("Daily forecast for " + city + ", " + country);
        headLineText.setText(fiveDayWeather.getHeadline());
        forecastText.setText("Forecast on " + sd.format(fiveDayWeather.getDailyWeather().get(0).getDate()));

        MetricTemperature maxWeatherToDisplay = new MetricTemperature();
        MetricTemperature minWeatherToDisplay = new MetricTemperature();
        TemperatureConvertor temperatureConvertor = new TemperatureConvertor();

        String temperatureUnitToDisplay = sharedPreferences.getString(MainActivity.TEMPERATURE_UNIT, null);
        if (temperatureUnitToDisplay != null && temperatureUnitToDisplay.equalsIgnoreCase(MainActivity.FAHRENHEIT)) {
            maxWeatherToDisplay = fiveDayWeather.getDailyWeather().get(0).getMaximumTemperature();
            minWeatherToDisplay = fiveDayWeather.getDailyWeather().get(0).getMinimumTemperature();
        } else {
            maxWeatherToDisplay = temperatureConvertor.convertFromFahrenheitToCelsius(fiveDayWeather.getDailyWeather().get(0).getMaximumTemperature());
            minWeatherToDisplay = temperatureConvertor.convertFromFahrenheitToCelsius(fiveDayWeather.getDailyWeather().get(0).getMinimumTemperature());
        }

        //temperatureText.setText("      Temperature: "+fiveDayWeather.getDailyWeather().get(0).getMaximumTemperature().getValue()+degreeSymbol+"/"+fiveDayWeather.getDailyWeather().get(0).getMinimumTemperature().getValue()+degreeSymbol);

        temperatureText.setText("      Temperature: " + maxWeatherToDisplay.getValue() + degreeSymbol + "/" + minWeatherToDisplay.getValue() + degreeSymbol);

        String dayIcon = fiveDayWeather.getDailyWeather().get(0).getDayIcon();
        String dayImageUrl = "http://developer.accuweather.com/sites/default/files/" + dayIcon + "-s.png";

        String nightIcon = fiveDayWeather.getDailyWeather().get(0).getNightIcon();
        String nightImageUrl = "http://developer.accuweather.com/sites/default/files/" + nightIcon + "-s.png";

        Picasso.with(this).load(dayImageUrl).into(dayImage);
        Picasso.with(this).load(nightImageUrl).into(nightImage);

        dayWeatherPredictionText.setText(fiveDayWeather.getDailyWeather().get(0).getDayPhase());
        nightWeatherPredictionText.setText(fiveDayWeather.getDailyWeather().get(0).getNightphase());

        clickHereForExtendedForecastText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fiveDayWeather.getExtendedWeatherForecastUrl()));
                startActivity(browserIntent);
            }
        });

        clickHereForMoreDetailsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fiveDayWeather.getDailyWeather().get(0).getDailyWeatherForecastUrl()));
                startActivity(browserIntent);
            }
        });

        ItemGridAdapter itemAdapter = new ItemGridAdapter(CityWeatherActivity.this, fiveDayWeather.getDailyWeather());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);

        gridView.setLayoutManager(mLayoutManager);
        gridView.setItemAnimator(new DefaultItemAnimator());
        gridView.setAdapter(itemAdapter);

        loading.dismiss();
    }

    void openSettings(MenuItem menuItem) {
        Intent intent = new Intent(this, MainMenuPreference.class);
        startActivity(intent);
    }

    void saveCity(MenuItem menuItem){
        /*Cities cityToBeUpdated = new Cities();
        cityToBeUpdated.setFavourite(false);
        cityToBeUpdated.setCityName(city);
        cityToBeUpdated.setCityKey(key);
        cityToBeUpdated.setCountry(country);
        cityToBeUpdated.setMetricTemperature(fiveDayWeather.getDailyWeather().get(0).get);

        mDatabase.child("Cities").child(key).removeValue();
        mDatabase.child("Cities").child(key).setValue();*/
        Toast.makeText(this, "City Updated",Toast.LENGTH_SHORT).show();
    }

    void setAsCurrentCity(MenuItem menuItem){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString(MainActivity.KEY, null) != null) {
            editor.putString(MainActivity.CITY, city);
            editor.putString(MainActivity.COUNTRY, country);
            editor.putString(MainActivity.KEY, key);
            editor.commit();
            Toast.makeText(this,"Current City Updated",Toast.LENGTH_SHORT).show();
        }else{
            editor.putString(MainActivity.CITY, city);
            editor.putString(MainActivity.COUNTRY, country);
            editor.putString(MainActivity.KEY, key);
            editor.commit();
            Toast.makeText(this,"Current City Saved",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String preference_temperature = settings.getString("preference_temperature", "");
        sharedPreferences.edit().putString(MainActivity.TEMPERATURE_UNIT,preference_temperature).commit();
        if(fiveDayWeather!=null) {
            loadFiveDayWeather(fiveDayWeather);
        }
    }
}
