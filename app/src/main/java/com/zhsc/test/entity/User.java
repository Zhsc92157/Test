package com.zhsc.test.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 用户数据类型
 */
public class User extends BmobUser {
    /**
     * 用户名 账号 唯一识别
     */
    private String username;
    /**
     * 密码
     */
    private String password;
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
    private BmobFile avatar;

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public BmobFile getAvatar() {
        return avatar;
    }
}
