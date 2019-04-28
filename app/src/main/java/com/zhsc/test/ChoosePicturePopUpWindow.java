package com.zhsc.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * 用于选择相片来源的popupwindow
 */
public class ChoosePicturePopUpWindow extends PopupWindow {

    private Context context;
    private RelativeLayout photo;
    private RelativeLayout album;
    private RelativeLayout cancel;

    public ChoosePicturePopUpWindow(Context context, EditMyselfActivity.Listener onClickListener){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.choosepic_popupwindow_layout,null);
        photo = view.findViewById(R.id.layout_choosePic_popUpWindow_take_photo);
        album = view.findViewById(R.id.layout_choosePic_popUpWindow_album);
        cancel = view.findViewById(R.id.layout_choosePic_popUpWindow_cancel);

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

        photo.setOnClickListener(onClickListener);
        album.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);

    }



}
