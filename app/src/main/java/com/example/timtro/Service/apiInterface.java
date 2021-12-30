package com.example.timtro.Service;

import com.example.timtro.Model.mHinhAnh;
import com.example.timtro.Model.mHuyen;
import com.example.timtro.Model.mLichSu;
import com.example.timtro.Model.mLoaiPhong;
import com.example.timtro.Model.mLoaiTin;
import com.example.timtro.Model.mPhieuDatPhong;
import com.example.timtro.Model.mPhieuTienIch;
import com.example.timtro.Model.mTaiKhoan;
import com.example.timtro.Model.mThongBao;
import com.example.timtro.Model.mTienIchPhong;
import com.example.timtro.Model.mTin;
import com.example.timtro.Model.mTinh;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiInterface {

    @FormUrlEncoded
    @POST("getHuyen.php")
    Call<ArrayList<mHuyen>> getHuyen(
            @Field("matinh") String matinh
    );

    @FormUrlEncoded
    @POST("getTenNguoiDatPhong.php")
    Call<mTaiKhoan> getTenNguoiDatPhong(
            @Field("matin") String matin
    );

    @GET("getTinh.php")
    Call<List<mTinh>> getTinh();


    @GET("getTienIchPhong.php")
    Call<List<mTienIchPhong>> getTienIchPhong();


    @GET("getLoaiPhong.php")
    Call<List<mLoaiPhong>> getLoaiPhong();


    @GET("getLoaiTin.php")
    Call<List<mLoaiTin>> getLoaiTin();

    @FormUrlEncoded
    @POST("getTenTinh.php")
    Call<mTinh> getTenTinh(
            @Field("matinh") String matinh
    );

    @FormUrlEncoded
    @POST("getPhongdadat.php")
    Call<ArrayList<mTin>> getPhongdadat(
            @Field("mataikhoan") String mataikhoan
    );

    @FormUrlEncoded
    @POST("getPhongdaluu.php")
    Call<ArrayList<mTin>> getPhongdaluu(
            @Field("mataikhoan") String mataikhoan
    );


    @FormUrlEncoded
    @POST("getTenHuyen.php")
    Call<mHuyen> getTenHuyen(
            @Field("mahuyen") String mahuyen
    );

    @FormUrlEncoded
    @POST("getDangNhap.php")
    Call<mTaiKhoan> getDangNhap(
            @Field("tentaikhoan") String tentaikhoan
    );

    @FormUrlEncoded
    @POST("getTaiKhoan.php")
    Call<mTaiKhoan> getTaiKhoan(
            @Field("mataikhoan") String mataikhoan
    );

    @FormUrlEncoded
    @POST("countDuyetDatPhong.php")
    Call<Integer> countDuyetDatPhong(
            @Field("mataikhoan") String mataikhoan
    );

    @FormUrlEncoded
    @POST("getTinTheoMaTaiKhoan.php")
    Call<ArrayList<mTin>> getTinTheoMaTaiKhoan(
            @Field("mataikhoan") String mataikhoan
    );

    @GET("getMaTinNew.php")
    Call<String> getMaTinNew(

    );

    @FormUrlEncoded
    @POST("getAnhChinhTin.php")
    Call<String> getAnhChinhTin(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("getAnhTheoMaTin.php")
    Call<ArrayList<mHinhAnh>> getAnhTheoMaTin(
            @Field("matin") String matin
    );

    @GET("getTinChuaDuyet.php")
    Call<List<mTin>> getTinChuaDuyet();

    @FormUrlEncoded
    @POST("getDanhGia.php")
    Call<String> getDanhGia(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("getTienIchTheoMaTin.php")
    Call<ArrayList<mPhieuTienIch>> getTienIchTheoMaTin(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("getTinTimKiem.php")
    Call<ArrayList<mTin>> getTinTimKiem(
            @Field("tukhoa") String tukhoa
    );

    @FormUrlEncoded
    @POST("getLichSuTimKiem.php")
    Call<ArrayList<mLichSu>> getLichSuTimKiem(
            @Field("mataikhoan") String mataikhoan
    );

    @FormUrlEncoded
    @POST("getTinTheoMaTin.php")
    Call<mTin> getTinTheoMaTin(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("getTinMoiDangLM6.php")
    Call<ArrayList<mTin>> getTinMoiDangLM6(
            @Field("matinh") String matinh
    );

    @GET("getTinDaDuyet.php")
    Call<List<mTin>> getTinDaDuyet();

    @GET("getTinKhongDuyet.php")
    Call<List<mTin>> getTinKhongDuyet();

    @FormUrlEncoded
    @POST("getTinOGhepLM6.php")
    Call<ArrayList<mTin>> getTinOGhepLM6(
            @Field("matinh") String matinh
    );

    @FormUrlEncoded
    @POST("getThongBao.php")
    Call<List<mThongBao>> getThongBao(
            @Field("mataikhoan") String mataikhoan
    );

    @FormUrlEncoded
    @POST("getTinTongHop.php")
    Call<ArrayList<mTin>> getTinTongHop(
            @Field("matinh") String matinh
    );


    @FormUrlEncoded
    @POST("insertTaiKhoan.php")
    Call<String> insertTaiKhoan(
            @Field("matinh") String matinh,
            @Field("mahuyen") String mahuyen,
            @Field("diachi") String diachi,
            @Field("tentaikhoan") String tentaikhoan,
            @Field("matkhau") String matkhau,
            @Field("ho") String ho,
            @Field("ten") String ten,
            @Field("sodienthoai") String sodienthoai,
            @Field("loaitaikhoan") int loaitaikhoan,
            @Field("matkhaucap2") String matkhaucap2,
            @Field("avatar") Text avatar
    );

    @FormUrlEncoded
    @POST("insertLichSuTimKiem.php")
    Call<String> insertLichSuTimKiem(
            @Field("mataikhoan") String mataikhoan,
            @Field("noidung") String noidung
    );


    @FormUrlEncoded
    @POST("insertImage.php")
    Call<String> insertImage(
            @Field("matin") String matin,
            @Field("name") String name,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("insertTin.php")
    Call<String> insertTin(
            @Field("mataikhoan") String mataikhoan,
            @Field("maloaitin") String maloaitin,
            @Field("maloaiphong") String maloaiphong,
            @Field("tentieude") String tentieude,
            @Field("matinh") String matinh,
            @Field("mahuyen") String mahuyen,
            @Field("diachi") String diachi,
            @Field("tenlienhe") String tenlienhe,
            @Field("sdt") String sdt,
            @Field("giaphong") double giaphong,
            @Field("dientich") double dientich,
            @Field("motachitiet") String motachitiet,
            @Field("trangthaiduyet") int trangthaiduyet,
            @Field("thoigiandang") String thoigiandang,
            @Field("trangthaidat") int trangthaidat,
            @Field("name") String name,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("insertPhieuTienIch.php")
    Call<String> insertPhieuTienIch(
            @Field("matin") String matin,
            @Field("matienich") String matienich
    );

    @FormUrlEncoded
    @POST("updateTin.php")
    Call<String> updateTin(
            @Field("matin") String matin,
            @Field("maloaitin") String maloaitin,
            @Field("maloaiphong") String maloaiphong,
            @Field("tentieude") String tentieude,
            @Field("matinh") String matinh,
            @Field("mahuyen") String mahuyen,
            @Field("diachi") String diachi,
            @Field("tenlienhe") String tenlienhe,
            @Field("sdt") String sdt,
            @Field("giaphong") double giaphong,
            @Field("dientich") double dientich,
            @Field("motachitiet") String motachitiet,
            @Field("thoigiandang") String thoigiandang,
            @Field("name") String name,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("updateTinKhongCoAnh.php")
    Call<String> updateTinKhongCoAnh(
            @Field("matin") String matin,
            @Field("maloaitin") String maloaitin,
            @Field("maloaiphong") String maloaiphong,
            @Field("tentieude") String tentieude,
            @Field("matinh") String matinh,
            @Field("mahuyen") String mahuyen,
            @Field("diachi") String diachi,
            @Field("tenlienhe") String tenlienhe,
            @Field("sdt") String sdt,
            @Field("giaphong") double giaphong,
            @Field("dientich") double dientich,
            @Field("motachitiet") String motachitiet,
            @Field("thoigiandang") String thoigiandang
    );
    @GET("countTinChuaDuyet.php")
    Call<Integer> countTinChuaDuyet();


    @FormUrlEncoded
    @POST("updateQuenmatkhau.php")
    Call<String> updateQuenmatkhau(
            @Field("tentaikhoan") String tentaikhoan,
            @Field("matkhau") String matkhau
    );

    @FormUrlEncoded
    @POST("updateImage.php")
    Call<String> updateImage(
        @Field("matin") String matin
    );


    @FormUrlEncoded
    @POST("updatePhieuTienIch.php")
    Call<String> updatePhieuTienIch(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("checkTenTaiKhoan.php")
    Call<Integer> checkTenTaiKhoan(
            @Field("tentaikhoan") String tentaikhoan
    );

    @FormUrlEncoded
    @POST("checkSdt.php")
    Call<Integer>checkSdt(
            @Field("sdt") String sdt
    );

    @FormUrlEncoded
    @POST("updateTrangThaiDuyet.php")
    Call<String> updateTrangThaiDuyet(
            @Field("matin") String matin,
            @Field("trangthaiduyet") int trangthaiduyet
    );

    @FormUrlEncoded
    @POST("countDanhGia.php")
    Call<Integer> countDanhGia(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("updateAvatarTaiKhoan.php")
    Call<String> updateAvatarTaiKhoan(
            @Field("mataikhoan") String mataikhoan,
            @Field("name") String name,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("insertThongBao.php")
    Call<String> insertThongBao(
            @Field("maloaithongbao") int maloaithongbao,
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin,
            @Field("trangthai") int trangthai,
            @Field("thoigian") String thoigian
    );

    @FormUrlEncoded
    @POST("countThongBao.php")
    Call<Integer> countThongBao(
            @Field("mataikhoan") String mataikhoan
    );

    @FormUrlEncoded
    @POST("updateTrangThaiXem.php")
    Call<String> updateTrangThaiXem(
            @Field("mathongbao") String mathongbao,
            @Field("trangthaixem") int trangthaixem
    );

    @FormUrlEncoded
    @POST("updateMatKhauTaiKhoan.php")
    Call<String> updateMatKhauTaiKhoan(
            @Field("mataikhoan") String mataikhoan,
            @Field("matkhau") String matkhau,
            @Field("matkhaumoi") String matkhaumoi
    );

    @FormUrlEncoded
    @POST("deleteLichSuTimKiem.php")
    Call<String> deleteLichSuTimKiem(
            @Field("mataikhoan") String mataikhoan
    );


    @FormUrlEncoded
    @POST("getTinMoiDang.php")
    Call<ArrayList<mTin>> getTinMoiDang(
            @Field("matinh") String matinh
    );

    @FormUrlEncoded
    @POST("getTinOGhep.php")
    Call<ArrayList<mTin>> getTinOGhep(
            @Field("matinh") String matinh
    );

    @FormUrlEncoded
    @POST("insertDanhGia.php")
    Call<String> insertDanhGia(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin,
            @Field("diem") float diem
    );

    @FormUrlEncoded
    @POST("insertPhieuLuuPhong.php")
    Call<String> insertPhieuLuuPhong(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("deletePhieuLuuPhong.php")
    Call<String> deletePhieuLuuPhong(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("getDangGiaTheoMaTaiKhoan.php")
    Call<Float> getDangGiaTheoMaTaiKhoan(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("updateDanhGia.php")
    Call<String> updateDanhGia(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin,
            @Field("diem") float diem
    );

    @FormUrlEncoded
    @POST("updateTaiKhoan.php")
    Call<String> updateTaiKhoan(
            @Field("mataikhoan") String mataikhoan,
            @Field("matinh") String matinh,
            @Field("mahuyen") String mahuyen,
            @Field("diachi") String diachi,
            @Field("ho") String ho,
            @Field("ten") String ten,
            @Field("sodienthoai") String sodienthoai
    );

    @FormUrlEncoded
    @POST("updateTinDatPhong.php")
    Call<String> updateTinDatPhong(
            @Field("matin") String matin
    );
    @FormUrlEncoded
    @POST("insertPhienDatPhong.php")
    Call<String> insertPhienDatPhong(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin,
            @Field("tenkhachdat") String tenkhachdat,
            @Field("diachi") String diachi,
            @Field("sdt") String sdt,
            @Field("ghichu") String ghichu,
            @Field("thoigiandat") String thoigiandat,
            @Field("trangthaiduyet") int trangthaiduyet
    );

    @FormUrlEncoded
    @POST("countTrungPhieuDatPhong.php")
    Call<Integer> countTrungPhieuDatPhong(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("countPhieuDatPhong.php")
    Call<Integer> countPhieuDatPhong(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("countPhieuLuuTheoTaiKhoan.php")
    Call<Integer> countPhieuLuuTheoTaiKhoan(
            @Field("mataikhoan") String mataikhoan,
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("deleteTin.php")
    Call<String> deleteTin(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("getPhieuDatPhong.php")
    Call<ArrayList<mPhieuDatPhong>> getPhieuDatPhong(
            @Field("mataikhoan") String mataikhoan,
            @Field("trangthaiduyet") int trangthaiduyet
    );

    @FormUrlEncoded
    @POST("updateTrangThaiDat.php")
    Call<String> updateTrangThaiDat(
            @Field("matin") String matin
    );

    @FormUrlEncoded
    @POST("updateTrangThaiDuyetDat.php")
    Call<String> updateTrangThaiDuyetDat(
            @Field("madatphong") String madatphong,
            @Field("trangthaiduyet") int trangthaiduyet
    );

}
