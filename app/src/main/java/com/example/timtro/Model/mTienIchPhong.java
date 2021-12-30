package com.example.timtro.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mTienIchPhong {

    @SerializedName("matienich")
    @Expose
    private String matienich;
    @SerializedName("tentienich")
    @Expose
    private String tentienich;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getMatienich() {
        return matienich;
    }

    public void setMatienich(String matienich) {
        this.matienich = matienich;
    }

    public String getTentienich() {
        return tentienich;
    }

    public void setTentienich(String tentienich) {
        this.tentienich = tentienich;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}