package com.zhsc.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhsc.test.com.zhsc.test.com.zhsc.test.adapter.MyFolderListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPopUpWindow extends PopupWindow {

    private ArrayList<String> imageFolderList = new ArrayList<String>();
    private MyFolderListAdapter adapter;
    private ListView folderListView;
    private Context context;
    private SelectItemListener selectItemListener;

    public MyPopUpWindow(Context context,ArrayList<String> list){
        this.context = context;

        View v = LayoutInflater.from(context).inflate(R.layout.album_popupwindow_layout,null);

        this.setContentView(v);
        this.setWidth(RecyclerView.LayoutParams.MATCH_PARENT);
        this.setHeight(800);
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        this.update();

        folderListView = v.findViewById(R.id.listView_folder);

        for (int i = 0;i<list.size();i++){
            imageFolderList.add(list.get(i).substring(list.get(i).lastIndexOf("/")+1,list.get(i).length()));
            Log.e("添加文件夹名",imageFolderList.get(i));
        }

        adapter = new MyFolderListAdapter(context,imageFolderList);
        folderListView.setAdapter(adapter);

        Listener listener = new Listener();

        folderListView.setOnItemClickListener(listener);

    }

    public interface SelectItemListener{
        void selectItem(String name,int position);
    }

    public void setSelectItemListener(SelectItemListener listener){
        selectItemListener = listener;
    }


    public class Listener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (selectItemListener != null)
                selectItemListener.selectItem(imageFolderList.get((int)id),position);
            Toast.makeText(view.getContext(),"position:"+position+" id:"+id,Toast.LENGTH_SHORT).show();
            /*AlbumActivity.menuIndex = position;
            View v = LayoutInflater.from(context).inflate(R.layout.activity_album,null);
            TextView selectedTextView = v.findViewById(R.id.selectedText);
            selectedTextView.setText(imageFolderList.get((int)id));*/
            dismiss();
        }
    }

}
