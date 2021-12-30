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

public class frDaDuyet extends Fragment {

    private View vfrdaduyet;
    private ArrayList<mTin> armTinDD = new ArrayList<>();
    private ListView lvTinDaDuyet;
    private TinChungApdater tinChungApdater;
    private String mataikhoan = "-1";
    private String matin = "-1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vfrdaduyet = inflater.inflate(R.layout.fr_da_duyet, container, false);

        AnhXa();
        getTinDaDuyet();

        lvTinDaDuyet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matin = armTinDD.get(position).getMatin();
                mataikhoan = armTinDD.get(position).getMataikhoan();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        lvTinDaDuyet.getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(lvTinDaDuyet.getContext()).inflate(R.layout.da_duyet_bottom_sheet_dialog, lvTinDaDuyet, false);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetView.findViewById(R.id.btnXemTinDaDuyet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), vChiTietTinDuyet.class);
                        Gson gson = new Gson();
                        String mTinasString = gson.toJson(armTinDD.get(position));
                        intent.putExtra("mTinasString", mTinasString);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnBoDuyet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateBoDuyet(armTinDD.get(position).getMatin());
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        return vfrdaduyet;
    }

    private void AnhXa() {
        lvTinDaDuyet = (ListView) vfrdaduyet.findViewById(R.id.lvTinDaDuyet);
    }

    private void getTinDaDuyet(){
        apiInterface getTinDaDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTin>> callBack = getTinDaDuyet.getTinDaDuyet();
        callBack.enqueue(new Callback<List<mTin>>() {
            @Override
            public void onResponse( Call<List<mTin>> call, Response<List<mTin>> response) {
                tinChungApdater = new TinChungApdater((ArrayList<mTin>) response.body(), getContext(), R.layout.line_tin_chung);
                lvTinDaDuyet.setAdapter(tinChungApdater);
                tinChungApdater.notifyDataSetChanged();
                if(armTinDD.size()>0){
                    armTinDD.clear();
                }
                armTinDD = (ArrayList<mTin>) response.body();
            }

            @Override
            public void onFailure(Call<List<mTin>> call, Throwable t) {

            }
        });
    }

    private void updateBoDuyet(String matin) {
        apiInterface updateBoDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateBoDuyet.updateTrangThaiDuyet(matin, 2);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    getTinDaDuyet();
                    Toast.makeText(getContext(), "Đã bỏ duyệt !", Toast.LENGTH_SHORT).show();
                    insertThongBaoDuyetTin(matin, 2);
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
        getTinDaDuyet();
        super.onResume();
    }
}
