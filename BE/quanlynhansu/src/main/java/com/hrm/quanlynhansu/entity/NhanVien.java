package com.hrm.quanlynhansu.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "nhan_vien")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "loai_nhan_vien")
@Data
public abstract class NhanVien { // Giữ nguyên abstract theo ý bạn

    @Id
    @Column(name = "ma_nv")
    private String maNV;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai; // Số điện thoại nằm ở đây là ĐÚNG

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "ngay_vao_lam")
    private LocalDate ngayVaoLam;

    @Column(name = "loai_nhan_vien", insertable = false, updatable = false)
    private String loaiNhanVien;

    // --- CÁC CỘT LƯƠNG (Bắt buộc phải khai báo ở đây để Controller gọi được) ---
    @Column(name = "he_so_luong")
    private Double heSoLuong;

    @Column(name = "luong_co_ban")
    private Double luongCoBan; // Controller cần cái này để getLuongCoBan()

    @Column(name = "chuc_vu")
    private String chucVu;

    @Column(name = "so_gio_lam")
    private Integer soGioLam;

    @Column(name = "luong_theo_gio")
    private Double luongTheoGio;
    // -------------------------------------------------------------

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "quan_ly")
    private String quanLy;

    @Column(name = "vi_tri")
    private String viTri;

    @ManyToOne
    @JoinColumn(name = "ma_phong")
    private PhongBan phongBan;

    // Các hàm abstract giữ nguyên
    public abstract Double tinhLuong();

    public abstract Double tinhLuongTheoThang(double soNgayLam);

    public Double tinhLuongTheoThang(double soNgayLam, double tongGioLam) {
        return tinhLuongTheoThang(soNgayLam);
    }

    public abstract String getTenLoai();
}