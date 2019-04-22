package com.zhsc.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText password_again;

    Button bt_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");
        username = findViewById(R.id.editTextRegUsername);
        password = findViewById(R.id.editTextRegPassword);

        bt_reg = findViewById(R.id.button_reg_fragment);

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {

        final BmobUser user = new BmobUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"尚未失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
