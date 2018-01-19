package group16.mad.com.triviaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vipul.Shukla on 2/9/2017.
 */

/*
* Assignment #04
* ImageLoader.java
* Vipul Shukla, Shanmukh Anand
*
* */

public class ImageLoader extends AsyncTask<String,Void,Bitmap>{

    ImageLoad activity;

    public ImageLoader(ImageLoad activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.loadImage(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            if(params[0]!=null) {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                Bitmap image = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                return image;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    interface ImageLoad{
        void loadImage(Bitmap image);
    }
}
