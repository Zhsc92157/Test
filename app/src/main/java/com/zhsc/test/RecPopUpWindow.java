package com.zhsc.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecPopUpWindow extends PopupWindow {

    private Context context;
    private TextView text;

    public RecPopUpWindow(Context context, String result){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.recognition_popupwindow_layout,null);
        text = view.findViewById(R.id.recWindow_text);

        text.setText(result);

        this.setContentView(view);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable());
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }else
                    return false;
            }
        });
        this.update();

    }

}
