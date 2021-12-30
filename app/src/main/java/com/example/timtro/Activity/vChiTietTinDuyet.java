package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timtro.Adapter.PhieuTienIchAdapter;
import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Adapter.SliderHinhAnhAdapter;
import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mPhieuTienIch;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mTienIchPhong;
import com.example.timtro.Model.mTin;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vChiTietTinDuyet extends AppCompatActivity {

    private TextView txtGiaPhongChiTietTinDuyet, txtTieudeChiTietTinDuyet, txtDiaChiChiTietTinDuyet,
            txtLienHeChiTietTinDuyet, txtDienTichPhongChiTietTinDuyet, txtDanhGiaChiTietTinDuyet, txtMoTaChiTietTinDuyet;
    private ViewPager viewPagerimgChiTietTinDuyet;
    private ImageButton btnBackChiTietTinDuyet;
    private GridView gridTienIchPhongChiTietTinDuyet;
    private com.example.timtro.Model.mTin mTin = null;
    private ProgressDialog load;
    private PhieuTienIchAdapter phieuTienIchAdapter;
    private FloatingActionButton btnTuyChonCTTCD;
    private ArrayList<mTienIchPhong> armTienIchPhong = new ArrayList<>();
    private CircleImageView imgAvtTaikhoanTinDuyet;
    private TextView txtTenTaiKhoanTinDuyet, txtDiaChiTaiKhoanTinDuyet;
    private CircleIndicator indicatorCTTinDuyet;
    private mTinh mTinh = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_chi_tiet_tin_duyet);

        Paper.init(vChiTietTinDuyet.this);

        Gson gson = new Gson();
        String mTinasString = getIntent().getStringExtra("mTinasString");
        mTin = gson.fromJson(mTinasString, mTin.class);

        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();

        AnhXa();
        getTienIchPhong();
        getAnhTheoMaTin(mTin.getMatin());

        btnBackChiTietTinDuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        btnTuyChonCTTCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mTin.getTrangthaiduyet()) {
                    case "0":
                        BottomSheetDialog bottomSheetDialogduyet = new BottomSheetDialog(
                                vChiTietTinDuyet.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetViewduyet = LayoutInflater.from(vChiTietTinDuyet.this).inflate(R.layout.duyet_bottom_sheet_dialog_view, null, false);
                        bottomSheetDialogduyet.setContentView(bottomSheetViewduyet);
                        bottomSheetViewduyet.findViewById(R.id.btnDuyetTinViewDuyet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDuyet(mTin.getMataikhoan(), mTin.getMatin(), 1);
                                bottomSheetDialogduyet.dismiss();
                            }
                        });
                        bottomSheetViewduyet.findViewById(R.id.btnKhongDuyetTinViewDuyet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDuyet(mTin.getMataikhoan(), mTin.getMatin(), 2);
                                bottomSheetDialogduyet.dismiss();
                            }
                        });
                        bottomSheetDialogduyet.show();
                        break;
                    case "1":
                        BottomSheetDialog bottomSheetDialogdaduyet = new BottomSheetDialog(
                                vChiTietTinDuyet.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetViewdaduyet = LayoutInflater.from(vChiTietTinDuyet.this).inflate(R.layout.da_duyet_bottom_sheet_dialog_view, null, false);
                        bottomSheetDialogdaduyet.setContentView(bottomSheetViewdaduyet);
                        bottomSheetViewdaduyet.findViewById(R.id.btnBoDuyetViewDuyet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDuyet(mTin.getMataikhoan(), mTin.getMatin(), 2);
                                bottomSheetDialogdaduyet.dismiss();
                            }
                        });
                        bottomSheetDialogdaduyet.show();
                        break;
                    case "2":
                        BottomSheetDialog bottomSheetDialogkhongduyet = new BottomSheetDialog(
                                vChiTietTinDuyet.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetViewkhongduyet = LayoutInflater.from(vChiTietTinDuyet.this).inflate(R.layout.duyet_lai_bottom_sheet_dialog_view, null, false);
                        bottomSheetDialogkhongduyet.setContentView(bottomSheetViewkhongduyet);
                        bottomSheetViewkhongduyet.findViewById(R.id.btnDuyetLaiViewDuyet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDuyet(mTin.getMataikhoan(), mTin.getMatin(), 0);
                                bottomSheetDialogkhongduyet.dismiss();
                            }
                        });
                        bottomSheetDialogkhongduyet.show();
                        break;
                }
            }
        });
    }

    private void AnhXa() {
        indicatorCTTinDuyet = (CircleIndicator) findViewById(R.id.indicatorCTTinDuyet);
        btnTuyChonCTTCD = (FloatingActionButton) findViewById(R.id.btnTuyChonCTTCD);
        btnBackChiTietTinDuyet = (ImageButton) findViewById(R.id.btnBackChiTietTinDuyet);
        viewPagerimgChiTietTinDuyet = (ViewPager) findViewById(R.id.viewPagerimgChiTietTinDuyet);
        gridTienIchPhongChiTietTinDuyet = (GridView) findViewById(R.id.gridTienIchPhongChiTietTinDuyet);
        txtGiaPhongChiTietTinDuyet = (TextView) findViewById(R.id.txtGiaPhongChiTietTinDuyet);
        txtTieudeChiTietTinDuyet = (TextView) findViewById(R.id.txtTieudeChiTietTinDuyet);
        txtDiaChiChiTietTinDuyet = (TextView) findViewById(R.id.txtDiaChiChiTietTinDuyet);
        txtLienHeChiTietTinDuyet = (TextView) findViewById(R.id.txtLienHeChiTietTinDuyet);
        txtDienTichPhongChiTietTinDuyet = (TextView) findViewById(R.id.txtDienTichPhongChiTietTinDuyet);
        txtDanhGiaChiTietTinDuyet = (TextView) findViewById(R.id.txtDanhGiaChiTietTinDuyet);
        txtMoTaChiTietTinDuyet = (TextView) findViewById(R.id.txtMoTaChiTietTinDuyet);
        imgAvtTaikhoanTinDuyet = (CircleImageView) findViewById(R.id.imgAvtTaikhoanTinDuyet);
        txtTenTaiKhoanTinDuyet = (TextView) findViewById(R.id.txtTenTaiKhoanTinDuyet);
        txtDiaChiTaiKhoanTinDuyet = (TextView) findViewById(R.id.txtDiaChiTaiKhoanTinDuyet);
        getTinhChiTiet(mTin.getMatinh(), mTin.getMahuyen());
        String giaphong = PhongDaDangApdater.formatmoney(mTin.getGiaphong());
        txtGiaPhongChiTietTinDuyet.setText(" " + giaphong + "/Tháng");
        txtTieudeChiTietTinDuyet.setText("" + mTin.getTentieude());
        txtLienHeChiTietTinDuyet.setText(": " + mTin.getSdt() + " - " + mTin.getTenlienhe());
        txtMoTaChiTietTinDuyet.setText(mTin.getMotachitiet());
        txtDienTichPhongChiTietTinDuyet.setText(": " + mTin.getDientich() + " m2");
        getDanhGia(mTin.getMatin());
        getTaiKhoan(mTin.getMataikhoan());
    }

    private void getPhieuTienIchTheoMaTin(String matin) {
        apiInterface getPhieuTienIchTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mPhieuTienIch>> callBack = getPhieuTienIchTheoMaTin.getTienIchTheoMaTin(matin);
        callBack.enqueue(new Callback<ArrayList<mPhieuTienIch>>() {
            @Override
            public void onResponse(Call<ArrayList<mPhieuTienIch>> call, Response<ArrayList<mPhieuTienIch>> response) {
                phieuTienIchAdapter = new PhieuTienIchAdapter(armTienIchPhong, response.body(), vChiTietTinDuyet.this
                        , R.layout.line_tien_ich_phong);
                gridTienIchPhongChiTietTinDuyet.setAdapter(phieuTienIchAdapter);
                phieuTienIchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<mPhieuTienIch>> call, Throwable t) {

            }
        });
    }

    private void getTienIchPhong() {
        apiInterface getTienIchPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTienIchPhong>> callBack = getTienIchPhong.getTienIchPhong();
        callBack.enqueue(new Callback<List<mTienIchPhong>>() {
            @Override
            public void onResponse(Call<List<mTienIchPhong>> call, Response<List<mTienIchPhong>> response) {
                if (armTienIchPhong.size() > 0) {
                    armTienIchPhong.clear();
                }
                armTienIchPhong = (ArrayList<mTienIchPhong>) response.body();
                getPhieuTienIchTheoMaTin(mTin.getMatin());
            }


            @Override
            public void onFailure(Call<List<mTienIchPhong>> call, Throwable t) {

            }
        });
    }

    private void getDanhGia(String matin) {
        apiInterface getDanhGiaTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = getDanhGiaTheoMaTin.getDanhGia(matin);
        callBackdanhgia.enqueue(new Callback<String>() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onResponse(Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    txtDanhGiaChiTietTinDuyet.setText(": " + String.format("%.2f", Double.parseDouble(response.body())) + " /5");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                txtDanhGiaChiTietTinDuyet.setText(": Chưa có đánh giá");
            }
        });
    }

    private void getAnhTheoMaTin(String matin) {
        apiInterface getAnhTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHinhAnh>> callback = getAnhTheoMaTin.getAnhTheoMaTin(matin);
        callback.enqueue(new Callback<ArrayList<mHinhAnh>>() {
            @Override
            public void onResponse(Call<ArrayList<mHinhAnh>> call, Response<ArrayList<mHinhAnh>> response) {
                SliderHinhAnhAdapter sliderHinhAnhAdapter = new SliderHinhAnhAdapter(vChiTietTinDuyet.this, response.body(), R.layout.item_hinh_anh);
                viewPagerimgChiTietTinDuyet.setAdapter(sliderHinhAnhAdapter);
                sliderHinhAnhAdapter.notifyDataSetChanged();
                indicatorCTTinDuyet.setViewPager(viewPagerimgChiTietTinDuyet);
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
            }

            @Override
            public void onFailure(Call<ArrayList<mHinhAnh>> call, Throwable t) {

            }
        });
    }

    private void getTaiKhoan(String mataikhoan) {
        apiInterface getTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callback = getTaiKhoan.getTaiKhoan(mataikhoan);
        callback.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse(Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                if (!response.body().getAvatar().isEmpty()) {
                    Picasso.get().load(response.body().getAvatar()).into(imgAvtTaikhoanTinDuyet);
                }
                txtTenTaiKhoanTinDuyet.setText(response.body().getTentaikhoan());
                txtDiaChiTaiKhoanTinDuyet.setText(response.body().getDiachi());
            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {

            }
        });
    }

    private void updateDuyet(String mataikhoan, String matin, int trangthaiduyet) {
        apiInterface updateDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateDuyet.updateTrangThaiDuyet(matin, trangthaiduyet);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Success")) {
                    switch (trangthaiduyet) {
                        case 0:
                            Toast.makeText(vChiTietTinDuyet.this, "Duyệt lại !", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(vChiTietTinDuyet.this, "Đã duyệt !", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(vChiTietTinDuyet.this, "Không duyệt !", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    vChiTietTinDuyet.super.onBackPressed();
                    insertThongBaoDuyetTin(mataikhoan, matin, trangthaiduyet);
                } else {
                    Toast.makeText(vChiTietTinDuyet.this, "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void insertThongBaoDuyetTin(String mataikhoan, String matin, int trangthaiduyet) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        apiInterface insertThongBao = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insertThongBao.insertThongBao(1, mataikhoan, matin, trangthaiduyet, strDate);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vChiTietTinDuyet.this, "" + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTinhChiTiet(String matinh, String mahuyen) {
        apiInterface getTingDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTinh> callBack = getTingDatPhong.getTenTinh(matinh);
        callBack.enqueue(new Callback<mTinh>() {
            @Override
            public void onResponse(Call<mTinh> call, Response<mTinh> response) {
                mTinh = response.body();
                getHuyenDat(mahuyen);
            }

            @Override
            public void onFailure(Call<mTinh> call, Throwable t) {

            }
        });
    }

    private void getHuyenDat(String mahuyen) {
        apiInterface getHuyenDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<mHuyen> callBack = getHuyenDat.getTenHuyen(mahuyen);
        callBack.enqueue(new Callback<mHuyen>() {
            @Override
            public void onResponse(Call<mHuyen> call, Response<mHuyen> response) {
                txtDiaChiChiTietTinDuyet.setText(mTin.getDiachi() + ", " + response.body().getTenhuyen() + ", " + mTinh.getTentinh());
            }

            @Override
            public void onFailure(Call<mHuyen> call, Throwable t) {

            }
        });
    }

}