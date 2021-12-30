package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timtro.Adapter.PhieuTienIchAdapter;
import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Adapter.SliderHinhAnhAdapter;
import com.example.timtro.Adapter.TinTrangChuAdapter;
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
import com.example.timtro.vNhaChinh;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vChiTietTinChinh extends AppCompatActivity {

    private TextView txtGiaPhongChiTietTinChinh, txtTieudeChiTietTinChinh, txtDiaChiChiTietTinChinh,
            txtLienHeChiTietTinChinh, txtDienTichPhongChiTietTinChinh, txtDanhGiaChiTietTin, txtMoTaChiTietTinChinh;
    private ViewPager viewPagerimgChiTietTinChinh;
    private ImageButton btnBackChiTietTinChinh;
    private GridView gridTienIchPhongChiTietTinChinh;
    private mTin mTin = null;
    private ProgressDialog load;
    private PhieuTienIchAdapter phieuTienIchAdapter;
    private FloatingActionButton btnTuyChonCTTC;
    private ArrayList<mTienIchPhong> armTienIchPhong = new ArrayList<>();
    private float diemsao = 0;
    private int checkdat = 0;
    private AppCompatCheckBox checkboxLuuTinChinh;
    private CircleImageView imgAvtTaikhoanTin;
    private TextView txtTenTaiKhoanTin, txtDiaChiTaiKhoanTin;
    private mTinh mTinh = null;
    private CircleIndicator indicatorCTTinChinh;
    private int countdanhgia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_chi_tiet_tin_chinh);

        Paper.init(vChiTietTinChinh.this);

        Gson gson = new Gson();
        String mTinasString = getIntent().getStringExtra("mTinasString");
        mTin = gson.fromJson(mTinasString, mTin.class);

        countTrungPhieuDatPhong(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin());

        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();

        AnhXa();
        getTienIchPhong();
        getAnhTheoMaTin(mTin.getMatin());

        btnBackChiTietTinChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vChiTietTinChinh.super.onBackPressed();
                finish();
            }
        });

        btnTuyChonCTTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDangGiaTheoMaTaiKhoan(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin());
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        vChiTietTinChinh.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(vChiTietTinChinh.this).inflate(R.layout.tuy_chon_cttc_bottom_sheet_dialog, null, false);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetView.findViewById(R.id.btnCall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse("tel:" + mTin.getSdt()));
                        startActivity(intentCall);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnMess).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentSMS = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mTin.getSdt(), null));
                        startActivity(intentSMS);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnDanhGia).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Paper.book().read(appAccountDetails.mataikhoan) != null) {
                            if(!Paper.book().read(appAccountDetails.mataikhoan).equals(mTin.getMataikhoan())){
                                SetDialogDanhGia();
                            }else{
                                Toast.makeText(vChiTietTinChinh.this, "Vui lòng không đánh giá phòng do mình đăng !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(vChiTietTinChinh.this).create();
                            alertDialog.setTitle("Đăng nhập");
                            alertDialog.setMessage("Vui lòng đăng nhập để đánh giá !");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Có",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(vChiTietTinChinh.this, vDangNhap.class));
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Không",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnDatPhong).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Paper.book().read(appAccountDetails.mataikhoan) != null) {
                            if(!Paper.book().read(appAccountDetails.mataikhoan).equals(mTin.getMataikhoan())){
                                if(mTin.getTrangthaidat().equals("0")){
                                    if (checkdat == 1) {
                                        Toast.makeText(vChiTietTinChinh.this, "Phòng này bạn đã đặt vui lòng chờ duyệt !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(vChiTietTinChinh.this, vDatPhong.class);
                                        Gson gson = new Gson();
                                        String mTinDatPhong = gson.toJson(mTin);
                                        intent.putExtra("mTinDatPhong", mTinDatPhong);
                                        startActivity(intent);
                                    }
                                }else{
                                    Toast.makeText(vChiTietTinChinh.this, "Phòng đã được đặt !", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(vChiTietTinChinh.this, "Vui lòng không đặt phòng do mình đăng !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(vChiTietTinChinh.this).create();
                            alertDialog.setTitle("Đăng nhập");
                            alertDialog.setMessage("Vui lòng đăng nhập để đặt phòng !");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Có",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(vChiTietTinChinh.this, vDangNhap.class));
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Không",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }
        });

        checkboxLuuTinChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Paper.book().read(appAccountDetails.mataikhoan).equals(mTin.getMataikhoan())){
                    if (checkboxLuuTinChinh.isChecked()) {
                        insertPhieuLuuPhong(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin());
                    } else {
                        deletePhieuLuuPhong(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin());
                    }
                }else{
                    checkboxLuuTinChinh.setChecked(false);
                    Toast.makeText(vChiTietTinChinh.this, "Vui lòng không lưu phòng do mình đăng !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void countTrungPhieuDatPhong(String mataikhoan, String matin) {
        apiInterface countTrungPhieuDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = countTrungPhieuDatPhong.countTrungPhieuDatPhong(mataikhoan, matin);
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() > 0) {
                    checkdat = response.body();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void getDangGiaTheoMaTaiKhoan(String mataikhoan, String matin) {
        apiInterface getDangGiaTheoMaTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<Float> callBack = getDangGiaTheoMaTaiKhoan.getDangGiaTheoMaTaiKhoan(mataikhoan, matin);
        callBack.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if (response.body() != null) {
                    diemsao = response.body();
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {

            }
        });
    }

    private void countPhieuLuuTheoTaiKhoan(String mataikhoan, String matin) {
        apiInterface countPhieuLuuTheoTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = countPhieuLuuTheoTaiKhoan.countPhieuLuuTheoTaiKhoan(mataikhoan, matin);
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null && response.body() == 1) {
                    checkboxLuuTinChinh.setChecked(true);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void AnhXa() {
        indicatorCTTinChinh = (CircleIndicator) findViewById(R.id.indicatorCTTinChinh);
        checkboxLuuTinChinh = (AppCompatCheckBox) findViewById(R.id.checkboxLuuTinChinh);
        countPhieuLuuTheoTaiKhoan(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin());
        btnTuyChonCTTC = (FloatingActionButton) findViewById(R.id.btnTuyChonCTTC);
        btnBackChiTietTinChinh = (ImageButton) findViewById(R.id.btnBackChiTietTinChinh);
        viewPagerimgChiTietTinChinh = (ViewPager) findViewById(R.id.viewPagerimgChiTietTinChinh);
        gridTienIchPhongChiTietTinChinh = (GridView) findViewById(R.id.gridTienIchPhongChiTietTinChinh);
        txtGiaPhongChiTietTinChinh = (TextView) findViewById(R.id.txtGiaPhongChiTietTinChinh);
        txtTieudeChiTietTinChinh = (TextView) findViewById(R.id.txtTieudeChiTietTinChinh);
        txtDiaChiChiTietTinChinh = (TextView) findViewById(R.id.txtDiaChiChiTietTinChinh);
        txtLienHeChiTietTinChinh = (TextView) findViewById(R.id.txtLienHeChiTietTinChinh);
        txtDienTichPhongChiTietTinChinh = (TextView) findViewById(R.id.txtDienTichPhongChiTietTinChinh);
        txtDanhGiaChiTietTin = (TextView) findViewById(R.id.txtDanhGiaChiTietTin);
        txtMoTaChiTietTinChinh = (TextView) findViewById(R.id.txtMoTaChiTietTinChinh);
        imgAvtTaikhoanTin = (CircleImageView) findViewById(R.id.imgAvtTaikhoanTin);
        txtTenTaiKhoanTin = (TextView) findViewById(R.id.txtTenTaiKhoanTin);
        txtDiaChiTaiKhoanTin = (TextView) findViewById(R.id.txtDiaChiTaiKhoanTin);
        String giaphong = PhongDaDangApdater.formatmoney(mTin.getGiaphong());
        txtGiaPhongChiTietTinChinh.setText(" " + giaphong + "/Tháng");
        txtTieudeChiTietTinChinh.setText("" + mTin.getTentieude());
        getTinhChiTiet(mTin.getMatinh(), mTin.getMahuyen());
        txtLienHeChiTietTinChinh.setText(": " + mTin.getSdt() + " - " + mTin.getTenlienhe());
        txtMoTaChiTietTinChinh.setText(mTin.getMotachitiet());
        txtDienTichPhongChiTietTinChinh.setText(": " + mTin.getDientich() + " m2");
        countDanhGia(mTin.getMatin());
        getTaiKhoan(mTin.getMataikhoan());
        if (Paper.book().read(appAccountDetails.mataikhoan) == null) {
            checkboxLuuTinChinh.setVisibility(View.GONE);
        }
    }

    private void getPhieuTienIchTheoMaTin(String matin) {
        apiInterface getPhieuTienIchTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mPhieuTienIch>> callBack = getPhieuTienIchTheoMaTin.getTienIchTheoMaTin(matin);
        callBack.enqueue(new Callback<ArrayList<mPhieuTienIch>>() {
            @Override
            public void onResponse(Call<ArrayList<mPhieuTienIch>> call, Response<ArrayList<mPhieuTienIch>> response) {
                phieuTienIchAdapter = new PhieuTienIchAdapter(armTienIchPhong, response.body(), vChiTietTinChinh.this
                        , R.layout.line_tien_ich_phong);
                gridTienIchPhongChiTietTinChinh.setAdapter(phieuTienIchAdapter);
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
                if(!response.body().isEmpty()){
                    txtDanhGiaChiTietTin.setText(Html.fromHtml(": " + String.format("%.2f", Double.parseDouble(response.body())) + " /5 " + "<font color=\"red\">(" + countdanhgia + " đánh giá)</font>"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                txtDanhGiaChiTietTin.setText(": Chưa có đánh giá");
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

    private void getAnhTheoMaTin(String matin) {
        apiInterface getAnhTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHinhAnh>> callback = getAnhTheoMaTin.getAnhTheoMaTin(matin);
        callback.enqueue(new Callback<ArrayList<mHinhAnh>>() {
            @Override
            public void onResponse(Call<ArrayList<mHinhAnh>> call, Response<ArrayList<mHinhAnh>> response) {
                SliderHinhAnhAdapter sliderHinhAnhAdapter = new SliderHinhAnhAdapter(vChiTietTinChinh.this, response.body(), R.layout.item_hinh_anh);
                viewPagerimgChiTietTinChinh.setAdapter(sliderHinhAnhAdapter);
                indicatorCTTinChinh.setViewPager(viewPagerimgChiTietTinChinh);
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
                    Picasso.get().load(response.body().getAvatar()).into(imgAvtTaikhoanTin);
                }
                txtTenTaiKhoanTin.setText(response.body().getTentaikhoan());
                txtDiaChiTaiKhoanTin.setText(response.body().getDiachi());
            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {

            }
        });
    }

    private void SetDialogDanhGia() {
        final Dialog dialog = new Dialog(vChiTietTinChinh.this);
        dialog.setContentView(R.layout.dialog_danh_gia);

        AppCompatButton btnHuyDanhGia = (AppCompatButton) dialog.findViewById(R.id.btnHuyDanhGia);
        AppCompatButton btnOKDanhGia = (AppCompatButton) dialog.findViewById(R.id.btnOKDanhGia);
        AppCompatRatingBar ratingDanhGia = (AppCompatRatingBar) dialog.findViewById(R.id.ratingDanhGia);

        ratingDanhGia.setRating(diemsao);

        btnHuyDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOKDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingDanhGia.getRating() > 0.0) {
                    if (diemsao > 0) {
                        updateDanhGia(ratingDanhGia.getRating());
                    } else {
                        insertDanhGia(ratingDanhGia.getRating());
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(vChiTietTinChinh.this, "Vui lòng chọn số sao !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void insertDanhGia(float diem) {
        apiInterface insertDanhGia = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = insertDanhGia.insertDanhGia(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin(), diem);
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, @NotNull Response<String> response) {
                countDanhGia(mTin.getMatin());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vChiTietTinChinh.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDanhGia(float diem) {
        apiInterface updateDanhGia = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = updateDanhGia.updateDanhGia(Paper.book().read(appAccountDetails.mataikhoan), mTin.getMatin(), diem);
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, @NotNull Response<String> response) {
                countDanhGia(mTin.getMatin());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vChiTietTinChinh.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertPhieuLuuPhong(String mataikhoan, String matin) {
        apiInterface insertPhieuLuuPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = insertPhieuLuuPhong.insertPhieuLuuPhong(mataikhoan, matin);
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, @NotNull Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vChiTietTinChinh.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePhieuLuuPhong(String mataikhoan, String matin) {
        apiInterface deletePhieuLuuPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = deletePhieuLuuPhong.deletePhieuLuuPhong(mataikhoan, matin);
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, @NotNull Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vChiTietTinChinh.this, "" + t, Toast.LENGTH_SHORT).show();
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
                txtDiaChiChiTietTinChinh.setText(mTin.getDiachi() + ", " + response.body().getTenhuyen() + ", " + mTinh.getTentinh());
            }

            @Override
            public void onFailure(Call<mHuyen> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}