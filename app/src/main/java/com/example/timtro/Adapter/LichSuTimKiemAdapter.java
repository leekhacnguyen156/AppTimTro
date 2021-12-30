package com.example.timtro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.timtro.Fragment.frLichSuTimKiem;
import com.example.timtro.Fragment.frTimKiem;
import com.example.timtro.Model.mLichSu;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;

import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuTimKiemAdapter extends BaseAdapter {
    private final ArrayList<String> armLichSu;
    private final Context context;
    private final int layout;

    public LichSuTimKiemAdapter(ArrayList<String> armLichSu, Context context, int layout) {
        this.armLichSu = armLichSu;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return armLichSu.size();
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
        TextView txtNoiDungLichSu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.txtNoiDungLichSu = convertView.findViewById(R.id.txtNoiDungLichSu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.txtNoiDungLichSu.setText(armLichSu.get(position) + "");
        return convertView;
    }
}
