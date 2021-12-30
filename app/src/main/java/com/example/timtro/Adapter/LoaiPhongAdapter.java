package com.example.timtro.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.timtro.Activity.vSuaTin;
import com.example.timtro.Fragment.frThongTin;
import com.example.timtro.Model.mLoaiPhong;
import com.example.timtro.Model.mLoaiTin;
import com.example.timtro.R;

import java.util.ArrayList;

public class LoaiPhongAdapter extends BaseAdapter {

    private ArrayList<mLoaiPhong> armLoaiPhong;
    private Context context;
    private int layout;
    private int row_index = 0;
    private boolean suatin = true;

    public LoaiPhongAdapter(ArrayList<mLoaiPhong> armLoaiPhong, Context context, int layout) {
        this.armLoaiPhong = armLoaiPhong;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return armLoaiPhong.size();
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
        AppCompatButton btnLoaiPhong;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.btnLoaiPhong = (AppCompatButton) convertView.findViewById(R.id.btnLoaiPhong);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnLoaiPhong.setText(armLoaiPhong.get(position).getTenloaiphong());

        viewHolder.btnLoaiPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                frThongTin.iLoaiPhong = row_index;
            }
        });

        if (vSuaTin.mTin.getMaloaitin() != null && suatin) {
            if (vSuaTin.mTin.getMaloaiphong().equals("1")){
                row_index = 0;
                frThongTin.iLoaiPhong = row_index;
                suatin = false;
            }
            else if (vSuaTin.mTin.getMaloaiphong().equals("2")) {
                row_index = 1;
                frThongTin.iLoaiPhong = row_index;
                suatin = false;
            }else if(vSuaTin.mTin.getMaloaiphong().equals("3")){
                row_index = 2;
                frThongTin.iLoaiPhong = row_index;
                suatin = false;
            }else if(vSuaTin.mTin.getMaloaiphong().equals("4")){
                row_index = 3;
                frThongTin.iLoaiPhong = row_index;
                suatin = false;
            }
        }

        if(row_index == position){
            viewHolder.btnLoaiPhong.setTextColor(Color.WHITE);
            viewHolder.btnLoaiPhong.setBackgroundColor(Color.parseColor("#4a90e2"));
        }
        else{
            viewHolder.btnLoaiPhong.setTextColor(Color.parseColor("#4a90e2"));
            viewHolder.btnLoaiPhong.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
