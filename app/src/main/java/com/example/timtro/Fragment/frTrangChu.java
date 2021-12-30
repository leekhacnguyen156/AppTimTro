package com.example.timtro.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.timtro.Activity.vChiTietTinChinh;
import com.example.timtro.Activity.vDangNhap;
import com.example.timtro.Activity.vDangTin;
import com.example.timtro.Activity.vLoad;
import com.example.timtro.Adapter.DanhMucTinAdapter;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.ModelApp.appDanhMuc;
import com.example.timtro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import io.paperdb.Paper;

public class frTrangChu extends Fragment {

    private View vtrangchu;
    private ArrayList<appDanhMuc> arappDanhMuc = new ArrayList<>();
    private ListView lvDanhMucTin;
    private DanhMucTinAdapter danhMucTinAdapter;
    private FloatingActionButton btnDangTinTrangChu;
    private SwipeRefreshLayout refeshTrangChu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(vtrangchu == null){
            vtrangchu = inflater.inflate(R.layout.fr_trang_chu, container, false);
        }

        Paper.init(Objects.requireNonNull(getContext()));

        frThongBao.getThongBaoMoi(Paper.book().read(appAccountDetails.mataikhoan));

        AnhXa();

        SETAPDAPTERDANHMUC();

        btnDangTinTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Paper.book().read(appAccountDetails.mataikhoan) == null){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Đăng nhập");
                    alertDialog.setMessage("Vui lòng đăng nhập để đăng tin !");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Có",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getContext(), vDangNhap.class));
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Không",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else{
                    startActivity(new Intent(getContext(), vDangTin.class));
                }
            }
        });

        refeshTrangChu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(getContext(), vLoad.class));
                refeshTrangChu.setRefreshing(false);
            }
        });

        return vtrangchu;
    }

    private void SETAPDAPTERDANHMUC() {
        if (arappDanhMuc.size() > 0){
            arappDanhMuc.clear();
        }
        arappDanhMuc.add(new appDanhMuc("Phòng mới đăng", R.drawable.ic_new));
        arappDanhMuc.add(new appDanhMuc("Phòng ở ghép", R.drawable.ic_people));
        arappDanhMuc.add(new appDanhMuc("Phòng tổng hợp", R.drawable.ic_home));
        danhMucTinAdapter = new DanhMucTinAdapter(arappDanhMuc, getContext(), R.layout.line_danh_muc);
        lvDanhMucTin.setAdapter(danhMucTinAdapter);
        danhMucTinAdapter.notifyDataSetChanged();
    }

    private void AnhXa() {
        refeshTrangChu = vtrangchu.findViewById(R.id.refeshTrangChu);
        lvDanhMucTin = (ListView) vtrangchu.findViewById(R.id.lvDanhMucTin);
        btnDangTinTrangChu = (FloatingActionButton) vtrangchu.findViewById(R.id.btnDangTinTrangChu);
    }
}
