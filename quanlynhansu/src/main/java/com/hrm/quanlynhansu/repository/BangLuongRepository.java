package com.hrm.quanlynhansu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hrm.quanlynhansu.entity.BangLuong;

@Repository
public interface BangLuongRepository extends JpaRepository<BangLuong, Long> {
    // Tìm bảng lương của một tháng cụ thể
    List<BangLuong> findByThangAndNam(int thang, int nam);

    // Kiểm tra xem nhân viên đã được tính lương tháng này chưa (tránh tính 2 lần)
    boolean existsByNhanVien_MaNVAndThangAndNam(String maNV, int thang, int nam);

    @Query("SELECT SUM(b.thucLanh) FROM BangLuong b WHERE b.thang = :thang AND b.nam = :nam")
    Double tinhTongLuongThang(@Param("thang") int thang, @Param("nam") int nam);
}