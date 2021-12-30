package com.example.timtro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.timtro.Fragment.frTaiKhoan;
import com.example.timtro.Fragment.frTaiKhoanChuaDangNhap;
import com.example.timtro.Fragment.frThongBao;
import com.example.timtro.Fragment.frTimKiem;
import com.example.timtro.Fragment.frTrangChu;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vNhaChinh extends AppCompatActivity {

    private ChipNavigationBar chipnavTab;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragmentArray = new ArrayList<>();
    private int Request_code_image = 123;
    public static ArrayList<mTinh> armTinh = new ArrayList<>();
    public static ArrayList<String> arTenTinh = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_nha_chinh);

        Paper.init(this);

        AnhXa();
        GetThongtin();
        if (savedInstanceState == null) {
            chipnavTab.setItemSelected(R.id.tabTrangchu, true);
            fragmentManager = getSupportFragmentManager();
            frTrangChu frtrangChu = new frTrangChu();
            fragmentManager.beginTransaction().replace(R.id.frlayoutNhaChinh, frtrangChu).commit();
        }


        chipnavTab.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment frTamp = null;
                switch (id) {
                    case R.id.tabTrangchu:
                        frTamp = fragmentArray.get(0);
                        break;
                    case R.id.tabTimkiem:
                        frTamp = fragmentArray.get(1);
                        break;
                    case R.id.tabThongbao:
                        frTamp = fragmentArray.get(2);
                        chipnavTab.dismissBadge(R.id.tabThongbao);
                        break;
                    case R.id.tabTaikhoan:
                        Paper.init(vNhaChinh.this);
                        String Id = Paper.book().read(appAccountDetails.mataikhoan);
                        if (Id != "") {
                            if (!TextUtils.isEmpty(Id)) {
                                frTamp = fragmentArray.get(3);
                            } else {
                                frTamp = fragmentArray.get(4);
                            }
                        }
                        break;
                }
                if (frTamp != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frlayoutNhaChinh, frTamp).commit();
                }
            }
        });
    }



    private void AnhXa() {
        chipnavTab = (ChipNavigationBar) findViewById(R.id.chipnavTab);
        fragmentArray.add(new frTrangChu());
        fragmentArray.add(new frTimKiem());
        fragmentArray.add(new frThongBao());
        fragmentArray.add(new frTaiKhoan());
        fragmentArray.add(new frTaiKhoanChuaDangNhap());
    }

    private void GetThongtin() {
        getTinh();
        setCountthongbao(Paper.book().read(appAccountDetails.mataikhoan));
        frTaiKhoan.getTaikhoan(Paper.book().read(appAccountDetails.mataikhoan));
    }

    public static void getTinh() {
        apiInterface getTinh = APIClient.getAPIClient().create(apiInterface.class);
        Call<List<mTinh>> callBack = getTinh.getTinh();
        callBack.enqueue(new Callback<List<mTinh>>() {
            @Override
            public void onResponse( Call<List<mTinh>> call, Response<List<mTinh>> response) {
                if(armTinh.size() > 0){
                    armTinh.clear();
                }
                if(arTenTinh.size() > 0){
                    arTenTinh.clear();
                }
                armTinh = (ArrayList<mTinh>)response.body();
                for (mTinh tinh : armTinh){
                    arTenTinh.add(tinh.getTentinh());
                }
            }

            @Override
            public void onFailure(Call<List<mTinh>> call, Throwable t) {
            }
        });
    }

    private void setCountthongbao(String mataikhoan){
        apiInterface setCountthongbao = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = setCountthongbao.countThongBao(mataikhoan);
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse( Call<Integer> call, Response<Integer> response) {
                if(response.body() > 0 && response.body() < 6){
                    chipnavTab.showBadge(R.id.tabThongbao, response.body());
                }else{
                    chipnavTab.showBadge(R.id.tabThongbao);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Xác nhận");
        alertDialog.setMessage("Nhấn OK để thoát khỏi ứng dụng!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        finishAffinity();
                        System.exit(0);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}