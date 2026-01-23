package com.hrm.quanlynhansu.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.quanlynhansu.entity.ThuongPhat;

@Repository
public interface ThuongPhatRepository extends JpaRepository<ThuongPhat, Long> {

    // Tìm các khoản thưởng/phạt của nhân viên trong một khoảng thời gian (để tính lương tháng)
    // Ví dụ: Tìm tất cả khoản thưởng của NV01 từ ngày 01/05 đến 31/05
    List<ThuongPhat> findByNhanVien_MaNVAndNgayQuyetDinhBetween(String maNV, LocalDate startDate, LocalDate endDate);
}
