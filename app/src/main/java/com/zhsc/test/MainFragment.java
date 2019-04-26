package com.zhsc.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhsc.test.util.PermissionUtil;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MainFragment extends Fragment{

    private static int CAMERA_REQUEST_CODE = 0;    //拍照回传码
    private static int ALBUM_REQUEST_CODE = 1;  //相册回传码

    String mFilePath = null;

    TextView textView = null;
    Button bt_photo = null;
    Button bt_album = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.main_fragment_text);
        bt_photo = view.findViewById(R.id.bt_main_fragment_photo);
        bt_album = view.findViewById(R.id.bt_main_fragment_album);

        textView.setText("OCR");
        textView.setTextSize(40);

        Listener listener = new Listener();

        bt_photo.setOnClickListener(listener);
        bt_album.setOnClickListener(listener);

        //Android 7.0以上系统解决拍照问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.bt_main_fragment_photo){         //单击拍照
                if (PermissionUtil.hasPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)) {
                    startCamera();
                }
            }else if (v.getId() == R.id.bt_main_fragment_album){   //单击相册
                if(PermissionUtil.hasPermission(getActivity().getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    //startSystemAlbum();
                    startAlbum();
                }
            }
        }
    }

    /**
     * 启动相机
     */
    private void startCamera() {
        Toast.makeText(getActivity().getApplicationContext(),"Open Camera",Toast.LENGTH_SHORT).show();

        if(PermissionUtil.hasPermission(getActivity().getApplicationContext(),
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
            } else{
                Log.e("MainActivity.java", "请确认已经插入SD卡");
            }
        }

    }

    /**
     * 启动相册
     */
    private void startAlbum(){
        Toast.makeText(getActivity().getApplicationContext(),"Open Album",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("from","MainFragment");
        intent.setClass(getActivity(),AlbumActivity.class);
        startActivity(intent);
    }

    private void startSystemAlbum() {

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,ALBUM_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(getActivity().getApplicationContext(),
                        "get picture,path = "+mFilePath,Toast.LENGTH_SHORT).show();
                displayImage(mFilePath);
            }else if (resultCode == RESULT_CANCELED){

            }else{

            }
        }else if(requestCode == ALBUM_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                handleImageOnKitKat(data);
            }
        }
    }

    /**
     * 2019/1/23
     * @param mFilePath
     */
    private void displayImage(String mFilePath) {
        File file = new File(mFilePath);
        Bitmap bitmap = BitmapFactory.decodeFile(mFilePath);
        Bundle basket = new Bundle();
        basket.putString("FilePath",mFilePath);
        basket.putInt("FileWidth",bitmap.getWidth());
        basket.putInt("FileHeight",bitmap.getHeight());
        Intent intent = new Intent(getActivity().getApplicationContext(), ImageActivity.class);
        intent.putExtras(basket);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity().getApplicationContext(),uri)){
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
        Cursor c = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(c!=null){
            if(c.moveToNext()){
                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            c.close();
        }
        return path;
    }

}
