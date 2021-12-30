package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.timtro.Adapter.ChiTietHinhAnhAdapter;
import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class vChiTietHinhAnh extends AppCompatActivity {

    private  ArrayList<mHinhAnh> armHinhAnh = new ArrayList<>();
    private int vitri = 0;
    private ChiTietHinhAnhAdapter chiTietHinhAnhAdapter;
    private ViewPager pagerChiTietImg;
    private CircleIndicator indicatorCTHinhAnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_chi_tiet_hinh_anh);
        Gson gson = new Gson();
        String arImgString = getIntent().getStringExtra("arImgString");
        Type type = new TypeToken<ArrayList<mHinhAnh>>(){}.getType();
        armHinhAnh = gson.fromJson(arImgString, type);
        vitri = getIntent().getIntExtra("vitri", 0);

        AnhXa();

        SETADPATERVIEWPAGER();

    }

    private void AnhXa() {
        pagerChiTietImg = (ViewPager) findViewById(R.id.pagerChiTietImg);
        indicatorCTHinhAnh = (CircleIndicator) findViewById(R.id.indicatorCTHinhAnh);
    }

    private void SETADPATERVIEWPAGER(){
        chiTietHinhAnhAdapter = new ChiTietHinhAnhAdapter(vChiTietHinhAnh.this, armHinhAnh, R.layout.item_image_view);
        pagerChiTietImg.setAdapter(chiTietHinhAnhAdapter);
        indicatorCTHinhAnh.setViewPager(pagerChiTietImg);
        pagerChiTietImg.setCurrentItem(vitri,true);
    }
}