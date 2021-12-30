package com.example.timtro.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timtro.Activity.vChiTietTinChinh;
import com.example.timtro.Activity.vChiTietTinDuyet;
import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Adapter.TinChungApdater;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frChuaDuyet extends Fragment {

    private View vfrchuaduyet;
    public static ArrayList<mTin> armTinCD = new ArrayList<>();
    public static ListView lvTinChuaDuyet;
    private TinChungApdater tinChungApdater;
    private String matin = "-1";
    private String mataikhoan = "-1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vfrchuaduyet = inflater.inflate(R.layout.fr_chua_duyet, container, false);

        AnhXa();
        getTinChuaDuyet();
        lvTinChuaDuyet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matin = armTinCD.get(position).getMatin();
                mataikhoan = armTinCD.get(position).getMataikhoan();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        lvTinChuaDuyet.getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(lvTinChuaDuyet.getContext()).inflate(R.layout.duyet_bottom_sheet_dialog, lvTinChuaDuyet, false);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetView.findViewById(R.id.btnXemTin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), vChiTietTinDuyet.class);
                        Gson gson = new Gson();
                        String mTinasString = gson.toJson(armTinCD.get(position));
                        intent.putExtra("mTinasString", mTinasString);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnDuyetTin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyet(matin, 1);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnKhongDuyetTin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyet(armTinCD.get(position).getMatin(), 2);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }
        });
        return vfrchuaduyet;
    }

    private void updateDuyet(String matin, int trangthaiduyet) {
        apiInterface updateDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateDuyet.updateTrangThaiDuyet(matin, trangthaiduyet);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    getTinChuaDuyet();
                    Toast.makeText(getContext(), "Đã duyệt !", Toast.LENGTH_SHORT).show();
                    insertThongBaoDuyetTin(matin, trangthaiduyet);
                }else{
                    Toast.makeText(getContext(), "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void insertThongBaoDuyetTin(String matin, int trangthaiduyet) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        apiInterface insertThongBao = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insertThongBao.insertThongBao(1,mataikhoan, matin, trangthaiduyet, strDate);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void AnhXa() {
        lvTinChuaDuyet = (ListView) vfrchuaduyet.findViewById(R.id.lvTinChuaDuyet);
    }

    private void getTinChuaDuyet(){
        apiInterface getTinChuaDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTin>> callBack = getTinChuaDuyet.getTinChuaDuyet();
        callBack.enqueue(new Callback<List<mTin>>() {
            @Override
            public void onResponse( Call<List<mTin>> call, Response<List<mTin>> response) {
                tinChungApdater = new TinChungApdater((ArrayList<mTin>) response.body(), getContext(), R.layout.line_tin_chung);
                lvTinChuaDuyet.setAdapter(tinChungApdater);
                tinChungApdater.notifyDataSetChanged();
                if(armTinCD.size()>0){
                    armTinCD.clear();
                }
                armTinCD = (ArrayList<mTin>) response.body();
            }

            @Override
            public void onFailure(Call<List<mTin>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        getTinChuaDuyet();
        super.onResume();
    }
}
