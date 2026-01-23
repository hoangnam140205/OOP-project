package com.hrm.quanlynhansu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.quanlynhansu.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Tìm log theo nhân viên
    List<AuditLog> findByMaNVOrderByTimestampDesc(String maNV);

    // Tìm log theo action (LOGIN, LOGOUT)
    List<AuditLog> findByActionOrderByTimestampDesc(String action);

    // Tìm log theo role
    List<AuditLog> findByRoleOrderByTimestampDesc(String role);

    // Tìm log trong khoảng thời gian
    List<AuditLog> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);

    // Tìm tất cả log, sắp xếp theo thời gian
    List<AuditLog> findAllByOrderByTimestampDesc();
}
