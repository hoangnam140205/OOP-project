package com.hrm.quanlynhansu.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("FULL_TIME") // Định danh trong database
@Data
@EqualsAndHashCode(callSuper = true)
public class NhanVienFullTime extends NhanVien {

    private Double heSoLuong;
    private Double luongCoBan;
    private String chucVu;

    @Override
    public Double tinhLuong() {
        if (heSoLuong != null && luongCoBan != null) {
            return heSoLuong * luongCoBan;
        }
        return 0.0;
    }

    @Override
    public Double tinhLuongTheoThang(double soNgayLam) {
        if (heSoLuong != null && luongCoBan != null) {
            double luongCung = heSoLuong * luongCoBan;
            return (luongCung / 26) * soNgayLam;
        }
        return 0.0;
    }

    @Override
    public String getTenLoai() {
        return "Full Time";
    }
}
