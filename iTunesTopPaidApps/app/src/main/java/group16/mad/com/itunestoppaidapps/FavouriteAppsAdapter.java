package group16.mad.com.itunestoppaidapps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static group16.mad.com.itunestoppaidapps.AppAdapter.APP_FAVOURITES_SHARED_PREFERENCES_SET;

/**
 * Created by Vipul.Shukla on 2/23/2017.
 */
/*
Assignment#Homework 06.
File Name: FavouriteAppsAdapter.java
Vipul Shukla, Shanmukh Anand
*/


public class FavouriteAppsAdapter extends ArrayAdapter {
    Context mContext;
    int mResource;
    List mObjects;
    View mConvertView;

    public FavouriteAppsAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("favouriteApps", MODE_PRIVATE);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        mConvertView = convertView;
        ImageView appImage = (ImageView) convertView.findViewById(R.id.appImage);
        TextView appData = (TextView) convertView.findViewById(R.id.appData);
        ImageView star = (ImageView) convertView.findViewById(R.id.favouriteStar);

        App app = (App) mObjects.get(position);

            if (sharedPreferences.getStringSet(APP_FAVOURITES_SHARED_PREFERENCES_SET, new HashSet<String>()).contains(app.getAppName())) {
                if (app.getImageUrl() != null && app.getImageUrl().length() > 0) {
                    Picasso.with(mContext).load(app.getImageUrl()).into(appImage);
                }
                appData.setText(app.getAppName() + "\n" + app.getPrice());

                Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.blackstar);
                star.setImageBitmap(icon);

                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final App app = ((App) mObjects.get(position));
                        if (sharedPreferences.getStringSet(APP_FAVOURITES_SHARED_PREFERENCES_SET, new HashSet<String>()).contains(app.getAppName())) {

                            new AlertDialog.Builder(mContext)
                                    .setTitle("Add to favourites")
                                    .setMessage("Are you sure you want to remove this App from favourites?")
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            Set<String> appHashSet = sharedPreferences.getStringSet(APP_FAVOURITES_SHARED_PREFERENCES_SET, new HashSet<String>());
                                            appHashSet.remove(app.getAppName());
                                            editor.clear();
                                            editor.putStringSet(APP_FAVOURITES_SHARED_PREFERENCES_SET, appHashSet);
                                            editor.commit();
                                            mObjects.remove(app);
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }

            return convertView;
        }

    }