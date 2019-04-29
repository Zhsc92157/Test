package com.zhsc.test;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhsc.test.adapter.MyImageViewAdapter;
import com.zhsc.test.entity.User;
import com.zhsc.test.impl.MyAlbumInterface;
import com.zhsc.test.impl.MyPopWindowSelectListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static android.os.Environment.DIRECTORY_DCIM;

public class AlbumActivity extends AppCompatActivity {

    List<String> picturePathList = new ArrayList<>();
    List<String> imageFolderList = new ArrayList<>();

    TextView selectedTextView = null; //弹出菜单选择
    ImageView backImageView = null; //回退
    RecyclerView mRecyclerView = null;  //显示图片的recyclerView
    ImageView imageViewItem = null;

    MyPopUpWindow myPopUpWindow;

    ImageSelectedListener imageSelectedListener;

    String call_mode = null;
    String imagePathSelected = null;
    int imageWidth = 0;
    int imageHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getApplicationContext(),"aacd4289a9b9bc7135ae79bf1e765687");
        setContentView(R.layout.activity_album);

        call_mode = getIntent().getStringExtra("from");

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
        //处理回调
        imageSelectedListener = new ImageSelectedListener();
        adapter.setSelectItemImageInterface(imageSelectedListener);

        mRecyclerView.setAdapter(adapter);

        ClickListener listener = new ClickListener();

        imageViewItem.setOnClickListener(listener);
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
                if (call_mode.equals("MainFragment"))
                    finish();
                if (call_mode.equals("MyselfEdit")){
                    Intent intent = new Intent(getApplicationContext(),EditMyselfActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else if (v.getId() == R.id.selectedText){
                showPopUpMenu();
            }
        }
    }

    /**
     * 获取点击图片的信息
     */
    class ImageSelectedListener implements MyAlbumInterface{

        @Override
        public void selectItem(String imagePath, int width, int height) {
            imagePathSelected = imagePath;
            imageWidth = width;
            imageHeight = height;
            final Intent intent;
            if (call_mode.equals("MainFragment")){
                intent = new Intent(getApplicationContext(),ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FilePath",imagePathSelected);
                bundle.putInt("FileWidth",imageWidth);
                bundle.putInt("FileHeight",imageHeight);
                intent.putExtras(bundle);
                startActivity(intent);
            }else if (call_mode.equals("MyselfEdit")){
                intent = new Intent(getApplicationContext(),EditMyselfActivity.class);
                //更新用户的头像数据
                final User user = BmobUser.getCurrentUser(User.class);
                user.setPath(imagePathSelected);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            intent.putExtra("update",true);
                            Toast.makeText(getApplicationContext(),"更新用户信息成功",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            intent.putExtra("update",false);
                            Toast.makeText(getApplicationContext(),"更新用户信息失败",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }
    }

    private void showPopUpMenu() {
        myPopUpWindow = new MyPopUpWindow(getApplicationContext(),(ArrayList<String>)imageFolderList);
        myPopUpWindow.setSelectItemListener(new MyPopWindowSelectListener() {
            @Override
            public void selectItem(String name, int position) {
                Log.e( "selectItem: ", name);
                initPicturePathList(imageFolderList,position);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
                mRecyclerView.setLayoutManager(layoutManager);
                MyImageViewAdapter adapter = new MyImageViewAdapter(picturePathList);
                imageSelectedListener = new ImageSelectedListener();
                adapter.setSelectItemImageInterface(imageSelectedListener);
                mRecyclerView.setAdapter(adapter);
                if(myPopUpWindow.isShowing()&&myPopUpWindow!=null)
                    myPopUpWindow.dismiss();
            }
        });
        myPopUpWindow.showAsDropDown(selectedTextView,0,-0);
    }

}
