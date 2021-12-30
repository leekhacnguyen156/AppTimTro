package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timtro.Adapter.PhieuTienIchAdapter;
import com.example.timtro.Adapter.SliderHinhAnhAdapter;
import com.example.timtro.MainActivity;
import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mPhieuTienIch;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mTienIchPhong;
import com.example.timtro.Model.mTin;
import com.example.timtro.Model.mTinh;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;
import com.example.timtro.xml.ExpandableHeightGridView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.timtro.Adapter.PhongDaDangApdater.formatmoney;

public class vChiTietTin extends AppCompatActivity {
    private ImageButton btnBack;
    public static mTin mTin;
    private ArrayList<mPhieuTienIch> armPhieuTienIch = new ArrayList<>();
    public static ArrayList<mHuyen> armHuyen = new ArrayList<>();
    private PhieuTienIchAdapter phieuTienIchAdapter;
    private TextView txtGiaPhong, txtTieude, txtDiaChi, txtLienHe
            , txtDienTich, txtDanhGia, txtMota, txtTenNguoiDatPhong;
    private ViewPager vpHinhAnh;
    private ExpandableHeightGridView gridTienIch;
    private ProgressDialog loadConnect;
    public static String tentinh = "", tenhuyen = "";
    private CircleIndicator indicatorCTTin;
    private int countdanhgia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_chitiettin);
        LoadProgressDialog();
        Gson gson = new Gson();
        String mTinAsAString = getIntent().getStringExtra("mTinAsAString");
        mTin = gson.fromJson(mTinAsAString, mTin.class);
        getPhieuTienIchTheoMaTin(mTin.getMatin());
        getHuyen(mTin.getMatinh());
        for (mTinh mtinh : vNhaChinh.armTinh) {
            if (mtinh.getMatinh().equals(mTin.getMatinh())) {
                tentinh = mtinh.getTentinh()+"";
                break;
            }
        }
        for (mHuyen mhuyen : armHuyen) {
            if (mTin.getMahuyen().equals(mhuyen.getMahuyen())) {
                tenhuyen = mhuyen.getTenhuyen()+"";
                break;
            }
        }

        Anhxa();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void LoadProgressDialog() {
        loadConnect = new ProgressDialog(this);
        loadConnect.setTitle("Đang load dữ liệu");
        loadConnect.setCanceledOnTouchOutside(false);
        loadConnect.setMessage("Vui lòng chờ giây lát..");
        loadConnect.show();
    }


    @SuppressLint("SetTextI18n")
    private void Anhxa() {
        indicatorCTTin = (CircleIndicator) findViewById(R.id.indicatorCTTin);
        btnBack = findViewById(R.id.btnBackChiTietTin);
        txtGiaPhong = findViewById(R.id.txtGiaPhongChiTietTin);
        txtTieude = findViewById(R.id.txtTieudeChiTietTin);
        txtDiaChi = findViewById(R.id.txtDiaChiChiTietTin);
        txtLienHe = findViewById(R.id.txtLienHeChiTietTin);
        txtDienTich = findViewById(R.id.txtDienTichPhongChiTiettin);
        gridTienIch = findViewById(R.id.gridTienIchPhongChiTietTin);
        txtDanhGia = findViewById(R.id.txtDanhGiaChiTietTin);
        vpHinhAnh = findViewById(R.id.viewPagerimgChiTietTin);
        txtMota = findViewById(R.id.txtMoTaChiTietTin);
        txtTenNguoiDatPhong = findViewById(R.id.txtNguoiDatPhongChiTietTin);

        //set data
        getAnhTheoMaTin(mTin.getMatin());
        getTienIchPhong();
        String giaphong = formatmoney(mTin.getGiaphong());
        txtGiaPhong.setText(" " + giaphong + "/Tháng");
        txtTieude.setText("" + mTin.getTentieude());
        txtDiaChi.setText(": " + mTin.getDiachi() + ", " + tenhuyen + ", " + tentinh);
        txtLienHe.setText(": " + mTin.getSdt() + " - " + mTin.getTenlienhe());
        txtDienTich.setText(": " + mTin.getDientich() + " m2");
        txtMota.setText(mTin.getMotachitiet() + "");
        countDanhGia(mTin.getMatin());
        getTenNguoiDatPhong(mTin.getMatin());

    }

    private void getPhieuTienIchTheoMaTin(String matin) {
        apiInterface getPhieuTienIchTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mPhieuTienIch>> callBack = getPhieuTienIchTheoMaTin.getTienIchTheoMaTin(matin);
        callBack.enqueue(new Callback<ArrayList<mPhieuTienIch>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<mPhieuTienIch>> call, @NotNull Response<ArrayList<mPhieuTienIch>> response) {
                if (armPhieuTienIch.size() > 0) {
                    armPhieuTienIch.clear();
                }
                armPhieuTienIch = response.body();
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<mPhieuTienIch>> call, @NotNull Throwable t) {

            }
        });
    }

    private void getTienIchPhong() {
        apiInterface getTienIchPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTienIchPhong>> callBack = getTienIchPhong.getTienIchPhong();
        callBack.enqueue(new Callback<List<mTienIchPhong>>() {
            @Override
            public void onResponse(Call<List<mTienIchPhong>> call, Response<List<mTienIchPhong>> response) {
                phieuTienIchAdapter = new PhieuTienIchAdapter((ArrayList<mTienIchPhong>) response.body(), armPhieuTienIch, vChiTietTin.this
                        , R.layout.line_tien_ich_phong);
                gridTienIch.setAdapter(phieuTienIchAdapter);
                phieuTienIchAdapter.notifyDataSetChanged();
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
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                txtDanhGia.setText(Html.fromHtml(": " + String.format("%.2f", Double.parseDouble(response.body())) + " /5 " + "<font color=\"red\">(" + countdanhgia + " đánh giá)</font>"));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getAnhTheoMaTin(String matin) {
        apiInterface getAnhTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHinhAnh>> callback = getAnhTheoMaTin.getAnhTheoMaTin(matin);
        callback.enqueue(new Callback<ArrayList<mHinhAnh>>() {
            @Override
            public void onResponse(Call<ArrayList<mHinhAnh>> call, Response<ArrayList<mHinhAnh>> response) {
                SliderHinhAnhAdapter adapter = new SliderHinhAnhAdapter(vChiTietTin.this, response.body(), R.layout.item_hinh_anh);
                vpHinhAnh.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                indicatorCTTin.setViewPager(vpHinhAnh);
                Thread time = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    loadConnect.dismiss();
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

    public static void getHuyen(String matinh) {
        apiInterface getHuyen = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHuyen>> callBack = getHuyen.getHuyen(matinh);
        callBack.enqueue(new Callback<ArrayList<mHuyen>>() {
            @Override
            public void onResponse(Call<ArrayList<mHuyen>> call, Response<ArrayList<mHuyen>> response) {
                if (armHuyen.size() > 0) {
                    armHuyen.clear();
                }
                armHuyen = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mHuyen>> call, Throwable t) {

            }
        });
    }

    private void getTenNguoiDatPhong(String matin){
        apiInterface getTenNguoiDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callback = getTenNguoiDatPhong.getTenNguoiDatPhong(matin);
        callback.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse(Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                if (response.body() != null){
                    txtTenNguoiDatPhong.setText(": "+response.body().getHo() + " " + response.body().getTen());
                }else{
                    txtTenNguoiDatPhong.setText(": Chưa có");
                }

            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {

            }
        });
    }

    private void countDanhGia(String matin) {
        apiInterface countDanhGia = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer>callBackdanhgia = countDanhGia.countDanhGia(matin);
        callBackdanhgia.enqueue(new Callback<Integer>() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onResponse(Call<Integer> call, @NotNull Response<Integer> response) {
                countdanhgia = response.body();
                getDanhGia(mTin.getMatin());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
}