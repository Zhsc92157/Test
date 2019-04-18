package com.zhsc.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhsc.test.helper.PermissionHelper;
import com.zhsc.test.impl.PermissionInterface;
import com.zhsc.test.util.CalculateUtil;
import com.zhsc.test.util.PermissionUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements PermissionInterface {

    private static int CAMERA_REQUEST_CODE = 0;    //拍照回传码
    private static int ALBUM_REQUEST_CODE = 1;  //相册回传码

    String mFilePath = null;

    private PermissionHelper permissionHelper;


    TextView textView = null;
    Button bt_photo = null;
    Button bt_album = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        bt_photo = findViewById(R.id.bt_photo);
        bt_album = findViewById(R.id.bt_album);

        textView.setText("OCR");
        textView.setTextSize(40);

        Listener listener = new Listener();

        bt_photo.setOnClickListener(listener);
        bt_album.setOnClickListener(listener);

        permissionHelper = new PermissionHelper(this,this);
        permissionHelper.requestPermissions();

        //Android 7.0以上系统解决拍照问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

        if (CalculateUtil.isCorrect("6-2*(3+5)=-10"))
            Log.e("afterCal","right");
        else
            Log.e("afterCal","false");

    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.bt_photo){         //单击拍照
                if (PermissionUtil.hasPermission(getApplicationContext(),Manifest.permission.CAMERA)) {
                    startCamera();
                }
            }else if (v.getId() == R.id.bt_album){   //单击相册
                if(PermissionUtil.hasPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    //startSystemAlbum();
                    startAlbum();
                }
            }
        }
    }

    /**
     * 重写Activity的权限请求返回结果方法
     * @param requestCode
     * @param permissions
     * @param grantResults
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

    @Override
    public String[] getPermissions() {
        String [] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET
        };
        return permissions;
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {
        finish();
    }

    /**
     * 启动相机
     */
    private void startCamera() {
        Toast.makeText(getApplicationContext(),"Open Camera",Toast.LENGTH_SHORT).show();

        if(PermissionUtil.hasPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)) {
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
                finish();
            } else{
                Log.e("MainActivity.java", "请确认已经插入SD卡");
            }
        }

    }

    /**
     * 启动相册
     */
    private void startAlbum(){
        Toast.makeText(getApplicationContext(),"Open Album",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,AlbumActivity.class);
        startActivity(intent);
    }

    private void startSystemAlbum() {

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,ALBUM_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(),
                        "get picture,path = "+mFilePath,Toast.LENGTH_SHORT).show();
                displayImage(mFilePath);

            }else if (resultCode == RESULT_CANCELED){
                //取消
            }else{
                Toast.makeText(getApplicationContext(),"capture fail",Toast.LENGTH_SHORT);
            }
        }else if(requestCode == ALBUM_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                if(Build.VERSION.SDK_INT >= 19){
                    handleImageOnKitKat(data);
                }else{
                    handleImageBeforeKitKat(data);
                }
            }
        }
    }


    /**
     * 2019/1/23
     * @param mFilePath
     */

    private void displayImage(String mFilePath) {
        Bundle basket = new Bundle();
        basket.putString("FilePath",mFilePath);
        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document型的uri 通过document id来处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor c = getContentResolver().query(uri,null,selection,null,null);
        if(c!=null){
            if(c.moveToNext()){
                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            c.close();
        }
        return path;
    }
}

