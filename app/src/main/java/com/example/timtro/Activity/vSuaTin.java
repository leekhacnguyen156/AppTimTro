package com.example.timtro.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.timtro.Fragment.frHinhAnh;
import com.example.timtro.Fragment.frThongTin;
import com.example.timtro.Fragment.frViTri;
import com.example.timtro.Fragment.frXacNhan;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mTin;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;
import com.shuhart.stepview.StepView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vSuaTin extends AppCompatActivity {

    private StepView stpTienDo;
    private int stp = 0;
    private String[] stpTen = {"Vị trí", "Thông tin", "Hình ảnh", "Xác nhận"};
    private TextView btnTiepTuc, btnHuyTin, titleDangTin;
    private FragmentManager fragmentManager;
    private String mataikhoan;
    public static mTin mTin = new mTin();
    private mHuyen mHuyen = new mHuyen();
    private mTinh mTinh = new mTinh();
    private double giaphong = 0;
    private double dientich = 0;
    private ProgressDialog load;

    private ArrayList<Fragment> fragmentArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_dang_tin);

        Paper.init(vSuaTin.this);
        mataikhoan = Paper.book().read(appAccountDetails.mataikhoan);
        getIntent().getStringExtra("mTinAsAString");
        Gson gson = new Gson();
        String mTinAsAString = getIntent().getStringExtra("mTinAsAString");
        mTin = gson.fromJson(mTinAsAString, mTin.class);
        frViTri.getHuyen(mTin.getMatinh());
        AnhXa();
        frViTri.getTinh();
        frThongTin.getLoaiTin();
        frThongTin.getLoaiPhong();
        frThongTin.getTienIchPhong();
        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frlayDangTin, fragmentArray.get(0)).commit();
        }

        stpTienDo.getState()
                .animationType(StepView.ANIMATION_ALL)
                .steps(Arrays.asList(stpTen))
                .animationDuration(getResources().getInteger(android.R.integer.config_longAnimTime))
                .commit();
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = false;
                switch (stp) {
                    case 0:
                        if (frViTri.edtChonTinhTin.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng chọn tỉnh !", Toast.LENGTH_SHORT).show();
                        } else if (frViTri.edtChonHuyenTin.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng chọn huyện !", Toast.LENGTH_SHORT).show();
                        } else if (frViTri.edtDiaChiTin.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập số nhà, tên đường !", Toast.LENGTH_SHORT).show();
                        } else {
                            check = true;
                        }
                        break;
                    case 1:
                        dientich = Double.parseDouble(frThongTin.edtDienTich.getText().toString().trim());
                        NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.ITALY);
                        try {
                            giaphong = usFormat.parse(frThongTin.edtGiaPhong.getText().toString().trim()).doubleValue();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (frThongTin.edtGiaPhong.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập giá phòng !", Toast.LENGTH_SHORT).show();
                        } else if (giaphong < 100000) {
                            Toast.makeText(vSuaTin.this, "Giá phòng tối thiểu là 100.000 vnđ !", Toast.LENGTH_SHORT).show();
                        } else if (frThongTin.edtDienTich.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập diện tích !", Toast.LENGTH_SHORT).show();
                        } else if (dientich < 6) {
                            Toast.makeText(vSuaTin.this, "Diện tích tối thiểu là 6 m2 !", Toast.LENGTH_SHORT).show();
                        } else {
                            check = true;

                        }
                        break;
                    case 2:
                        if (frHinhAnh.armUri.size() <= 0 && frHinhAnh.armHinhAnh.size() <= 0) {
                            Toast.makeText(vSuaTin.this, "Vui lòng chọn ít nhất 1 ảnh !", Toast.LENGTH_SHORT).show();
                        } else {
                            check = true;
                        }
                        break;
                    case 3:
                        if (frXacNhan.edtTieuDeBaiDang.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập tiêu đề bài đăng !", Toast.LENGTH_SHORT).show();
                        } else if (frXacNhan.edtTieuDeBaiDang.getText().toString().trim().length() < 20) {
                            Toast.makeText(vSuaTin.this, "Tiêu đề tối thiểu phải 20 kí tự !", Toast.LENGTH_SHORT).show();
                        } else if (frXacNhan.edtTenChuTin.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập tên chủ tin !", Toast.LENGTH_SHORT).show();
                        } else if (frXacNhan.edtSoDienThoai.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập số điện thoại liên lạc !", Toast.LENGTH_SHORT).show();
                        } else if (frXacNhan.edtSoDienThoai.getText().toString().trim().length() != 10) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập số điện thoại hợp lệ !", Toast.LENGTH_SHORT).show();
                        } else if (frXacNhan.edtMoTa.getText().toString().trim().isEmpty()) {
                            Toast.makeText(vSuaTin.this, "Vui lòng nhập mô tả bài đăng !", Toast.LENGTH_SHORT).show();
                        } else if (frXacNhan.edtMoTa.getText().toString().trim().length() < 100) {
                            Toast.makeText(vSuaTin.this, "Mô tả tối thiểu phải 100 kí tự !", Toast.LENGTH_SHORT).show();
                        } else {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strDate = sdf.format(c.getTime());
                            if (frHinhAnh.armUri.size() > 0 && frHinhAnh.armHinhAnh.size() <= 0){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(vSuaTin.this.getContentResolver(), frHinhAnh.armUri.get(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                                String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                                String name = String.valueOf(Calendar.getInstance().getTimeInMillis());
                                UPDATETIN(mTin.getMatin(), frThongTin.armLoaiTin.get(frThongTin.iLoaiTin).getMaloaitin(),
                                        frThongTin.armLoaiPhong.get(frThongTin.iLoaiPhong).getMaloaiphong(),
                                        frXacNhan.edtTieuDeBaiDang.getText().toString().trim(),
                                        frViTri.matinh, frViTri.mahuyen,
                                        frViTri.edtDiaChiTin.getText().toString().trim(),
                                        frXacNhan.edtTenChuTin.getText().toString().trim(),
                                        frXacNhan.edtSoDienThoai.getText().toString().trim(),
                                        giaphong,
                                        dientich,
                                        frXacNhan.edtMoTa.getText().toString().trim(), strDate, name, image);
                                //Sửa tin
                            }else{
                                UPDATETINKHONGANH(mTin.getMatin(), frThongTin.armLoaiTin.get(frThongTin.iLoaiTin).getMaloaitin(),
                                        frThongTin.armLoaiPhong.get(frThongTin.iLoaiPhong).getMaloaiphong(),
                                        frXacNhan.edtTieuDeBaiDang.getText().toString().trim(),
                                        frViTri.matinh, frViTri.mahuyen,
                                        frViTri.edtDiaChiTin.getText().toString().trim(),
                                        frXacNhan.edtTenChuTin.getText().toString().trim(),
                                        frXacNhan.edtSoDienThoai.getText().toString().trim(),
                                        giaphong,
                                        dientich,
                                        frXacNhan.edtMoTa.getText().toString().trim(), strDate);
                            }

                        }
                        break;
                }
                if (check) {
                    Fragment frTamp = null;
                    stp++;
                    stpTienDo.go(stp, true);
                    switch (stp) {
                        case 1:
                            frTamp = fragmentArray.get(1);
                            btnHuyTin.setText("Quay lại");
                            btnHuyTin.setTextColor(Color.parseColor("#CC000000"));
                            break;
                        case 2:
                            frTamp = fragmentArray.get(2);
                            break;
                        case 3:
                            frTamp = fragmentArray.get(3);
                            btnTiepTuc.setText("Lưu");
                            break;
                    }
                    if (frTamp != null) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frlayDangTin, frTamp).commit();
                    }
                }
            }
        });

        btnHuyTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frTamp = null;
                if (stp > 0) {
                    stp--;
                    stpTienDo.go(stp, true);
                    switch (stp) {
                        case 0:
                            frTamp = fragmentArray.get(0);
                            btnHuyTin.setText("Hủy");
                            btnHuyTin.setTextColor(Color.RED);
                            break;
                        case 1:
                            frTamp = fragmentArray.get(1);
                            btnHuyTin.setText("Quay lại");
                            btnHuyTin.setTextColor(Color.parseColor("#CC000000"));
                            break;
                        case 2:
                            frTamp = fragmentArray.get(2);
                            btnTiepTuc.setText("Tiếp tục");
                            break;
                    }
                    if (frTamp != null) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frlayDangTin, frTamp).commit();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(vSuaTin.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    builder.setMessage("Bạn có chắc muốn dừng sửa tin này ?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vSuaTin.super.onBackPressed();
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private void UPDATETIN(String matin, String maloaitin,
                           String maloaiphong, String tentieude,
                           String matinh, String mahuyen, String diachi, String tenlienhe,
                           String sdt, double giaphong, double dientich,
                           String motachitiet,
                           String thoigiandang, String name, String image) {
        apiInterface updateTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTin.updateTin(matin, maloaitin, maloaiphong, tentieude, matinh, mahuyen,
                diachi, tenlienhe, sdt, giaphong, dientich, motachitiet,
                thoigiandang, name, image);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("Success")) {
                    updateImage(mTin.getMatin());
                    load = new ProgressDialog(vSuaTin.this);
                    load.setTitle("Đang tải");
                    load.setMessage("Đang sửa tin...");
                    load.setCanceledOnTouchOutside(false);
                    load.show();
                    Toast.makeText(vSuaTin.this, "Sửa tin thành công!", Toast.LENGTH_SHORT).show();onBackPressed();
                    Thread time = new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        load.dismiss();
                                        finish();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    );
                    time.start();
                } else {
                    Toast.makeText(vSuaTin.this, "Đã xảy ra lỗi, vui lòng thử tin lại !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vSuaTin.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UPDATETINKHONGANH(String matin, String maloaitin,
                           String maloaiphong, String tentieude,
                           String matinh, String mahuyen, String diachi, String tenlienhe,
                           String sdt, double giaphong, double dientich,
                           String motachitiet,
                           String thoigiandang) {
        apiInterface updateTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTin.updateTinKhongCoAnh(matin, maloaitin, maloaiphong, tentieude, matinh, mahuyen,
                diachi, tenlienhe, sdt, giaphong, dientich, motachitiet, thoigiandang);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("Success")) {
                    updatePhieutienich(matin);
                    for (String matienich : frThongTin.armmatienich) {
                        if (!matienich.isEmpty()) {
                            insertPhieuTienIch(matin, matienich);
                        }
                    }
                    Toast.makeText(vSuaTin.this, "Sửa tin thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(vSuaTin.this, "Đã xảy ra lỗi, vui lòng thử tin lại !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(vSuaTin.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa() {
        stpTienDo = (StepView) findViewById(R.id.stpTienDo);
        btnTiepTuc = (TextView) findViewById(R.id.btnTiepTuc);
        titleDangTin = (TextView) findViewById(R.id.titleDangTin);
        titleDangTin.setText("Sửa tin");
        btnHuyTin = (TextView) findViewById(R.id.btnHuyTin);
        fragmentArray.add(new frViTri());
        fragmentArray.add(new frThongTin());
        fragmentArray.add(new frHinhAnh());
        fragmentArray.add(new frXacNhan());
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(vSuaTin.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setMessage("Bạn có chắc muốn dừng sửa tin này ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTin = new mTin();
                vSuaTin.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void updatePhieutienich(String matin) {
        apiInterface updatePhieutienich = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> call = updatePhieutienich.updatePhieuTienIch(matin);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void updateImage(String matin) {
        apiInterface updateImage = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> call = updateImage.updateImage(matin);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updatePhieutienich(matin);
                for (Uri uri : frHinhAnh.armUri) {
                    try {
                        insertImage(MediaStore.Images.Media.getBitmap(vSuaTin.this.getContentResolver(), uri), matin);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (String matienich : frThongTin.armmatienich) {
                    if (!matienich.isEmpty()) {
                        insertPhieuTienIch(matin, matienich);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void insertImage(Bitmap bitmap, String matin) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        apiInterface insertImage = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> call = insertImage.insertImage(matin, name, image);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void insertPhieuTienIch(String matin, String matienich) {
        apiInterface insertPhieuTienIch = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> call = insertPhieuTienIch.insertPhieuTienIch(matin, matienich);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
