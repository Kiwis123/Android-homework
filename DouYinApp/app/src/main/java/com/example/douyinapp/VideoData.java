package com.example.douyinapp;

public class VideoData {
    /**
     * _id : 5e9830b0ce330a0248e89d86
     * feedurl : http://jzvd.nathen.cn/video/1137e480-170bac9c523-0007-1823-c86-de200.mp4
     * nickname : 王火火
     * description : 这是第一条Feed数据
     * likecount : 10000
     * avatar : http://jzvd.nathen.cn/snapshot/f402a0e012b14d41ad07939746844c5e00005.jpg
     */

    private String _id;
    private String feedurl;
    private String nickname;
    private String description;
    private int likecount;
    private String avatar;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
