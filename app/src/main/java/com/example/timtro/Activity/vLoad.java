package com.example.timtro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.timtro.Adapter.DanhMucTinAdapter;
import com.example.timtro.MainActivity;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.vNhaChinh;

import io.paperdb.Paper;

public class vLoad extends AppCompatActivity {

    public static ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_load);

        load = new ProgressDialog(this);
        load.setTitle("Đang tải");
        load.setMessage("Vui lòng chờ giây lát..");
        load.setCanceledOnTouchOutside(false);
        load.show();

        Paper.init(this);

        DanhMucTinAdapter.getTinOGhepLM6(Paper.book().read(appAccountDetails.matinh));
        DanhMucTinAdapter.getTinMoiDangLM6(Paper.book().read(appAccountDetails.matinh));
        DanhMucTinAdapter.getTinTongHop(Paper.book().read(appAccountDetails.matinh));

        Thread time = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
                            vLoad.load.dismiss();
                            Intent intent = new Intent(vLoad.this, vNhaChinh.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        time.start();
    }
}