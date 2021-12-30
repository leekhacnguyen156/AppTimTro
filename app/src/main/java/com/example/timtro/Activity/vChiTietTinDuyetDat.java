package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Model.mPhieuDatPhong;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vChiTietTinDuyetDat extends AppCompatActivity {

    private ImageButton btnBackDuyetDatCT;
    private ImageView imgDuyetDatCT;
    private TextView txtGiaDuyetDatCT, txtTieuDeDuyetDatCT, txtDiaChiDuyetDatCT, txtDienTichDuyetDatCT, txtDanhGiaDuyetDatCT, txtThoiGianDuyetDatCT;
    private CircleImageView imgAvtTaikhoanDatCT;
    private TextView txtTenTaiKhoanDatCT, txtDiaChiTaiKhoanDatCT;
    private EditText edtGiaPhongDatCT, edtTenKhachDatCT, edtDiaChiDatCT, edtSoDienThoaiDatCT, edtGhiChuCT;
    private FloatingActionButton btnTuyChonCTDT;
    private mPhieuDatPhong mPhieuDatPhong = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_chi_tiet_tin_duyet_dat);

        Gson gson = new Gson();
        String mPhieuDatPhongn = getIntent().getStringExtra("mPhieuDatPhong");
        mPhieuDatPhong = gson.fromJson(mPhieuDatPhongn, mPhieuDatPhong.class);

        AnhXa();

        btnTuyChonCTDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialogduyet = new BottomSheetDialog(
                        vChiTietTinDuyetDat.this, R.style.BottomSheetDialogTheme);
                View bottomSheetViewduyet = LayoutInflater.from(vChiTietTinDuyetDat.this).inflate(R.layout.duyet_dat_bottom_sheet_dialog_view, null, false);
                bottomSheetDialogduyet.setContentView(bottomSheetViewduyet);
                bottomSheetViewduyet.findViewById(R.id.btnDuyetDatView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyetDatPhong(mPhieuDatPhong.getMatin(), mPhieuDatPhong.getMadatphong(), mPhieuDatPhong.getMataikhoan(), 1);
                        bottomSheetDialogduyet.dismiss();
                    }
                });
                bottomSheetViewduyet.findViewById(R.id.btnKhongDuyetDatView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyetDatPhong(mPhieuDatPhong.getMatin(), mPhieuDatPhong.getMadatphong(), mPhieuDatPhong.getMataikhoan(), 2);
                        bottomSheetDialogduyet.dismiss();
                    }
                });
                bottomSheetDialogduyet.show();
            }
        });

    }

    private void getTaiKhoanDat(String mataikhoan) {
        apiInterface getTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callBack1 = getTaiKhoan.getTaiKhoan(mataikhoan);
        callBack1.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse(Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                Picasso.get().load(response.body().getAvatar()).into(imgAvtTaikhoanDatCT);
                txtTenTaiKhoanDatCT.setText(response.body().getTentaikhoan());
                txtDiaChiTaiKhoanDatCT.setText(response.body().getDiachi());
            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {

            }
        });
    }

    private void getTinDat(String matin) {
        apiInterface getTinTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTin> callBack = getTinTheoMaTin.getTinTheoMaTin(matin);
        callBack.enqueue(new Callback<mTin>() {
            @Override
            public void onResponse(Call<mTin> call, Response<mTin> response) {
                Picasso.get().load(response.body().getImage()).into(imgDuyetDatCT);
                txtTieuDeDuyetDatCT.setText(response.body().getTentieude());
                txtGiaDuyetDatCT.setText(PhongDaDangApdater.formatmoney(response.body().getGiaphong()+""));
                txtDiaChiDuyetDatCT.setText(": "+response.body().getDiachi());
                txtDienTichDuyetDatCT.setText(": "+response.body().getDientich() + " m2");
                getDanhGia(mPhieuDatPhong.getMatin());
                txtThoiGianDuyetDatCT.setText(" :"+response.body().getThoigiandang());
                String giaphong = NumberFormat.getInstance(new Locale("it", "IT")).format(Double.parseDouble(response.body().getGiaphong()));
                edtGiaPhongDatCT.setText(giaphong);
            }

            @Override
            public void onFailure(Call<mTin> call, Throwable t) {

            }
        });
    }

    private void getDanhGia(String matin){
        //get điểm đánh giá
        apiInterface getDanhGiaTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = getDanhGiaTheoMaTin.getDanhGia(matin);
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                txtDanhGiaDuyetDatCT.setText(": "+String.format("%.2f", Double.parseDouble(response.body())) + " /5");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void AnhXa() {
        btnBackDuyetDatCT = (ImageButton) findViewById(R.id.btnBackDuyetDatCT);
        imgDuyetDatCT = (ImageView) findViewById(R.id.imgDuyetDatCT);
        txtGiaDuyetDatCT = (TextView) findViewById(R.id.txtGiaDuyetDatCT);
        txtTieuDeDuyetDatCT = (TextView) findViewById(R.id.txtTieuDeDuyetDatCT);
        txtDiaChiDuyetDatCT = (TextView) findViewById(R.id.txtDiaChiDuyetDatCT);
        txtDienTichDuyetDatCT = (TextView) findViewById(R.id.txtDienTichDuyetDatCT);
        txtDanhGiaDuyetDatCT = (TextView) findViewById(R.id.txtDanhGiaDuyetDatCT);
        txtThoiGianDuyetDatCT = (TextView) findViewById(R.id.txtThoiGianDuyetDatCT);
        imgAvtTaikhoanDatCT = (CircleImageView) findViewById(R.id.imgAvtTaikhoanDatCT);

        txtTenTaiKhoanDatCT = (TextView) findViewById(R.id.txtTenTaiKhoanDatCT);
        txtDiaChiTaiKhoanDatCT = (TextView) findViewById(R.id.txtDiaChiTaiKhoanDatCT);

        edtGiaPhongDatCT = (EditText) findViewById(R.id.edtGiaPhongDatCT);
        edtTenKhachDatCT = (EditText) findViewById(R.id.edtTenKhachDatCT);
        edtDiaChiDatCT = (EditText) findViewById(R.id.edtDiaChiDatCT);
        edtSoDienThoaiDatCT = (EditText) findViewById(R.id.edtSoDienThoaiDatCT);
        edtGhiChuCT = (EditText) findViewById(R.id.edtGhiChuCT);

        edtTenKhachDatCT.setText(mPhieuDatPhong.getTenkhachdat());
        edtDiaChiDatCT.setText(mPhieuDatPhong.getDiachi());
        edtSoDienThoaiDatCT.setText(mPhieuDatPhong.getSdt());
        edtGhiChuCT.setText(mPhieuDatPhong.getGhichu());

        btnTuyChonCTDT = (FloatingActionButton) findViewById(R.id.btnTuyChonCTDT);
        if(!mPhieuDatPhong.getTrangthaiduyet().equals("0")){
            btnTuyChonCTDT.setVisibility(View.GONE);
        }
        getTaiKhoanDat(mPhieuDatPhong.getMataikhoan());
        getTinDat(mPhieuDatPhong.getMatin());
    }

    private void updateDuyetDatPhong(String matin, String madatphong, String mataikhoan, int trangthaiduyet) {
        apiInterface updateTrangThaiDuyetDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTrangThaiDuyetDat.updateTrangThaiDuyetDat(madatphong, trangthaiduyet);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    Toast.makeText(vChiTietTinDuyetDat.this, "Đã duyệt !", Toast.LENGTH_SHORT).show();
                    insertThongBaoDuyetTin(matin, trangthaiduyet, mataikhoan);
                    vChiTietTinDuyetDat.super.onBackPressed();
                }else{
                    Toast.makeText(vChiTietTinDuyetDat.this, "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vChiTietTinDuyetDat.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTrangThaiDat(String matin){
        apiInterface updateTrangThaiDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTrangThaiDat.updateTrangThaiDat(matin);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void insertThongBaoDuyetTin(String matin, int trangthaiduyet, String mataikhoan) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        apiInterface insertThongBao = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insertThongBao.insertThongBao(2,mataikhoan, matin, trangthaiduyet, strDate);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    if(trangthaiduyet == 1){
                        updateTrangThaiDat(matin);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}