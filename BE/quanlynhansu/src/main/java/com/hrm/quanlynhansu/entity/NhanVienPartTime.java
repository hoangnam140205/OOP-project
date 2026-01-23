package com.hrm.quanlynhansu.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("PART_TIME")
@Data
@EqualsAndHashCode(callSuper = true)
public class NhanVienPartTime extends NhanVien {

    @Override
    public Double tinhLuong() {
        // Part-time tính theo giờ, hàm này tạm trả về 0 hoặc logic khác
        return 0.0;
    }

    @Override
    public Double tinhLuongTheoThang(double soNgayLam) {
        // Part-time không tính theo ngày công chuẩn
        return 0.0; 
    }
    
    // Overload cho Part-time
    public Double tinhLuongPartTime(double soGioLam) {
        if (getLuongTheoGio() == null) return 0.0;
        return getLuongTheoGio() * soGioLam;
    }

    @Override
    public String getTenLoai() {
        return "Nhân viên thời vụ";
    }
}