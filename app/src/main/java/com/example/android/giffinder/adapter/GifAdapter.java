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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifViewHolder>{
    private List<String> imageUrlList;
    private Context context;

    public GifAdapter(Context context, List<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
    }

    @NonNull
    @Override
    public GifAdapter.GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gifs, parent, false);
        return new GifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GifAdapter.GifViewHolder holder, int position) {
        String imageUrl = imageUrlList.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.gifImageView);
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
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
