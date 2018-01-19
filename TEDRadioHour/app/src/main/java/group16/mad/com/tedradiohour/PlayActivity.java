package group16.mad.com.tedradiohour;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
/* Assignment # 7.
   PlayActivity.java
   Vipul Shukla, Shanmukh Anand
* */

public class PlayActivity extends AppCompatActivity {
    MediaPlayer localMediaPlayer= new MediaPlayer();
    SeekBar audioSeekBar;
    ImageButton playButton;
    ImageButton pauseButton;
    Runnable run;
    Handler seekHandler = new Handler();
    ImageButton playAudioButton;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        item = (Item) getIntent().getExtras().get(MainActivity.ITEM);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");

        TextView episodeTitle = (TextView) findViewById(R.id.episodeTitle);
        ImageView episodeImage = (ImageView) findViewById(R.id.episodeImage);
        TextView description = (TextView) findViewById(R.id.description);
        TextView publicationDate = (TextView) findViewById(R.id.publicationDate);
        TextView duration = (TextView) findViewById(R.id.duration);

        playAudioButton = (ImageButton) findViewById(R.id.playButton);
        playButton = (ImageButton) findViewById(R.id.playButton);
        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        audioSeekBar = (SeekBar) findViewById(R.id.seekBar);


        episodeTitle.setText(item.getTitle());
        Picasso.with(this).load(item.getImageURL()).into(episodeImage);
        description.setText(item.getDescription());
        publicationDate.setText("Publication Date: " + dateFormat.format(item.getPublicationDate()));
        if (item.getDuration() != null) {
            double dur = Integer.parseInt(item.getDuration()) / 60;
            duration.setText("Duration: " + dur + " minutes");
        }
    }

    public void onBackPressed() {
        finish();
        localMediaPlayer.reset();
    }

    void playAudio(View view) {
        try {
            localMediaPlayer.setDataSource(item.getmP3FileURL());
            localMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!localMediaPlayer.isPlaying()) {
            localMediaPlayer.start();
            audioSeekBar.setVisibility(View.VISIBLE);
            audioSeekBar.setMax(localMediaPlayer.getDuration());
            playButton.setVisibility(View.INVISIBLE);
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

    void pauseAudio(View view){
        this.localMediaPlayer.pause();
        this.pauseButton.setVisibility(View.INVISIBLE);
        this.playButton.setVisibility(View.VISIBLE);
    }

    public void seekUpdation() {
        audioSeekBar.setProgress(localMediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }
}
