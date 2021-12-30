package com.example.timtro.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timtro.Activity.vSuaTin;
import com.example.timtro.Adapter.HinhAnhSuaTinAdapter;
import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.xml.ExpandableHeightGridView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class frHinhAnh extends Fragment {

    private View vfrhinhanh;
    private TextView txtChonAnh, txtSoAnh;
    private ExpandableHeightGridView gridHinhAnh;
    private HinhAnhAdapter hinhAnhAdapter;
    private static final int PICK_IMAGES_CODE = 123;
    private static final int PICK_IMAGES_CAMERA = 0;
    public static ArrayList<Uri> armUri = new ArrayList<>();
    private Uri uri;
    private boolean checkcamera = false;
    private HinhAnhSuaTinAdapter hinhAnhSuaTinAdapter;
    public static ArrayList<mHinhAnh> armHinhAnh = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vfrhinhanh == null) {
            vfrhinhanh = inflater.inflate(R.layout.fr_hinh_anh, container, false);
            AnhXa();
            if (vSuaTin.mTin.getMatin() != null) {
                getAnhTheoMaTin(vSuaTin.mTin.getMatin());
            }
        }

        txtChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_chose_img, null, false);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetView.findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCameraInten();
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetView.findViewById(R.id.btnMedia).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPickImagesInten();
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        return vfrhinhanh;
    }

    private void SETADAPTERHINHANH() {
        txtSoAnh.setText(armUri.size() + "/4");
        hinhAnhAdapter = new HinhAnhAdapter(armUri, R.layout.line_hinh_anh, getContext());
        gridHinhAnh.setAdapter(hinhAnhAdapter);
        hinhAnhAdapter.notifyDataSetChanged();
    }

    private void setPickImagesInten() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_CODE);
    }

    private void setCameraInten() {
        checkcamera = true;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        uri = Objects.requireNonNull(getContext()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent inten = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        inten.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(inten, PICK_IMAGES_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (checkcamera) {
                    if((armUri.size() + 1) > 4){
                        Toast.makeText(getContext(), "Vui lòng không chọn quá 4 ảnh !", Toast.LENGTH_SHORT).show();
                    }else{
                        armUri.add(uri);
                    }
                    checkcamera = false;
                } else {
                    assert data != null;
                    armHinhAnh.clear();
                    if (data.getClipData() != null) {
                        if ((data.getClipData().getItemCount() + armUri.size()) > 4) {
                            Toast.makeText(getContext(), "Vui lòng không chọn quá 4 ảnh !", Toast.LENGTH_SHORT).show();
                        } else {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                armUri.add(imageUri);
                            }
                        }
                    } else {
                        if ((armUri.size() + 1) > 4) {
                            Toast.makeText(getContext(), "Vui lòng không chọn quá 4 ảnh !", Toast.LENGTH_SHORT).show();
                        } else {
                            Uri imageUri = data.getData();
                            armUri.add(imageUri);
                        }
                    }
                }
            }
            SETADAPTERHINHANH();
        }
    }


    private void AnhXa() {
        txtChonAnh = (TextView) vfrhinhanh.findViewById(R.id.txtChonAnh);
        gridHinhAnh = (ExpandableHeightGridView) vfrhinhanh.findViewById(R.id.gridHinhAnh);
        txtSoAnh = (TextView) vfrhinhanh.findViewById(R.id.txtSoAnh);
        gridHinhAnh.setExpanded(true);
    }

    private void getAnhTheoMaTin(String matin) {
        apiInterface getAnhTheoMaTin = APIClient.getAPIClient().create(apiInterface.class);
        Call<ArrayList<mHinhAnh>> callback = getAnhTheoMaTin.getAnhTheoMaTin(matin);
        callback.enqueue(new Callback<ArrayList<mHinhAnh>>() {
            @Override
            public void onResponse(Call<ArrayList<mHinhAnh>> call, Response<ArrayList<mHinhAnh>> response) {
                if (armHinhAnh.size() > 0) {
                    armHinhAnh.clear();
                } else if (armUri.size() > 0) {
                    armUri.clear();
                }
                txtSoAnh.setText(response.body().size() + "/4");
                hinhAnhSuaTinAdapter = new HinhAnhSuaTinAdapter(response.body(), R.layout.line_hinh_anh, getContext());
                gridHinhAnh.setAdapter(hinhAnhSuaTinAdapter);
                hinhAnhSuaTinAdapter.notifyDataSetChanged();
                armHinhAnh = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<mHinhAnh>> call, Throwable t) {

            }
        });
    }

    private class HinhAnhAdapter extends BaseAdapter {

        private ArrayList<Uri> armUrim = new ArrayList<>();
        private int layout;

        private HinhAnhAdapter(ArrayList<Uri> armUri, int layout, Context context) {
            this.armUrim = armUri;
            this.layout = layout;
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
            ImageView imgThemTin, deleteImg;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(layout, null);
                viewHolder.imgThemTin = (ImageView) convertView.findViewById(R.id.imgThemTin);
                viewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.deleteImg);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.deleteImg.setVisibility(View.VISIBLE);

            Uri uri = armUrim.get(position);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                viewHolder.imgThemTin.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    armUri.remove(position);
                    SETADAPTERHINHANH();
                }
            });

            return convertView;
        }
    }
}
