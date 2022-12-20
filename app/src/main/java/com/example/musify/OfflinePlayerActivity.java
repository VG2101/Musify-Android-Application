package com.example.musify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class OfflinePlayerActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer3.stop(); mediaPlayer3.release();
        updateSeek3.interrupt();
    }

    TextView textView3;
    ImageView play3, previous3, next3 , songImage3;
    ArrayList<File> songs3;
    MediaPlayer mediaPlayer3;
    int position3; String textContent3;
    SeekBar seekBar3; Intent intent3;
    Thread updateSeek3;
    MediaMetadataRetriever mr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_player);
        textView3 = findViewById(R.id.textView3); play3 = findViewById(R.id.pause3); previous3 = findViewById(R.id.previous3);
        next3 = findViewById(R.id.next3); seekBar3 = findViewById(R.id.seekBar3); songImage3= findViewById(R.id.songImage3);
        mr=new MediaMetadataRetriever();

        intent3=getIntent();
        Bundle bundle = intent3.getExtras();
        songs3 = (ArrayList) bundle.getParcelableArrayList("offlineSongsList");
        position3 = intent3.getIntExtra("position", 0);
        textContent3=songs3.get(position3).getName().toString().replace(".mp3","");
        textView3.setText(textContent3);
        textView3.setSelected(true);
        imageChange(position3);
        Uri uri = Uri.parse(songs3.get(position3).toString());
        mediaPlayer3 = MediaPlayer.create(this, uri);
        mediaPlayer3.start();
        //Log.d("check","Media start");
        seekBar3.setMax(mediaPlayer3.getDuration());
        changeSeekbar();

        play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer3.isPlaying()){
                    play3.setImageResource(R.drawable.play);
                    mediaPlayer3.pause();
                }
                else{
                    play3.setImageResource(R.drawable.pause);
                    mediaPlayer3.start();
                }

            }
        });

        previous3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer3.stop();
                mediaPlayer3.release(); updateSeek3.interrupt();
                if(position3!=0){
                    position3 = position3 - 1;
                }
                else{
                    position3 = songs3.size() - 1;
                }
                songChange(position3);
            }
        });

        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer3.stop();
                mediaPlayer3.release(); updateSeek3.interrupt();
                if(position3!=songs3.size()-1){
                    position3 = position3 + 1;
                }
                else{
                    position3 = 0;
                }
                songChange(position3);

            }
        });
        mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next3.performClick();
            }
        });
    }
    private void songChange(int position){
        Uri uri = Uri.parse(songs3.get(position).toString());
        mediaPlayer3 = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer3.start();
        play3.setImageResource(R.drawable.pause);
        seekBar3.setMax(mediaPlayer3.getDuration());
        changeSeekbar();
        imageChange(position);
        mr.setDataSource(songs3.get(position).getPath());
        textView3.setText(mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
    }
    private void changeSeekbar() {
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer3.seekTo(seekBar.getProgress());
            }
        });

        updateSeek3 = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while(currentPosition<mediaPlayer3.getDuration()){
                        currentPosition = mediaPlayer3.getCurrentPosition();
                        seekBar3.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek3.start();
    }
    private void imageChange(int position){
      mr.setDataSource(songs3.get(position).getPath());
        byte [] data = mr.getEmbeddedPicture();

        if(data != null)
        {Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            songImage3.setImageBitmap(bitmap); }
        else
        { songImage3.setImageResource(R.drawable.logo); }
    }
}