package com.example.musify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SongsActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    RecyclerView sRecyclerView;
    ProgressBar sProgressBar;
    TextView sTitle; ImageView sPic, sPlay_pause, sNext,sPrev; SeekBar seekBar; Thread updateSeek;
    List<GetSongs> sDownload; JcSongsAdapter sAdapter;
    DatabaseReference sDBRef; ValueEventListener valueEventListener;
    MediaPlayer mediaPlayer; Intent sIntent; int sPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        sRecyclerView=findViewById(R.id.songActivityRecyclerView);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(SongsActivity.this));
        sProgressBar=findViewById(R.id.progressBarShowSong); sTitle=findViewById(R.id.songTitle); seekBar=findViewById(R.id.seekBar1);
        sPic=findViewById(R.id.songPic); sPlay_pause=findViewById(R.id.play_pause); sNext=findViewById(R.id.nextSong); sPrev=findViewById(R.id.previousSong);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        sIntent= getIntent();
        sDownload=new ArrayList<>();
        sDBRef= FirebaseDatabase.getInstance().getReference("songs");
        valueEventListener=sDBRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sDownload.clear();
                for(DataSnapshot dss:snapshot.getChildren()){
                    GetSongs getSongs=dss.getValue(GetSongs.class);
                    getSongs.setmKey(dss.getKey());
                    sDownload.add(getSongs);
                }
                sAdapter=new JcSongsAdapter(SongsActivity.this, sDownload, new JcSongsAdapter.RecyclerItemClickListner() {
                    @Override
                    public void onClickListner(GetSongs getSongs, int position) {
                        Uri uri = Uri.parse(sDownload.get(position).getSongLink());
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        sTitle.setText(sDownload.get(position).getSongTitle()); seekBar.setMax(mediaPlayer.getDuration());
                        changeSeekBar(); sPosition=position;
                    }
                });
                sRecyclerView.setAdapter(sAdapter);
                sAdapter.notifyDataSetChanged();
                sProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sProgressBar.setVisibility(View.GONE);
            }
        });
        sPlay_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause(); sPlay_pause.setImageResource(R.drawable.play);
                }
                else {
                    mediaPlayer.start(); sPlay_pause.setImageResource(R.drawable.pause);}
            }
        });
        sPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop(); mediaPlayer.release(); updateSeek.interrupt();
                if(sPosition!=0){ sPosition=sPosition-1;}
                else { sPosition=sDownload.size()-1;}
                songChange();
            }
        });
        sNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop(); mediaPlayer.release(); updateSeek.interrupt();
                if(sPosition!=sDownload.size()-1){ sPosition=sPosition+1;}
                else {sPosition=0;}
                songChange();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                sNext.performClick();
            }
        });
}

    private void songChange() {
        Uri uri = Uri.parse(sDownload.get(sPosition).getSongLink());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        sTitle.setText(sDownload.get(sPosition).getSongTitle());
        sPlay_pause.setImageResource(R.drawable.pause);
        seekBar.setMax(mediaPlayer.getDuration());
        changeSeekBar();
    }
    private void changeSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while(currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();
    }
}