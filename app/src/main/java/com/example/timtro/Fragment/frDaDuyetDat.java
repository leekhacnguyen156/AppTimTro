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

import com.example.timtro.Activity.vChiTietTinDuyetDat;
import com.example.timtro.Adapter.DuyetDatPhongApdater;
import com.example.timtro.Model.mPhieuDatPhong;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frDaDuyetDat extends Fragment {
    private View vfrdaduyetdat;
    private ListView lvDuyetDaDuyetDat;
    private DuyetDatPhongApdater duyetDatPhongApdater;
    private ArrayList<mPhieuDatPhong> armPhieuDatPh = new ArrayList<>();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        vfrdaduyetdat = inflater.inflate(R.layout.fr_da_duyet_dat, container, false);

        AnhXa();

        Paper.init(Objects.requireNonNull(getContext()));

        getPhieuDatPhong(Paper.book().read(appAccountDetails.mataikhoan));

        lvDuyetDaDuyetDat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), vChiTietTinDuyetDat.class);
                Gson gson = new Gson();
                String mPhieuDatPhong = gson.toJson(armPhieuDatPh.get(position));
                intent.putExtra("mPhieuDatPhong", mPhieuDatPhong);
                getContext().startActivity(intent);
            }
        });

        return vfrdaduyetdat;
    }

    private void AnhXa() {
        lvDuyetDaDuyetDat = vfrdaduyetdat.findViewById(R.id.lvDuyetDaDuyetDat);
    }

    private void getPhieuDatPhong(String mataikhoan) {
        apiInterface getPhieuDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mPhieuDatPhong>> callBack = getPhieuDatPhong.getPhieuDatPhong(mataikhoan, 1);
        callBack.enqueue(new Callback<ArrayList<mPhieuDatPhong>>() {
            @Override
            public void onResponse(Call<ArrayList<mPhieuDatPhong>> call, Response<ArrayList<mPhieuDatPhong>> response) {
                duyetDatPhongApdater = new DuyetDatPhongApdater(response.body(), getContext(), R.layout.line_duyet_dat_phong);
                lvDuyetDaDuyetDat.setAdapter(duyetDatPhongApdater);
                duyetDatPhongApdater.notifyDataSetChanged();
                if(armPhieuDatPh.size()>0){
                    armPhieuDatPh.clear();
                }
                armPhieuDatPh = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mPhieuDatPhong>> call, Throwable t) {
                Toast.makeText(getContext(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
