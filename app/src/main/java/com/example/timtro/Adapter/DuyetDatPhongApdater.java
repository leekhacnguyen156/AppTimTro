package com.example.timtro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.timtro.Activity.vChiTietTinDuyet;
import com.example.timtro.Fragment.frDuyetDatPhong;
import com.example.timtro.MainActivity;
import com.example.timtro.Model.mPhieuDatPhong;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mThongBao;
import com.example.timtro.Model.mTin;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DuyetDatPhongApdater extends BaseAdapter {

    private ArrayList<mPhieuDatPhong> armPhieuDatPhong;
    private mTin mTin = null;
    private mTaiKhoan mTaiKhoan = null;
    private Context context;
    private int layout;

    public DuyetDatPhongApdater(ArrayList<mPhieuDatPhong> armPhieuDatPhong, Context context, int layout) {
        this.armPhieuDatPhong = armPhieuDatPhong;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return armPhieuDatPhong.size();
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
        ImageView imgAnhMinhHoaTinDuyetDat;
        TextView txtGiaPhongDuyetDat, txtTieuDePhongDuyetDat,
                txtDiaChiPhongDuyetDat, txtDienTichPhongDuyetDat, txtDanhGiaPhongDuyetDat, txtThoiGianPhongDuyetDat;
        CircleImageView imgAvtTaikhoanDat;
        TextView txtTenTaiKhoanDat, txtDiaChiTaiKhoanDat;
        RelativeLayout rlCardviewDuyetDat;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getTinTheoMaTin(armPhieuDatPhong.get(position).getMatin());
        getTaiKhoan(armPhieuDatPhong.get(position).getMataikhoan());
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.rlCardviewDuyetDat = (RelativeLayout) convertView.findViewById(R.id.rlCardviewDuyetDat);
            viewHolder.imgAnhMinhHoaTinDuyetDat = (ImageView) convertView.findViewById(R.id.imgAnhMinhHoaTinDuyetDat);
            viewHolder.txtGiaPhongDuyetDat = (TextView) convertView.findViewById(R.id.txtGiaPhongDuyetDat);
            viewHolder.txtTieuDePhongDuyetDat = (TextView) convertView.findViewById(R.id.txtTieuDePhongDuyetDat);
            viewHolder.txtDiaChiPhongDuyetDat = (TextView) convertView.findViewById(R.id.txtDiaChiPhongDuyetDat);
            viewHolder.txtDienTichPhongDuyetDat = (TextView) convertView.findViewById(R.id.txtDienTichPhongDuyetDat);
            viewHolder.txtDanhGiaPhongDuyetDat = (TextView) convertView.findViewById(R.id.txtDanhGiaPhongDuyetDat);
            viewHolder.txtThoiGianPhongDuyetDat = (TextView) convertView.findViewById(R.id.txtThoiGianPhongDuyetDat);
            viewHolder.imgAvtTaikhoanDat = (CircleImageView) convertView.findViewById(R.id.imgAvtTaikhoanDat);
            viewHolder.txtTenTaiKhoanDat = (TextView) convertView.findViewById(R.id.txtTenTaiKhoanDat);
            viewHolder.txtDiaChiTaiKhoanDat = (TextView) convertView.findViewById(R.id.txtDiaChiTaiKhoanDat);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //get Ảnh của tin
        apiInterface getTinTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTin> callBack = getTinTheoMaTin.getTinTheoMaTin(armPhieuDatPhong.get(position).getMatin());
        callBack.enqueue(new Callback<mTin>() {
            @Override
            public void onResponse(Call<mTin> call, Response<mTin> response) {
                if(!response.body().getImage().isEmpty()){
                    Picasso.get().load(response.body().getImage()).into(viewHolder.imgAnhMinhHoaTinDuyetDat);
                }
                viewHolder.txtTieuDePhongDuyetDat.setText(response.body().getTentieude());
                viewHolder.txtDiaChiPhongDuyetDat.setText(": " + response.body().getDiachi());
                viewHolder.txtDienTichPhongDuyetDat.setText(": " + response.body().getDientich() + " m2");
                viewHolder.txtGiaPhongDuyetDat.setText(formatmoney(response.body().getGiaphong() + ""));
                Date mDateTime = null;
                try {
                    mDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body().getThoigiandang());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                viewHolder.txtThoiGianPhongDuyetDat.setText(": " + MainActivity.dateFormat.format(mDateTime));
            }

            @Override
            public void onFailure(Call<mTin> call, Throwable t) {

            }
        });

        //get điểm đánh giá
        apiInterface getDanhGiaTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = getDanhGiaTheoMaTin.getDanhGia(armPhieuDatPhong.get(position).getMatin());
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                viewHolder.txtDanhGiaPhongDuyetDat.setText(": " + String.format("%.2f", Double.parseDouble(response.body())) + " /5");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //set text

        apiInterface getTaiKhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callBack1 = getTaiKhoan.getTaiKhoan(armPhieuDatPhong.get(position).getMataikhoan());
        callBack1.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse(Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                Picasso.get().load(response.body().getAvatar()).into(viewHolder.imgAvtTaikhoanDat);
                viewHolder.txtTenTaiKhoanDat.setText(response.body().getTentaikhoan());
                viewHolder.txtDiaChiTaiKhoanDat.setText(response.body().getDiachi());
            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {

            }
        });

        return convertView;
    }

    private void getTinTheoMaTin(String matin) {

    }

    private void getTaiKhoan(String mataikhoan) {

    }

    private String formatmoney(String money) {
        String giaphong = money;
        if (giaphong != null) {
            if (giaphong.length() == 6) {

                String sub = giaphong.substring(0, 2);
                if (sub.indexOf("0") == 1) {
                    return sub.charAt(0) + " K";
                } else {
                    return sub.substring(0, 1) + "," + sub.substring(1, 1) + " K";
                }

            } else if (giaphong.length() == 7) {

                String sub = giaphong.substring(0, 2);
                if (sub.indexOf("0") == 1) {
                    return sub.charAt(0) + " Triệu";
                } else {
                    return sub.charAt(0) + "," + sub.charAt(1) + " Triệu";
                }

            } else if (giaphong.length() == 8) {
                String sub = giaphong.substring(0, 3);
                if (sub.indexOf("0") == 2) {
                    return sub.substring(0, 2) + " Triệu";
                } else {
                    return sub.substring(0, 2) + "," + sub.charAt(2) + " Triệu";
                }
            } else if (giaphong.length() == 9) {
                String sub = giaphong.substring(0, 4);
                if (sub.indexOf("0") == 3) {
                    return sub.substring(0, 3) + " Triệu";
                } else {
                    return sub.substring(0, 3) + "," + sub.substring(3, 1) + " Triệu";
                }
            }
        }
        return giaphong;
    }
}
