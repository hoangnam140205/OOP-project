package com.hrm.quanlynhansu.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.quanlynhansu.entity.NhanVien;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    // 1. Tìm kiếm theo tên (cũ)
    List<NhanVien> findByHoTenContaining(String hoTen);

    // 2. Tìm kiếm nâng cao: Theo (Tên) HOẶC (Mã nhân viên)
    // Dùng cho ô tìm kiếm ở trang chủ
    List<NhanVien> findByHoTenContainingOrMaNVContaining(String hoTen, String maNV);

    // 3. Đếm số nhân viên trong một phòng ban
    // Dùng để kiểm tra điều kiện trước khi Xóa phòng ban (nếu > 0 thì không cho xóa)
    long countByPhongBan_MaPhong(String maPhong);

    // 4. Lấy danh sách nhân viên thuộc một phòng ban cụ thể
    // Dùng cho trang "Chi tiết phòng ban"
    List<NhanVien> findByPhongBan_MaPhong(String maPhong);

    // Đếm số nhân viên vào làm trong khoảng thời gian (Thống kê tăng nhân sự)
    long countByNgayVaoLamBetween(LocalDate startDate, LocalDate endDate);
}