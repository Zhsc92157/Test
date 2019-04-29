package com.zhsc.test;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhsc.test.entity.User;
import com.zhsc.test.util.PermissionUtil;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditMyselfActivity extends AppCompatActivity {

    private static int CAMERA_REQUEST_CODE = 0;//

    ImageView back;
    ImageView image_edit;
    TextView text_nickname;
    TextView text_username;

    RelativeLayout layout_image;
    RelativeLayout layout_nickname;
    RelativeLayout layout_photo;
    RelativeLayout layout_album;
    RelativeLayout layout_cancel;

    //存储修改头像时拍照或相册选取照片的变量
    String mFilePath;

    Listener listener;

    ChoosePicturePopUpWindow popUpWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myself);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInformation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserInformation();
    }

    private void initView() {
        back = findViewById(R.id.back);
        image_edit = findViewById(R.id.editActivity_image);
        text_nickname = findViewById(R.id.editActivity_nickname);
        text_username = findViewById(R.id.editActivity_username);
        layout_image = findViewById(R.id.relativeLayout_editImage);
        layout_nickname = findViewById(R.id.relativeLayout_editNickname);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.choosepic_popupwindow_layout,null);
        layout_photo = view.findViewById(R.id.layout_choosePic_popUpWindow_take_photo);
        layout_album = view.findViewById(R.id.layout_choosePic_popUpWindow_album);
        layout_cancel = view.findViewById(R.id.layout_choosePic_popUpWindow_cancel);

        getUserInformation();

        listener = new Listener();
        popUpWindow = new ChoosePicturePopUpWindow(getApplicationContext(),listener);

        back.setOnClickListener(listener);
        layout_image.setOnClickListener(listener);
        layout_nickname.setOnClickListener(listener);


    }

    private void getUserInformation() {

        final User user = BmobUser.getCurrentUser(User.class);
        if (user.getNickname()==null){
            text_nickname.setText("昵称未设置");
        }else{
            text_nickname.setText(user.getNickname());
        }
        if (user.getPath()!=null){
            Glide.with(getApplicationContext()).load(new File(user.getPath())).into(image_edit);
        }
        text_username.setText(user.getUsername());

    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back) {
                finish();
            }
            if (v.getId() == R.id.relativeLayout_editImage){
                showPopUpWindow();
            }
            if (v.getId() == R.id.relativeLayout_editNickname){
                //跳转到一个修改昵称的界面
                startActivity(new Intent(getApplicationContext(),EditNicknameActivity.class));
                finish();
            }
            if (v.getId() == R.id.layout_choosePic_popUpWindow_cancel){
                if (popUpWindow!=null&&popUpWindow.isShowing()){
                    popUpWindow.dismiss();
                }
            }
            if (v.getId() == R.id.layout_choosePic_popUpWindow_take_photo){
                if (popUpWindow!=null&&popUpWindow.isShowing()){
                    popUpWindow.dismiss();
                }
                startCamera();
            }
            if (v.getId() == R.id.layout_choosePic_popUpWindow_album){
                if (popUpWindow!=null&&popUpWindow.isShowing()){
                    popUpWindow.dismiss();
                }
                startAlbum();
            }
        }
    }

    private void showPopUpWindow() {
        //屏幕变暗
       // ChoosePicturePopUpWindow popUpWindow = new ChoosePicturePopUpWindow(getApplicationContext(),listener);

        if (popUpWindow!=null&&popUpWindow.isShowing()){
            popUpWindow.dismiss();
        }
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha=0.3f;
        getWindow().setAttributes(layoutParams);

        popUpWindow.showAtLocation(findViewById(R.id.editMyselfActivityLayout), Gravity.BOTTOM,0,0);

        popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //屏幕变亮
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });

    }

    private void startAlbum() {
        Toast.makeText(getApplicationContext(),"Open Album",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("from","MyselfEdit");
        intent.setClass(getApplicationContext(),AlbumActivity.class);
        startActivity(intent);
        finish();
    }

    private void startCamera() {
        Toast.makeText(getApplicationContext(),"Open Camera",Toast.LENGTH_SHORT).show();

        if(PermissionUtil.hasPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)) {
                //调用系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
                mFilePath = outFile.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } else{
                Log.e("EditMyselfActivity.java", "请确认已经插入SD卡");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),
                        "get picture,path = " + mFilePath, Toast.LENGTH_SHORT).show();
                updateImagePath(mFilePath);
            }
        }
    }

    private void updateImagePath(String mFilePath){
        final User user = BmobUser.getCurrentUser(User.class);
        user.setPath(mFilePath);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                    recreate();
                }else{
                    Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
