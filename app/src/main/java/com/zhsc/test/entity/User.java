package com.zhsc.test.entity;

import cn.bmob.v3.BmobUser;

/**
 * 用户数据类型
 */
public class User extends BmobUser {
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 头像
     */
    private String path;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getGender() {
        return gender;
    }

    public String getPath() {
        return path;
    }
}
