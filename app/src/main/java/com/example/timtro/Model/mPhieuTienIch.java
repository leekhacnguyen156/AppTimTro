package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mPhieuTienIch {
    @SerializedName("maphieu")
    @Expose
    private String maphieu;

    @SerializedName("matin")
    @Expose
    private String matin;

    @SerializedName("matienich")
    @Expose
    private String matienich;

    public String getMaphieu() {
        return maphieu;
    }

    public void setMaphieu(String maphieu) {
        this.maphieu = maphieu;
    }

    public String getMatin() {
        return matin;
    }

    public void setMatin(String matin) {
        this.matin = matin;
    }

    public String getMatienich() {
        return matienich;
    }

    public void setMatienich(String matienich) {
        this.matienich = matienich;
    }
}
