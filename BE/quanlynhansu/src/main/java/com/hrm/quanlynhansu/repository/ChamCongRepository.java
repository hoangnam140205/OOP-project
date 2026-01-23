package com.hrm.quanlynhansu.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.hrm.quanlynhansu.entity.ChamCong;


public interface ChamCongRepository extends org.springframework.data.jpa.repository.JpaRepository<ChamCong, Long> {

    // Tìm chấm công của nhân viên theo tháng (QUAN TRỌNG: dùng để tính lương)
    // Cú pháp: Tìm trong bảng ChamCong, dựa vào field 'nhanVien', lấy field 'maNv' của nó
    List<ChamCong> findByNhanVien_MaNVAndNgayChamCongBetween(String maNv, LocalDate startDate, LocalDate endDate);

    // Để tìm chấm công của 1 ngày cụ thể (cho Dashboard)
    List<ChamCong> findByNgayChamCong(LocalDate date);

    // Tìm lịch sử của 1 nhân viên (Để tính lương)
    List<ChamCong> findByNhanVien_MaNV(String maNV);

    // Kiểm tra xem nhân viên đã chấm công ngày hôm nay chưa (để chặn chấm công 2 lần)
    boolean existsByNhanVien_MaNVAndNgayChamCong(String maNv, LocalDate ngayChamCong);

    // Lấy bản ghi chấm công cụ thể (để cập nhật giờ ra - Check out)
    Optional<ChamCong> findByNhanVien_MaNVAndNgayChamCong(String maNv, LocalDate ngayChamCong);

    void deleteByNhanVien_MaNV(String maNV);
}
