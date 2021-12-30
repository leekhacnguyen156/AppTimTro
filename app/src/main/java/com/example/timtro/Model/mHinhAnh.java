package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mHinhAnh {
    @SerializedName("hinhanh")
    @Expose
    private String hinhanh;

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
