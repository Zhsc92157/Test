package com.zhsc.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhsc.test.entity.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditNicknameActivity extends AppCompatActivity {

    ImageView back;

    TextView button;

    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        back = findViewById(R.id.back);
        text = findViewById(R.id.editTestNickname);
        button = findViewById(R.id.editNicknameButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditMyselfActivity.class));
                finish();
            }
        });
        text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                button.setText("修改");
            }
        });
        final User user = BmobUser.getCurrentUser(User.class);
        if (user.getNickname()!=null){
            text.setHint(user.getNickname());
        }else{
            text.setHint("请输入宝贝昵称");
        }
        text.setHintTextColor(Color.parseColor("#808080"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新用户的昵称数据
                final User user = BmobUser.getCurrentUser(User.class);
                user.setNickname(text.getText().toString());
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),EditMyselfActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
