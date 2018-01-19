package com.mad.group16.chatroom;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vipul on 4/10/2017.
 */

public class Message {
    String id, name, text, imageURL;
    long date;
    boolean iscomment;
    ArrayList<Message> comments;

    public Message() {
    }

    public boolean iscomment() {
        return iscomment;
    }

    public void setIscomment(boolean iscomment) {
        this.iscomment = iscomment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message(String id, String name, String text, String imageURL, long date) {
        this.name = name;
        this.id = id;
        this.text = text;
        this.imageURL = imageURL;
        this.date = date;
        comments = new ArrayList();
        iscomment = false;
    }

    public ArrayList<Message> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Message> comments) {
        this.comments.addAll(comments);
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", date=" + date +
                '}';
    }

    public Map<String, Object> toMap(){
        Map<String, Object> m1 = new HashMap<>();
        Map<String, Object> m2 = new HashMap<>();
        m1.put("name",name);
        m1.put("id",id);
        m1.put("text",text);
        m1.put("iscomment",iscomment);
        m1.put("imageURL",imageURL);
        m1.put("date",date);

        int i=0;
        for(Message m: comments){
            m2.put("comment"+i,m);
        }

        m1.put("comments",m2);

        return m1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void initial() {
        if(comments == null)
            comments = new ArrayList<>();
    }

    public static Message fromJSON(JSONObject obj) throws JSONException {
        Message m = new Message();
        m.setId(obj.getString("id"));
        m.setName(obj.getString("name"));
        if(obj.has("imageURL"))
            m.setImageURL(obj.getString("imageURL"));
        m.setDate(obj.getLong("date"));
        if(obj.has("text"))
            m.setText(obj.getString("text"));
        if(obj.has("comments")) {
            m.initial();
            ArrayList<Message> ms = new ArrayList<>();
            JSONArray array = obj.getJSONArray("comments");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj1 = array.getJSONObject(i);
                Message m1 = new Message();
                m1.setId(obj1.getString("id"));
                m1.setName(obj1.getString("name"));
                if(obj1.has("imageURL"))
                    m1.setImageURL(obj1.getString("imageURL"));
                if(obj1.has("text"))
                    m1.setText(obj1.getString("text"));
                m1.setDate(obj1.getLong("date"));
                ms.add(m1);
            }
            m.setComments(ms);
        }
        return m;
    }

}
