package com.mad.group16.weatherapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GetCurrentCityAsyncTask.LoadLocation, GetWeatherForCurrentLocationAsyncTask.LoadWeather {
    ProgressDialog loadingLocation;
    ProgressDialog loadingWeather;
    SharedPreferences sharedPreferences;
    public static String CITY = "city";
    public static String COUNTRY = "country";
    public static String KEY = "key";
    public static String TEMPERATURE_UNIT = "temperatureUnitToDisplay";
    public static String CELSIUS="celsius";
    public static String FAHRENHEIT = "fahrenheit";
    Boolean searchButton = false;
    Cities cityDetails;
    private DatabaseReference mDatabase;
    RecyclerView cityList;
    Intent intentToStartCityWeatherActivity;
    Weather weatherDetails;

    Button setCurrentCityButton;
    TextView currentCityNotSetText;
    TextView locationText;
    TextView weatherText;
    ImageView weatherImage;
    TextView temperatureText;
    TextView updateTimeText;
    TextView noCitiesToDisplay;
    CityListAdapter cityListAdapter;
    RecyclerView recyclerView;

    ArrayList<Cities> savedCities = new ArrayList<Cities>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        loadingLocation = new ProgressDialog(MainActivity.this);

        cityList = (RecyclerView) findViewById(R.id.cityList);

        recyclerView = (RecyclerView) findViewById(R.id.cityList);

        Collections.sort(savedCities, new Comparator<Cities>() {
            @Override
            public int compare(Cities o1, Cities o2) {
                if (o1.getFavourite() == true && o2.getFavourite() == false) {
                    return -1;
                } else if (o1.getFavourite() == false && o2.getFavourite() == true) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        cityListAdapter = new CityListAdapter(savedCities, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cityListAdapter);

        setCurrentCityButton = (Button) findViewById(R.id.setCurrentCityButton);
        currentCityNotSetText = (TextView) findViewById(R.id.CurrentCityNotSetText);
        locationText = (TextView) findViewById(R.id.locationText);
        weatherText = (TextView) findViewById(R.id.weatherText);
        weatherImage = (ImageView) findViewById(R.id.weatherImage);
        temperatureText = (TextView) findViewById(R.id.temperatureText);
        updateTimeText = (TextView) findViewById(R.id.updateTimeText);
        noCitiesToDisplay = (TextView) findViewById(R.id.noCitiesToDisplay);

        sharedPreferences = getSharedPreferences("weatherapp", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (sharedPreferences.getString(KEY, null) != null) {
            setCurrentCityButton.setVisibility(View.INVISIBLE);
            currentCityNotSetText.setVisibility(View.INVISIBLE);
            locationText.setVisibility(View.VISIBLE);
            weatherText.setVisibility(View.VISIBLE);
            weatherImage.setVisibility(View.VISIBLE);
            temperatureText.setVisibility(View.VISIBLE);
            updateTimeText.setVisibility(View.VISIBLE);
            displayWeather();
        } else {
            setCurrentCityButton.setVisibility(View.VISIBLE);
            currentCityNotSetText.setVisibility(View.VISIBLE);
            locationText.setVisibility(View.INVISIBLE);
            weatherText.setVisibility(View.INVISIBLE);
            weatherImage.setVisibility(View.INVISIBLE);
            temperatureText.setVisibility(View.INVISIBLE);
            updateTimeText.setVisibility(View.INVISIBLE);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            ArrayList<Cities> cities = new ArrayList<Cities>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                savedCities.clear();
                recyclerView.removeAllViews();

                HashMap mapCities = (HashMap) dataSnapshot.getValue();
                HashMap citiesMap = new HashMap();
                if (mapCities != null) {
                    citiesMap = (HashMap) mapCities.get("Cities");
                }

                for (Object key : citiesMap.keySet()) {
                    HashMap cityMap = (HashMap) citiesMap.get((String) key);

                    Cities city = new Cities();
                    city.setCityName((String) cityMap.get("cityName"));
                    city.setFavourite((Boolean) cityMap.get("favourite"));
                    city.setCountry((String) cityMap.get("country"));
                    city.setCityKey((String) cityMap.get("cityKey"));
                    city.setLastUpdated((String) cityMap.get("lastUpdated"));
                    HashMap<String, String> metricTemperatureMap = (HashMap<String, String>) cityMap.get("metricTemperature");

                    Integer metricTemperatureValue = Integer.parseInt(String.valueOf(metricTemperatureMap.get("value")));
                    String metricTemperatureUnit = metricTemperatureMap.get("unit");
                    Integer metricTemperatureUnitType = Integer.parseInt(String.valueOf(metricTemperatureMap.get("unitType")));

                    MetricTemperature metricTemperature = new MetricTemperature(metricTemperatureValue, metricTemperatureUnit, metricTemperatureUnitType);
                    city.setMetricTemperature(metricTemperature);
                    savedCities.add(city);
                }

                if (savedCities.size() == 0) {
                    cityList.setVisibility(View.INVISIBLE);
                    noCitiesToDisplay.setVisibility(View.VISIBLE);
                } else {
                    cityList.setVisibility(View.VISIBLE);
                    noCitiesToDisplay.setVisibility(View.INVISIBLE);
                }

                cityListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return true;
    }


    public void setCurrentCity(View view) {
        searchButton = false;
        final AlertDialog.Builder citySelectorAlertDialog = new AlertDialog.Builder(this);
        citySelectorAlertDialog.setTitle("Enter City Details");

        final EditText city = new EditText(MainActivity.this);
        city.setHint("Enter Your City");
        final EditText country = new EditText(MainActivity.this);
        country.setHint("Enter Your Country");

        LinearLayout locationEdittextlayout = new LinearLayout(MainActivity.this);
        locationEdittextlayout.setOrientation(LinearLayout.VERTICAL);

        locationEdittextlayout.addView(city);
        locationEdittextlayout.addView(country);

        View selectorView = new View(MainActivity.this);
        citySelectorAlertDialog.setView(locationEdittextlayout);

        citySelectorAlertDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String cityValue = city.getText().toString();
                String countryValue = country.getText().toString();
                loadingLocation.setCancelable(false);
                GetCurrentCityAsyncTask getCurrentCityAsyncTask = new GetCurrentCityAsyncTask(MainActivity.this);
                String[] params = {cityValue, countryValue};
                if (TextUtils.isEmpty(cityValue) || TextUtils.isEmpty(countryValue)) {
                    Toast.makeText(MainActivity.this, "City/Country value cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    loadingLocation.show();
                    getCurrentCityAsyncTask.execute(params);
                }
            }
        });

        citySelectorAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        citySelectorAlertDialog.show();
    }

    @Override
    public void loadLocation(Location location) {
        if (location != null) {
            if (searchButton == true) {
                cityDetails = new Cities();
                cityDetails.setCityKey(location.getKey());
                cityDetails.setCityName(location.getCity());
                cityDetails.setCountry(location.getCountryId());
                cityDetails.setFavourite(false);
                intentToStartCityWeatherActivity.putExtra(KEY, location.getKey());
                startActivity(intentToStartCityWeatherActivity);
                getWeatherDetails(cityDetails.getCityKey());

            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CITY, location.getCity());
                editor.putString(COUNTRY, location.getCountryId());
                editor.putString(KEY, location.getKey());
                editor.commit();
                loadingLocation.dismiss();
                displayWeather();
                Toast.makeText(this, "Current City details saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show();
            loadingLocation.dismiss();
        }
    }

    void getWeatherDetails(String key) {
        GetWeatherForCurrentLocationAsyncTask getWeatherTask = new GetWeatherForCurrentLocationAsyncTask(this);
        getWeatherTask.execute(key);
    }

    void displayWeather() {
        String key = sharedPreferences.getString(KEY, null);
        GetWeatherForCurrentLocationAsyncTask getWeatherTask = new GetWeatherForCurrentLocationAsyncTask(this);
        loadingWeather = new ProgressDialog(MainActivity.this);
        loadingWeather.setCancelable(false);
        loadingWeather.show();
        getWeatherTask.execute(key);
    }

    @Override
    public void loadWeather(Weather weather) {
        if (weather != null) {
            if (searchButton == true) {
                cityDetails.setLastUpdated(weather.getLocalObservationDateTime().toString());
                cityDetails.setMetricTemperature(weather.getMetricTemperature());
                mDatabase.child("Cities").child(cityDetails.getCityKey()).setValue(cityDetails);
            } else {
                this.weatherDetails = weather;
                displayWeatherDetailsForCurrentCity(weather);
            }
        } else {
            Toast.makeText(MainActivity.this, "Unable to load weather for this city", Toast.LENGTH_SHORT).show();
        }
    }

    void displayWeatherDetailsForCurrentCity(Weather weather) {
        setCurrentCityButton.setVisibility(View.INVISIBLE);
        currentCityNotSetText.setVisibility(View.INVISIBLE);
        locationText.setVisibility(View.VISIBLE);
        weatherText.setVisibility(View.VISIBLE);
        weatherImage.setVisibility(View.VISIBLE);
        temperatureText.setVisibility(View.VISIBLE);
        updateTimeText.setVisibility(View.VISIBLE);
        String city = sharedPreferences.getString(CITY, null);
        String country = sharedPreferences.getString(COUNTRY, null);
        String imageUrl = "http://developer.accuweather.com/sites/default/files/" + weather.getWeatherIcon() + "-s.png";
        char degreeSymbol = (char) 0x00B0;
        PrettyTime prettyTime = new PrettyTime();

        locationText.setText(city + ", " + country.toUpperCase());
        weatherText.setText(weather.getWeatherText());
        Picasso.with(this).load(imageUrl).into(weatherImage);

        String temperatureUnitToDisplay = sharedPreferences.getString(TEMPERATURE_UNIT,CELSIUS);
        MetricTemperature temperatureToDisplay=null;
        TemperatureConvertor temperatureConvertor = new TemperatureConvertor();

        if(temperatureUnitToDisplay.equalsIgnoreCase(FAHRENHEIT)){
            temperatureToDisplay = temperatureConvertor.convertFromCelsiusToFahrenheit(weather.getMetricTemperature());
            cityListAdapter.notifyDataSetChanged();
        }else{
            temperatureToDisplay = weather.getMetricTemperature();
            cityListAdapter.notifyDataSetChanged();
        }

        temperatureText.setText("Temperature: " + temperatureToDisplay.getValue() + " " + degreeSymbol + temperatureToDisplay.getUnit());
        //temperatureText.setText("Temperature: " + weather.getMetricTemperature().getValue() + " " + degreeSymbol + weather.getMetricTemperature().getUnit());
        updateTimeText.setText("Updated " + prettyTime.format(weather.getLocalObservationDateTime()));
        loadingWeather.dismiss();
    }

    public void startCityWeatherActivity(View view) {
        EditText searchCityText = (EditText) findViewById(R.id.cityNameForSearch);
        EditText searchCountryText = (EditText) findViewById(R.id.countryNameForSearch);
        Cities cities = new Cities();

        if (TextUtils.isEmpty(searchCityText.getText().toString()) || TextUtils.isEmpty(searchCountryText.getText().toString())) {
            Toast.makeText(MainActivity.this, "City/Country value cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            saveCityDetailsToDB(searchCityText.getText().toString(), searchCountryText.getText().toString());
            intentToStartCityWeatherActivity = new Intent(this, CityWeatherActivity.class);
            intentToStartCityWeatherActivity.putExtra(CITY, searchCityText.getText().toString());
            intentToStartCityWeatherActivity.putExtra(COUNTRY, searchCountryText.getText().toString());
        }
    }

    void saveCityDetailsToDB(String city, String country) {
        GetCurrentCityAsyncTask getCurrentCityAsyncTask = new GetCurrentCityAsyncTask(MainActivity.this);
        String[] params = {city, country};
        searchButton = true;
        getCurrentCityAsyncTask.execute(params);
    }

    void openSettings(MenuItem menuItem) {
        Intent intent = new Intent(this, MainMenuPreference.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String preference_temperature = settings.getString("preference_temperature", "");
        sharedPreferences.edit().putString(TEMPERATURE_UNIT,preference_temperature).commit();
        if(weatherDetails!=null) {
            displayWeatherDetailsForCurrentCity(weatherDetails);
        }
    }

}
