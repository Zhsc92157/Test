package com.zhsc.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    String user_id;
    String user_pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottonUIMune();
        this.fullScreen();
        setContentView(R.layout.activity_splash);

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //跳转
            SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
            user_id = sp.getString("ocr_username",null);
            user_pd = sp.getString("ocr_password",null);
            Log.e("SplashActivity",user_id+"*******************************"+user_pd);
            if (user_id == null||user_pd == null) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }else
            {
                Intent intent = new Intent(getApplicationContext(),BottomBarActivity.class);
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
