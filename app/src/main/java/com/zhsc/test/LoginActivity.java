package com.zhsc.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button bt_login;
    Button bt_reg;

    Listener listener = new Listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        initView();

    }

    private void initView() {

        username = findViewById(R.id.editTextLogInUsername);
        username.setText("");
        password = findViewById(R.id.editTextLogInPassword);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password.setText("");
        bt_login = findViewById(R.id.button_login_fragment);
        bt_reg = findViewById(R.id.button_reg_fragment);

        bt_login.setOnClickListener(listener);
        bt_reg.setOnClickListener(listener);

    }


    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_reg_fragment){
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }else if (v.getId() == R.id.button_login_fragment){
                login();
            }
        }
    }

    private void login() {

        final BmobUser user = new BmobUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    BmobUser get = BmobUser.getCurrentUser(BmobUser.class);
                    loginSuccess();
                    Toast.makeText(getApplicationContext(), "登录成功：" + get.getUsername(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "登录失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginSuccess() {
        Intent intent = new Intent(getApplicationContext(),BottomBarActivity.class);
        startActivity(intent);
        finish();
    }

}
