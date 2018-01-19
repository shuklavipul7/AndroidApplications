package group16.mad.com.triviaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/*
* Assignment #04
* TriviaActivity.java
* Vipul Shukla, Shanmukh Anand
*
* */

public class TriviaActivity extends AppCompatActivity implements ImageLoader.ImageLoad {
    Integer questionNumber = 0;
    TextView questionNumberText;
    TextView questionText;
    List<Question> questionBank;
    RadioGroup choicesGroup;
    Bitmap image;
    ImageView imageView;
    ProgressBar progressBar;
    TextView loadingImage;
    Map<Integer, String> answers = new LinkedHashMap<>();
    Map<Integer, String> correctAnswers = new LinkedHashMap<>();
    final static String WRONG_ANSWERS = "wrongAnswers";
    final static String CORRECT_ANSWERS = "correctAnswers";
    final static String QUESTION_TEXT = "questionText";
    MyCountDownTimer countDownTimer = new MyCountDownTimer(120000, 1000);
    TextView countDownTimerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        questionBank = getIntent().getExtras().getParcelableArrayList(MainActivity.QUESTION_BANK);
        countDownTimerText = (TextView) findViewById(R.id.timer);
        countDownTimerText.setTextColor(Color.BLACK);
        displayFirstQuestion(questionBank);

