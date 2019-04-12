package com.zhsc.test.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhsc.test.R;

import java.util.ArrayList;
public class MyFolderListAdapter extends BaseAdapter {

    private ArrayList<String> folderList;
    private Context context;

    public  MyFolderListAdapter(Context context,ArrayList<String> list){
        super();
        folderList = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.album_folderlist_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.listView_folder_item);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        String content = folderList.get(position);
        if(!TextUtils.isEmpty(content)){
            viewHolder.textView.setText(content);
        }
        return view;
    }

    class ViewHolder {
        public TextView textView;
    }
}

