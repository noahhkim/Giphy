package com.example.android.giffinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.giffinder.R;
import com.example.android.giffinder.data.room.Gif;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifViewHolder> {
    private List<Gif> mGifs;
    private Context context;

    public GifAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GifAdapter.GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gifs, parent, false);
        return new GifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GifAdapter.GifViewHolder holder, int position) {
        Gif gif = mGifs.get(position);
        Glide.with(context)
                .load(gif.getGifUrl())
                .into(holder.gifImageView);
    }

    public void setGifs(List<Gif> gifs) {
        mGifs = gifs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mGifs != null) {
            return mGifs.size();
        } else return 0;
    }

    public class GifViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gif_images)
        ImageView gifImageView;

        public GifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
