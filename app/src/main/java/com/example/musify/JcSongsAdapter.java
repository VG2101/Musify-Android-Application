package com.example.musify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JcSongsAdapter extends RecyclerView.Adapter<JcSongsAdapter.SongAdapterViewHolder> {

    private final Context context;
    private List<GetSongs> arrayListSongs;
    RecyclerItemClickListner listner;
    int click=0;

    public JcSongsAdapter(Context context, List<GetSongs> arrayListSongs,RecyclerItemClickListner listner) {
        this.context = context;
        this.arrayListSongs = arrayListSongs;
        this.listner=listner;
    }

    @NonNull
    @Override
    public SongAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.songs_row,parent,false);
        return new SongAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapterViewHolder holder, int position) {
        GetSongs getSongs=arrayListSongs.get(position);
        holder.getTvTitle().setText(getSongs.getSongTitle());
        String duration= Utility.convertDuration(Long.parseLong(getSongs.getSongDuration()));
        holder.getTvDuration().setText(duration);
        holder.bind(getSongs,listner);
    }

    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }

    public class SongAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvDuration;
        private ImageView songArt;
        public SongAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=(TextView) itemView.findViewById(R.id.tv_title);
            tvDuration=(TextView) itemView.findViewById(R.id.tv_duration);
            songArt=(ImageView) itemView.findViewById(R.id.iv_artwork);

        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvDuration() {
            return tvDuration;
        }

        public ImageView getSongArt() {
            return songArt;
        }

        public void bind(GetSongs getSongs,RecyclerItemClickListner listner) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   listner.onClickListner(getSongs,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerItemClickListner {
        void onClickListner(GetSongs getSongs,int position);
    }
}
