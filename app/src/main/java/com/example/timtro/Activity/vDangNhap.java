package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timtro.Fragment.frTaiKhoan;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vDangNhap extends AppCompatActivity {
    private AppCompatButton btnDangNhap;
    private ImageButton btnBackDangnhap;
    private EditText edtTaikhoan, edtMatkhau;
    private TextView txtQuenmatkhau,txtDangky;
    private mTaiKhoan mTaiKhoan;
    private static mTinh mTinh;
    private ProgressDialog loadConnect;
    private AppCompatCheckBox checkboxXemMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_dang_nhap);
        AnhXa();
        Paper.init(this);


        //Set nút thoát khỏi trang đăng nhập
        btnBackDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //check các trường và xử lý dữ liệu khi bấm Button đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTaikhoan.getText().toString().isEmpty()){
                    Toast.makeText(vDangNhap.this, "Vui lòng nhập tài khoản!", Toast.LENGTH_SHORT).show();
                    edtTaikhoan.requestFocus();
                }else if(edtMatkhau.getText().toString().isEmpty()){
                    Toast.makeText(vDangNhap.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.requestFocus();
                }else{
                    loadConnect.setTitle("Đăng Nhập");
                    loadConnect.setMessage("Vui lòng chờ trong giây lát...");
                    loadConnect.setCanceledOnTouchOutside(false);
                    loadConnect.show();

                    getDangNhap(edtTaikhoan.getText().toString());
                }
            }
        });


        //Khi bấm đăng ký
        txtDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(vDangNhap.this, vDangKy.class));
            }
        });

        //Khi bấm quên mật khẩu
        txtQuenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(vDangNhap.this, vQuenMatKhau.class));
            }
        });

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

    }

    private void AnhXa() {
        checkboxXemMK = (AppCompatCheckBox) findViewById(R.id.checkboxXemMK);
        btnDangNhap = (AppCompatButton) findViewById(R.id.btnDangnhap);
        btnBackDangnhap = (ImageButton) findViewById(R.id.btnBackDangNhap);
        edtTaikhoan = (EditText) findViewById(R.id.edtTaikhoanDangnhap);
        edtMatkhau = (EditText) findViewById(R.id.edtMatkhauDangnhap);
        txtQuenmatkhau = (TextView) findViewById(R.id.txtvQuenmatkhau);
        txtDangky = (TextView) findViewById(R.id.txtvDangky);
        loadConnect = new ProgressDialog(vDangNhap.this);
    }


    //Hàm get tài khoản về để kiểm tra đăng nhập
    private void getDangNhap(String tentaikhoan) {
        apiInterface getDangNhap = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callBack = getDangNhap.getDangNhap(tentaikhoan);
        callBack.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse( Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                mTaiKhoan = new mTaiKhoan();
                mTaiKhoan = response.body();
                if(response.body() != null){
                    if (mTaiKhoan.getMatkhau().equals(edtMatkhau.getText().toString())){
                        loadConnect.dismiss();
                        Toast.makeText(vDangNhap.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(vDangNhap.this, vLoad.class));
                        //Ghi lại tài khoản để lần sau không cần login lại
                        Paper.book().write(appAccountDetails.mataikhoan, mTaiKhoan.getMataikhoan());
                        Paper.book().write(appAccountDetails.matinh, mTaiKhoan.getMatinh());
                        Paper.book().write(appAccountDetails.mahuyen, mTaiKhoan.getMahuyen());
                    }else{
                        loadConnect.dismiss();
                        Toast.makeText(vDangNhap.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                        edtMatkhau.requestFocus();
                    }
                }else{
                    loadConnect.dismiss();
                    Toast.makeText(vDangNhap.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    edtTaikhoan.requestFocus();
                }
            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {
                Toast.makeText(vDangNhap.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getTenTinh(String matinh) {
        apiInterface getTenTinh = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTinh> callBack = getTenTinh.getTenTinh(matinh);
        callBack.enqueue(new Callback<mTinh>() {
            @Override
            public void onResponse(Call<mTinh> call, Response<mTinh> response) {
                mTinh = response.body();
                Paper.book().write(appAccountDetails.tentinh, mTinh.getTentinh()+"");
            }

            @Override
            public void onFailure(Call<mTinh> call, Throwable t) {

            }
        });
    }
}