package com.zhsc.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhsc.test.R;
import com.zhsc.test.ResultListItem;

import java.util.List;

public class ResultListAdapter extends ArrayAdapter<ResultListItem> {

    private int textViewId;

    class ViewHolder{
        ImageView image;
        TextView text;
    }

    public ResultListAdapter(Context context,int textViewResourceId,List<ResultListItem> list){
        super(context,textViewResourceId,list);
        textViewId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResultListItem resultListItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(textViewId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.image = view.findViewById(R.id.result_image);
            viewHolder.text = view.findViewById(R.id.result_text);
            view.setTag(viewHolder);//viewHolder存储在view中
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.text.setText(resultListItem.getText());
        viewHolder.image.setImageResource(resultListItem.getImgId());

        return view;
    }
}
