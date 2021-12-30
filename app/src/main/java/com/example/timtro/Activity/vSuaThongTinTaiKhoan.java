package com.example.timtro.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.timtro.Fragment.frTaiKhoan;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.timtro.Fragment.frTaiKhoan.mTaiKhoan;

public class vSuaThongTinTaiKhoan extends AppCompatActivity {

    private ImageButton btnBack, btnSave;
    private AppCompatButton btnDoiMatKhau;
    private EditText edtHodem, edtTen, edtSDT, edtTinh, edtHuyen, edtDiaChi;
    private CircleImageView imgThongtintaikhoan;
    private final int Request_code_image = 123;
    private Bitmap bmp;
    public static ArrayList<String> arTenHuyen = new ArrayList<>();
    public static ArrayList<mHuyen> armHuyen = new ArrayList<>();
    private String matinh = "", mahuyen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_sua_thong_tin_tai_khoan);
        Paper.init(this);
        getHuyen(mTaiKhoan.getMatinh());
        Anhxa();
        LoadThongtin();

        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogDoiMatKhau();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDialogTinh();
            }
        });
        edtHuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDialogHuyen();
            }
        });

        imgThongtintaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Request_code_image);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtHodem.getText().toString().trim().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập họ đệm!", Toast.LENGTH_SHORT).show();
                    edtHodem.requestFocus();
                } else if (edtTen.getText().toString().trim().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
                    edtTen.requestFocus();
                } else if (edtSDT.getText().toString().trim().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                    edtSDT.requestFocus();
                } else if (edtSDT.getText().toString().trim().length() <= 7) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập lại số điện thoại hợp lệ!", Toast.LENGTH_SHORT).show();
                    edtSDT.requestFocus();
                } else if (edtTinh.getText().toString().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng chọn tỉnh!", Toast.LENGTH_SHORT).show();
                } else if (edtHuyen.getText().toString().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng chọn huyện!", Toast.LENGTH_SHORT).show();
                } else if (edtDiaChi.getText().toString().trim().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập địa chỉ cụ thể!", Toast.LENGTH_SHORT).show();
                    edtDiaChi.requestFocus();
                } else {
                    if (bmp != null) {
                        frTaiKhoan.insertImage(bmp, Paper.book().read(appAccountDetails.mataikhoan));
                    }
                    updateTaiKhoan(Paper.book().read(appAccountDetails.mataikhoan), matinh, mahuyen, edtDiaChi.getText().toString(),
                            edtHodem.getText().toString(), edtTen.getText().toString(), edtSDT.getText().toString());
                }
            }
        });


    }

    private void setDialogDoiMatKhau() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_doi_mat_khau);
        final EditText edtMatkhau = dialog.findViewById(R.id.edtMatkhauDoimatkhau);
        final EditText edtMatkhauMoi = dialog.findViewById(R.id.edtMatkhauMoiDoiMatKhau);
        final EditText edtNhapLaiMatkhauMoi = dialog.findViewById(R.id.edtNhapLaiMatkhauMoiDoiMatKhau);
        AppCompatCheckBox checkboxXemMK = dialog.findViewById(R.id.checkboxXemMKDoiMK);
        final Button btnOK = dialog.findViewById(R.id.btnOKDoiMatKhau);
        final Button btnHuy = dialog.findViewById(R.id.btnHuyDoiMatKhau);
        AppCompatCheckBox checkboxXemMKMDoiMK = dialog.findViewById(R.id.checkboxXemMKMDoiMK);
        AppCompatCheckBox checkboxNhapMKLaiDoiMatKhau = dialog.findViewById(R.id.checkboxNhapMKLaiDoiMatKhau);

        edtNhapLaiMatkhauMoi.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtNhapLaiMatkhauMoi.getText().toString().isEmpty()){
                    if(edtNhapLaiMatkhauMoi.getText().toString().equals(edtMatkhauMoi.getText().toString())){
                        checkboxNhapMKLaiDoiMatKhau.setChecked(true);
                        checkboxNhapMKLaiDoiMatKhau.setVisibility(View.VISIBLE);
                    }else{
                        checkboxNhapMKLaiDoiMatKhau.setVisibility(View.GONE);
                    }
                }else{
                    checkboxNhapMKLaiDoiMatKhau.setVisibility(View.GONE);
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

        edtNhapLaiMatkhauMoi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!edtNhapLaiMatkhauMoi.isFocused()){
                    if(!edtNhapLaiMatkhauMoi.getText().toString().isEmpty()){
                        if(!edtNhapLaiMatkhauMoi.getText().toString().equals(edtMatkhauMoi.getText().toString())){
                            checkboxNhapMKLaiDoiMatKhau.setChecked(false);
                            checkboxNhapMKLaiDoiMatKhau.setVisibility(View.VISIBLE);
                        }
                    }
                }
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

        checkboxXemMKMDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxXemMKMDoiMK.isChecked()){
                    edtMatkhauMoi.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtMatkhauMoi.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMatkhau.getText().toString().trim().isEmpty()) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.setText("");
                    edtMatkhau.requestFocus();
                } else if (!edtMatkhau.getText().toString().equals(mTaiKhoan.getMatkhau())) {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                    edtMatkhau.setText("");
                    edtMatkhau.requestFocus();
                }else if(edtMatkhauMoi.getText().toString().trim().isEmpty()){
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập mật khẩu mới!"
                            , Toast.LENGTH_SHORT).show();
                    edtMatkhauMoi.setText("");
                    edtMatkhauMoi.requestFocus();
                }else if(edtMatkhauMoi.getText().toString().trim().equals(mTaiKhoan.getMatkhau())){
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Mật khẩu mới không được trùng với mật khẩu cũ!"
                            , Toast.LENGTH_SHORT).show();
                    edtMatkhauMoi.setText("");
                    edtNhapLaiMatkhauMoi.setText("");
                    edtMatkhauMoi.requestFocus();
                }else if(edtNhapLaiMatkhauMoi.getText().toString().trim().isEmpty()){
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Vui lòng nhập lại mật khẩu mới!"
                            , Toast.LENGTH_SHORT).show();
                    edtNhapLaiMatkhauMoi.setText("");
                    edtNhapLaiMatkhauMoi.requestFocus();
                }else if(!edtNhapLaiMatkhauMoi.getText().toString().trim().equals(edtMatkhauMoi.getText().toString().trim())){
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Mật khẩu nhập lại không giống mật khẩu mới!"
                            , Toast.LENGTH_LONG).show();
                    edtNhapLaiMatkhauMoi.setText("");
                    edtNhapLaiMatkhauMoi.requestFocus();
                }else if(edtMatkhauMoi.getText().toString().trim().length() < 6){
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Mật khẩu phải có ít nhất 6 ký tự!"
                            , Toast.LENGTH_SHORT).show();
                    edtMatkhauMoi.setText("");
                    edtMatkhauMoi.requestFocus();
                }else {
                    updateMatKhauTaiKhoan(frTaiKhoan.mTaiKhoan.getMataikhoan(), edtMatkhau.getText().toString().trim(), edtMatkhauMoi.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void Anhxa() {
        btnBack = findViewById(R.id.btnBackThongtintaikhoan);
        btnSave = findViewById(R.id.btnSaveThongtintaikhoan);
        edtHodem = findViewById(R.id.edtHodemThongtintaikhoan);
        edtTen = findViewById(R.id.edtTenThongtintaikhoan);
        edtTinh = findViewById(R.id.edtTinhThongtintaikhoan);
        edtSDT = findViewById(R.id.edtSDTThongtintaikhoan);
        edtHuyen = findViewById(R.id.edtHuyenThongtintaikhoan);
        edtDiaChi = findViewById(R.id.edtDiachiThongtintaikhoan);
        imgThongtintaikhoan = findViewById(R.id.imgThongtintaikhoan);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);


        edtHodem.setText(mTaiKhoan.getHo());
        edtTen.setText(mTaiKhoan.getTen());
        edtDiaChi.setText(mTaiKhoan.getDiachi() + "");
        edtSDT.setText(mTaiKhoan.getSodienthoai());
        matinh = mTaiKhoan.getMatinh();
        mahuyen = mTaiKhoan.getMahuyen();
        Picasso.get().load(mTaiKhoan.getAvatar()).into(imgThongtintaikhoan);
    }

    private void LoadThongtin() {
        for (mTinh mtinh : vNhaChinh.armTinh) {
            if (mtinh.getMatinh().equals(mTaiKhoan.getMatinh())) {
                edtTinh.setText(mtinh.getTentinh());
                break;
            }
        }
        if (armHuyen.size() > 0) {
            for (mHuyen mhuyen : armHuyen) {
                if (mTaiKhoan.getMahuyen().equals(mhuyen.getMahuyen())) {
                    edtHuyen.setText(mhuyen.getTenhuyen());
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
                if (arTenHuyen.size() > 0) {
                    arTenHuyen.clear();
                }
                armHuyen = (ArrayList<mHuyen>) response.body();
                for (mHuyen huyen : armHuyen) {
                    arTenHuyen.add(huyen.getTenhuyen());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<mHuyen>> call, Throwable t) {
            }
        });
    }


    private void SetDialogHuyen() {
        Dialog dialogHuyen = new Dialog(this);
        dialogHuyen.setContentView(R.layout.dialog_huyen);
        ListView listHuyen = (ListView) dialogHuyen.findViewById(R.id.listHuyen);
        ArrayAdapter adapter = new ArrayAdapter(vSuaThongTinTaiKhoan.this, android.R.layout.simple_spinner_dropdown_item, arTenHuyen);
        listHuyen.setAdapter(adapter);
        listHuyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mahuyen = armHuyen.get(position).getMahuyen();
                edtHuyen.setText(armHuyen.get(position).getTenhuyen());
                dialogHuyen.dismiss();
            }
        });
        dialogHuyen.show();
    }

    private void SetDialogTinh() {
        Dialog dialogTinh = new Dialog(this);
        dialogTinh.setContentView(R.layout.dialog_tinh);
        ListView listTinh = (ListView) dialogTinh.findViewById(R.id.listTinh);
        ArrayAdapter adapter = new ArrayAdapter(vSuaThongTinTaiKhoan.this, android.R.layout.simple_spinner_dropdown_item, vNhaChinh.arTenTinh);
        listTinh.setAdapter(adapter);
        listTinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matinh = vNhaChinh.armTinh.get(position).getMatinh();
                mahuyen = "";
                edtHuyen.setText("");
                edtTinh.setText(vNhaChinh.armTinh.get(position).getTentinh());
                dialogTinh.dismiss();
                getHuyen(matinh);
            }
        });
        dialogTinh.show();
    }

    private void updateTaiKhoan(String mataikhoan, String matinh, String mahuyen, String diachi,
                                String ho, String ten, String SDT) {
        apiInterface updateTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callback = updateTaiKhoan.updateTaiKhoan(mataikhoan, matinh, mahuyen, diachi, ho, ten, SDT);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Success")) {
                    Paper.book().write(appAccountDetails.matinh, matinh);
                    Paper.book().write(appAccountDetails.mahuyen, mahuyen);
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    frTaiKhoan.getTaikhoan(Paper.book().read(appAccountDetails.mataikhoan));
                    finish();
                } else {
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Đã có lỗi xảy ra! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Request_code_image && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                bmp = BitmapFactory.decodeStream(inputStream);
                imgThongtintaikhoan.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateMatKhauTaiKhoan(String mataikhoan, String matkhau, String matkhaumoi){
        apiInterface updateMatKhauTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callback = updateMatKhauTaiKhoan.updateMatKhauTaiKhoan(mataikhoan,matkhau,matkhaumoi);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Success")){
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    frTaiKhoan.getTaikhoan(Paper.book().read(appAccountDetails.mataikhoan));
                    vSuaThongTinTaiKhoan.this.finish();
                }else{
                    Toast.makeText(vSuaThongTinTaiKhoan.this, "Đã xảy ra lỗi! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}