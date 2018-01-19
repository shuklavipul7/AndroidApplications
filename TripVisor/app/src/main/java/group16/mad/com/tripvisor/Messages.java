package group16.mad.com.tripvisor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vipul on 4/18/2017.
 */

public class Messages implements Serializable{
    String text, id;
    User user;
    String time;
    Boolean isImage = false;

    public Messages(String id, String text , User user, String time) {
        this.text = text;
        this.id = id;
        this.user = user;
        this.time = time;
    }

    public Messages() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }
}
