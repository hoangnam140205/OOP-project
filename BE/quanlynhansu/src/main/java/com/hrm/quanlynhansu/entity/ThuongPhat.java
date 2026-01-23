package com.hrm.quanlynhansu.entity;

import java.time.LocalDate;

import com.hrm.quanlynhansu.enums.LoaiThuongPhat;

import jakarta.persistence.Column;
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
@Table(name = "thuong_phat")
public class ThuongPhat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai", nullable = false)
    private LoaiThuongPhat loai; // THUONG hoặc PHAT

    @Column(name = "so_tien", nullable = false)
    private Double soTien; // Số tiền (VNĐ)

    @Column(name = "ly_do")
    private String lyDo; // Ví dụ: "Thưởng dự án A", "Đi làm muộn 3 lần"

    @Column(name = "ngay_quyet_dinh")
    private LocalDate ngayQuyetDinh; // Ngày ghi nhận thưởng/phạt (để biết tính vào lương tháng nào)

    // --- LIÊN KẾT VỚI NHÂN VIÊN ---
    @ManyToOne
    @JoinColumn(name = "ma_nv", nullable = false)
    private NhanVien nhanVien;

    // --- CONSTRUCTOR ---
    public ThuongPhat() {
    }

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LoaiThuongPhat getLoai() { return loai; }
    public void setLoai(LoaiThuongPhat loai) { this.loai = loai; }

    public Double getSoTien() { return soTien; }
    public void setSoTien(Double soTien) { this.soTien = soTien; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public LocalDate getNgayQuyetDinh() { return ngayQuyetDinh; }
    public void setNgayQuyetDinh(LocalDate ngayQuyetDinh) { this.ngayQuyetDinh = ngayQuyetDinh; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }
}
