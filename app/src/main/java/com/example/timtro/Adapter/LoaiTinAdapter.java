package com.example.timtro.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.timtro.Activity.vSuaTin;
import com.example.timtro.Fragment.frThongTin;
import com.example.timtro.Model.mLoaiTin;
import com.example.timtro.ModelApp.appDanhMuc;
import com.example.timtro.R;

import java.util.ArrayList;

public class LoaiTinAdapter extends BaseAdapter {

    private ArrayList<mLoaiTin> armLoaiTin;
    private Context context;
    private int layout;
    private int row_index = 0;
    private boolean suatin = true;

    public LoaiTinAdapter(ArrayList<mLoaiTin> armLoaiTin, Context context, int layout) {
        this.armLoaiTin = armLoaiTin;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return armLoaiTin.size();
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
        AppCompatButton btnLoaiTin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.btnLoaiTin = (AppCompatButton) convertView.findViewById(R.id.btnLoaiTin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnLoaiTin.setText(armLoaiTin.get(position).getTenloaitin());


        viewHolder.btnLoaiTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                frThongTin.iLoaiTin = row_index;
            }
        });

        if (vSuaTin.mTin.getMaloaitin() != null && suatin) {
            if (vSuaTin.mTin.getMaloaitin().equals("1")){
                row_index = 0;
                frThongTin.iLoaiTin = row_index;
                suatin = false;
            }
            else if (vSuaTin.mTin.getMaloaitin().equals("2")) {
                row_index = 1;
                frThongTin.iLoaiTin = row_index;
                suatin = false;
            }
        }
        if(row_index == position){
            viewHolder.btnLoaiTin.setTextColor(Color.WHITE);
            viewHolder.btnLoaiTin.setBackgroundColor(Color.parseColor("#4a90e2"));
        }
        else{
            viewHolder.btnLoaiTin.setTextColor(Color.parseColor("#4a90e2"));
            viewHolder.btnLoaiTin.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
