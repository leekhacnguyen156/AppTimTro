package com.example.timtro.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timtro.Activity.vDangNhap;
import com.example.timtro.Adapter.ThongBaoAdapter;
import com.example.timtro.Model.mThongBao;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frThongBao extends Fragment {

    private View vthongbao;
    private ListView listThongBaoMoi;
    public static ArrayList<mThongBao> arrmThongBao = new ArrayList<>();
    public static ArrayList<mTin> arrmTin = new ArrayList<>();;
    private ThongBaoAdapter thongBaoAdapter;
    private RelativeLayout relDangNhapTB;
    private AppCompatButton btnDangNhapThongBao;
    private SwipeRefreshLayout refeshThongBao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(vthongbao == null){
            vthongbao = inflater.inflate(R.layout.fr_thong_bao, container, false);
        }

        Paper.init(Objects.requireNonNull(getContext()));

        AnhXa();

        btnDangNhapThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vDangNhap.class));
            }
        });

        refeshThongBao.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SETADAPTERTB();
                refeshThongBao.setRefreshing(false);
            }
        });

        return vthongbao;
    }

    private void AnhXa() {
        refeshThongBao = vthongbao.findViewById(R.id.refeshThongBao);
        listThongBaoMoi = (ListView) vthongbao.findViewById(R.id.listThongBaoMoi);
        relDangNhapTB = (RelativeLayout) vthongbao.findViewById(R.id.relDangNhapTB);
        btnDangNhapThongBao = (AppCompatButton) vthongbao.findViewById(R.id.btnDangNhapThongBao);
        if(Paper.book().read(appAccountDetails.mataikhoan) == null){
            relDangNhapTB.setVisibility(View.VISIBLE);
        }else{
            relDangNhapTB.setVisibility(View.GONE);
            SETADAPTERTB();
        }
    }

    public static void getThongBaoMoi(String mataikhoan){
        apiInterface getThongBaoMoi = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mThongBao>> callBack = getThongBaoMoi.getThongBao(mataikhoan);
        callBack.enqueue(new Callback<List<mThongBao>>() {
            @Override
            public void onResponse( Call<List<mThongBao>> call, Response<List<mThongBao>> response) {
                if(arrmThongBao.size() > 0){
                    arrmThongBao.clear();
                }
                arrmThongBao = (ArrayList<mThongBao>) response.body();
                if(arrmTin.size()>0){
                    arrmTin.clear();
                }
                for (mThongBao mThongBao : arrmThongBao){
                    getTinTB(mThongBao.getMatin());
                }
            }

            @Override
            public void onFailure(Call<List<mThongBao>> call, Throwable t) {

            }
        });
    }

    public static void getTinTB(String matin){
        apiInterface getTinTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTin> callBack = getTinTheoMaTin.getTinTheoMaTin(matin);
        callBack.enqueue(new Callback<mTin>() {
            @Override
            public void onResponse(@NotNull Call<mTin> call, @NotNull Response<mTin> response) {
                arrmTin.add(response.body());
            }

            @Override
            public void onFailure(Call<mTin> call, Throwable t) {

            }
        });
    }

    private void SETADAPTERTB(){
        thongBaoAdapter = new ThongBaoAdapter(getContext(), arrmThongBao, arrmTin, R.layout.line_thong_bao);
        listThongBaoMoi.setAdapter(thongBaoAdapter);
        thongBaoAdapter.notifyDataSetChanged();
    }
}
