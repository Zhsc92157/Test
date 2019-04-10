package com.zhsc.test;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhsc.test.com.zhsc.test.com.zhsc.test.adapter.MyImageViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DCIM;

public class AlbumActivity extends AppCompatActivity {

    List<String> picturePathList = new ArrayList<>();
    List<String> imageFolderList = new ArrayList<>();

    TextView selectedTextView = null; //弹出菜单选择
    ImageView backImageView = null; //回退
    RecyclerView mRecyclerView = null;  //显示图片的recyclerView
    ImageView imageViewItem = null;

    MyPopUpWindow myPopUpWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        selectedTextView = findViewById(R.id.selectedText);
        backImageView = findViewById(R.id.back);
        mRecyclerView = findViewById(R.id.recyclerView_album);

        final FrameLayout frameLayout = (FrameLayout)getLayoutInflater().
                inflate(R.layout.album_imgitem,null);
        imageViewItem = frameLayout.findViewById(R.id.img_photo);

        imageFolderList = getImgFolderList(); //获取相册信息

        initPicturePathList(imageFolderList,0);//初始化相册中的照片数据

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        MyImageViewAdapter adapter = new MyImageViewAdapter(picturePathList);
        mRecyclerView.setAdapter(adapter);

        ClickListener listener = new ClickListener();

        backImageView.setOnClickListener(listener);
        selectedTextView.setOnClickListener(listener);

    }

    /**
     * 初始化一个存放该文件夹下所有图片的路径的list
     * @param imgFolderList 存放相片的文件夹的路径list
     * @param position 哪个文件夹
     */
    private void initPicturePathList(List<String> imgFolderList,int position) {
        File readFile = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM);
        if (readFile == null){
            return;
        }
        String path = imgFolderList.get(position);
        String folderName = path.substring(path.lastIndexOf("/")+1,path.length());
        selectedTextView.setText(folderName);
        picturePathList = getPicture(path);
        if (picturePathList == null){
            Log.e("List为空","picturePathList");
            return;
        }
    }

    /**
     * 获得目录下的所有图片
     * @param path 目录
     * @return 返回一个包含所有图片路径的list
     */
    public List<String> getPicture(final String path){
        List<String> pictureList = new ArrayList<String>();
        File file = new File(path);
        File[] allFiles = file.listFiles();
        if(allFiles == null){
            return null;
        }
        for(int k = 0; k<allFiles.length;k++) {
            final File pictureFiles = allFiles[k];
            if (pictureFiles.isFile()) {
                int index = pictureFiles.getPath().lastIndexOf(".");
                if (index <= 0) {
                    continue;
                }
                String suffix = pictureFiles.getPath().substring(index);
                if (suffix.toLowerCase().equals(".jpg") ||
                        suffix.toLowerCase().equals(".jpeg") ||
                        suffix.toLowerCase().equals(".bmp") ||
                        suffix.toLowerCase().equals(".png")) {
                    pictureList.add(pictureFiles.getPath());
                }
            }
        }
        return pictureList;
    }

    //得到图片文件夹集合
    public List<String> getImgFolderList(){
        List<String> imgFolderList = new ArrayList<String>();
        ContentResolver mContentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = null;
        //扫描图片
        try{
            cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE+ "= ? or "+ MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/png","image/jpeg"},MediaStore.Images.Media.DATE_MODIFIED);

            while(cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//路径
                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                    continue;
                String dir = parentFile.getAbsolutePath();

                if (!imgFolderList.contains(dir)){
                    imgFolderList.add(dir);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return imgFolderList;
    }

    class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back){
                finish();
            }else if (v.getId() == R.id.selectedText){
                showPopUpMenu();
            }
        }
    }

    private void showPopUpMenu() {
        myPopUpWindow = new MyPopUpWindow(getApplicationContext(),(ArrayList<String>)imageFolderList);
        myPopUpWindow.setSelectItemListener(new MyPopUpWindow.SelectItemListener() {
            @Override
            public void selectItem(String name, int position) {
                Log.e( "selectItem: ", name);
                initPicturePathList(imageFolderList,position);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
                mRecyclerView.setLayoutManager(layoutManager);
                MyImageViewAdapter adapter = new MyImageViewAdapter(picturePathList);
                mRecyclerView.setAdapter(adapter);
                if(myPopUpWindow.isShowing()&&myPopUpWindow!=null)
                    myPopUpWindow.dismiss();
            }
        });
        myPopUpWindow.showAsDropDown(selectedTextView,0,-0);
    }

}
