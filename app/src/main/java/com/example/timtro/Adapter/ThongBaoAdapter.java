package com.example.timtro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timtro.Activity.vChiTietTin;
import com.example.timtro.Activity.vChiTietTinChinh;
import com.example.timtro.Activity.vPhongDaDang;
import com.example.timtro.MainActivity;
import com.example.timtro.Model.mThongBao;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongBaoAdapter extends BaseAdapter {

    Context context;
    ArrayList<mThongBao> armThongBao;
    ArrayList<mTin> armTin;
    int layout;
    private mTin mTinm = null;

    public ThongBaoAdapter(Context context, ArrayList<mThongBao> armThongBao, ArrayList<mTin> armTin, int layout) {
        this.context = context;
        this.armThongBao = armThongBao;
        this.armTin = armTin;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return armThongBao.size();
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
        RelativeLayout rellineThongBaoDuyetTin, reClickThongBao;
        ImageView imgThongBao;
        TextView txtNoiDungThongBao, txtThoiGianThongBao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.rellineThongBaoDuyetTin = (RelativeLayout) convertView.findViewById(R.id.rellineThongBaoDuyetTin);
            viewHolder.reClickThongBao = (RelativeLayout) convertView.findViewById(R.id.reClickThongBao);
            viewHolder.imgThongBao = (ImageView) convertView.findViewById(R.id.imgThongBao);
            viewHolder.txtNoiDungThongBao = (TextView) convertView.findViewById(R.id.txtNoiDungThongBao);
            viewHolder.txtThoiGianThongBao = (TextView) convertView.findViewById(R.id.txtThoiGianThongBao);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mThongBao mThongBao = armThongBao.get(position);

        for(mTin mTin : armTin){
            if(mTin.getMatin().equals(mThongBao.getMatin())){
                assert mTin.getImage() != null;
                Picasso.get().load(mTin.getImage()).into(viewHolder.imgThongBao);
                if (mThongBao.getMaloaithongbao().equals("1")) {
                    if (mThongBao.getTrangthai().equals("0")) {
                        viewHolder.txtNoiDungThongBao.setText(Html.fromHtml("Bài đăng " + "<font color=\"#4a90e2\">" + mTin.getTentieude() + "</font>" + " của bạn <font color=\"#4a90e2\">đã được duyệt lại</font>."));
                    } else if (mThongBao.getTrangthai().equals("1")) {
                        viewHolder.txtNoiDungThongBao.setText(Html.fromHtml("Bài đăng " + "<font color=\"#4a90e2\">" + mTin.getTentieude() + "</font>" + " của bạn <font color=\"#4a90e2\">đã được duyệt</font>."));
                    } else {
                        viewHolder.txtNoiDungThongBao.setText(Html.fromHtml("Bài đăng " + "<font color=\"#4a90e2\">" + mTin.getTentieude() + "</font>" + " của bạn <font color=\"red\">không được duyệt</font>."));
                    }
                } else {
                    if (mThongBao.getTrangthai().equals("0")) {

                    } else if (mThongBao.getTrangthai().equals("1")) {
                        viewHolder.txtNoiDungThongBao.setText(Html.fromHtml("Yêu cầu đặt phòng " + "<font color=\"#4a90e2\">" + mTin.getTentieude() + "</font>" + " của bạn <font color=\"#4a90e2\">đã được duyệt.</font>."));
                    } else {
                        viewHolder.txtNoiDungThongBao.setText(Html.fromHtml("Yêu cầu đặt phòng " + "<font color=\"#4a90e2\">" + mTin.getTentieude() + "</font>" + " của bạn <font color=\"red\">không được duyệt.</font>."));
                    }
                }
                mTinm = mTin;
                break;
            }
        }

        if (mThongBao.getTrangthaixem().equals("0")) {
            viewHolder.rellineThongBaoDuyetTin.setBackgroundColor(Color.parseColor("#E5EAED"));
        } else {
            viewHolder.rellineThongBaoDuyetTin.setBackgroundColor(Color.WHITE);
        }

        Date mDateTime = null;
        try {
            mDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mThongBao.getThoigian());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.txtThoiGianThongBao.setText(MainActivity.dateFormat.format(mDateTime));

        viewHolder.reClickThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThongBao.getMaloaithongbao().equals("1")) {
                    Intent intent = new Intent(context, vChiTietTin.class);
                    Gson gson = new Gson();
                    String mTinAsAString = gson.toJson(mTinm);
                    intent.putExtra("mTinAsAString", mTinAsAString);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, vChiTietTinChinh.class);
                    Gson gson = new Gson();
                    String mTinasString = gson.toJson(mTinm);
                    intent.putExtra("mTinasString", mTinasString);
                    context.startActivity(intent);
                }
                viewHolder.rellineThongBaoDuyetTin.setBackgroundColor(Color.WHITE);
                updateTrangThaiXemTB(mThongBao.getMathongbao());
            }
        });

        return convertView;
    }

    private void updateTrangThaiXemTB(String mathongbao) {
        apiInterface updateTrangThaiXem = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBack = updateTrangThaiXem.updateTrangThaiXem(mathongbao, 1);
        callBack.enqueue(new Callback<String>() {
            @Override
            public void onResponse( Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
