package com.example.musify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class OfflineModeAdapter extends RecyclerView.Adapter<OfflineModeAdapter.OffViewHolder> {

    private ArrayList<File> localDataSet;
    Context context;
    Intent intent;

    public OfflineModeAdapter(Context context,ArrayList<File> localDataSet) {
        this.localDataSet = localDataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public OffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.songs_row,parent,false);
        return new OffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffViewHolder holder, int position) {
        MediaMetadataRetriever metadataRetriever=new MediaMetadataRetriever();
        metadataRetriever.setDataSource(localDataSet.get(position).getPath());
        String s=localDataSet.get(position).getName().toString().replace(".mp3","");
        holder.getOffTitle().setText(s);
        String duration=Utility.convertDuration(Long.parseLong(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        holder.getOffDuration().setText(duration);
        byte []oByte; oByte=metadataRetriever.getEmbeddedPicture();
        if (oByte!=null){
            Bitmap bitmap=BitmapFactory.decodeByteArray(oByte,0,oByte.length);
            holder.getOffSongPic().setImageBitmap(bitmap);
        }
        else { holder.getOffSongPic().setImageResource(R.drawable.logo);}
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public class OffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView offTitle, offDuration;
        private ImageView offSongPic;
        public OffViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            offTitle=itemView.findViewById(R.id.tv_title);
            offDuration=itemView.findViewById(R.id.tv_duration);
            offSongPic=itemView.findViewById(R.id.iv_artwork);
        }

        public TextView getOffTitle() {
            return offTitle;
        }

        public TextView getOffDuration() {
            return offDuration;
        }

        public ImageView getOffSongPic() {
            return offSongPic;
        }

        @Override
        public void onClick(View view) {
            int p=getAdapterPosition();
          intent=new Intent(context,OfflinePlayerActivity.class);
          intent.putExtra("offlineSongsList",localDataSet);
          intent.putExtra("position",p);
          context.startActivity(intent);
        }
    }
}
