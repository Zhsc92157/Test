package com.zhsc.test.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhsc.test.R;
import com.zhsc.test.impl.MyAlbumInterface;

import java.util.List;

public class MyImageViewAdapter extends RecyclerView.Adapter<MyImageViewAdapter.ViewHolder> {

    List<String> imagePathList;

    MyAlbumInterface albumInterface;

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

    public void setSelectItemImageInterface(MyAlbumInterface myAlbumInterface){
        albumInterface = myAlbumInterface;
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

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(imageViewPath,options);
                Log.e("options",""+options.outWidth);
                int width = options.outWidth;
                int height = options.outHeight;

                if (albumInterface!=null) {
                    albumInterface.selectItem(imageViewPath, width, height);
                }

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
