package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Adapter.TinChungApdater;
import com.example.timtro.Fragment.frTaiKhoan;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vDaLuu extends AppCompatActivity {
    private ImageButton btnBack;
    private ListView lvPhongDaLuu;
    private ArrayList<mTin> armTin = new ArrayList<>();
    private TinChungApdater tinChungApdater;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_daluu);
        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();
        Anhxa();
        getPhongDaLuu(frTaiKhoan.mTaiKhoan.getMataikhoan());

        lvPhongDaLuu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(vDaLuu.this, vChiTietTinChinh.class);
                Gson gson = new Gson();
                String mTinasString = gson.toJson(armTin.get(position));
                intent.putExtra("mTinasString", mTinasString);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });
    }

    private void Anhxa() {
        btnBack = findViewById(R.id.btnBackPhongDaLuu);
        lvPhongDaLuu = findViewById(R.id.lvPhongDaLuu);
    }

    private void SetAdapterPhongDaLuu(){
        tinChungApdater = new TinChungApdater(armTin, vDaLuu.this, R.layout.line_tin_chung);
        lvPhongDaLuu.setAdapter(tinChungApdater);
        tinChungApdater.notifyDataSetChanged();
    }

    private void getPhongDaLuu(String mataikhoan){
        apiInterface getPhongDaLuu = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callback = getPhongDaLuu.getPhongdaluu(mataikhoan);
        callback.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                if (armTin.size() > 0){
                    armTin.clear();
                }
                armTin = response.body();
                SetAdapterPhongDaLuu();
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

    @Override
    protected void onResume() {
        getPhongDaLuu(frTaiKhoan.mTaiKhoan.getMataikhoan());
        super.onResume();
    }
}