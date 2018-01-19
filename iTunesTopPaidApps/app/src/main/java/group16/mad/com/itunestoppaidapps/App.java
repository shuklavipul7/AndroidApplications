package group16.mad.com.itunestoppaidapps;

import java.io.Serializable;

/**
 * Created by Vipul.Shukla on 2/22/2017.
 */

/*
Assignment#Homework 06.
File Name: App.java
Vipul Shukla, Shanmukh Anand
*/

public class App implements Serializable{
    String appName, imageUrl, price;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
