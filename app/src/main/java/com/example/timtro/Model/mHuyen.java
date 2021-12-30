package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mHuyen {

    @SerializedName("mahuyen")
    @Expose
    private String mahuyen;
    @SerializedName("tenhuyen")
    @Expose
    private String tenhuyen;
    @SerializedName("loai")
    @Expose
    private String loai;
    @SerializedName("matinh")
    @Expose
    private String matinh;

    public String getMahuyen() {
        return mahuyen;
    }

    public void setMahuyen(String mahuyen) {
        this.mahuyen = mahuyen;
    }

    public String getTenhuyen() {
        return tenhuyen;
    }

    public void setTenhuyen(String tenhuyen) {
        this.tenhuyen = tenhuyen;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getMatinh() {
        return matinh;
    }

    public void setMatinh(String matinh) {
        this.matinh = matinh;
    }

}