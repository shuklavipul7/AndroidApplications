package group16.mad.com.thegamesdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vipul.Shukla on 2/16/2017.
 */

/*
Assignment #Homework 5.
Game.java
Vipul Shukla, Shanmukh Anand*/

public class Game implements Serializable{
    String title, platform, releaseYear, imageUrl, overview, genre, publisher, youtubeUrl;
    List<Integer> similarGamesId=new ArrayList<>();
    Integer id;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public List<Integer> getSimilarGamesId() {
        return similarGamesId;
    }

    public void setSimilarGamesId(List<Integer> similarGamesId) {
        this.similarGamesId = similarGamesId;
    }

    @Override
    public String toString() {
        return title +" Released in " + releaseYear + ". Platform:" + platform;
    }
}
