package com.example.timtro.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.timtro.Adapter.LichSuTimKiemAdapter;
import com.example.timtro.Adapter.PhongDaDangApdater;
import com.example.timtro.Model.mLichSu;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;

import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frTimKiem extends Fragment {

    private View vtimkiem;
    private FragmentManager fragmentManager;
    public static SearchView searchView;
    private final ArrayList<Fragment> fragmentArray = new ArrayList<>();
    private String mataikhoan;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(vtimkiem == null){
            vtimkiem = inflater.inflate(R.layout.fr_tim_kiem, container, false);
        }
        Paper.init(getContext());
        mataikhoan = Paper.book().read(appAccountDetails.mataikhoan);
        frThongBao.getThongBaoMoi(Paper.book().read(appAccountDetails.mataikhoan));
        Anhxa();


        if (savedInstanceState == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            frLichSuTimKiem frlichSuTimKiem = new frLichSuTimKiem();
            fragmentManager.beginTransaction().replace(R.id.frlayoutTimKiem, frlichSuTimKiem).commit();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(Paper.book().read(appAccountDetails.mataikhoan)!=null){
                    InsertLichSuTimKiem(mataikhoan, searchView.getQuery().toString());
                }
                frKetQuaTimKiem frKetQuaTimKiem = new frKetQuaTimKiem();
                fragmentManager.beginTransaction().replace(R.id.frlayoutTimKiem, frKetQuaTimKiem).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    frLichSuTimKiem frLichSuTimKiem = new frLichSuTimKiem();
                    fragmentManager.beginTransaction().replace(R.id.frlayoutTimKiem, frLichSuTimKiem).commit();
                }
                return false;
            }
        });

        return vtimkiem;
    }

    private void Anhxa() {
        searchView = vtimkiem.findViewById(R.id.edtTimKiem);
        searchView.clearFocus();
        fragmentArray.add(new frLichSuTimKiem());
        fragmentArray.add(new frKetQuaTimKiem());
    }


    private void InsertLichSuTimKiem(String mataikhoan, String noidung) {
        apiInterface insert = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = insert.insertLichSuTimKiem(mataikhoan, noidung);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
