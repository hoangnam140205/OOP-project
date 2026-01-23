package com.hrm.quanlynhansu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "bang_luong")
public class BangLuong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "thang")
    private Integer thang;

    @Column(name = "nam")
    private Integer nam;

    @Column(name = "so_ngay_cong")
    private Double soNgayCong; 

    @Column(name = "so_gio_lam")
    private Double soGioLam;

    @Column(name = "luong_chinh")
    private Double luongChinh;

    @Column(name = "tong_thuong")
    private Double tongThuong;

    @Column(name = "tong_phat")
    private Double tongPhat;

    @Column(name = "thuc_lanh")
    private Double thucLanh;

    @Column(name = "trang_thai")
    private String trangThai; // "PAID", "PENDING"

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @ManyToOne
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @PrePersist
    public void prePersist() {
        if (this.ngayTao == null) {
            this.ngayTao = LocalDateTime.now();
        }
    }
}