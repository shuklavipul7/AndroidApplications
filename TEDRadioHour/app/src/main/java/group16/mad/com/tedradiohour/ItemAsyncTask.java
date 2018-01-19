package group16.mad.com.tedradiohour;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Vipul.Shukla on 3/9/2017.
 */
/* Assignment # 7.
   ItemAsyncTask.java
   Vipul Shukla, Shanmukh Anand
* */

public class ItemAsyncTask extends AsyncTask<String, Void, ArrayList<Item>> {
    LoadDataActivity activity;

    public ItemAsyncTask(LoadDataActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> items) {
        super.onPostExecute(items);
        activity.loadData(items);
    }

    @Override
    protected ArrayList<Item> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int statusCode = conn.getResponseCode();
            if(statusCode==HTTP_OK){
                InputStream in = conn.getInputStream();
                return ItemUtil.ItemParser.parseGameList(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    interface LoadDataActivity{
        void loadData(ArrayList<Item> itemList);
    }
}
