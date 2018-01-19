package group16.mad.com.triviaapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vipul.Shukla on 2/8/2017.
 */

/*
* Assignment #04
* TriviaDataLoader.java
* Vipul Shukla, Shanmukh Anand
*
* */

public class TriviaDataLoader extends AsyncTask<String, Void, String> {
    MainActivity activity;
    ProgressDialog progressDialog;
    TriviaDataLoader(MainActivity activity){
        this.activity = activity;
    }


    @Override
    protected String doInBackground(String... params) {
        String jsonString = downloadJsonString(params[0]);
        return jsonString;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading Trivia");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ArrayList<Question> questionBank = parseJsonToQuestionList(s);
        activity.applicationReady(questionBank);
        progressDialog.dismiss();
    }

    String downloadJsonString(String urlStr) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    ArrayList<Question> parseJsonToQuestionList(String jsonString){
        ArrayList<Question> questionBank=new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray questionArray = root.getJSONArray("questions");
            for(int i=0;i<questionArray.length();i++){
                Question question;

                Integer id=0;
                String text="";
                String imageURL="";
                List<String> choices = new ArrayList<>();
                Integer answer=0;

                id=questionArray.getJSONObject(i).getInt("id");
                text=questionArray.getJSONObject(i).getString("text");

                answer=(questionArray.getJSONObject(i).getJSONObject("choices").getInt("answer"));

                JSONArray choicesArray = questionArray.getJSONObject(i).getJSONObject("choices").getJSONArray("choice");
                for (int j=0;j<choicesArray.length();j++){
                    choices.add(choicesArray.getString(j));
                }

                if(questionArray.getJSONObject(i).length()==4) {
                    imageURL =(questionArray.getJSONObject(i).getString("image"));
                }

                question = new Question(id,text,imageURL,choices,answer);

                questionBank.add(question);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionBank;
    }

}
