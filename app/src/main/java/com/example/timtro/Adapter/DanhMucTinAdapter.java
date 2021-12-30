package com.example.timtro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timtro.Activity.vLoad;
import com.example.timtro.Activity.vXemThemTin;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appDanhMuc;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhMucTinAdapter extends BaseAdapter {

    private ArrayList<appDanhMuc> arappDanhMuc;
    private Context context;
    private int layout;
    private TinTrangChuAdapter tinTrangChuAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static ArrayList<mTin> armTinMoiDang = new ArrayList<>();
    public static ArrayList<mTin> armTinOGhep = new ArrayList<>();
    public static ArrayList<mTin> armTinTongHop = new ArrayList<>();


    public DanhMucTinAdapter(ArrayList<appDanhMuc> arappDanhMuc, Context context, int layout) {
        this.arappDanhMuc = arappDanhMuc;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arappDanhMuc.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgIconDanhMuc;
        TextView txtTenDanhMuc, txtXemThem;
        RecyclerView recyTinTrangChu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgIconDanhMuc = (ImageView) convertView.findViewById(R.id.imgDanhMuc);
            viewHolder.txtTenDanhMuc = (TextView) convertView.findViewById(R.id.txtTenDanhMuc);
            viewHolder.txtXemThem = (TextView) convertView.findViewById(R.id.txtXemThem);
            viewHolder.recyTinTrangChu = (RecyclerView) convertView.findViewById(R.id.recyTinTrangChu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, vXemThemTin.class);
                intent.putExtra("madanhmuc", position);
                context.startActivity(intent);
            }
        });

        viewHolder.imgIconDanhMuc.setImageResource(arappDanhMuc.get(position).getAppIcon());
        viewHolder.txtTenDanhMuc.setText(arappDanhMuc.get(position).getAppTen());

        switch (position){
            case 0:
                tinTrangChuAdapter = new TinTrangChuAdapter(armTinMoiDang, context);
                viewHolder.txtXemThem.setVisibility(View.VISIBLE);
                break;
            case 1:
                tinTrangChuAdapter = new TinTrangChuAdapter(armTinOGhep, context);
                viewHolder.txtXemThem.setVisibility(View.VISIBLE);

                break;
            case 2:
                tinTrangChuAdapter = new TinTrangChuAdapter(armTinTongHop, context);
                viewHolder.txtXemThem.setVisibility(View.GONE);
                break;
        }

        layoutManager = new LinearLayoutManager(context);
        layoutManager = new GridLayoutManager(context, 2);
        viewHolder.recyTinTrangChu.setLayoutManager(layoutManager);
        viewHolder.recyTinTrangChu.setAdapter(tinTrangChuAdapter);
//        tinTrangChuAdapter.notifyDataSetChanged();

        return convertView;
    }

    public static void getTinMoiDangLM6(String matinh) {
        apiInterface getTinMoiDangLM6 = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callBack = getTinMoiDangLM6.getTinMoiDangLM6(matinh);
        callBack.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                if(armTinMoiDang.size() > 0){
                    armTinMoiDang.clear();
                }
                armTinMoiDang = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mTin>> call, Throwable t) {
            }
        });
    }

    public static void getTinOGhepLM6(String matinh) {
        apiInterface getTinOGhepLM6 = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callBack = getTinOGhepLM6.getTinOGhepLM6(matinh);
        callBack.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                if(armTinOGhep.size() > 0){
                    armTinOGhep.clear();
                }
                armTinOGhep = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mTin>> call, Throwable t) {
            }
        });
    }

    public static void getTinTongHop(String matinh) {
        apiInterface getTinTongHop = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mTin>> callBack = getTinTongHop.getTinTongHop(matinh);
        callBack.enqueue(new Callback<ArrayList<mTin>>() {
            @Override
            public void onResponse(Call<ArrayList<mTin>> call, Response<ArrayList<mTin>> response) {
                if(armTinTongHop.size() > 0){
                    armTinTongHop.clear();
                }
                armTinTongHop = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mTin>> call, Throwable t) {
            }
        });
    }

}
