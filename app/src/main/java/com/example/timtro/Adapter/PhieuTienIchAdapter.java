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

import retrofit2.Callback;

public class PhieuTienIchAdapter extends BaseAdapter {

    private ArrayList<mTienIchPhong> armTienIchPhong;
    private ArrayList<mPhieuTienIch> armPhieuTienIch;
    private Context context;
    private int layout;

    public PhieuTienIchAdapter(ArrayList<mTienIchPhong> armTienIchPhong, ArrayList<mPhieuTienIch> armPhieuTienIch, Context context, int layout) {
        this.armTienIchPhong = armTienIchPhong;
        this.armPhieuTienIch = armPhieuTienIch;
        this.context = context;
        this.layout = layout;
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
            viewHolder.relTienIchPhong = (RelativeLayout) convertView.findViewById(R.id.relTienIchPhong);
            viewHolder.imgTienIchPhong = (ImageView) convertView.findViewById(R.id.imgTienIchPhong);
            viewHolder.txtTenTienIchPhong = (TextView) convertView.findViewById(R.id.txtTenTienIchPhong);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTenTienIchPhong.setText(armTienIchPhong.get(position).getTentienich());
        Picasso.get().load(armTienIchPhong.get(position).getIcon()).into(viewHolder.imgTienIchPhong);

        for(mPhieuTienIch pt : armPhieuTienIch){
            if(pt.getMatienich().equals(armTienIchPhong.get(position).getMatienich())){
                viewHolder.txtTenTienIchPhong.setTextColor(Color.parseColor("#4a90e2"));
                viewHolder.imgTienIchPhong.setColorFilter(Color.parseColor("#4a90e2"));
            }
        }

        return convertView;
    }
}
