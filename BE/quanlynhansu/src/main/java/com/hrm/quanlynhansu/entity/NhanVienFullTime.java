package com.hrm.quanlynhansu.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("FULL_TIME") // Giá trị lưu vào cột loai_nhan_vien
@Data
@EqualsAndHashCode(callSuper = true)
public class NhanVienFullTime extends NhanVien {
    
    @Override
    public Double tinhLuong() {
        if (getLuongCoBan() == null || getHeSoLuong() == null) return 0.0;
        return getLuongCoBan() * getHeSoLuong();
    }

    @Override
    public Double tinhLuongTheoThang(double soNgayLam) {
        // Ví dụ: Lương cứng / 26 ngày * số ngày làm thực tế
        if (getLuongCoBan() == null) return 0.0;
        return (getLuongCoBan() / 26) * soNgayLam;
    }

    @Override
    public String getTenLoai() {
        return "Nhân viên chính thức";
    }
}