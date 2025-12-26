package com.hrm.quanlynhansu.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "phong_ban")
public class PhongBan {
    @Id
    @Column(name = "ma_phong") // Dùng String làm ID như sơ đồ
    private String maPhong;

    @Column(name = "ten_phong", nullable = false)
    private String tenPhong;

    // Một phòng ban có nhiều nhân viên
    // mappedBy trỏ tới tên biến "phongBan" trong class NhanVien
    @OneToMany(mappedBy = "phongBan", cascade = CascadeType.ALL)
    private List<NhanVien> danhSachNV;
}
