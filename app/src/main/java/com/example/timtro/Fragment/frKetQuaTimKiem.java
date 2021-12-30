package com.example.timtro.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.timtro.Activity.vChiTietTinChinh;
import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Adapter.TinChungApdater;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frKetQuaTimKiem extends Fragment {
    private View vtimkiem;
    private ListView ketqua;
    private TinChungApdater tinChungApdater;
    private ProgressDialog loadConnect;
    private ArrayList<mTin> armTinSearch = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vtimkiem == null) {
            vtimkiem = inflater.inflate(R.layout.fr_ket_qua_tim_kiem, container, false);
        }
        Anhxa();

        loadConnect = new ProgressDialog(getContext());
        loadConnect.setTitle("Đang tìm kiếm");
        loadConnect.setMessage("Vui lòng chờ giây lát..");
        loadConnect.show();
        getTinTimKiem(frTimKiem.searchView.getQuery().toString());

        ketqua.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), vChiTietTinChinh.class);
                Gson gson = new Gson();
                String mTinasString = gson.toJson(armTinSearch.get(position));
                intent.putExtra("mTinasString", mTinasString);
                startActivity(intent);
            }
        });

        return vtimkiem;
    }

    private void Anhxa() {
        ketqua = vtimkiem.findViewById(R.id.lvKetQuaTimKiem);
    }

    private void getTinTimKiem(String tukhoa) {
        apiInterface getTinTimKiem = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callback = getTinTimKiem.getTinTimKiem(tukhoa);
        callback.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                if (response.body().size() > 0) {
                    tinChungApdater = new TinChungApdater(response.body(), getContext(), R.layout.line_tin_chung);
                    ketqua.setAdapter(tinChungApdater);
                    tinChungApdater.notifyDataSetChanged();
                    if(armTinSearch.size() > 0){
                        armTinSearch.clear();
                    }
                    armTinSearch = response.body();
                }
                Thread time = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(250);
                                    loadConnect.dismiss();
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
