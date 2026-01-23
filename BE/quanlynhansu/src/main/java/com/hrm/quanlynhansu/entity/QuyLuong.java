package com.hrm.quanlynhansu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "quy_luong")
public class QuyLuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "thang")
    private int thang;

    @Column(name = "nam")
    private int nam;

    @Column(name = "ngan_sach")
    private Double nganSach;
}