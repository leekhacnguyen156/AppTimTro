package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class vPhongDaDat extends AppCompatActivity {
    private ImageButton btnBack;
    private ListView lvPhongDaDat;
    private ArrayList<mTin> armTin = new ArrayList<>();
    private TinChungApdater tinChungApdater;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_phongdadat);
        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();
        Anhxa();
        getPhongDaDat(frTaiKhoan.mTaiKhoan.getMataikhoan());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        lvPhongDaDat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(vPhongDaDat.this, vChiTietTinChinh.class);
                Gson gson = new Gson();
                String mTinasString = gson.toJson(armTin.get(position));
                intent.putExtra("mTinasString", mTinasString);
                startActivity(intent);
            }
        });

    }

    private void Anhxa() {
        btnBack = findViewById(R.id.btnBackPhongDaDat);
        lvPhongDaDat = findViewById(R.id.lvPhongDaDat);
    }

    private void SetAdapterPhongDaDat(){
        tinChungApdater = new TinChungApdater(armTin, vPhongDaDat.this, R.layout.line_tin_chung);
        lvPhongDaDat.setAdapter(tinChungApdater);
        tinChungApdater.notifyDataSetChanged();
    }

    private void getPhongDaDat(String mataikhoan){
        apiInterface getPhongDaDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callback = getPhongDaDat.getPhongdadat(mataikhoan);
        callback.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                if (armTin.size() > 0){
                    armTin.clear();
                }
                armTin = response.body();
                SetAdapterPhongDaDat();
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
}