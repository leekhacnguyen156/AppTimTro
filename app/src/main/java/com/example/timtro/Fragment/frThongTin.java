package com.example.timtro.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timtro.Activity.vSuaTin;
import com.example.timtro.Adapter.DanhMucTinAdapter;
import com.example.timtro.Adapter.LoaiPhongAdapter;
import com.example.timtro.Adapter.LoaiTinAdapter;
import com.example.timtro.Adapter.TienIchPhongAdapter;
import com.example.timtro.Model.mLoaiPhong;
import com.example.timtro.Model.mLoaiTin;
import com.example.timtro.Model.mPhieuTienIch;
import com.example.timtro.Model.mTienIchPhong;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.xml.ExpandableHeightGridView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frThongTin extends Fragment {

    private View vfrthongtin;
    private ExpandableHeightGridView gridLoaiTin, gridLoaiPhong, gridTienIchPhong;
    public static EditText edtGiaPhong, edtDienTich;
    private LoaiTinAdapter loaiTinAdapter;
    private LoaiPhongAdapter loaiPhongAdapter;
    private TienIchPhongAdapter tienIchPhongAdapter;
    public static ArrayList<mLoaiTin> armLoaiTin = new ArrayList<>();
    public static ArrayList<mLoaiPhong> armLoaiPhong = new ArrayList<>();
    public static ArrayList<mTienIchPhong> armTienIchPhong = new ArrayList<>();
    public static ArrayList<mPhieuTienIch> armPhieuTienIch = new ArrayList<>();
    public static int iLoaiPhong = 0;
    public static int iLoaiTin = 0;
    public static String[] armmatienich = {"", "", "", "", "", "", "", ""};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(vfrthongtin == null){
            vfrthongtin = inflater.inflate(R.layout.fr_thong_tin, container, false);
        }

        AnhXa();

        if (vSuaTin.mTin.getMatin() != null){
            String giaphong = NumberFormat.getInstance(new Locale("it", "IT")).format(Double.parseDouble(vSuaTin.mTin.getGiaphong()));
            edtGiaPhong.setText(giaphong+"");
            edtDienTich.setText(vSuaTin.mTin.getDientich());
        }

        if(loaiTinAdapter == null){
            loaiTinAdapter = new LoaiTinAdapter(armLoaiTin, getContext(), R.layout.line_loai_tin);
            gridLoaiTin.setAdapter(loaiTinAdapter);
            loaiTinAdapter.notifyDataSetChanged();
        }
        if(loaiPhongAdapter == null){
            loaiPhongAdapter = new LoaiPhongAdapter(armLoaiPhong, getContext(), R.layout.line_loai_phong);
            gridLoaiPhong.setAdapter(loaiPhongAdapter);
            loaiPhongAdapter.notifyDataSetChanged();
        }

        if ((tienIchPhongAdapter == null)){
            tienIchPhongAdapter = new TienIchPhongAdapter(armTienIchPhong, armPhieuTienIch, getContext(), R.layout.line_tien_ich_phong);
            gridTienIchPhong.setAdapter(tienIchPhongAdapter);
            tienIchPhongAdapter.notifyDataSetChanged();
        }

        edtGiaPhong.addTextChangedListener(new TextWatcher() {
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (!s.toString().equals(current)) {
                        String cleanString = s.toString().replaceAll("[,.]", "");
                        double parsed = Double.parseDouble(cleanString);
                        String formated = NumberFormat.getInstance(new Locale("it", "IT")).format((parsed));
                        current = formated;
                        edtGiaPhong.setText(formated);
                        edtGiaPhong.setSelection(formated.length());
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return vfrthongtin;
    }

    public static void getLoaiTin() {
        apiInterface getLoaiTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mLoaiTin>> callBack = getLoaiTin.getLoaiTin();
        callBack.enqueue(new Callback<List<mLoaiTin>>() {
            @Override
            public void onResponse( Call<List<mLoaiTin>> call, Response<List<mLoaiTin>> response) {
                if(armLoaiTin.size() > 0){
                    armLoaiTin.clear();
                }
                armLoaiTin = (ArrayList<mLoaiTin>)response.body();
            }

            @Override
            public void onFailure(Call<List<mLoaiTin>> call, Throwable t) {

            }
        });
    }

    public static void getLoaiPhong() {
        apiInterface getLoaiPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mLoaiPhong>> callBack = getLoaiPhong.getLoaiPhong();
        callBack.enqueue(new Callback<List<mLoaiPhong>>() {
            @Override
            public void onResponse( Call<List<mLoaiPhong>> call, Response<List<mLoaiPhong>> response) {
                if(armLoaiPhong.size() > 0){
                    armLoaiPhong.clear();
                }
                armLoaiPhong = (ArrayList<mLoaiPhong>)response.body();
            }

            @Override
            public void onFailure(Call<List<mLoaiPhong>> call, Throwable t) {

            }
        });
    }
    public static void getTienIchPhong() {
        apiInterface getTienIchPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTienIchPhong>> callBack = getTienIchPhong.getTienIchPhong();
        callBack.enqueue(new Callback<List<mTienIchPhong>>() {
            @Override
            public void onResponse( Call<List<mTienIchPhong>> call, Response<List<mTienIchPhong>> response) {
                if(armTienIchPhong.size() > 0){
                    armTienIchPhong.clear();
                }
                armTienIchPhong = (ArrayList<mTienIchPhong>)response.body();
            }

            @Override
            public void onFailure(Call<List<mTienIchPhong>> call, Throwable t) {

            }
        });
    }

    public static void getPhieuTienIchTheoMaTin(String matin){
        apiInterface getPhieuTienIchTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mPhieuTienIch>> callBack = getPhieuTienIchTheoMaTin.getTienIchTheoMaTin(matin);
        callBack.enqueue(new Callback<ArrayList<mPhieuTienIch>>() {
            @Override
            public void onResponse(Call<ArrayList<mPhieuTienIch>> call, Response<ArrayList<mPhieuTienIch>> response) {
                if(armPhieuTienIch.size() > 0){
                    armPhieuTienIch.clear();
                }
                armPhieuTienIch = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mPhieuTienIch>> call, Throwable t) {

            }
        });
    }


    private void AnhXa() {
        gridLoaiTin = (ExpandableHeightGridView) vfrthongtin.findViewById(R.id.gridLoaiTin);
        gridLoaiTin.setExpanded(true);
        gridLoaiPhong = (ExpandableHeightGridView) vfrthongtin.findViewById(R.id.gridLoaiPhong);
        gridLoaiPhong.setExpanded(true);
        gridTienIchPhong = (ExpandableHeightGridView) vfrthongtin.findViewById(R.id.gridTienIchPhong);
        gridTienIchPhong.setExpanded(true);
        edtGiaPhong = (EditText) vfrthongtin.findViewById(R.id.edtGiaPhong);
        edtDienTich = (EditText) vfrthongtin.findViewById(R.id.edtDienTich);
    }
}
