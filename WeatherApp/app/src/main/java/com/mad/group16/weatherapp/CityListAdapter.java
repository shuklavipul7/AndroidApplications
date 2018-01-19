package com.mad.group16.weatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vipul.Shukla on 4/6/2017.
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.RowHolder> {
    private ArrayList<Cities> itemList;

    TextView location;
    TextView temperature;
    TextView listUpdatedTimeText;
    ImageView greyImage;
    ImageView goldImage;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    Context context;
    String temperatureUnitToDisplay;


    public class RowHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public RowHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            location = (TextView) itemView.findViewById(R.id.listLocationText);
            temperature = (TextView) itemView.findViewById(R.id.listTemperatureText);
            listUpdatedTimeText = (TextView) itemView.findViewById(R.id.listUpdatedTimeText);
            greyImage = (ImageView) itemView.findViewById(R.id.listGreyImage);
            goldImage = (ImageView) itemView.findViewById(R.id.listGoldImage);
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    public CityListAdapter(ArrayList<Cities> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_layout, parent, false);
        return new RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences("weatherapp", MODE_PRIVATE);
        temperatureUnitToDisplay = sharedPreferences.getString(MainActivity.TEMPERATURE_UNIT,null);
        char degreeSymbol = (char) 0x00B0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        PrettyTime prettyTime = new PrettyTime();
        final Cities city = itemList.get(position);

        location.setText(city.getCityName() + ", " + city.getCountry());

        MetricTemperature temperatureToDisplay=null;
        TemperatureConvertor temperatureConvertor = new TemperatureConvertor();

        if(temperatureUnitToDisplay.equalsIgnoreCase(MainActivity.FAHRENHEIT)){
            temperatureToDisplay = temperatureConvertor.convertFromCelsiusToFahrenheit(city.getMetricTemperature());
        }else{
            temperatureToDisplay = city.getMetricTemperature();
        }

        temperature.setText("Temperature: " + temperatureToDisplay.getValue() + degreeSymbol + temperatureToDisplay.getUnit());
        //temperature.setText("Temperature: " + city.getMetricTemperature().getValue() + degreeSymbol + city.getMetricTemperature().getUnit());
        try {
            listUpdatedTimeText.setText("Updated " + prettyTime.format(simpleDateFormat.parse(city.getLastUpdated())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (city.getFavourite() == true) {
            goldImage.setVisibility(View.VISIBLE);
            greyImage.setVisibility(View.INVISIBLE);
        } else {
            goldImage.setVisibility(View.INVISIBLE);
            greyImage.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDatabase.child("Cities").child(city.getCityKey()).removeValue();
                return false;
            }
        });

        greyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Cities").child(city.getCityKey()).removeValue();
                city.setFavourite(true);
                mDatabase.child("Cities").child(city.getCityKey()).setValue(city);
                notifyDataSetChanged();
            }
        });

        goldImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Cities").child(city.getCityKey()).removeValue();
                city.setFavourite(false);
                mDatabase.child("Cities").child(city.getCityKey()).setValue(city);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
