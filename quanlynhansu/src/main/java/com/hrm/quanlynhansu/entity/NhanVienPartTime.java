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

    private Integer soGioLam;
    private Double luongTheoGio;

    @Override
    public Double tinhLuong() {
        if (soGioLam != null && luongTheoGio != null) {
            return soGioLam * luongTheoGio;
        }
        return 0.0;
    }

    @Override
    public Double tinhLuongTheoThang(double soNgayLam) {
        Double luongTheoGioLocal = this.luongTheoGio;
        if (luongTheoGioLocal != null) {
            double tongGio = soNgayLam * 4; // Giả sử mỗi ngày công = 4 giờ
            return tongGio * luongTheoGioLocal;
        }
        return 0.0;
    }

    @Override
    public Double tinhLuongTheoThang(double soNgayLam, double tongGioLam) {
        Double luongTheoGioLocal = this.luongTheoGio;
        if (luongTheoGioLocal != null) {
            double totalHours = (tongGioLam > 0) ? tongGioLam : soNgayLam * 4;
            return totalHours * luongTheoGioLocal;
        }
        return 0.0;
    }

    @Override
    public String getTenLoai() {
        return "Part Time";
    }
}
