package com.example.timtro.ModelApp;

public class appDanhMuc {
    private String appTen;
    private int appIcon;

    public appDanhMuc(String appTen, int appIcon) {
        this.appTen = appTen;
        this.appIcon = appIcon;
    }

    public String getAppTen() {
        return appTen;
    }

    public void setAppTen(String appTen) {
        this.appTen = appTen;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }
}
