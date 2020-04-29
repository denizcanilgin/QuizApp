package com.tansuyegen.quizapp.Models;

public class LeaderBoardUser {

    int user_order_no;
    int user_point;
    String user_nickname;
    String userId;
    String user_profile_photo_url;



    public LeaderBoardUser(String userId,int user_order_no, int user_point, String user_nickname, String user_profile_photo_url) {
        this.user_order_no = user_order_no;
        this.userId = userId;
        this.user_point = user_point;
        this.user_nickname = user_nickname;
        this.user_profile_photo_url = user_profile_photo_url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_profile_photo_url() {
        return user_profile_photo_url;
    }

    public void setUser_profile_photo_url(String user_profile_photo_url) {
        this.user_profile_photo_url = user_profile_photo_url;
    }

    public int getUser_order_no() {
        return user_order_no;
    }

    public void setUser_order_no(int user_order_no) {
        this.user_order_no = user_order_no;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }
}
