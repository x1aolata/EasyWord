package com.x1aolata.easyword.db;

/**
 * @author x1aolata
 * @date 2020/5/29 8:23
 * @script 用户类
 * 登录验证使用
 */
public class User {

    private String user_id;
    private String phone_number;
    private String username;
    private String password;

    public User(String user_id, String phone_number, String username, String password) {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.username = username;
        this.password = password;
    }


    public String getUser_id() {
        return user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
