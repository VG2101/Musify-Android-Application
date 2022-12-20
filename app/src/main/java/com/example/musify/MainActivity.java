package com.example.musify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<Download> downloads;
    private ArrayList<File> offSongs;
    Intent intent;
    FloatingActionButton fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        fb=findViewById(R.id.changeOffline);

        progressDialog=new ProgressDialog(this);
        downloads=new ArrayList<>();
        progressDialog.setMessage("please wait.....");
        progressDialog.show();

        mDatabase= FirebaseDatabase.getInstance().getReference("uploads");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot postSnapshot: snapshot.getChildren() ) {
                    Download upload= postSnapshot.getValue(Download.class);
                    downloads.add(upload);
                }
                adapter=new RecyclerViewAdapter(MainActivity.this,downloads);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                offSongs = fetchSongs(Environment.getExternalStorageDirectory());
                Collections.sort(offSongs, new Comparator<File>() {
                    @Override
                    public int compare(File file, File t1) {
                        String s1 = file.getName();
                        String s2 = t1.getName();
                        return s1.compareToIgnoreCase(s2);
                    }
                });
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
       fb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               intent=new Intent(MainActivity.this,OfflineModeActivity.class);
               intent.putExtra("localSongs",offSongs);
               startActivity(intent);
           }
       });
    }
    private ArrayList<File> fetchSongs(File file) {
        Log.d("check","Search start");
        ArrayList<File> arrayList = new ArrayList();
        File [] song = file.listFiles() ;
        if(song !=null){
            for(File myfile: song){
                if (!myfile.isHidden() && myfile.isDirectory()){
                    arrayList.addAll(fetchSongs(myfile));
                }
                else {
                    if ( myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".") ){
                        arrayList.add(myfile);
                    }
                }
            }
        }
        return arrayList;
    }
}