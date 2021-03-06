package com.example.timtro.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vQuenMatKhau extends AppCompatActivity {

    private EditText edtTaikhoan, edtMatkhau, edtNhaplaimatkhau, edtMatkhaucap2;
    private AppCompatButton btnXacnhan;
    private mTaiKhoan mTaiKhoan;
    private AppCompatCheckBox checkboxXemMK, checkboxXemMKCap2QuenMK, checkboxNhapMKLaiQuen;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_quen_mat_khau);

        Anhxa();

        checkboxXemMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxXemMK.isChecked()){
                    edtMatkhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtMatkhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        checkboxXemMKCap2QuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxXemMKCap2QuenMK.isChecked()){
                    edtMatkhaucap2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtMatkhaucap2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Set on click button
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check l???i
                if (edtTaikhoan.getText().toString().isEmpty()){
                    Toast.makeText(vQuenMatKhau.this, "Vui l??ng nh???p t??i kho???n!", Toast.LENGTH_SHORT).show();
                    edtTaikhoan.requestFocus();
                }else if (edtMatkhau.getText().toString().isEmpty()){
                    Toast.makeText(vQuenMatKhau.this, "Vui l??ng nh???p m???t kh???u!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.requestFocus();
                }else if (edtMatkhau.getText().toString().length() < 6){
                    Toast.makeText(vQuenMatKhau.this, "M???t kh???u ph???i c?? ??t nh???t 6 k?? t???!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.requestFocus();
                }else if (edtNhaplaimatkhau.getText().toString().isEmpty() ||
                        !edtNhaplaimatkhau.getText().toString().equals(edtMatkhau.getText().toString())){
                    Toast.makeText(vQuenMatKhau.this, "M???t kh???u nh???p l???i kh??ng kh???p", Toast.LENGTH_SHORT).show();
                    checkboxNhapMKLaiQuen.setChecked(false);
                    checkboxNhapMKLaiQuen.setVisibility(View.VISIBLE);
                    edtNhaplaimatkhau.requestFocus();
                }else if (edtMatkhaucap2.getText().toString().isEmpty()){
                    Toast.makeText(vQuenMatKhau.this, "Vui l??ng nh???p m???t kh???u c???p 2!", Toast.LENGTH_SHORT).show();
                    edtMatkhaucap2.requestFocus();
                }
                //X??? l??
                else{
                    getTaikhoan(edtTaikhoan.getText().toString());
                }
            }
        });

        edtNhaplaimatkhau.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtNhaplaimatkhau.getText().toString().isEmpty()){
                    if(edtNhaplaimatkhau.getText().toString().equals(edtMatkhau.getText().toString())){
                        checkboxNhapMKLaiQuen.setChecked(true);
                        checkboxNhapMKLaiQuen.setVisibility(View.VISIBLE);
                    }else{
                        checkboxNhapMKLaiQuen.setVisibility(View.GONE);
                    }
                }else{
                    checkboxNhapMKLaiQuen.setVisibility(View.GONE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtNhaplaimatkhau.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!edtNhaplaimatkhau.isFocused()){
                    if(!edtNhaplaimatkhau.getText().toString().isEmpty()){
                        if(!edtNhaplaimatkhau.getText().toString().equals(edtMatkhau.getText().toString())){
                            checkboxNhapMKLaiQuen.setChecked(false);
                            checkboxNhapMKLaiQuen.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

    }

    private void Anhxa() {
        checkboxNhapMKLaiQuen = (AppCompatCheckBox) findViewById(R.id.checkboxNhapMKLaiQuen);
        checkboxXemMK = (AppCompatCheckBox) findViewById(R.id.checkboxXemMKQuenmatkhau);
        checkboxXemMKCap2QuenMK = (AppCompatCheckBox) findViewById(R.id.checkboxXemMKCap2QuenMK);
        edtTaikhoan = (EditText) findViewById(R.id.edtTaikhoanQuen);
        edtMatkhau = (EditText) findViewById(R.id.edtMatkhauQuen);
        edtNhaplaimatkhau = (EditText) findViewById(R.id.edtMatkhauQuennhaplai);
        edtMatkhaucap2 = (EditText) findViewById(R.id.edtMatkhaucap2Quen);
        btnXacnhan = (AppCompatButton) findViewById(R.id.btnXacnhanquenmatkhau);
    }

    //get t??i kho???n v??? ????? l???y l???i mk
    private void getTaikhoan(String tentaikhoan) {
        apiInterface getDangNhap = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callBack = getDangNhap.getDangNhap(tentaikhoan);
        callBack.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse( Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                mTaiKhoan = new mTaiKhoan();
                mTaiKhoan = response.body();
                if(response.body() != null){
                    if (mTaiKhoan.getMatkhaucap2().equals(edtMatkhaucap2.getText().toString())) {
                        Toast.makeText(vQuenMatKhau.this, "?????i m???t kh???u th??nh c??ng!", Toast.LENGTH_SHORT).show();
                        Capnhatmatkhau(edtTaikhoan.getText().toString(), edtMatkhau.getText().toString());
                        startActivity(new Intent(vQuenMatKhau.this, vDangNhap.class));
                        finish();
                    }else{
                        Toast.makeText(vQuenMatKhau.this, "Vui l??ng nh???p l???i m???t kh???u c???p 2!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(vQuenMatKhau.this, "T??i kho???n kh??ng t???n t???i!", Toast.LENGTH_SHORT).show();
                    edtTaikhoan.requestFocus();
                }
            }

            @Override
            public void onFailure(Call<com.example.timtro.Model.mTaiKhoan> call, Throwable t) {

            }
        });
    }

    private void Capnhatmatkhau(String tentaikhoan, String matkhau) {
        apiInterface Capnhatmatkhau = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = Capnhatmatkhau.updateQuenmatkhau(tentaikhoan, matkhau);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String trangthai = response.body();
                if (trangthai.equals("Success")){
                    Toast.makeText(vQuenMatKhau.this, "C???p nh???t m???t kh???u th??nh c??ng!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(vQuenMatKhau.this, vDangNhap.class));
                    finish();
                }else{
                    Toast.makeText(vQuenMatKhau.this, "???? x???y ra l???i, vui l??ng th???c hi???n l???i!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
