package group16.mad.com.tedradiohour;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
* Assignment # 7.
MainActivity.java
Vipul Shukla, Shanmukh Anand
* */

public class MainActivity extends AppCompatActivity implements ItemAsyncTask.LoadDataActivity {
    ArrayList<Item> itemArrayList;
    ProgressDialog loading;
    boolean isGrid = false;
    ImageButton pauseButton;
    ImageButton playButton;
    SeekBar audioSeekBar;
    MediaPlayer mediaPlayer = new MediaPlayer();
    public static final String ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        playButton = (ImageButton) findViewById(R.id.playButton);
        audioSeekBar = (SeekBar) findViewById(R.id.audio_seek_bar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("TED Radio Hour Podcast");
        mTitleTextView.setTextColor(Color.WHITE);
        mTitleTextView.setTextSize(20);

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isGrid == false) {

                    mediaPlayer.reset();
                    audioSeekBar.setVisibility(View.INVISIBLE);
                    pauseButton.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);


                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    Collections.sort(itemArrayList, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            return o2.getPublicationDate().compareTo(o1.getPublicationDate());
                        }
                    });

                    ItemGridAdapter itemAdapter = new ItemGridAdapter(MainActivity.this, itemArrayList, MainActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(itemAdapter);
                    isGrid = true;
                } else {
                    mediaPlayer.reset();
                    audioSeekBar.setVisibility(View.INVISIBLE);
                    pauseButton.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);


                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    Collections.sort(itemArrayList, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            return o2.getPublicationDate().compareTo(o1.getPublicationDate());
                        }
                    });
                    ItemRowAdapter itemAdapter = new ItemRowAdapter(MainActivity.this, itemArrayList, MainActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(itemAdapter);
                    isGrid = false;
                }

            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        loading = new ProgressDialog(this);
        loading.setMessage("Loading Episodes...");
        loading.setCancelable(false);
        loading.show();

        ItemAsyncTask itemAsyncTask = new ItemAsyncTask(this);
        itemAsyncTask.execute("https://www.npr.org/rss/podcast.php?id=510298");
    }


    @Override
    public void loadData(ArrayList<Item> itemList) {
        this.itemArrayList = itemList;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Collections.sort(itemArrayList, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o2.getPublicationDate().compareTo(o1.getPublicationDate());
            }
        });
        ItemRowAdapter itemAdapter = new ItemRowAdapter(this, itemArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);

        loading.dismiss();
    }

    void pauseAudio(View view) {
        this.mediaPlayer.pause();
        this.pauseButton.setVisibility(View.INVISIBLE);
        this.playButton.setVisibility(View.VISIBLE);
    }

    void playAudio(View view) {
        this.mediaPlayer.start();
        this.pauseButton.setVisibility(View.VISIBLE);
        this.playButton.setVisibility(View.INVISIBLE);
    }

}
