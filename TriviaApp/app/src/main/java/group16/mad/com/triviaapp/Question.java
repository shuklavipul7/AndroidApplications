package group16.mad.com.triviaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vipul.Shukla on 2/8/2017.
 */

/*
* Assignment #04
* Question.java
* Vipul Shukla, Shanmukh Anand
*
* */

public class Question implements Parcelable {
    private Integer id;
    private String text;
    private String imageURL;
    private List<String> choices=new ArrayList<>();
    private Integer answer;

    public Question(Integer id, String text, String imageURL, List<String> choices, Integer answer) {
        this.id = id;
        this.text = text;
        this.imageURL = imageURL;
        this.choices = choices;
        this.answer = answer;
    }

    protected Question(Parcel in) {
        this.id = in.readInt();
        this.text = in.readString();
        this.imageURL = in.readString();
        in.readStringList(choices);
        this.answer = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.text);
        dest.writeString(this.imageURL);
        dest.writeStringList(this.choices);
        dest.writeInt(this.answer);
    }
}
