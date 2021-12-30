package com.example.timtro.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.timtro.Activity.vDangNhap;
import com.example.timtro.Activity.vDangTin;
import com.example.timtro.R;

public class frTaiKhoanChuaDangNhap extends Fragment {

    private View vtaikhoanchuadangnhap;
    private AppCompatButton btnDangNhapt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(vtaikhoanchuadangnhap == null){
            vtaikhoanchuadangnhap = inflater.inflate(R.layout.fr_tai_khoan_chua_dang_nhap, container, false);
        }

        btnDangNhapt = vtaikhoanchuadangnhap.findViewById(R.id.btnDangNhapt);

        btnDangNhapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vDangNhap.class));
            }
        });

        return vtaikhoanchuadangnhap;
    }
}
