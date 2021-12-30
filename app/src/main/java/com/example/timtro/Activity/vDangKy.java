package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;
import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vDangKy extends AppCompatActivity {

    private EditText edtTinhdangky, edtHuyendangky, edtTentaikhoandangky, edtMatkhaudangky, edtMatkhaudangkynhaplai, edtHodemdangky
            , edtTendangky, edtSDTdangky, edtDiachidangky, edtMatkhaucap2;
    public static String matinh = "", mahuyen = "";
    private AppCompatButton btnDangky;
    private ImageButton btnBackDangky;
    private ProgressDialog loadConnect;
    public static ArrayList<mHuyen> armHuyen = new ArrayList<>();
    public static ArrayList<String> arTenHuyen = new ArrayList<>();
    private AppCompatCheckBox checkboxXemDangKy, checkboxCap2DangKy, checkboxNhapMKLai;
    private boolean checktrungsdt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_dang_ky);
        AnhXa();
        vNhaChinh.getTinh();


        // Set On Click Button
        edtTinhdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtHuyendangky.setText("");
                SetDialogTinh();
            }
        });

        edtHuyendangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTinhdangky.getText().equals("")){
                    Toast.makeText(vDangKy.this, "Vui lòng chọn tỉnh", Toast.LENGTH_SHORT).show();
                }else{
                    SetDialogHuyen();
                }
            }
        });

        checkboxXemDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxXemDangKy.isChecked()){
                    edtMatkhaudangky.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtMatkhaudangky.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        edtMatkhaudangkynhaplai.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtMatkhaudangkynhaplai.getText().toString().isEmpty()){
                    if(edtMatkhaudangkynhaplai.getText().toString().equals(edtMatkhaudangky.getText().toString())){
                        checkboxNhapMKLai.setChecked(true);
                        checkboxNhapMKLai.setVisibility(View.VISIBLE);
                    }else{
                        checkboxNhapMKLai.setVisibility(View.GONE);
                    }
                }else{
                    checkboxNhapMKLai.setVisibility(View.GONE);
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

        edtMatkhaudangkynhaplai.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!edtMatkhaudangkynhaplai.isFocused()){
                    if(!edtMatkhaudangkynhaplai.getText().toString().isEmpty()){
                        if(!edtMatkhaudangkynhaplai.getText().toString().equals(edtMatkhaudangky.getText().toString())){
                            checkboxNhapMKLai.setChecked(false);
                            checkboxNhapMKLai.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        checkboxCap2DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxCap2DangKy.isChecked()){
                    edtMatkhaucap2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtMatkhaucap2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checksdt();
                //Hàm hide keyboard when click button
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE); imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                //Check lỗi
                if (edtTentaikhoandangky.getText().toString().trim().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập tên tài khoản!", Toast.LENGTH_SHORT).show();
                    edtTentaikhoandangky.requestFocus();
                }else if (edtTentaikhoandangky.getText().toString().trim().length() < 6 ){
                    Toast.makeText(vDangKy.this, "Tài khoản phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                    edtTentaikhoandangky.requestFocus();
                }else if (edtMatkhaudangky.getText().toString().trim().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    edtMatkhaudangky.requestFocus();
                }else if (edtMatkhaudangky.getText().toString().trim().length() < 6){
                    Toast.makeText(vDangKy.this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                    edtMatkhaudangky.requestFocus();
                }else if (edtMatkhaudangkynhaplai.getText().toString().isEmpty() || !edtMatkhaudangkynhaplai.getText().toString().equals(edtMatkhaudangky.getText().toString())){
                    Toast.makeText(vDangKy.this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                    checkboxNhapMKLai.setChecked(false);
                    checkboxNhapMKLai.setVisibility(View.VISIBLE);
                    edtMatkhaudangkynhaplai.requestFocus();
                }else if (edtMatkhaucap2.getText().toString().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập mật khẩu cấp 2!", Toast.LENGTH_SHORT).show();
                    edtMatkhaucap2.requestFocus();
                }else if (edtMatkhaucap2.getText().toString().length() < 6){
                    Toast.makeText(vDangKy.this, "Mật khẩu cấp 2 ít nhất phải có 6 ký tự!", Toast.LENGTH_SHORT).show();
                    edtMatkhaucap2.requestFocus();
                }else if (edtHodemdangky.getText().toString().trim().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập họ đệm!", Toast.LENGTH_SHORT).show();
                    edtHodemdangky.requestFocus();
                }else if(edtTendangky.getText().toString().trim().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
                    edtTendangky.requestFocus();
                }else if (edtSDTdangky.getText().toString().trim().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                    edtSDTdangky.requestFocus();
                }else if (edtSDTdangky.getText().toString().trim().length() <= 7){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập lại số điện thoại hợp lệ!", Toast.LENGTH_SHORT).show();
                    edtSDTdangky.requestFocus();
                }else if (edtTinhdangky.getText().toString().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng chọn tỉnh!", Toast.LENGTH_SHORT).show();
                }else if (edtHuyendangky.getText().toString().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng chọn huyện!", Toast.LENGTH_SHORT).show();
                }else if (edtDiachidangky.getText().toString().trim().isEmpty()){
                    Toast.makeText(vDangKy.this, "Vui lòng nhập địa chỉ cụ thể!", Toast.LENGTH_SHORT).show();
                }else{
                    loadConnect.setTitle("Đăng ký");
                    loadConnect.setMessage("Vui lòng chờ trong giây lát đang đăng ký tài khoản.");
                    loadConnect.setCanceledOnTouchOutside(false);
                    loadConnect.show();
                    CountDownTimer countDownTimerCreate = new CountDownTimer(3500, 1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            checkTenTaiKhoan(edtTentaikhoandangky.getText().toString().trim());
                        }
                    }.start();
                }
            }
        });

        btnBackDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checksdt() {
        apiInterface checkSdt = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = checkSdt.checkSdt(edtSDTdangky.getText().toString().trim());
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse( Call<Integer> call, Response<Integer> response) {
                if(response.body() > 0){
                    checktrungsdt = false;
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(vDangKy.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkTenTaiKhoan(String tentaikhoan) {
        apiInterface checkten = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = checkten.checkTenTaiKhoan(tentaikhoan);
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse( Call<Integer> call, Response<Integer> response) {
                int i = response.body();
                if( i > 0){
                    Toast.makeText(vDangKy.this, "Tên tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                    edtTentaikhoandangky.requestFocus();
                }else if (!checktrungsdt){
                    Toast.makeText(vDangKy.this, "Số điện thoại đã đăng ký bởi tài khoản khác!", Toast.LENGTH_SHORT).show();
                    edtSDTdangky.requestFocus();
                }else{
                    insertTaiKhoan(matinh, mahuyen,edtDiachidangky.getText().toString(), edtTentaikhoandangky.getText().toString().trim(),
                            edtMatkhaudangky.getText().toString().trim(), edtHodemdangky.getText().toString(),
                            edtTendangky.getText().toString(), edtSDTdangky.getText().toString(),1,
                            edtMatkhaucap2.getText().toString(),null);
                    Toast.makeText(vDangKy.this, "Đăng ký thành công! Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                loadConnect.dismiss();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(vDangKy.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa() {
        checkboxNhapMKLai = (AppCompatCheckBox) findViewById(R.id.checkboxNhapMKLai);
        checkboxXemDangKy = (AppCompatCheckBox) findViewById(R.id.checkboxXemDangKy);
        checkboxCap2DangKy = (AppCompatCheckBox) findViewById(R.id.checkboxCap2DangKy);
        edtTinhdangky = (EditText) findViewById(R.id.edtTinhdangky);
        edtHuyendangky = (EditText) findViewById(R.id.edtHuyendangky);
        btnDangky = (AppCompatButton) findViewById(R.id.btnDangky);
        btnBackDangky = (ImageButton) findViewById(R.id.btnBackDangKy);
        edtTentaikhoandangky = (EditText) findViewById(R.id.edtTentaikhoandangky);
        edtMatkhaudangky = (EditText) findViewById(R.id.edtMatkhaudangky);
        edtMatkhaudangkynhaplai = (EditText) findViewById(R.id.edtMatkhaudangkynhaplai);
        edtHodemdangky = (EditText) findViewById(R.id.edtHodemdangky);
        edtTendangky = (EditText) findViewById(R.id.edtTendangky);
        edtDiachidangky = (EditText) findViewById(R.id.edtDiachidangky);
        edtSDTdangky = (EditText) findViewById(R.id.edtSDTdangky);
        loadConnect = new ProgressDialog(vDangKy.this);
        edtMatkhaucap2 = (EditText) findViewById(R.id.edtMatkhaucap2);
    }

    private void getHuyen() {
        apiInterface getHuyen = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHuyen>> callBack = getHuyen.getHuyen(vDangKy.matinh);
        callBack.enqueue(new Callback<ArrayList<mHuyen>>() {
            @Override
            public void onResponse( Call<ArrayList<mHuyen>> call, Response<ArrayList<mHuyen>> response) {
                if(armHuyen.size() > 0){
                    armHuyen.clear();
                }
                if(arTenHuyen.size() > 0){
                    arTenHuyen.clear();
                }
                armHuyen = response.body();
                for (mHuyen huyen : armHuyen){
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
        ArrayAdapter adapter = new ArrayAdapter(vDangKy.this, android.R.layout.simple_spinner_dropdown_item, arTenHuyen);
        listHuyen.setAdapter(adapter);
        listHuyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mahuyen = armHuyen.get(position).getMahuyen();
                edtHuyendangky.setText(armHuyen.get(position).getTenhuyen());
                dialogHuyen.dismiss();
            }
        });
        dialogHuyen.show();
    }

    private void SetDialogTinh() {
        Dialog dialogTinh = new Dialog(this);
        dialogTinh.setContentView(R.layout.dialog_tinh);
        ListView listTinh = (ListView) dialogTinh.findViewById(R.id.listTinh);
        ArrayAdapter adapter = new ArrayAdapter(vDangKy.this, android.R.layout.simple_spinner_dropdown_item, vNhaChinh.arTenTinh);
        listTinh.setAdapter(adapter);
        listTinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matinh = vNhaChinh.armTinh.get(position).getMatinh();
                edtTinhdangky.setText(vNhaChinh.armTinh.get(position).getTentinh());
                dialogTinh.dismiss();
                getHuyen();
            }
        });
        dialogTinh.show();
    }

    private void insertTaiKhoan(String matinh, String mahuyen,String diachi, String tentaikhoan, String matkhau, String ho, String ten, String SDT, int loaitaikhoan, String matkhaucap2,  Text avatar){
        apiInterface insertTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insertTaiKhoan.insertTaiKhoan(matinh, mahuyen, diachi, tentaikhoan, matkhau, ho, ten, SDT, loaitaikhoan,matkhaucap2, avatar);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                String trangthai = response.body();
                if (trangthai.equals("Success")){
                    Toast.makeText(vDangKy.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(vDangKy.this, "Đã xảy ra lỗi, vui lòng thực hiện lại!", Toast.LENGTH_SHORT).show();
                }
            }

            //toast lỗi chỗ này!!
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(vDangKy.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}