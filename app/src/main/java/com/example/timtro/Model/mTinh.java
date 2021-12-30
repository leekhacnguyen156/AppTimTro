package com.example.timtro.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mTinh {

@SerializedName("matinh")
@Expose
private String matinh;
@SerializedName("tentinh")
@Expose
private String tentinh;
@SerializedName("loai")
@Expose
private String loai;

public String getMatinh() {
return matinh;
}

public void setMatinh(String matinh) {
this.matinh = matinh;
}

public String getTentinh() {
return tentinh;
}

public void setTentinh(String tentinh) {
this.tentinh = tentinh;
}

public String getLoai() {
return loai;
}

public void setLoai(String loai) {
this.loai = loai;
}

}