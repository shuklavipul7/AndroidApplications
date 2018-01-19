package com.mad.group16.chatroom;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vipul on 4/10/2017.
 */

public class CustomAdapter extends ArrayAdapter {
    ArrayList<Message> messages;
    Context context;
    int r1, r2;

    public CustomAdapter(Context context, int resource, ArrayList<Message> objects) {
        super(context, resource, objects);
        messages = objects;
        this.context = context;
        r1 = R.layout.textlayout;
        r2 = R.layout.imagelayout;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message m = messages.get(position);
        PrettyTime pt = new PrettyTime();
        if(m.getImageURL()==null){
            convertView = LayoutInflater.from(context).inflate(r1, parent, false);
            ((TextView)convertView.findViewById(R.id.messageTextId)).setText(m.getText());
            ((TextView)convertView.findViewById(R.id.userName)).setText(m.getName());
            Log.d("demo",m.iscomment()+"");
            if(m.iscomment()) {
                convertView.findViewById(R.id.reply).setVisibility(View.INVISIBLE);
                convertView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else
                convertView.findViewById(R.id.reply).setTag(position);
            ((TextView)convertView.findViewById(R.id.chattime)).setText(pt.format(new Date(m.getDate())));
        }else {
            convertView = LayoutInflater.from(context).inflate(r2, parent, false);
            ((TextView) convertView.findViewById(R.id.name2)).setText(m.getName());
            ((TextView) convertView.findViewById(R.id.time2)).setText(pt.format(new Date(m.getDate())));
            if(m.iscomment()) {
                convertView.findViewById(R.id.replyButton).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.deleteButton).setVisibility(View.INVISIBLE);
                convertView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            } else {
                convertView.findViewById(R.id.replyButton).setTag(position);
            }
            final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(m.getImageURL());
            ((ImageView) convertView.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ref.delete();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("message/" + m.getId());
                    reference.removeValue();
                    messages.remove(m);
                }
            });
            ImageView v = ((ImageView) convertView.findViewById(R.id.chatImage));
            Glide.with(context).using(new FirebaseImageLoader()).load(ref).into(v);
        }
        return  convertView;
    }
}
