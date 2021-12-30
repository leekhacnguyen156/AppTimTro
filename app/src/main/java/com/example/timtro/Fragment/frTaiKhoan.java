package com.example.timtro.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.timtro.Activity.vAdmin;
import com.example.timtro.Activity.vDaLuu;
import com.example.timtro.Activity.vDangKy;
import com.example.timtro.Activity.vDangNhap;
import com.example.timtro.Activity.vDangTin;
import com.example.timtro.Activity.vDuyetPhongDaDat;
import com.example.timtro.Activity.vPhongDaDang;
import com.example.timtro.Activity.vPhongDaDat;
import com.example.timtro.Activity.vQuenMatKhau;
import com.example.timtro.Activity.vSuaThongTinTaiKhoan;
import com.example.timtro.MainActivity;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mTinh;
import com.example.timtro.ModelApp.appAccountDetails;
import com.example.timtro.R;
import com.example.timtro.Service.APIClient;
import com.example.timtro.Service.apiInterface;
import com.example.timtro.vNhaChinh;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

import static android.app.Activity.RESULT_OK;

public class frTaiKhoan extends Fragment {
    private View vtaikhoan;
    public static CircleImageView imgavatartaikhoan;
    private RelativeLayout rlNoiNoiOHienTai;
    private TextView txtCountDuyetDat, txtHovaten, txtLoaitaikhoan, txtPhongDaDang, txtPhongDaLuu, txtPhongDaDat, txtDangXuat, txtDuyetDatPhong;
    public static mTaiKhoan mTaiKhoan = new mTaiKhoan();
    public static ImageBadgeView imgAdmin;
    private final int Request_code_image = 123;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vtaikhoan == null) {
            vtaikhoan = inflater.inflate(R.layout.fr_tai_khoan, container, false);
        }
        Paper.init(getContext());
        getTaikhoan(Paper.book().read(appAccountDetails.mataikhoan));
        frThongBao.getThongBaoMoi(Paper.book().read(appAccountDetails.mataikhoan));
        anhxa();
        countDuyetDatPhong();
        vSuaThongTinTaiKhoan.getHuyen(mTaiKhoan.getMatinh());
        MessageQueue.IdleHandler handler = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                LoadThongtin();
                return false;
            }
        };
        Looper.myQueue().addIdleHandler(handler);

        imgAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDialogDangnhapAdmin();
            }
        });

        txtDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Đăng xuất");
                alertDialog.setMessage("Bạn có chắc muốn đăng xuất?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Xóa bộ nhớ đệm nhớ tài khoản & back về trang chủ
                                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                                Paper.book().delete(appAccountDetails.mataikhoan);
                                Paper.book().delete(appAccountDetails.tentinh);
                                Paper.book().delete(appAccountDetails.matinh);
                                Paper.book().delete(appAccountDetails.mahuyen);
                                Paper.book().delete(appAccountDetails.mahuyen);
                                Paper.book().delete(appAccountDetails.hovaten);
                                startActivity(new Intent(getContext(), vNhaChinh.class));
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        txtPhongDaDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vPhongDaDang.class));
            }
        });

        txtPhongDaDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vPhongDaDat.class));
            }
        });

        txtPhongDaLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vDaLuu.class));
            }
        });

        imgavatartaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Request_code_image);
            }
        });

        rlNoiNoiOHienTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vSuaThongTinTaiKhoan.class));
            }
        });

        txtDuyetDatPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), vDuyetPhongDaDat.class));
            }
        });

        return vtaikhoan;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Request_code_image && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                insertImage(bmp, Paper.book().read(appAccountDetails.mataikhoan));
                imgavatartaikhoan.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void countTinChuaDuyet() {
        apiInterface countTinChuaDuyet = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = countTinChuaDuyet.countTinChuaDuyet();
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() > 0) {
                    imgAdmin.setBadgeValue(response.body())
                            .setBadgeOvalAfterFirst(true)
                            .setBadgeTextFont(Typeface.SERIF)
                            .setBadgeTextStyle(Typeface.BOLD)
                            .setBadgeColor(Color.parseColor("#FFEB3B"))
                            .visibleBadge(true)
                            .setShowCounter(true);
                } else {
                    imgAdmin.setBadgeColor(android.R.color.transparent).setShowCounter(false);
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }

    private void countDuyetDatPhong() {
        apiInterface countDuyetDatPhong = APIClient.getAPIClient().create(apiInterface.class);
        Call<Integer> callBack = countDuyetDatPhong.countDuyetDatPhong(Paper.book().read(appAccountDetails.mataikhoan));
        callBack.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() > 0) {
                    txtCountDuyetDat.setText(response.body()+"");
                    txtCountDuyetDat.setVisibility(View.VISIBLE);
                } else {
                    txtCountDuyetDat.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }

    private void SetDialogDangnhapAdmin() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_nhapmatkhauadmin);
        final EditText edtMatkhau = dialog.findViewById(R.id.edtMatkhauvaoAdmin);
        Button btnOK = dialog.findViewById(R.id.btnOKDangnhapAdmin);
        Button btnHuy = dialog.findViewById(R.id.btnHuyDangnhapAdmin);
        AppCompatCheckBox checkboxCap2Admin = dialog.findViewById(R.id.checkboxCap2Admin);

        checkboxCap2Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkboxCap2Admin.isChecked()){
                    edtMatkhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtMatkhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMatkhau.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                } else if (!edtMatkhau.getText().toString().equals(mTaiKhoan.getMatkhaucap2())) {
                    Toast.makeText(getContext(), "Sai mật khẩu cấp 2!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    startActivity(new Intent(getContext(), vAdmin.class));
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void anhxa() {
        txtCountDuyetDat = vtaikhoan.findViewById(R.id.txtCountDuyetDat);
        imgavatartaikhoan = vtaikhoan.findViewById(R.id.imgAvatarTaikhoan);
        txtHovaten = vtaikhoan.findViewById(R.id.txtHovatenTaikhoan);
        txtLoaitaikhoan = vtaikhoan.findViewById(R.id.txtLoaitaikhoan);
        imgAdmin = vtaikhoan.findViewById(R.id.imgAdmin);
        txtPhongDaDang = vtaikhoan.findViewById(R.id.txtPhongDaDang);
        txtPhongDaLuu = vtaikhoan.findViewById(R.id.txtPhongDaLuu);
        txtDangXuat = vtaikhoan.findViewById(R.id.txtDangXuat);
        txtPhongDaDat = vtaikhoan.findViewById(R.id.txtPhongDaDat);
        rlNoiNoiOHienTai = vtaikhoan.findViewById(R.id.rlNoiNoiOHienTai);
        txtDuyetDatPhong = vtaikhoan.findViewById(R.id.txtDuyetDatPhong);
    }

    public static void getTaikhoan(String mataikhoan) {
        apiInterface getTaikhoan = APIClient.getAPIClient().create(apiInterface.class);
        Call<mTaiKhoan> callBack = getTaikhoan.getTaiKhoan(mataikhoan);
        callBack.enqueue(new Callback<mTaiKhoan>() {
            @Override
            public void onResponse(Call<mTaiKhoan> call, Response<mTaiKhoan> response) {
                mTaiKhoan = response.body();
                if (mTaiKhoan.getMatinh() != null){
                    vSuaThongTinTaiKhoan.getHuyen(mTaiKhoan.getMatinh());
                }

            }

            @Override
            public void onFailure(Call<mTaiKhoan> call, Throwable t) {
            }
        });
    }

    private void LoadThongtin() {
        if (mTaiKhoan.getMataikhoan() != null){
            txtHovaten.setText(mTaiKhoan.getHo()+" "+mTaiKhoan.getTen());
            if (!mTaiKhoan.getAvatar().isEmpty()) {
                Picasso.get().load(mTaiKhoan.getAvatar()).into(imgavatartaikhoan);
            }
            if (mTaiKhoan.getLoaitaikhoan().equals("0")) {
                countTinChuaDuyet();
                txtLoaitaikhoan.setText("ADMIN");
                imgAdmin.setVisibility(View.VISIBLE);
            } else {
                txtLoaitaikhoan.setText("NGƯỜI DÙNG");
                imgAdmin.setVisibility(View.GONE);
            }
        }
    }

    public static void insertImage(Bitmap bitmap, String mataikhoan) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        apiInterface insertImage = APIClient.getAPIClient().create(apiInterface.class);
        Call<String> call = insertImage.updateAvatarTaiKhoan(mataikhoan, name, image);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        LoadThongtin();
        countDuyetDatPhong();
        super.onResume();
    }
}
