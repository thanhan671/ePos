package com.thanhan.epos;

import java.util.HashMap;
import java.util.Map;

public class ProductObj {
    int donGiaNhap;
    int donGiaXuat;
    String donViTinh;
    int id;
    String loaiHangHoa;
    String maCode;
    String tenHangHoa;
    int tonKho;

    public ProductObj() {
    }

    public ProductObj(int donGiaNhap, int donGiaXuat, String donViTinh, int id, String loaiHangHoa, String maCode, String tenHangHoa, int tonKho) {
        this.donGiaNhap = donGiaNhap;
        this.donGiaXuat = donGiaXuat;
        this.donViTinh = donViTinh;
        this.id = id;
        this.loaiHangHoa = loaiHangHoa;
        this.maCode = maCode;
        this.tenHangHoa = tenHangHoa;
        this.tonKho = tonKho;
    }

    public int getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(int donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

    public int getDonGiaXuat() {
        return donGiaXuat;
    }

    public void setDonGiaXuat(int donGiaXuat) {
        this.donGiaXuat = donGiaXuat;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoaiHangHoa() {
        return loaiHangHoa;
    }

    public void setLoaiHangHoa(String loaiHangHoa) {
        this.loaiHangHoa = loaiHangHoa;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public String getTenHangHoa() {
        return tenHangHoa;
    }

    public void setTenHangHoa(String tenHangHoa) {
        this.tenHangHoa = tenHangHoa;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }

    public Map<String, Object> toMapProduct() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("donGiaNhap", donGiaNhap);
        result.put("donGiaXuat", donGiaXuat);
        result.put("donViTinh", donViTinh);
        result.put("loaiHangHoa", loaiHangHoa);
        result.put("maCode", maCode);
        result.put("tenHangHoa", tenHangHoa);
        result.put("tonKho", tonKho);

        return result;
    }
}
