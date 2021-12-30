package com.example.timtro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timtro.Activity.vChiTietTinChinh;
import com.example.timtro.Activity.vXemThemTin;
import com.example.timtro.MainActivity;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.timtro.Adapter.PhongDaDangApdater.formatmoney;

public class TinTrangChuAdapter extends RecyclerView.Adapter<TinTrangChuAdapter.MyViewHolder> {

    ArrayList<mTin> armTin;
    Context context;

    public TinTrangChuAdapter(ArrayList<mTin> armTin, Context context) {
        this.armTin = armTin;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTinTrangChu;
        TextView txtGiaTinTrangChu, txtDiaChiTinTrangChu, txtDienTichTinTrangChu, txtThoiGianTinTrangChu;
        RelativeLayout relTinTrangChu;

        MyViewHolder(View view) {
            super(view);
            this.imgTinTrangChu = view.findViewById(R.id.imgTinTrangChu);
            this.txtGiaTinTrangChu = view.findViewById(R.id.txtGiaTinTrangChu);
            this.txtDiaChiTinTrangChu = view.findViewById(R.id.txtDiaChiTinTrangChu);
            this.txtDienTichTinTrangChu = view.findViewById(R.id.txtDienTichTinTrangChu);
            this.txtThoiGianTinTrangChu = view.findViewById(R.id.txtThoiGianTinTrangChu);
            relTinTrangChu = view.findViewById(R.id.relTinTrangChu);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.line_tin_trang_chu, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        ImageView imgTinTrangChu = holder.imgTinTrangChu;
        TextView txtGiaTinTrangChu = holder.txtGiaTinTrangChu;
        TextView txtDiaChiTinTrangChu = holder.txtDiaChiTinTrangChu;
        TextView txtDienTichTinTrangChu = holder.txtDienTichTinTrangChu;
        TextView txtThoiGianTinTrangChu = holder.txtThoiGianTinTrangChu;
        RelativeLayout relTinTrangChu = holder.relTinTrangChu;

        Picasso.get().load(armTin.get(position).getImage()).into(imgTinTrangChu);
        txtGiaTinTrangChu.setText(PhongDaDangApdater.formatmoney(armTin.get(position).getGiaphong()));
        txtDiaChiTinTrangChu.setText(": "+armTin.get(position).getDiachi());
        txtDienTichTinTrangChu.setText(": "+armTin.get(position).getDientich() + "m2");

        Date mDateTime = null;
        try {
            mDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(armTin.get(position).getThoigiandang());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtThoiGianTinTrangChu.setText(": "+MainActivity.dateFormat.format(mDateTime));

        relTinTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, vChiTietTinChinh.class);
                Gson gson = new Gson();
                String mTinasString = gson.toJson(armTin.get(position));
                intent.putExtra("mTinasString", mTinasString);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return armTin.size();
    }

}
