package com.zhsc.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zhsc.test.adapter.MyFolderListAdapter;
import com.zhsc.test.impl.MyPopWindowSelectListener;

import java.util.ArrayList;

/**
 * 相册popupwindow
 */
public class MyPopUpWindow extends PopupWindow {

    private ArrayList<String> imageFolderList = new ArrayList<String>();
    private MyFolderListAdapter adapter;
    private ListView folderListView;
    private Context context;
    private MyPopWindowSelectListener selectItemListener;

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
            String folderName = list.get(i).substring(list.get(i).lastIndexOf("/")+1,list.get(i).length());
            imageFolderList.add(folderName);
            Log.e("添加文件夹名",imageFolderList.get(i));
        }

        adapter = new MyFolderListAdapter(context,imageFolderList);
        folderListView.setAdapter(adapter);

        Listener listener = new Listener();

        folderListView.setOnItemClickListener(listener);

    }


    public void setSelectItemListener(MyPopWindowSelectListener listener){
        selectItemListener = listener;
    }


    public class Listener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (selectItemListener != null)
                selectItemListener.selectItem(imageFolderList.get((int)id),position);
            Toast.makeText(view.getContext(),"position:"+position+" id:"+id,Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

}
