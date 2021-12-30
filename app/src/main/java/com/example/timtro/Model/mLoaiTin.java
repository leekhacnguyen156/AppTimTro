package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mLoaiTin {

    @SerializedName("maloaitin")
    @Expose
    private String maloaitin;
    @SerializedName("tenloaitin")
    @Expose
    private String tenloaitin;

    public String getMaloaitin() {
        return maloaitin;
    }

    public void setMaloaitin(String maloaitin) {
        this.maloaitin = maloaitin;
    }

    public String getTenloaitin() {
        return tenloaitin;
    }

    public void setTenloaitin(String tenloaitin) {
        this.tenloaitin = tenloaitin;
    }

}