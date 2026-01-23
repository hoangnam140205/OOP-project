package com.hrm.quanlynhansu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.quanlynhansu.entity.QuyLuong;

@Repository
public interface QuyLuongRepository extends JpaRepository<QuyLuong, Long> {
    // Tìm quỹ lương theo tháng và năm
    QuyLuong findByThangAndNam(int thang, int nam);
}