package com.zhsc.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhsc.test.entity.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import static com.zhsc.test.service.AuthService.getAuth;

public class SplashActivity extends AppCompatActivity {

    public static String accessToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottonUIMune();
        this.fullScreen();
        setContentView(R.layout.activity_splash);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");
        SpaThread thread = new SpaThread("SplashThread");
        thread.start();

    }

    public class SpaThread extends Thread{

        SpaThread(String name) {
            this.setName(name);
        }
        @Override
        public void run() {
            try {
                sleep(2000);//休眠
                accessToken = getAuth();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BmobUser bmobUser = BmobUser.getCurrentUser(User.class);
            if (bmobUser!=null) {
                Intent intent = new Intent(getApplicationContext(), BottomBarActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void fullScreen(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    protected void hideBottonUIMune(){
        //
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


}
