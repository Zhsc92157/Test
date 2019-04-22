package com.zhsc.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhsc.test.entity.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginFragment extends Fragment {

    EditText username;
    EditText password;
    Button bt_login;
    Button bt_reg;

    ProgressDialog progressDialog;

    Listener listener = new Listener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bmob.initialize(getActivity().getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        username = view.findViewById(R.id.editTextLogInUsername);
        password = view.findViewById(R.id.editTextLogInPassword);
        bt_login = view.findViewById(R.id.button_login_fragment);
        bt_reg = view.findViewById(R.id.button_reg_fragment);

        bt_login.setOnClickListener(listener);
        bt_reg.setOnClickListener(listener);

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("信息提示");
        progressDialog.setMessage("正在加载中，请稍后...");
        progressDialog.setCancelable(true);

    }


    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_reg_fragment){
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
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
                    User get = BmobUser.getCurrentUser(User.class);
                    Toast.makeText(getContext(), "登录成功：" + get.getUsername(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "登录失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
