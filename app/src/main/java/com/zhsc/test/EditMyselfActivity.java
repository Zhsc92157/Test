package com.zhsc.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhsc.test.entity.User;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class EditMyselfActivity extends AppCompatActivity {

    ImageView back;
    ImageView image_edit;
    TextView text_nickname;
    TextView text_username;

    RelativeLayout layout_image;
    RelativeLayout layout_nickname;

    final User user = BmobUser.getCurrentUser(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myself);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        initView();

    }

    private void initView() {
        back = findViewById(R.id.back);

        image_edit = findViewById(R.id.editActivity_image);
        if (user.getPath()!=null){
            Glide.with(getApplicationContext()).load(new File(user.getPath())).into(image_edit);
        }
        text_nickname = findViewById(R.id.editActivity_nickname);
        if (user.getNickname()!=null){
            text_nickname.setText(user.getNickname());
        }
        text_username = findViewById(R.id.editActivity_username);
        text_username.setText(user.getUsername());

        layout_image = findViewById(R.id.relativeLayout_editImage);
        layout_nickname = findViewById(R.id.relativeLayout_editNickname);

        Listener listener = new Listener();

        back.setOnClickListener(listener);
        layout_image.setOnClickListener(listener);
        layout_nickname.setOnClickListener(listener);

    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back)
                finish();
            if (v.getId() == R.id.relativeLayout_editImage){

            }
            if (v.getId() == R.id.relativeLayout_editNickname){

            }
        }
    }

}
