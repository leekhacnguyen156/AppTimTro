package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

public class mTaiKhoan {

    @SerializedName("mataikhoan")
    @Expose
    private String mataikhoan;
    @SerializedName("matinh")
    @Expose
    private String matinh;
    @SerializedName("mahuyen")
    @Expose
    private String mahuyen;
    @SerializedName("diachi")
    @Expose
    private String diachi;
    @SerializedName("tentaikhoan")
    @Expose
    private String tentaikhoan;
    @SerializedName("matkhau")
    @Expose
    private String matkhau;
    @SerializedName("ho")
    @Expose
    private String ho;
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("sodienthoai")
    @Expose
    private String sodienthoai;
    @SerializedName("loaitaikhoan")
    @Expose
    private String loaitaikhoan;
    @SerializedName("matkhaucap2")
    @Expose
    private String matkhaucap2;
    @SerializedName("avatar")
    @Expose
    private String avatar;



    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMataikhoan() {
        return mataikhoan;
    }

    public void setMataikhoan(String mataikhoan) {
        this.mataikhoan = mataikhoan;
    }

    public String getMatinh() {
        return matinh;
    }

    public void setMatinh(String matinh) {
        this.matinh = matinh;
    }

    public String getMahuyen() {
        return mahuyen;
    }

    public void setMahuyen(String mahuyen) {
        this.mahuyen = mahuyen;
    }

    public String getTentaikhoan() {
        return tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        this.tentaikhoan = tentaikhoan;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getLoaitaikhoan() {
        return loaitaikhoan;
    }

    public void setLoaitaikhoan(String loaitaikhoan) {
        this.loaitaikhoan = loaitaikhoan;
    }

    public String getMatkhaucap2() {
        return matkhaucap2;
    }

    public void setMatkhaucap2(String matkhaucap2) {
        this.matkhaucap2 = matkhaucap2;
    }
}