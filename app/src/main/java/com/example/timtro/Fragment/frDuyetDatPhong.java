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
import com.example.timtro.Activity.vChiTietTinDuyetDat;
import com.example.timtro.Adapter.DuyetDatPhongApdater;
import com.example.timtro.Model.mPhieuDatPhong;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frDuyetDatPhong extends Fragment {

    private View vfrduyetdat;
    private ListView lvDuyetDatPhong;
    private DuyetDatPhongApdater duyetDatPhongApdater;
    private ArrayList<mPhieuDatPhong> armPhieuDatPh = new ArrayList<>();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        vfrduyetdat = inflater.inflate(R.layout.fr_duyet_dat_phong, container, false);

        AnhXa();

        Paper.init(Objects.requireNonNull(getContext()));

        getPhieuDatPhong(Paper.book().read(appAccountDetails.mataikhoan));

        lvDuyetDatPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lvDuyetDatPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.duyet_dat_bottom_sheet_dialog, null, false);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetView.findViewById(R.id.btnXemPhieuDat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), vChiTietTinDuyetDat.class);
                        Gson gson = new Gson();
                        String mPhieuDatPhong = gson.toJson(armPhieuDatPh.get(position));
                        intent.putExtra("mPhieuDatPhong", mPhieuDatPhong);
                        getContext().startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnDuyetDat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyetDatPhong(armPhieuDatPh.get(position).getMatin(), armPhieuDatPh.get(position).getMadatphong(), armPhieuDatPh.get(position).getMataikhoan(), 1);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnKhongDuyetDat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDuyetDatPhong(armPhieuDatPh.get(position).getMatin(), armPhieuDatPh.get(position).getMadatphong(), armPhieuDatPh.get(position).getMataikhoan(), 2);
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
            }
        });

        return vfrduyetdat;
    }

    private void getPhieuDatPhong(String mataikhoan) {
        apiInterface getPhieuDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mPhieuDatPhong>> callBack = getPhieuDatPhong.getPhieuDatPhong(mataikhoan, 0);
        callBack.enqueue(new Callback<ArrayList<mPhieuDatPhong>>() {
            @Override
            public void onResponse(Call<ArrayList<mPhieuDatPhong>> call, Response<ArrayList<mPhieuDatPhong>> response) {
                if (armPhieuDatPh.size() > 0) {
                    armPhieuDatPh.clear();
                }
                armPhieuDatPh = response.body();
                duyetDatPhongApdater = new DuyetDatPhongApdater(response.body(), getContext(), R.layout.line_duyet_dat_phong);
                lvDuyetDatPhong.setAdapter(duyetDatPhongApdater);
                duyetDatPhongApdater.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<mPhieuDatPhong>> call, Throwable t) {
                Toast.makeText(getContext(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa() {
        lvDuyetDatPhong = vfrduyetdat.findViewById(R.id.lvDuyetDatPhong);
    }

    public void RESETLV() {
        getPhieuDatPhong(Paper.book().read(appAccountDetails.mataikhoan));
    }

    private void updateDuyetDatPhong(String matin, String madatphong, String mataikhoan, int trangthaiduyet) {
        apiInterface updateTrangThaiDuyetDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTrangThaiDuyetDat.updateTrangThaiDuyetDat(madatphong, trangthaiduyet);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    getPhieuDatPhong(Paper.book().read(appAccountDetails.mataikhoan));
                    Toast.makeText(getContext(), "Đã duyệt !", Toast.LENGTH_SHORT).show();
                    insertThongBaoDuyetTin(matin, trangthaiduyet, mataikhoan);
                }else{
                    Toast.makeText(getContext(), "Đã xảy ra sự cố !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTrangThaiDat(String matin){
        apiInterface updateTrangThaiDat = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTrangThaiDat.updateTrangThaiDat(matin);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void insertThongBaoDuyetTin(String matin, int trangthaiduyet, String mataikhoan) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        apiInterface insertThongBao = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insertThongBao.insertThongBao(2,mataikhoan, matin, trangthaiduyet, strDate);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {
                if(response.body().equals("Success")){
                    if(trangthaiduyet == 1){
                        updateTrangThaiDat(matin);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        getPhieuDatPhong(Paper.book().read(appAccountDetails.mataikhoan));
        super.onResume();
    }
}
