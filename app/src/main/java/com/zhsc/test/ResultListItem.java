package com.zhsc.test;

public class ResultListItem {

    private int imgId;//显示对错
    private String text;//显示识别出的题目

    public ResultListItem(int imgId,String text){
        this.imgId = imgId;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImgId(int imgId){
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public int getImgId() {
        return imgId;
    }
}
