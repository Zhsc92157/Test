package com.zhsc.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhsc.test.adapter.ResultListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ImageView imageViewBack = null;//回退键
    private ListView listView = null;//显示结果的List
    private ArrayList<String> textList = new ArrayList<>();//存放识别出的text的String
    private List<ResultListItem> resultList = new ArrayList<>();//存放listItem的list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initView();

        ResultListAdapter resultListAdapter = new ResultListAdapter(ResultActivity.this,R.layout.result_list_item,resultList);
        listView.setAdapter(resultListAdapter);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        //获取所得的结果list
        textList = getIntent().getStringArrayListExtra("resultList");
        //初始化结果信息list
        for(int i=0;i<textList.size();i++){
            Log.e("textList"+i,textList.get(i));
            deal(textList.get(i));
            ResultListItem resultListItem = new ResultListItem(R.drawable.img_delete,textList.get(i));
            resultList.add(resultListItem);
        }

        imageViewBack = findViewById(R.id.back);
        listView = findViewById(R.id.result_listView);

    }

    private int deal(String s) {
        return 0;
    }
}
