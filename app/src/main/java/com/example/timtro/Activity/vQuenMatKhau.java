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
                //check lỗi
                if (edtTaikhoan.getText().toString().isEmpty()){
                    Toast.makeText(vQuenMatKhau.this, "Vui lòng nhập tài khoản!", Toast.LENGTH_SHORT).show();
                    edtTaikhoan.requestFocus();
                }else if (edtMatkhau.getText().toString().isEmpty()){
                    Toast.makeText(vQuenMatKhau.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.requestFocus();
                }else if (edtMatkhau.getText().toString().length() < 6){
                    Toast.makeText(vQuenMatKhau.this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.requestFocus();
                }else if (edtNhaplaimatkhau.getText().toString().isEmpty() ||
                        !edtNhaplaimatkhau.getText().toString().equals(edtMatkhau.getText().toString())){
                    Toast.makeText(vQuenMatKhau.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                    checkboxNhapMKLaiQuen.setChecked(false);
                    checkboxNhapMKLaiQuen.setVisibility(View.VISIBLE);
                    edtNhaplaimatkhau.requestFocus();
                }else if (edtMatkhaucap2.getText().toString().isEmpty()){
                    Toast.makeText(vQuenMatKhau.this, "Vui lòng nhập mật khẩu cấp 2!", Toast.LENGTH_SHORT).show();
                    edtMatkhaucap2.requestFocus();
                }
                //Xử lý
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

    //get tài khoản về để lấy lại mk
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
                        Toast.makeText(vQuenMatKhau.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Capnhatmatkhau(edtTaikhoan.getText().toString(), edtMatkhau.getText().toString());
                        startActivity(new Intent(vQuenMatKhau.this, vDangNhap.class));
                        finish();
                    }else{
                        Toast.makeText(vQuenMatKhau.this, "Vui lòng nhập lại mật khẩu cấp 2!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(vQuenMatKhau.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(vQuenMatKhau.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(vQuenMatKhau.this, vDangNhap.class));
                    finish();
                }else{
                    Toast.makeText(vQuenMatKhau.this, "Đã xảy ra lỗi, vui lòng thực hiện lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
