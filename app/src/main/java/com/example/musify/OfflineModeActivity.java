package com.example.musify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OfflineModeActivity extends AppCompatActivity {

    RecyclerView recyclerView2;
    ArrayList<File> offSongs1;
    Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_mode);
        recyclerView2 = findViewById(R.id.recyclerview_id2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(OfflineModeActivity.this));
        intent2=getIntent();
        Bundle bundle = intent2.getExtras();
        offSongs1 = (ArrayList) bundle.getParcelableArrayList("localSongs");
        OfflineModeAdapter offlineModeAdapter=new OfflineModeAdapter(OfflineModeActivity.this,offSongs1);
        recyclerView2.setAdapter(offlineModeAdapter);
    }
}