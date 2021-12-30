package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mLichSu {
    @SerializedName("malichsu")
    @Expose
    private String malichsu;

    @SerializedName("mataikhoan")
    @Expose
    private String mataikhoan;

    @SerializedName("noidung")
    @Expose
    private String noidung;

    public String getMalichsu() {
        return malichsu;
    }

    public void setMalichsu(String malichsu) {
        this.malichsu = malichsu;
    }

    public String getMataikhoan() {
        return mataikhoan;
    }

    public void setMataikhoan(String mataikhoan) {
        this.mataikhoan = mataikhoan;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }
}
