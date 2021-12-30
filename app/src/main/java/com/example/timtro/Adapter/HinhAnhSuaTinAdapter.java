package com.example.timtro.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.R;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.ArrayList;

public class HinhAnhSuaTinAdapter extends BaseAdapter {

    private ArrayList<mHinhAnh> armUri = new ArrayList<>();
    private int layout;
    private Context context;

    public HinhAnhSuaTinAdapter(ArrayList<mHinhAnh> armUri, int layout, Context context) {
        this.armUri = armUri;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return armUri.size();
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
        ImageView imgThemTin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgThemTin = (ImageView) convertView.findViewById(R.id.imgThemTin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(armUri.get(position).getHinhanh()).into(viewHolder.imgThemTin);

        return convertView;
    }
}
