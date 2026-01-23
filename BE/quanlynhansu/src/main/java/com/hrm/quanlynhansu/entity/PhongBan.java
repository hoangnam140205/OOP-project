package com.hrm.quanlynhansu.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(name = "ma_phong")
    private String maPhong;

    @Column(name = "ten_phong", nullable = false)
    private String tenPhong;

    @OneToMany(mappedBy = "phongBan")
    @JsonIgnore
    private List<NhanVien> nhanViens;
}