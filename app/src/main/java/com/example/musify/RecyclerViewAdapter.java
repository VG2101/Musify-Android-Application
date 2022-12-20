package com.example.musify;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    Context mContext;
    private List<Download> download;
    private Download upload;
    Intent intent;

    public RecyclerViewAdapter(Context mContext, List<Download> uploads) {
        this.mContext = mContext;
        this.download = uploads;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater =LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.card_view_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        upload = download.get(position);
        holder.tvBookTitle.setText(upload.getName());

        Glide.with(mContext).load(upload.getUrl()).into(holder.ivBookThumbnail);

    }

    @Override
    public int getItemCount() {
        return download.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvBookTitle;
        ImageView ivBookThumbnail;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvBookTitle=itemView.findViewById(R.id.book_text_id);
            ivBookThumbnail=itemView.findViewById(R.id.book_image_id);
            cardView=itemView.findViewById(R.id.cardViewId);
        }

        @Override
        public void onClick(View view) {
            int p=this.getAdapterPosition();
            intent = new Intent(mContext,SongsActivity.class);
            mContext.startActivity(intent);
        }
    }

}
