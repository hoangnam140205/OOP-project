package com.hrm.quanlynhansu.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.hrm.quanlynhansu.enums.AttendanceStatus;

import jakarta.persistence.Column; // Import Enum vừa tạo
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cham_cong") // Tên bảng trong database
public class ChamCong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính tự tăng (1, 2, 3...)

    @Column(name = "ngay_cham_cong", nullable = false)
    private LocalDate ngayChamCong; // Ngày chấm công (Năm-Tháng-Ngày)

    @Column(name = "gio_vao")
    private LocalTime gioVao; // Giờ vào (Giờ:Phút:Giây)

    @Column(name = "gio_ra")
    private LocalTime gioRa; // Giờ ra

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private AttendanceStatus trangThai; // Lấy từ Enum (CO_MAT, VANG...)

    @Column(name = "so_gio_lam_them")
    private Double soGioLamThem = 0.0; // Mặc định là 0

    @Column(name = "ghi_chu")
    private String ghiChu;

    // --- LIÊN KẾT VỚI BẢNG NHÂN VIÊN ---
    // Vì bảng NhanVien của bạn dùng String mã NV (NV01) làm khóa chính hoặc khóa định danh
    @ManyToOne
    @JoinColumn(name = "ma_nv", nullable = false) 
    private NhanVien nhanVien; 

    // --- CONSTRUCTOR ---
    public ChamCong() {
    }

    // --- GETTER & SETTER (Bạn có thể dùng Generate của IDE để tạo nhanh) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getNgayChamCong() { return ngayChamCong; }
    public void setNgayChamCong(LocalDate ngayChamCong) { this.ngayChamCong = ngayChamCong; }

    public LocalTime getGioVao() { return gioVao; }
    public void setGioVao(LocalTime gioVao) { this.gioVao = gioVao; }

    public LocalTime getGioRa() { return gioRa; }
    public void setGioRa(LocalTime gioRa) { this.gioRa = gioRa; }

    public AttendanceStatus getTrangThai() { return trangThai; }
    public void setTrangThai(AttendanceStatus trangThai) { this.trangThai = trangThai; }

    public Double getSoGioLamThem() { return soGioLamThem; }
    public void setSoGioLamThem(Double soGioLamThem) { this.soGioLamThem = soGioLamThem; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }
}
