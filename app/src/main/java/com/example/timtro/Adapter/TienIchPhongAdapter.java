package com.example.timtro.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.timtro.Fragment.frThongTin;
import com.example.timtro.Model.mLoaiTin;
import com.example.timtro.Model.mPhieuTienIch;
import com.example.timtro.Model.mTienIchPhong;
import com.example.timtro.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TienIchPhongAdapter extends BaseAdapter {

    private final ArrayList<mTienIchPhong> armTienIchPhong;
    private final ArrayList<mPhieuTienIch> armPhieuTienIch;
    private final Context context;
    private final int layout;
    private int row_index = -1;
    private final boolean[] bool = {false, false, false, false, false, false, false, false};

    public TienIchPhongAdapter(ArrayList<mTienIchPhong> armTienIchPhong, ArrayList<mPhieuTienIch> armPhieuTienIch, Context context, int layout) {
        this.armTienIchPhong = armTienIchPhong;
        this.context = context;
        this.layout = layout;
        this.armPhieuTienIch = armPhieuTienIch;
    }

    @Override
    public int getCount() {
        return armTienIchPhong.size();
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
        RelativeLayout relTienIchPhong;
        ImageView imgTienIchPhong;
        TextView txtTenTienIchPhong;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.relTienIchPhong = convertView.findViewById(R.id.relTienIchPhong);
            viewHolder.imgTienIchPhong = convertView.findViewById(R.id.imgTienIchPhong);
            viewHolder.txtTenTienIchPhong = convertView.findViewById(R.id.txtTenTienIchPhong);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTenTienIchPhong.setText(armTienIchPhong.get(position).getTentienich());
        Picasso.get().load(armTienIchPhong.get(position).getIcon()).into(viewHolder.imgTienIchPhong);
        if (armPhieuTienIch.size() > 0){
            for(mPhieuTienIch pt : armPhieuTienIch){
                if(pt.getMatienich().equals(armTienIchPhong.get(position).getMatienich())){
                    bool[position] = true;
                    frThongTin.armmatienich[position] = armTienIchPhong.get(position).getMatienich();
                    viewHolder.txtTenTienIchPhong.setTextColor(Color.parseColor("#4a90e2"));
                    viewHolder.imgTienIchPhong.setColorFilter(Color.parseColor("#4a90e2"));
                    armPhieuTienIch.remove(pt);
                    break;
                }
            }
        }
        viewHolder.relTienIchPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
                if(row_index == position) {
                    if (!bool[row_index]) {
                        viewHolder.txtTenTienIchPhong.setTextColor(Color.parseColor("#4a90e2"));
                        viewHolder.imgTienIchPhong.setColorFilter(Color.parseColor("#4a90e2"));
                        frThongTin.armmatienich[position] = armTienIchPhong.get(position).getMatienich();
                        bool[row_index] = true;
                    } else {
                        viewHolder.txtTenTienIchPhong.setTextColor(Color.parseColor("#BF000000"));
                        viewHolder.imgTienIchPhong.setColorFilter(Color.parseColor("#40000000"));
                        frThongTin.armmatienich[position] = "";
                        bool[row_index] = false;
                    }
                }
            }
        });



        return convertView;
    }
}
