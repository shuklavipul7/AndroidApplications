package com.mad.group16.chitchat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Shanmukh on 3/27/2017.
 */
/*
* Vipul Shukla
* Shanmukh Anand*/

public class CustomAdapter extends ArrayAdapter {
    Context mContext;
    int mResource;
    List mObjects;
    View mConvertView;

    public CustomAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Channel channel = (Channel) mObjects.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        mConvertView = convertView;
        final TextView channelName = (TextView) convertView.findViewById(R.id.channelName);
        final Button view = (Button)  convertView.findViewById(R.id.viewButton);

        channelName.setText(channel.getChannelName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;


    }
}
