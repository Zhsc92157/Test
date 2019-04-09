package com.zhsc.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.image);

        Bundle getBasket = getIntent().getExtras();
        String path = getBasket.getString("FilePath");

        Glide.with(this).load(path).into(imageView);

    }
}
