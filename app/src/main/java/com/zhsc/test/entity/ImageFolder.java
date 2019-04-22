package com.zhsc.test.entity;

public class ImageFolder {


    /**当前文件夹的路径*/
    private String dir;
    /**文件夹名*/
    private String name;
    /**文件夹中图片的数量*/
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndex = dir.lastIndexOf("/");
        this.name = dir.substring(lastIndex + 1);
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setName(String name) {
        this.name = name;
    }

    public ImageFolder() {
    }

    public ImageFolder(String dir, String name, int count) {
        this.dir = dir;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImgFolder{" +
                "dir='" + dir + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}