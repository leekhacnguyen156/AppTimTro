package com.example.timtro.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.timtro.Adapter.LichSuTimKiemAdapter;
import com.example.timtro.Model.mLichSu;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;

import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frLichSuTimKiem extends Fragment {
    private View vtimkiem;
    private TextView txtXoaLichSuTimKiem;
    private ListView lichsu;
    private LichSuTimKiemAdapter lichSuTimKiemAdapter;
    private final ArrayList<String> arNoiDung = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vtimkiem == null) {
            vtimkiem = inflater.inflate(R.layout.fr_lich_su_tim_kiem, container, false);
        }
        Paper.init(getContext());
        Anhxa();
        getLichSuTimKiem(Paper.book().read(appAccountDetails.mataikhoan));
        txtXoaLichSuTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Paper.book().read(appAccountDetails.mataikhoan)!=null){
                    Toast.makeText(getContext(), "Xóa lịch sử thành công!", Toast.LENGTH_SHORT).show();
                    deleteLichSuTimKiem(Paper.book().read(appAccountDetails.mataikhoan));
                }else{
                    Toast.makeText(getContext(), "Lịch sử rỗng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lichsu.setOnItemClickListener((parent, view, position, id) -> frTimKiem.searchView.setQuery(arNoiDung.get(position), true));
        return vtimkiem;
    }

    private void Anhxa() {
        lichsu = vtimkiem.findViewById(R.id.lvLichSuTimKiem);
        txtXoaLichSuTimKiem = vtimkiem.findViewById(R.id.txtXoaLichSuTimKiem);
    }

    private void getLichSuTimKiem(String mataikhoan) {
        apiInterface getLichSuTimKiem = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mLichSu>> callBack = getLichSuTimKiem.getLichSuTimKiem(mataikhoan);
        callBack.enqueue(new Callback<ArrayList<mLichSu>>() {
            @Override
            public void onResponse(Call<ArrayList<mLichSu>> call, Response<ArrayList<mLichSu>> response) {
                for (mLichSu mls : response.body()) {
                    arNoiDung.add(mls.getNoidung());
                }
                lichSuTimKiemAdapter = new LichSuTimKiemAdapter(arNoiDung, getContext(), R.layout.line_lich_su);
                lichsu.setAdapter(lichSuTimKiemAdapter);
                lichSuTimKiemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<mLichSu>> call, Throwable t) {
            }
        });
    }

    private void deleteLichSuTimKiem(String mataikhoan) {
        apiInterface deleteLichSuTimKiem = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = deleteLichSuTimKiem.deleteLichSuTimKiem(mataikhoan);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                getLichSuTimKiem(Paper.book().read(appAccountDetails.mataikhoan));
                FragmentManager fragmentManager = frLichSuTimKiem.this.getFragmentManager();
                frLichSuTimKiem frlichSuTimKiem = new frLichSuTimKiem();
                fragmentManager.beginTransaction().replace(R.id.frlayoutTimKiem, frlichSuTimKiem).commit();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
