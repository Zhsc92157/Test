package com.zhsc.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhsc.test.entity.User;
import com.zhsc.test.util.CodeUtil;
import com.zhsc.test.util.ValidatorUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    ImageView confirmImage;

    EditText username;
    EditText password;
    EditText password_again;
    EditText getCode;
    String code;


    Button bt_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        confirmImage = findViewById(R.id.confirmCodeImage);
        username = findViewById(R.id.editTextRegUsername);
        password = findViewById(R.id.editTextRegPassword);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password_again = findViewById(R.id.editTextRegPasswordAgain);
        password_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
        getCode = findViewById(R.id.editTextRegConfirm);

        initConfirmCode();

        bt_reg = findViewById(R.id.button_reg_fragment);

        confirmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConfirmCode();
            }
        });

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是不是都填了
                if (username.getText().toString().equals("") ||
                        password.getText().toString().equals("") ||
                        password_again.getText().toString().equals("") ||
                        getCode.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请完善注册信息", Toast.LENGTH_SHORT).show();
                }else{
                    //用户名是否规范
                    if(ValidatorUtil.isUserName(username.getText().toString())) {
                        if (ValidatorUtil.isPassword(password.getText().toString())) {
                            if (password.getText().toString().equals(password_again.getText().toString())) {
                                if (getCode.getText().toString().equalsIgnoreCase(code)) {
                                    signUp();
                                }else
                                    Toast.makeText(getApplicationContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(getApplicationContext(), "两次密码不正确", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"密码长度要求6-16位",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"用户名格式为英文或英文＋数字，长度5-17位",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void initConfirmCode() {

        Bitmap bitmap = CodeUtil.getInstance().createBitmap();
        code = CodeUtil.getCode();
        confirmImage.setImageBitmap(bitmap);

    }

    private void signUp() {

        final User user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
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
