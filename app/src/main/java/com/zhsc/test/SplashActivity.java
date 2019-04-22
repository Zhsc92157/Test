package com.zhsc.test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

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

        public SpaThread(String name) {
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
            Intent intent = new Intent(getApplicationContext(),BottomBarActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void fullScreen(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    protected void hideBottonUIMune(){
        //
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }else if (Build.VERSION.SDK_INT >= 19){
            View decorView = this.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


}
