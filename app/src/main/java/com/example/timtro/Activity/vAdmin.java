package com.example.timtro.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.timtro.Fragment.frChuaDuyet;
import com.example.timtro.Fragment.frDaDuyet;
import com.example.timtro.Fragment.frKhongDuyet;
import com.example.timtro.Fragment.frTaiKhoan;
import com.example.timtro.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class vAdmin extends AppCompatActivity {

    private ChipNavigationBar chipnavTabDuyet;
    private FragmentManager fragmentManager;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_admin);
        AnhXa();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (savedInstanceState == null) {
            chipnavTabDuyet.setItemSelected(R.id.tabChuaduyet, true);
            fragmentManager = getSupportFragmentManager();
            frChuaDuyet frChuaDuyet = new frChuaDuyet();
            fragmentManager.beginTransaction().replace(R.id.frlayoutDangTin, frChuaDuyet).commit();
        }
    }

    private void AnhXa() {
        btnBack = findViewById(R.id.btnThoatDuyetTin);
        chipnavTabDuyet = findViewById(R.id.chipnavTabDuyet);
        chipnavTabDuyet.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment frTamp = null;
                //Notication
                chipnavTabDuyet.showBadge(R.id.tabThongbao);
                switch (id) {
                    case R.id.tabChuaduyet:
                        frTamp = new frChuaDuyet();
                        break;
                    case R.id.tabDaduyet:
                        frTamp = new frDaDuyet();
                        break;
                    case R.id.tabKhongduyet:
                        frTamp = new frKhongDuyet();
                        break;
                }
                if (frTamp != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frlayoutDangTin, frTamp).commit();
                }
            }
        });
    }

    @Override

    public void onBackPressed() {
        this.finish();
        frTaiKhoan.countTinChuaDuyet();
        super.onBackPressed();
    }
}
