package com.example.timtro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Fragment.frViTri;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vPhongDaDang extends AppCompatActivity {

    private ImageButton btnThemTin, btnThoatPhongDaDang;
    private PhongDaDangApdater phongDaDangApdater;
    private String mataikhoan;
    private ArrayList<mTin> armTin = new ArrayList<>();
    private ListView lvPhongDaDang;
    private ProgressDialog load;
    private int Phieudatphong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_phong_da_dang);
        Showprogressdialog();
        Paper.init(vPhongDaDang.this);
        mataikhoan = Paper.book().read(appAccountDetails.mataikhoan);
        AnhXa();
        btnThoatPhongDaDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvPhongDaDang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                frViTri.getTinh();
                frViTri.getHuyen(armTin.get(position).getMatinh());
                vChiTietTin.getHuyen(armTin.get(position).getMatinh());
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        lvPhongDaDang.getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetDialogView = LayoutInflater.from(lvPhongDaDang.getContext()).inflate(R.layout.dialog_itemelectphongdadang, lvPhongDaDang, false);
                bottomSheetDialog.setContentView(bottomSheetDialogView);

                bottomSheetDialogView.findViewById(R.id.btnXemPhongDaDang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(vPhongDaDang.this, vChiTietTin.class);
                        mTin item = armTin.get(position);
                        Gson gson = new Gson();
                        String mTinAsAString = gson.toJson(item);
                        intent.putExtra("mTinAsAString", mTinAsAString);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialogView.findViewById(R.id.btnSuaPhongDaDang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countPhieuDatPhong(armTin.get(position).getMatin());
                        if (armTin.get(position).getTrangthaidat().equals("1")){
                            Toast.makeText(vPhongDaDang.this
                                    , "Phòng đã được đặt, không thể sửa", Toast.LENGTH_SHORT).show();
                        }else if(Phieudatphong > 0){
                            Toast.makeText(vPhongDaDang.this
                                    , "Vui lòng xử lý các phiếu đặt phòng trước khi xóa!", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(vPhongDaDang.this, vSuaTin.class);
                            mTin item = armTin.get(position);
                            Gson gson = new Gson();
                            String mTinAsAString = gson.toJson(item);
                            intent.putExtra("mTinAsAString", mTinAsAString);
                            startActivity(intent);
                            bottomSheetDialog.dismiss();
                        }
                    }
                });

                bottomSheetDialogView.findViewById(R.id.btnXoaPhongDaDang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countPhieuDatPhong(armTin.get(position).getMatin());
                        AlertDialog.Builder al = new AlertDialog.Builder(vPhongDaDang.this);
                        al.setTitle("Xác nhận");
                        al.setMessage("Bạn có xóa tin này không?");
                        al.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (armTin.get(position).getTrangthaidat().equals("1")){
                                    dialog.dismiss();
                                    Toast.makeText(vPhongDaDang.this
                                            , "Phòng đã được đặt, không thể xóa", Toast.LENGTH_SHORT).show();
                                }else if(Phieudatphong > 0){
                                    Toast.makeText(vPhongDaDang.this
                                            , "Vui lòng xử lý các phiếu đặt phòng trước khi xóa!", Toast.LENGTH_SHORT).show();
                                }else{
                                    deleteTin(armTin.get(position).getMatin());
                                    Toast.makeText(vPhongDaDang.this, "Xóa tin thành công!"
                                            , Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    bottomSheetDialog.dismiss();
                                    finish();
                                    startActivity(new Intent(getIntent()));
                                }
                            }
                        });
                        al.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        al.show();
                    }
                });
                if (!bottomSheetDialog.isShowing()){
                    bottomSheetDialog.show();
                }
                bottomSheetDialog.setCanceledOnTouchOutside(true);
            }
        });

        btnThemTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(vPhongDaDang.this, vDangTin.class));
            }
        });

        btnThoatPhongDaDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPhongDaDang.super.onBackPressed();
            }
        });

    }

    private void Showprogressdialog() {
        load = new ProgressDialog(vPhongDaDang.this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();
    }

    private void AnhXa() {
        btnThoatPhongDaDang = (ImageButton) findViewById(R.id.btnBackPhongDaDang);
        btnThemTin = (ImageButton) findViewById(R.id.btnThemPhongDaDang);
        lvPhongDaDang = (ListView) findViewById(R.id.lvPhongDaDang);
        getTinTheoMaTaiKhoan(mataikhoan);
    }

    private void getTinTheoMaTaiKhoan(String mataikhoan){
        apiInterface getTinTheoMaTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callBack = getTinTheoMaTaiKhoan.getTinTheoMaTaiKhoan(mataikhoan);
        callBack.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse( Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                phongDaDangApdater = new PhongDaDangApdater(response.body(), vPhongDaDang.this, R.layout.line_phong_da_dang);
                lvPhongDaDang.setAdapter(phongDaDangApdater);
                phongDaDangApdater.notifyDataSetChanged();
                if(armTin.size() > 0){
                    armTin.clear();
                }
                armTin = response.body();
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
            public void onFailure(Call<ArrayList<mTin>> call, Throwable t) {
            }
        });
    }

    private void deleteTin(String matin){
        apiInterface deleteTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = deleteTin.deleteTin(matin);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void countPhieuDatPhong(String matin){
        apiInterface countPhieuDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> call = countPhieuDatPhong.countPhieuDatPhong(matin);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Phieudatphong = response.body();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        getTinTheoMaTaiKhoan(mataikhoan);
        super.onResume();
    }
}