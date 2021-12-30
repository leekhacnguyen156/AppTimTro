package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mThongBao {

    @SerializedName("mathongbao")
    @Expose
    private String mathongbao;
    @SerializedName("maloaithongbao")
    @Expose
    private String maloaithongbao;
    @SerializedName("mataikhoan")
    @Expose
    private String mataikhoan;
    @SerializedName("matin")
    @Expose
    private String matin;
    @SerializedName("trangthai")
    @Expose
    private String trangthai;
    @SerializedName("trangthaixem")
    @Expose
    private String trangthaixem;
    @SerializedName("thoigian")
    @Expose
    private String thoigian;

    public String getMathongbao() {
        return mathongbao;
    }

    public void setMathongbao(String mathongbao) {
        this.mathongbao = mathongbao;
    }

    public String getMaloaithongbao() {
        return maloaithongbao;
    }

    public void setMaloaithongbao(String maloaithongbao) {
        this.maloaithongbao = maloaithongbao;
    }

    public String getMataikhoan() {
        return mataikhoan;
    }

    public void setMataikhoan(String mataikhoan) {
        this.mataikhoan = mataikhoan;
    }

    public String getMatin() {
        return matin;
    }

    public void setMatin(String matin) {
        this.matin = matin;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getTrangthaixem() {
        return trangthaixem;
    }

    public void setTrangthaixem(String trangthaixem) {
        this.trangthaixem = trangthaixem;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

}