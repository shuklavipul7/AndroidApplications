package group16.mad.com.triviaapp;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;

/*
* Assignment #04
* StatsActivity.java
* Vipul Shukla, Shanmukh Anand
*
* */

public class StatsActivity extends AppCompatActivity {
    ArrayList<String> wrongAnswers = new ArrayList<>();
    ArrayList<String> correctAnswers = new ArrayList<>();
    ArrayList<String> questionText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        correctAnswers = (ArrayList<String>) getIntent().getExtras().get(TriviaActivity.CORRECT_ANSWERS);
        wrongAnswers = (ArrayList<String>) getIntent().getExtras().get(TriviaActivity.WRONG_ANSWERS);
        questionText = (ArrayList<String>) getIntent().getExtras().get(TriviaActivity.QUESTION_TEXT);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);


        LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 30, 0, 0);

        LinearLayoutCompat.LayoutParams lparams2 = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);


        for (int i = 0; i < questionText.size(); i++) {
            TextView question = new TextView(this);
            question.setLayoutParams(lparams);
            question.setText(questionText.get(i));
            question.setTextColor(Color.BLACK);

            TextView wrong = new TextView(this);
            wrong.setLayoutParams(lparams2);
            wrong.setText(wrongAnswers.get(i));
            wrong.setBackgroundColor(Color.RED);
            wrong.setTextColor(Color.BLACK);

            TextView correct = new TextView(this);
            correct.setLayoutParams(lparams2);
            correct.setText(correctAnswers.get(i));
            correct.setTextColor(Color.BLACK);

            linearLayout.addView(question);
            linearLayout.addView(wrong);
            linearLayout.addView(correct);
        }


        int correctAnswers = 16 - wrongAnswers.size();
        int performancePercent = (correctAnswers * 100) / 16;
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(performancePercent);
        seekBar.setEnabled(false);
        TextView textView = (TextView) findViewById(R.id.performacePercentID);
        textView.setTextColor(Color.BLACK);
        textView.setText("" + performancePercent + "%");

    }

    void finish(View view){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