        findCorrectAnswers(questionBank);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.choiceGroupId);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                answers.put(questionNumber, button.getText().toString());
                group.check(checkedId);
            }
        });
    }

    void findCorrectAnswers(List<Question> questionBank) {
        ArrayList<Question> questionList = (ArrayList<Question>) questionBank;
        ArrayList<String> choices;
        for (int i = 0; i < questionList.size(); i++) {
            choices = (ArrayList<String>) questionList.get(i).getChoices();
            correctAnswers.put(i, choices.get(questionList.get(i).getAnswer() - 1));
        }
    }

    void displayFirstQuestion(List<Question> questionBank) {
        countDownTimer.start();
        questionNumberText = (TextView) findViewById(R.id.QId);
        questionText = (TextView) findViewById(R.id.questionTextID);
        choicesGroup = (RadioGroup) findViewById(R.id.choiceGroupId);
        questionText.setText(questionBank.get(0).getText());
        choicesGroup.removeAllViews();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        loadingImage = (TextView) findViewById(R.id.loadingID);
        loadingImage.setVisibility(View.VISIBLE);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);

        ImageLoader imageLoader = new ImageLoader(this);
        imageLoader.execute(questionBank.get(0).getImageURL());

        List<String> choices = questionBank.get(0).getChoices();
        for (int j = 0; j < choices.size(); j++) {
            RadioButton choiceRadioButton = new RadioButton(this);
            choiceRadioButton.setText(choices.get(j));
            choicesGroup.addView(choiceRadioButton, j);
        }
        int questionNumberToDisplay = questionNumber + 1;
        questionNumberText.setText("Q" + questionNumberToDisplay);


        Button previousButton = (Button) findViewById(R.id.previousButton);
        previousButton.setClickable(false);
        previousButton.setBackgroundColor(getResources().getColor(R.color.grey));
    }

    void nextQuestion(View view) {
        RadioGroup choicesGroups = (RadioGroup) findViewById(R.id.choiceGroupId);
        int selected = choicesGroups.getCheckedRadioButtonId();
        if ((answers.size() > questionNumber)) {
            if (questionNumber + 1 < questionBank.size()) {
                questionNumber++;
                questionText = (TextView) findViewById(R.id.questionTextID);
                questionText.setText(questionBank.get(questionNumber).getText());
                choicesGroup.removeAllViews();

                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                loadingImage = (TextView) findViewById(R.id.loadingID);
                loadingImage.setVisibility(View.VISIBLE);

                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setVisibility(View.INVISIBLE);

                ImageLoader imageLoader = new ImageLoader(this);
                imageLoader.execute(questionBank.get(questionNumber).getImageURL());

                List<String> choices = questionBank.get(questionNumber).getChoices();
                for (int j = 0; j < choices.size(); j++) {
                    RadioButton choiceRadioButton = new RadioButton(this);
                    choiceRadioButton.setText(choices.get(j));
                    choiceRadioButton.setId(j);
                    choicesGroup.addView(choiceRadioButton, j);
                }

                if (answers.containsKey(questionNumber)) {
                    for (int i = 0; i < choicesGroup.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) findViewById(choicesGroup.getChildAt(i).getId());
                        if (radioButton.getText().toString().equals((String) answers.get(questionNumber))) {
                            radioButton.setChecked(true);
                        }
                    }
                }
                int questionNumberToDisplay = questionNumber + 1;
                questionNumberText.setText("Q" + questionNumberToDisplay);

                Button previousButton = (Button) findViewById(R.id.previousButton);
                previousButton.setClickable(true);
                previousButton.setBackgroundColor(getResources().getColor(R.color.aqua));

                if (questionNumber == 15) {
                    Button finishButton = (Button) findViewById(R.id.finishButton);
                    finishButton.setVisibility(View.VISIBLE);

                    Button nextButton = (Button) findViewById(R.id.nextButton);
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            Toast toast = Toast.makeText(this, "Please select your answer", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void previousQuestion(View view) {
        if (questionNumber > 0) {
            questionNumber--;
            questionText = (TextView) findViewById(R.id.questionTextID);
            questionText.setText(questionBank.get(questionNumber).getText());
            choicesGroup = (RadioGroup) findViewById(R.id.choiceGroupId);
            choicesGroup.removeAllViews();

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            TextView loadingImage = (TextView) findViewById(R.id.loadingID);
            loadingImage.setVisibility(View.VISIBLE);
            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setVisibility(View.INVISIBLE);

            ImageLoader imageLoader = new ImageLoader(this);
            imageLoader.execute(questionBank.get(questionNumber).getImageURL());

            List<String> choices = questionBank.get(questionNumber).getChoices();
            for (int j = 0; j < choices.size(); j++) {
                RadioButton choiceRadioButton = new RadioButton(this);
                choiceRadioButton.setText(choices.get(j));
                choicesGroup.addView(choiceRadioButton, j);
            }

            if (answers.containsKey(questionNumber)) {
                for (int i = 0; i < choicesGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) findViewById(choicesGroup.getChildAt(i).getId());
                    if (radioButton.getText().toString().equals((String) answers.get(questionNumber))) {
                        radioButton.setChecked(true);
                    }
                }
            }

            int questionNumberToDisplay = questionNumber + 1;
            questionNumberText.setText("Q" + questionNumberToDisplay);

            Button nextButton = (Button) findViewById(R.id.nextButton);
            nextButton.setClickable(true);
            nextButton.setBackgroundColor(getResources().getColor(R.color.aqua));

            if (questionNumber == 0) {
                Button previousButton = (Button) findViewById(R.id.previousButton);
                previousButton.setClickable(false);
                previousButton.setBackgroundColor(getResources().getColor(R.color.grey));
            }

            if (questionNumber < 15) {
                Button finishButton = (Button) findViewById(R.id.finishButton);
                finishButton.setVisibility(View.INVISIBLE);

                nextButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadImage(Bitmap image) {
        this.image = null;
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingImage = (TextView) findViewById(R.id.loadingID);

        this.image = image;
        if (this.image != null) {
            imageView.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            loadingImage.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            loadingImage.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    void finish(View view) {
        ArrayList<String> userChoices = new ArrayList<>();
        ArrayList<String> correctChoices = new ArrayList<>();
        ArrayList<String> questionText = new ArrayList<>();

        ArrayList<String> wrongAnswersList = new ArrayList<>();
        ArrayList<String> correctAnswersList = new ArrayList<>();

        Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);


        for (Integer key : answers.keySet()) {
            userChoices.add(key, answers.get(key));
        }

        for (Integer key : correctAnswers.keySet()) {
            correctChoices.add(key, correctAnswers.get(key));
        }

        for (int i = 0; i < questionBank.size(); i++) {

            if (!(userChoices.get(i).equals(correctChoices.get(i)))) {
                questionText.add(questionBank.get(i).getText());
                wrongAnswersList.add(userChoices.get(i));
                correctAnswersList.add(correctChoices.get(i));
            }
        }

        intent.putExtra(WRONG_ANSWERS, wrongAnswersList);
        intent.putExtra(CORRECT_ANSWERS, correctAnswersList);
        intent.putExtra(QUESTION_TEXT, questionText);
        startActivity(intent);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        public void onFinish() {
            System.out.println("countdown timer finished");
            ArrayList<String> userChoices = new ArrayList<>();
            ArrayList<String> correctChoices = new ArrayList<>();
            ArrayList<String> questionText = new ArrayList<>();

            ArrayList<String> wrongAnswersList = new ArrayList<>();
            ArrayList<String> correctAnswersList = new ArrayList<>();

            Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);


            for (Integer key : answers.keySet()) {
                userChoices.add(answers.get(key));
            }

            for (Integer key : correctAnswers.keySet()) {
                correctChoices.add(correctAnswers.get(key));
            }

            if (userChoices.size() != correctChoices.size()) {
                int numberOfQuestionsAnswered = userChoices.size();
                for (int j = numberOfQuestionsAnswered; j < questionBank.size(); j++) {
                    userChoices.add(".");
                }
            }

            for (int i = 0; i < questionBank.size(); i++) {

                if (!(userChoices.get(i).equals(correctChoices.get(i)))) {
                    questionText.add(questionBank.get(i).getText());
                    wrongAnswersList.add(userChoices.get(i));
                    correctAnswersList.add(correctChoices.get(i));
                }

            }


            intent.putExtra(WRONG_ANSWERS, wrongAnswersList);
            intent.putExtra(CORRECT_ANSWERS, correctAnswersList);
            intent.putExtra(QUESTION_TEXT, questionText);
            startActivity(intent);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countDownTimerText.setText("Time Left:" + (millisUntilFinished / 1000) + " seconds");
        }
    }
}


