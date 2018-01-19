package group16.mad.com.tedradiohour;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Vipul.Shukla on 3/8/2017.
 */
/*
* Assignment # 7.
Item.java
Vipul Shukla, Shanmukh Anand
* */

public class Item implements Serializable{
    String title;
    String description;
    Date publicationDate;
    String imageURL;
    String duration;
    String mP3FileURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getmP3FileURL() {
        return mP3FileURL;
    }

    public void setmP3FileURL(String mP3FileURL) {
        this.mP3FileURL = mP3FileURL;
    }
}
