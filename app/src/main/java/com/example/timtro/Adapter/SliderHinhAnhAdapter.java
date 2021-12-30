package com.example.timtro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.timtro.Activity.vChiTietHinhAnh;
import com.example.timtro.Activity.vChiTietTinChinh;
import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class SliderHinhAnhAdapter extends PagerAdapter {

    Context context;
    ArrayList<mHinhAnh> arHinhAnh;
    int layout;

    public SliderHinhAnhAdapter(Context context, ArrayList<mHinhAnh> arHinhAnh, int layout) {
        this.context = context;
        this.arHinhAnh = arHinhAnh;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arHinhAnh.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout,null);
        ImageView imgSliderImg = view.findViewById(R.id.imgSliderImg);

        Picasso.get().load(arHinhAnh.get(i).getHinhanh()).into(imgSliderImg);

        imgSliderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, vChiTietHinhAnh.class);
                Gson gson = new Gson();
                String arImgString = gson.toJson(arHinhAnh);
                intent.putExtra("arImgString", arImgString);
                intent.putExtra("vitri", i);
                context.startActivity(intent);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
