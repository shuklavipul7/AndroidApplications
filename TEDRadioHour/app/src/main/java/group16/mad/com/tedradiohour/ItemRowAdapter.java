package group16.mad.com.tedradiohour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vipul.Shukla on 3/9/2017.
 */
/* Assignment # 7.
   ItemRowAdapter.java
   Vipul Shukla, Shanmukh Anand
* */

public class ItemRowAdapter extends RecyclerView.Adapter<ItemRowAdapter.RowHolder> implements View.OnTouchListener {
    private ArrayList<Item> itemList;
    Context context;
    SeekBar audioSeekBar;
    ImageButton playButton;
    ImageButton pauseButton;
    MainActivity activity;
    MediaPlayer localMediaPlayer;

    Runnable run;
    Handler seekHandler = new Handler();


    public class RowHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemDetails;
        ImageButton playAudioButton;

        public RowHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            itemDetails = (TextView) itemView.findViewById(R.id.itemDetails);
            playAudioButton = (ImageButton) itemView.findViewById(R.id.playButton);
        }
    }

    public ItemRowAdapter(Context context, ArrayList<Item> itemList, MainActivity activity) {
        this.context = context;
        this.itemList = itemList;

        this.pauseButton = (ImageButton) activity.findViewById(R.id.pauseButton);
        this.playButton = (ImageButton) activity.findViewById(R.id.playButton);
        this.audioSeekBar = (SeekBar) activity.findViewById(R.id.audio_seek_bar);
        this.audioSeekBar.setOnTouchListener(this);
        localMediaPlayer = activity.mediaPlayer;
        this.activity = activity;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final Item item = itemList.get(position);


        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d, MMM yyyy");
        Picasso.with(context).load(item.getImageURL()).into(holder.itemImage);
        holder.itemDetails.setText(item.getTitle() + "\n" + "posted: " + dateFormat.format(item.getPublicationDate()));

        holder.playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
                try {
                    audioSeekBar.setProgress(0);
                    localMediaPlayer.reset();
                    localMediaPlayer.setDataSource(item.getmP3FileURL());
                    localMediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!localMediaPlayer.isPlaying()) {
                    localMediaPlayer.start();
                    audioSeekBar.setVisibility(View.VISIBLE);
                    audioSeekBar.setMax(localMediaPlayer.getDuration());
                    pauseButton.setVisibility(View.VISIBLE);

                    run = new Runnable() {
                        @Override
                        public void run() {
                            seekUpdation();
                        }
                    };

                    seekUpdation();

                }
            }

        });

        holder.itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PlayActivity.class);
                intent.putExtra(MainActivity.ITEM, item);
                localMediaPlayer.reset();
                pauseButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                audioSeekBar.setVisibility(View.INVISIBLE);
                activity.startActivity(intent);
            }
        });
    }

    public void seekUpdation() {
        audioSeekBar.setProgress(localMediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (localMediaPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) v;
            localMediaPlayer.seekTo(sb.getProgress());
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
