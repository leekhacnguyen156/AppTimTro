package com.example.timtro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timtro.MainActivity;
import com.example.timtro.Model.mTin;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TinChungApdater extends BaseAdapter {

    private final ArrayList<mTin> armTin;
    private final Context context;
    private final int layout;

    public TinChungApdater(ArrayList<mTin> armTin, Context context, int layout){
        this.armTin = armTin;
        this.context = context;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return armTin.size();
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
        ImageView imgAnhMinhHoaTinDaDang;
        TextView txtGiaPhongDaDang, txtTieuDePhongDaDang,
                txtDiaChiPhongDaDang, txtDienTichPhongDaDang,
                txtDanhGiaPhongDaDang, txtThoiGianPhongDaDang;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout,null);
            viewHolder.imgAnhMinhHoaTinDaDang = convertView.findViewById(R.id.imgAnhMinhHoaTinDaDang);
            viewHolder.txtGiaPhongDaDang = convertView.findViewById(R.id.txtGiaPhongDaDang);
            viewHolder.txtTieuDePhongDaDang = convertView.findViewById(R.id.txtTieuDePhongDaDang);
            viewHolder.txtDiaChiPhongDaDang = convertView.findViewById(R.id.txtDiaChiPhongDaDang);
            viewHolder.txtDienTichPhongDaDang = convertView.findViewById(R.id.txtDienTichPhongDaDang);
            viewHolder.txtDanhGiaPhongDaDang = convertView.findViewById(R.id.txtDanhGiaPhongDaDang);
            viewHolder.txtThoiGianPhongDaDang = convertView.findViewById(R.id.txtThoiGianPhongDaDang);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //get Ảnh của tin
        Picasso.get().load(armTin.get(position).getImage()).into(viewHolder.imgAnhMinhHoaTinDaDang);

        //get điểm đánh giá
        apiInterface getDanhGiaTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> callBackdanhgia = getDanhGiaTheoMaTin.getDanhGia(armTin.get(position).getMatin());
        callBackdanhgia.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                viewHolder.txtDanhGiaPhongDaDang.setText(": "+String.format("%.2f", Double.parseDouble(response.body())) + " /5");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //set text
        viewHolder.txtTieuDePhongDaDang.setText(armTin.get(position).getTentieude()+"");
        viewHolder.txtDiaChiPhongDaDang.setText(": "+armTin.get(position).getDiachi());
        viewHolder.txtDienTichPhongDaDang.setText(": "+armTin.get(position).getDientich() + " m2");
        viewHolder.txtGiaPhongDaDang.setText(formatmoney(armTin.get(position).getGiaphong()+""));
        Date mDateTime = null;
        try {
            mDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(armTin.get(position).getThoigiandang());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.txtThoiGianPhongDaDang.setText(": "+ MainActivity.dateFormat.format(mDateTime));

        return convertView;
    }

    public static String formatmoney(String money) {
        String giaphong = money;
        if (giaphong != null){
            if (giaphong.length() == 6){
                return giaphong.substring(0,3)+" Nghìn";
            }else if (giaphong.length() == 7){

                String sub = giaphong.substring(0,2);
                if (sub.charAt(1) == '0'){
                    return sub.charAt(0)+" Triệu";
                }else{
                    return sub.charAt(0)+","+sub.charAt(1) + " Triệu";
                }

            }else if (giaphong.length() == 8){
                String sub = giaphong.substring(0,3);
                if (sub.charAt(2) == '0'){
                    return sub.substring(0,2)+" Triệu";
                }else{
                    return sub.substring(0,2)+","+sub.charAt(2) + " Triệu";
                }
            }else if (giaphong.length() == 9) {
                String sub = giaphong.substring(0, 4);
                if (sub.charAt(3) == '0') {
                    return sub.substring(0, 3) + " Triệu";
                } else {
                    return sub.substring(0, 3) + "," + sub.charAt(3) + " Triệu";
                }
            }
        }
        return giaphong;

    }
}
