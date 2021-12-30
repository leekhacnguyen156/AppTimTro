package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mLoaiPhong {

    @SerializedName("maloaiphong")
    @Expose
    private String maloaiphong;
    @SerializedName("tenloaiphong")
    @Expose
    private String tenloaiphong;

    public String getMaloaiphong() {
        return maloaiphong;
    }

    public void setMaloaiphong(String maloaiphong) {
        this.maloaiphong = maloaiphong;
    }

    public String getTenloaiphong() {
        return tenloaiphong;
    }

    public void setTenloaiphong(String tenloaiphong) {
        this.tenloaiphong = tenloaiphong;
    }

}