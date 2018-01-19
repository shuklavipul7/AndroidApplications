package com.mad.group16.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vipul.Shukla on 4/7/2017.
 */

public class ItemGridAdapter  extends RecyclerView.Adapter<ItemGridAdapter.RowHolder>{
    private ArrayList<OneDayWeather> itemList;
    CityWeatherActivity activity;

    public class RowHolder extends RecyclerView.ViewHolder {
        ImageView weatherIcon;
        TextView  weatherDate;
        View itemView;

        public RowHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            weatherIcon = (ImageView) itemView.findViewById(R.id.gridWeatherIcon);
            weatherDate = (TextView) itemView.findViewById(R.id.dateText);
        }
    }

    public ItemGridAdapter(CityWeatherActivity activity,ArrayList<OneDayWeather> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_weather_layout, parent, false);
        return new RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final OneDayWeather item = itemList.get(position);

        SimpleDateFormat sd = new SimpleDateFormat("d MMM ''yy");

        String dayIcon = item.getDayIcon();
        String dayImageUrl = "http://developer.accuweather.com/sites/default/files/" + dayIcon + "-s.png";
        Picasso.with(activity).load(dayImageUrl).into(holder.weatherIcon);
        holder.weatherDate.setText(sd.format(item.getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView forecastText = (TextView) activity.findViewById(R.id.forecastTextId);
                TextView temperatureText = (TextView) activity.findViewById(R.id.temperatureText);
                ImageView dayImage = (ImageView) activity.findViewById(R.id.dayWeatherImageId);
                ImageView nightImage = (ImageView) activity.findViewById(R.id.nightWeatherImageId);
                TextView dayWeatherPredictionText = (TextView) activity.findViewById(R.id.dayWeatherPredictionText);
                TextView nightWeatherPredictionText = (TextView) activity.findViewById(R.id.nightWeatherPredictionText);
                TextView clickHereForMoreDetailsText = (TextView) activity.findViewById(R.id.clickHereForMoreDetailsText);

                SimpleDateFormat sd = new SimpleDateFormat("MMM d, yyyy");
                char degreeSymbol = (char) 0x00B0;


                forecastText.setText("Forecast on "+sd.format(item.getDate()));
                temperatureText.setText("      Temperature: "+item.getMaximumTemperature().getValue()+degreeSymbol+"/"+item.getMinimumTemperature().getValue()+degreeSymbol);

                String dayIcon = item.getDayIcon();
                String dayImageUrl = "http://developer.accuweather.com/sites/default/files/" + dayIcon + "-s.png";

                String nightIcon = item.getNightIcon();
                String nightImageUrl = "http://developer.accuweather.com/sites/default/files/" + nightIcon + "-s.png";

                Picasso.with(activity).load(dayImageUrl).into(dayImage);
                Picasso.with(activity).load(nightImageUrl).into(nightImage);

                dayWeatherPredictionText.setText(item.getDayPhase());
                nightWeatherPredictionText.setText(item.getNightphase());

                clickHereForMoreDetailsText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getDailyWeatherForecastUrl()));
                        activity.startActivity(browserIntent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
