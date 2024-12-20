package com.example.writelog;

public class SourceBean {

    private int type;//0:img,  1:video

    private String path;

    private long playTime;

    public SourceBean() {
    }

    public SourceBean(int type, String path, long playTime) {
        this.type = type;
        this.path = path;
        this.playTime = playTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
}
