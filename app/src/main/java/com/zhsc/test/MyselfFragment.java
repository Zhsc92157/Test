package com.zhsc.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhsc.test.entity.User;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class MyselfFragment extends Fragment {

    ImageView image_touxiang;
    ImageView image_edit;
    TextView nickname;

    RelativeLayout cuotiben;
    RelativeLayout xuexizhoubao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myself, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        initView(view);
        
        getUserInformation();
    }

    private void getUserInformation() {

        final User user = BmobUser.getCurrentUser(User.class);
        if (user.getNickname()==null){
            nickname.setText("昵称未设置");
        }
        if (user.getPath()!=null){
            Glide.with(getApplicationContext()).load(new File(user.getPath())).into(image_touxiang);
        }

    }

    private void initView(View view) {
        image_touxiang = view.findViewById(R.id.myself_touxiang);
        image_edit = view.findViewById(R.id.myself_edit);
        nickname = view.findViewById(R.id.myself_touxiang_text);
        cuotiben = view.findViewById(R.id.myself_cuotiben);
        xuexizhoubao = view.findViewById(R.id.myself_xuexizhoubao);

        Listener listener = new Listener();
        image_edit.setOnClickListener(listener);
        cuotiben.setOnClickListener(listener);
        xuexizhoubao.setOnClickListener(listener);

    }

    class Listener implements View.OnClickListener{
        int id;
        @Override
        public void onClick(View v) {
            id = v.getId();
            if (id == R.id.myself_cuotiben){
                //TODO 错题本
            }else if (id == R.id.myself_xuexizhoubao){
                //TODO 学习周报
            }else if (id == R.id.myself_edit){
                //TODO 编辑
                Intent intent = new Intent(getApplicationContext(),EditMyselfActivity.class);
                startActivity(intent);
            }
        }
    }

}
