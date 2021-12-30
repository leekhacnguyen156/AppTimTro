
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

public class frKhongDuyet extends Fragment {

    private View vfrkhongduyet;
    private ListView lvTinKhongDuyet;
    private ArrayList<mTin> armTinKD = new ArrayList<>();
    private TinChungApdater tinChungApdater;
    private String mataikhoan = "-1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vfrkhongduyet = inflater.inflate(R.layout.fr_khong_duyet, container, false);

        AnhXa();
        getTinKhongDuyet();

        lvTinKhongDuyet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mataikhoan = armTinKD.get(position).getMataikhoan();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        lvTinKhongDuyet.getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(lvTinKhongDuyet.getContext()).inflate(R.layout.duyet_lai_bottom_sheet_dialog, lvTinKhongDuyet, false);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetView.findViewById(R.id.btnXemTinKhongDuyet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), vChiTietTinDuyet.class);
                        Gson gson = new Gson();
                        String mTinasString = gson.toJson(armTinKD.get(position));
                        intent.putExtra("mTinasString", mTinasString);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnDuyetLai).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyetLai(armTinKD.get(position).getMatin());
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        return vfrkhongduyet;
    }

    private void AnhXa() {
        lvTinKhongDuyet = (ListView) vfrkhongduyet.findViewById(R.id.lvTinKhongDuyet);
    }

    private void getTinKhongDuyet(){
        apiInterface getTinKhongDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTin>> callBack = getTinKhongDuyet.getTinKhongDuyet();
        callBack.enqueue(new Callback<List<mTin>>() {
            @Override
            public void onResponse( Call<List<mTin>> call, Response<List<mTin>> response) {
                tinChungApdater = new TinChungApdater((ArrayList<mTin>) response.body(), getContext(), R.layout.line_tin_chung);
                lvTinKhongDuyet.setAdapter(tinChungApdater);
                tinChungApdater.notifyDataSetChanged();
                if(armTinKD.size()>0){
                    armTinKD.clear();
                }
                armTinKD = (ArrayList<mTin>) response.body();
            }

            @Override
            public void onFailure(Call<List<mTin>> call, Throwable t) {

            }
        });
    }

    private void updateDuyetLai(String matin) {
        apiInterface updateDuyetLai = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateDuyetLai.updateTrangThaiDuyet(matin, 0);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    getTinKhongDuyet();
                    Toast.makeText(getContext(), "Duyệt lại !", Toast.LENGTH_SHORT).show();
                    insertThongBaoDuyetTin(matin, 0);
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
                if(response.body().equals("Success")){

                }else{

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        getTinKhongDuyet();
        super.onResume();
    }
}
