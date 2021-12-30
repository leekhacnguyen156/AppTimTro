package com.example.timtro.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timtro.Activity.vDangKy;
import com.example.timtro.Activity.vDangTin;
import com.example.timtro.Activity.vSuaTin;
import com.example.timtro.MainActivity;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mTinh;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frViTri extends Fragment {

    private View vfrvitri;
    public static EditText edtChonTinhTin, edtChonHuyenTin, edtDiaChiTin;
    public static ArrayList<mHuyen> armHuyen = new ArrayList<>();
    public static ArrayList<String> arTenTinh = new ArrayList<>();
    public static ArrayList<String> arTenHuyen = new ArrayList<>();
    public static String matinh = "", mahuyen = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vfrvitri == null) {
            vfrvitri = inflater.inflate(R.layout.fr_vi_tri, container, false);
        }
        AnhXa();

        if (vSuaTin.mTin.getMatin() != null){
            matinh = vSuaTin.mTin.getMatinh();
            mahuyen = vSuaTin.mTin.getMahuyen();
            for (mTinh mtinh : vNhaChinh.armTinh) {
                if (mtinh.getMatinh().equals(matinh)) {
                    edtChonTinhTin.setText(mtinh.getTentinh());
                    break;
                }
            }
            if (armHuyen.size() > 0 && mahuyen != "") {
                for (mHuyen mhuyen : armHuyen) {
                    if (mahuyen.equals(mhuyen.getMahuyen())) {
                        edtChonHuyenTin.setText(mhuyen.getTenhuyen());
                        break;
                    }
                }
            }
            edtDiaChiTin.setText(vSuaTin.mTin.getDiachi());
            frThongTin.getPhieuTienIchTheoMaTin(vSuaTin.mTin.getMatin());
        }

        edtChonTinhTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDialogTinh();
            }
        });

        edtChonHuyenTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtChonTinhTin.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng chọn tỉnh !", Toast.LENGTH_SHORT).show();
                } else {
                    SetDialogHuyen();
                }
            }
        });

        return vfrvitri;
    }

    private void AnhXa() {
        edtChonTinhTin = (EditText) vfrvitri.findViewById(R.id.edtChonTinhTin);
        edtChonHuyenTin = (EditText) vfrvitri.findViewById(R.id.edtChonHuyenTin);
        edtDiaChiTin = (EditText) vfrvitri.findViewById(R.id.edtDiaChiTin);
    }

    private void SetDialogHuyen() {
        Dialog dialogHuyen = new Dialog(getContext());
        dialogHuyen.setContentView(R.layout.dialog_huyen);
        ListView listHuyen = (ListView) dialogHuyen.findViewById(R.id.listHuyen);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, arTenHuyen);
        listHuyen.setAdapter(adapter);
        listHuyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mahuyen = armHuyen.get(position).getMahuyen();
                edtChonHuyenTin.setText(armHuyen.get(position).getTenhuyen());
                dialogHuyen.dismiss();
            }
        });
        dialogHuyen.show();
    }

    private void SetDialogTinh() {
        Dialog dialogTinh = new Dialog(getContext());
        dialogTinh.setContentView(R.layout.dialog_tinh);
        ListView listTinh = (ListView) dialogTinh.findViewById(R.id.listTinh);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, arTenTinh);
        listTinh.setAdapter(adapter);
        listTinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matinh = vNhaChinh.armTinh.get(position).getMatinh();
                edtChonTinhTin.setText(vNhaChinh.armTinh.get(position).getTentinh());
                edtChonHuyenTin.setText("");
                mahuyen = "";
                getHuyen(matinh);
                dialogTinh.dismiss();
            }
        });
        dialogTinh.show();
    }

    public static void getTinh(){
        if(arTenTinh.size() > 0){
            arTenTinh.clear();
        }
        for (mTinh tinh : vNhaChinh.armTinh) {
            arTenTinh.add(tinh.getTentinh());
        }
    }

    public static void getHuyen(String matinh) {
        apiInterface getHuyen = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHuyen>> callBack = getHuyen.getHuyen(matinh);
        callBack.enqueue(new Callback<ArrayList<mHuyen>>() {
            @Override
            public void onResponse(Call<ArrayList<mHuyen>> call, Response<ArrayList<mHuyen>> response) {
                if (armHuyen.size() > 0) {
                    armHuyen.clear();
                }
                if (arTenHuyen.size() > 0) {
                    arTenHuyen.clear();
                }
                armHuyen = response.body();
                for (mHuyen huyen : armHuyen) {
                    arTenHuyen.add(huyen.getTenhuyen());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<mHuyen>> call, Throwable t) {

            }
        });
    }
}
