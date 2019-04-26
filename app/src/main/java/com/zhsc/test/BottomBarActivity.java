package com.zhsc.test;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.zhsc.test.helper.PermissionHelper;
import com.zhsc.test.impl.PermissionInterface;

public class BottomBarActivity extends AppCompatActivity implements PermissionInterface {

    BottomBar bottomBar;
    FrameLayout container = null;

    PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        bottomBar = findViewById(R.id.bottom_bar);

        container = findViewById(R.id.container);

        permissionHelper = new PermissionHelper(this,this);
        permissionHelper.requestPermissions();

        bottomBar.setContainer(R.id.container)
                .setTitleBeforeAndAfterColor("#999999","#000000")
                .addItem(MainFragment.class,"Main",R.drawable.main_unselected,R.drawable.main_selected)
                .addItem(MyselfFragment.class,"Me",R.drawable.result_unselected,R.drawable.result_selected)
                .build();

    }

    /**
     * 重写Activity的权限请求返回结果方法
     * @param requestCode 请求码
     * @param permissions 请求
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (permissionHelper.requestPermissionsResult(requestCode,permissions,grantResults))
            return;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 实现PermissionInterface的方法
     * @return
     */
    @Override
    public int getPermissionRequestCode() {
        return 92157;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {
        finish();
    }

    /**
     * 重写onActivityResult方法分发给fragment处理
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = this.getSupportFragmentManager().findFragmentById(0);
        if (f != null) {
            f.onActivityResult(requestCode,resultCode,data);
        }
    }

}
