package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Adapter.TinChungApdater;
import com.example.timtro.Adapter.TinTrangChuAdapter;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vXemThemTin extends AppCompatActivity {

    private int madanhmuc = -1;
    public static ProgressDialog load;
    private ArrayList<mTin> arTinMoiDang = new ArrayList<>();
    private ArrayList<mTin> arTinOGhep = new ArrayList<>();
    private TinChungApdater tinChungApdater;
    private ListView lvXemThemTin;
    private TextView titleXemThemTin;
    private ImageButton btnXemThemTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_xem_them_tin);
        madanhmuc = getIntent().getIntExtra("madanhmuc", -1);
        Paper.init(vXemThemTin.this);

        AnhXa();

        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();

        switch (madanhmuc){
            case 0:
                titleXemThemTin.setText("Phòng mới đăng");
                getTinMoiDang(Paper.book().read(appAccountDetails.matinh));
                break;
            case 1:
                titleXemThemTin.setText("Phòng ở ghép");
                getTinOGhep(Paper.book().read(appAccountDetails.matinh));
                break;
        }

        btnXemThemTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vXemThemTin.super.onBackPressed();
            }
        });

        lvXemThemTin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTin mTin = null;
                switch (madanhmuc) {
                    case 0:
                        mTin = arTinMoiDang.get(position);
                        break;
                    case 1:
                        mTin = arTinOGhep.get(position);
                        break;
                }
                Intent intent = new Intent(vXemThemTin.this, vChiTietTinChinh.class);
                Gson gson = new Gson();
                String mTinasString = gson.toJson(mTin);
                intent.putExtra("mTinasString", mTinasString);
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        lvXemThemTin = (ListView) findViewById(R.id.lvXemThemTin);
        titleXemThemTin = (TextView) findViewById(R.id.titleXemThemTin);
        btnXemThemTin = (ImageButton) findViewById(R.id.btnXemThemTin);
    }

    private void getTinMoiDang(String matinh) {
        apiInterface getTinMoiDang = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callBack = getTinMoiDang.getTinMoiDang(matinh);
        callBack.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                tinChungApdater = new TinChungApdater(response.body(), vXemThemTin.this, R.layout.line_tin_chung);
                lvXemThemTin.setAdapter(tinChungApdater);
                tinChungApdater.notifyDataSetChanged();
                arTinMoiDang = response.body();
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

    private void getTinOGhep(String matinh) {
        apiInterface getTinOGhep = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callBack = getTinOGhep.getTinOGhep(matinh);
        callBack.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                tinChungApdater = new TinChungApdater(response.body(), vXemThemTin.this, R.layout.line_tin_chung);
                lvXemThemTin.setAdapter(tinChungApdater);
                tinChungApdater.notifyDataSetChanged();
                arTinOGhep = response.body();
                Thread time = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
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