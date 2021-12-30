package com.example.timtro.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timtro.Activity.vSuaTin;
import com.example.timtro.R;

import java.text.NumberFormat;
import java.util.Locale;

public class frXacNhan extends Fragment {

    private View vfrxacnhan;
    public static EditText edtTieuDeBaiDang, edtTenChuTin, edtSoDienThoai, edtMoTa;
    private TextView txtSoKiTuTieuDe, txtSoKiTuMoTa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vfrxacnhan == null) {
            vfrxacnhan = inflater.inflate(R.layout.fr_xac_nhan, container, false);
        }

        AnhXa();

        if (vSuaTin.mTin.getMatin() != null) {
            edtTieuDeBaiDang.setText(vSuaTin.mTin.getTentieude());
            txtSoKiTuTieuDe.setText("" + edtTieuDeBaiDang.getText().toString().trim().length());
            edtTenChuTin.setText(vSuaTin.mTin.getTenlienhe());
            edtSoDienThoai.setText(vSuaTin.mTin.getSdt());
            edtMoTa.setText(vSuaTin.mTin.getMotachitiet());
            txtSoKiTuMoTa.setText("" + edtMoTa.getText().toString().trim().length());
        }

        edtTieuDeBaiDang.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtSoKiTuTieuDe.setText("" + edtTieuDeBaiDang.getText().toString().trim().length());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtMoTa.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtSoKiTuMoTa.setText("" + edtMoTa.getText().toString().trim().length());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return vfrxacnhan;
    }

    @SuppressLint("SetTextI18n")
    private void AnhXa() {
        edtTieuDeBaiDang = (EditText) vfrxacnhan.findViewById(R.id.edtTieuDeBaiDang);
        edtTenChuTin = (EditText) vfrxacnhan.findViewById(R.id.edtTenChuTin);
        edtTenChuTin.setText(frTaiKhoan.mTaiKhoan.getHo() + " " + frTaiKhoan.mTaiKhoan.getTen());
        edtSoDienThoai = (EditText) vfrxacnhan.findViewById(R.id.edtSoDienThoai);
        edtSoDienThoai.setText(frTaiKhoan.mTaiKhoan.getSodienthoai());
        edtMoTa = (EditText) vfrxacnhan.findViewById(R.id.edtMoTa);
        txtSoKiTuTieuDe = (TextView) vfrxacnhan.findViewById(R.id.txtSoKiTuTieuDe);
        txtSoKiTuMoTa = (TextView) vfrxacnhan.findViewById(R.id.txtSoKiTuMoTa);
    }
}
