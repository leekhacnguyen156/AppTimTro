package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mPhieuDatPhong {

    @SerializedName("madatphong")
    @Expose
    private String madatphong;
    @SerializedName("mataikhoan")
    @Expose
    private String mataikhoan;
    @SerializedName("matin")
    @Expose
    private String matin;
    @SerializedName("tenkhachdat")
    @Expose
    private String tenkhachdat;
    @SerializedName("diachi")
    @Expose
    private String diachi;
    @SerializedName("sdt")
    @Expose
    private String sdt;
    @SerializedName("ghichu")
    @Expose
    private String ghichu;
    @SerializedName("thoigiandat")
    @Expose
    private String thoigiandat;
    @SerializedName("trangthaiduyet")
    @Expose
    private String trangthaiduyet;

    public String getMadatphong() {
        return madatphong;
    }

    public void setMadatphong(String madatphong) {
        this.madatphong = madatphong;
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

    public String getTenkhachdat() {
        return tenkhachdat;
    }

    public void setTenkhachdat(String tenkhachdat) {
        this.tenkhachdat = tenkhachdat;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getThoigiandat() {
        return thoigiandat;
    }

    public void setThoigiandat(String thoigiandat) {
        this.thoigiandat = thoigiandat;
    }

    public String getTrangthaiduyet() {
        return trangthaiduyet;
    }

    public void setTrangthaiduyet(String trangthaiduyet) {
        this.trangthaiduyet = trangthaiduyet;
    }

}