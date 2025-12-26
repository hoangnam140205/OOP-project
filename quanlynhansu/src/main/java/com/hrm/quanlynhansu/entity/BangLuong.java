package com.hrm.quanlynhansu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bang_luong")
public class BangLuong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_nv", nullable = false)
    private NhanVien nhanVien;

    private int thang; // Tháng tính lương (1-12)
    private int nam;   // Năm (2025)

    @Column(name = "so_ngay_cong")
    private double soNgayCong; // Số ngày đi làm (đối với Full-time)

    @Column(name = "so_gio_lam")
    private double soGioLam;   // Số giờ làm (đối với Part-time)

    @Column(name = "luong_chinh")
    private Double luongChinh; // Lương cứng hoặc Lương theo giờ

    @Column(name = "tong_thuong")
    private Double tongThuong;

    @Column(name = "tong_phat")
    private Double tongPhat;

    @Column(name = "thuc_lanh")
    private Double thucLanh; // Con số cuối cùng chuyển khoản

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    // --- CONSTRUCTOR & GETTER SETTER ---
    public BangLuong() {}

    // Bạn hãy Generate Getter/Setter đầy đủ nhé (Alt+Insert)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }
    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }
    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }
    public double getSoNgayCong() { return soNgayCong; }
    public void setSoNgayCong(double soNgayCong) { this.soNgayCong = soNgayCong; }
    public double getSoGioLam() { return soGioLam; }
    public void setSoGioLam(double soGioLam) { this.soGioLam = soGioLam; }
    public Double getLuongChinh() { return luongChinh; }
    public void setLuongChinh(Double luongChinh) { this.luongChinh = luongChinh; }
    public Double getTongThuong() { return tongThuong; }
    public void setTongThuong(Double tongThuong) { this.tongThuong = tongThuong; }
    public Double getTongPhat() { return tongPhat; }
    public void setTongPhat(Double tongPhat) { this.tongPhat = tongPhat; }
    public Double getThucLanh() { return thucLanh; }
    public void setThucLanh(Double thucLanh) { this.thucLanh = thucLanh; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
