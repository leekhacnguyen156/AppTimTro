package com.example.timtro.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.timtro.Fragment.frDaDuyetDat;
import com.example.timtro.Fragment.frDuyetDatPhong;
import com.example.timtro.Fragment.frKhongDuyetDat;
import com.example.timtro.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class vDuyetPhongDaDat extends AppCompatActivity {

    private ChipNavigationBar chipnavTabDuyetDat;
    private FragmentManager fragmentManager;
    private ImageView btnThoatDuyetDat;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_duyet_phong_da_dat);

        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();

        Thread time = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            load.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        time.start();

        Anhxa();
        if (savedInstanceState == null) {
            chipnavTabDuyetDat.setItemSelected(R.id.tabChuaduyet, true);
            fragmentManager = getSupportFragmentManager();
            frDuyetDatPhong frDuyetDatPhong = new frDuyetDatPhong();
            fragmentManager.beginTransaction().replace(R.id.frlayoutDuyetDat, frDuyetDatPhong).commit();
        }

        btnThoatDuyetDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vDuyetPhongDaDat.super.onBackPressed();
            }
        });
    }

    private void Anhxa() {
        chipnavTabDuyetDat = (ChipNavigationBar) findViewById(R.id.chipnavTabDuyetDat);
        btnThoatDuyetDat = (ImageView) findViewById(R.id.btnThoatDuyetDat);
        chipnavTabDuyetDat.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment frTamp = null;
                switch (id) {
                    case R.id.tabChuaduyet:
                        frTamp = new frDuyetDatPhong();
                        break;
                    case R.id.tabDaduyet:
                        frTamp = new frDaDuyetDat();
                        break;
                    case R.id.tabKhongduyet:
                        frTamp = new frKhongDuyetDat();
                        break;
                }
                if (frTamp != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frlayoutDuyetDat, frTamp).commit();
                }
            }
        });
    }
}
