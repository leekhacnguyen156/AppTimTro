package com.example.timtro.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ChiTietHinhAnhAdapter extends PagerAdapter {

    Context context;
    ArrayList<mHinhAnh> arHinhAnh;
    int layout;

    public ChiTietHinhAnhAdapter(Context context, ArrayList<mHinhAnh> arHinhAnh, int layout) {
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
        ImageView imgViewImg = view.findViewById(R.id.imgViewImg);

        Picasso.get().load(arHinhAnh.get(i).getHinhanh()).into(imgViewImg);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
