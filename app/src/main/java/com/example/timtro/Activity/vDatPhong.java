package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mTin;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vDatPhong extends AppCompatActivity {

    private mTin mTin = null;
    private ImageButton btnBackDatPhong;
    private ImageView imgAnhTinDatPhong;
    private TextView txtGiaDatPhongTin, txtTieuDeDatPhong, txtDiaChiDatPhong, txtDienTichTinDatPhong,
            txtDanhGiaTinDatPhong, txtThoiGianDangDatPhong;
    private EditText edtGiaPhongDat, edtTenKhachDat, edtSoDienThoaiDat, edtGhiChu, edtDiaChiDat;
    private AppCompatButton btnHuyDatPhong, btnXNDatPhong;
    private mTinh mTinh = null;
    private mHuyen mHuyen = null;
    private mTaiKhoan mTaiKhoan = null;
    private String mataikhoan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_dat_phong);

        Gson gson = new Gson();
        String mTinDatPhong = getIntent().getStringExtra("mTinDatPhong");
        mTin = gson.fromJson(mTinDatPhong, mTin.class);

        Paper.init(vDatPhong.this);
        mataikhoan = Paper.book().read(appAccountDetails.mataikhoan);

        AnhXa();
        getDanhGia();
        getTaiKhoanDat();

        btnBackDatPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(vDatPhong.this);
                alertDialog.setTitle("Thông báo thoát ");
                alertDialog.setMessage("Bạn muốn thoát khỏi đặt phòng ?");
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vDatPhong.super.onBackPressed();
                    }
                });
                alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
                alertDialog.show();
            }
        });

        btnHuyDatPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(vDatPhong.this);
                alertDialog.setTitle("Thông báo hủy ");
                alertDialog.setMessage("Bạn muốn thoát hủy đặt phòng ?");
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vDatPhong.super.onBackPressed();
                    }
                });
                alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
                alertDialog.show();
            }
        });

        btnXNDatPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HovaTen = edtTenKhachDat.getText().toString().trim();
                String DiaChi = edtDiaChiDat.getText().toString().trim();
                String SoDienThoai = edtSoDienThoaiDat.getText().toString().trim();
                String GhiChu = edtGhiChu.getText().toString().trim();

                if(HovaTen.isEmpty()){
                    Toast.makeText(vDatPhong.this, "Vui lòng nhập họ và tên !", Toast.LENGTH_SHORT).show();
                }else if(DiaChi.isEmpty()){
                    Toast.makeText(vDatPhong.this, "Vui lòng nhập địa chỉ !", Toast.LENGTH_SHORT).show();
                }else if(SoDienThoai.isEmpty()){
                    Toast.makeText(vDatPhong.this, "Vui lòng nhập số điện thoại !", Toast.LENGTH_SHORT).show();
                }else if (SoDienThoai.length() != 10){
                    Toast.makeText(vDatPhong.this, "Vui lòng nhập đúng số điện thoại !", Toast.LENGTH_SHORT).show();
                }else{
                    insertPhienDatPhong(HovaTen, DiaChi, SoDienThoai, GhiChu);
                }
            }
        });

        LoadTinPhongDat();

    }

    private void insertPhienDatPhong(String HovaTen, String DiaChi, String SoDienThoai, String GhiChu) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        apiInterface insertPhienDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insertPhienDatPhong.insertPhienDatPhong(mataikhoan, mTin.getMatin(), HovaTen, DiaChi, SoDienThoai, GhiChu, strDate, 0);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    Toast.makeText(vDatPhong.this, "Đặt phòng thành công vui lòng chờ duyệt !", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(vDatPhong.this, vPhongDaDat.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getTinhDatPhong() {
        apiInterface getTingDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTinh> callBack = getTingDatPhong.getTenTinh(Paper.book().read(appAccountDetails.matinh));
        callBack.enqueue(new Callback<mTinh>() {
            @Override
            public void onResponse(Call<mTinh> call, Response<mTinh> response) {
                mTinh = response.body();
                getHuyenDat();
            }

            @Override
            public void onFailure(Call<mTinh> call, Throwable t) {

            }
        });
    }

    private void getHuyenDat() {
        apiInterface getHuyenDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<mHuyen> callBack = getHuyenDat.getTenHuyen(Paper.book().read(appAccountDetails.mahuyen));
        callBack.enqueue(new Callback<mHuyen>() {
            @Override
            public void onResponse(Call<mHuyen> call, Response<mHuyen> response) {
                mHuyen = response.body();
                edtDiaChiDat.setText(mHuyen.getTenhuyen() + ", " + mTinh.getTentinh());
            }

            @Override
            public void onFailure(Call<mHuyen> call, Throwable t) {

            }
        });
    }

    private void getDanhGia() {
        apiInterface getDanhGiaTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = getDanhGiaTheoMaTin.getDanhGia(mTin.getMatin());
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                txtDanhGiaTinDatPhong.setText(": "+String.format("%.2f", Double.parseDouble(response.body())) + " /5");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getTaiKhoanDat() {
        apiInterface getTaikhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callBack = getTaikhoan.getTaiKhoan(mataikhoan);
        callBack.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse(Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                edtTenKhachDat.setText(response.body().getHo()+" "+response.body().getTen());
                edtSoDienThoaiDat.setText(response.body().getSodienthoai());
                mTaiKhoan = response.body();
                getTinhDatPhong();
            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {
            }
        });
    }

    private void AnhXa() {
        txtTieuDeDatPhong = (TextView) findViewById(R.id.txtTieuDeDatPhong);
        btnBackDatPhong = (ImageButton) findViewById(R.id.btnBackDatPhong);
        imgAnhTinDatPhong = (ImageView) findViewById(R.id.imgAnhTinDatPhong);
        txtGiaDatPhongTin = (TextView) findViewById(R.id.txtGiaDatPhongTin);
        txtDiaChiDatPhong = (TextView) findViewById(R.id.txtDiaChiDatPhong);
        txtDienTichTinDatPhong = (TextView) findViewById(R.id.txtDienTichTinDatPhong);
        txtDanhGiaTinDatPhong = (TextView) findViewById(R.id.txtDanhGiaTinDatPhong);
        txtThoiGianDangDatPhong = (TextView) findViewById(R.id.txtThoiGianDangDatPhong);
        edtGiaPhongDat = (EditText) findViewById(R.id.edtGiaPhongDat);
        edtTenKhachDat = (EditText) findViewById(R.id.edtTenKhachDat);
        edtDiaChiDat = (EditText) findViewById(R.id.edtDiaChiDat);
        edtSoDienThoaiDat = (EditText) findViewById(R.id.edtSoDienThoaiDat);
        edtGhiChu = (EditText) findViewById(R.id.edtGhiChu);
        btnHuyDatPhong = (AppCompatButton) findViewById(R.id.btnHuyDatPhong);
        btnXNDatPhong = (AppCompatButton) findViewById(R.id.btnXNDatPhong);;
    }

    private void LoadTinPhongDat() {
        txtGiaDatPhongTin.setText(PhongDaDangApdater.formatmoney(mTin.getGiaphong()));
        txtTieuDeDatPhong.setText(mTin.getTentieude());
        Picasso.get().load(mTin.getImage()).into(imgAnhTinDatPhong);
        txtDiaChiDatPhong.setText(" :" + mTin.getDiachi());
        txtDienTichTinDatPhong.setText(" :" + mTin.getDientich()+" m2");
        txtThoiGianDangDatPhong.setText(" :" + mTin.getThoigiandang());
        String giaphong = NumberFormat.getInstance(new Locale("it", "IT")).format(Double.parseDouble(mTin.getGiaphong()));
        edtGiaPhongDat.setText(giaphong+" VND");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(vDatPhong.this);
        alertDialog.setTitle("Thông báo thoát ");
        alertDialog.setMessage("Bạn muốn thoát khỏi đặt phòng ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vDatPhong.super.onBackPressed();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        alertDialog.show();
    }
}