package com.mad.group16.chitchat;

import java.util.ArrayList;

/**
 * Created by vipul on 3/27/2017.
 */
/*
* Vipul Shukla
* Shanmukh Anand*/

public class SubscriptionsResponse {
    int status;
    String message;
    ArrayList<SubscriptionsResponseData> data = new ArrayList<SubscriptionsResponseData>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SubscriptionsResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<SubscriptionsResponseData> data) {
        this.data = data;
    }
}
