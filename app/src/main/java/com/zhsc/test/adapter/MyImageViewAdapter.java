package com.zhsc.test.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhsc.test.ImageActivity;
import com.zhsc.test.R;

import java.util.List;

public class MyImageViewAdapter extends RecyclerView.Adapter<MyImageViewAdapter.ViewHolder> {

    List<String> imagePathList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(View v){
            super(v);
            imageView = v.findViewById(R.id.img_photo);
        }

    }

    public MyImageViewAdapter(List<String> list){
        imagePathList = list;
    }

    @Override
    public MyImageViewAdapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_imgitem,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String imageViewPath = imagePathList.get(position);
                Bundle basket = new Bundle();
                basket.putString("FilePath",imageViewPath);
                Intent intent = new Intent(v.getContext(), ImageActivity.class);
                intent.putExtras(basket);
                v.getContext().startActivity(intent);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(MyImageViewAdapter.ViewHolder viewHolder, int position) {

        Glide.with(viewHolder.imageView.getContext())
                .load(imagePathList.get(position))
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

}
