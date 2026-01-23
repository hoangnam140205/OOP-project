package com.hrm.quanlynhansu.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.quanlynhansu.entity.AuditLog;
import com.hrm.quanlynhansu.repository.AuditLogRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Ghi log thành công
     */
    public void logSuccess(String maNV, String hoTen, String role, String action, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setMaNV(maNV);
        log.setHoTen(hoTen);
        log.setRole(role);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setSuccess(true);

        if (request != null) {
            log.setIpAddress(getClientIP(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }

        auditLogRepository.save(log);
    }

    /**
     * Ghi log thất bại
     */
    public void logFailure(String maNV, String hoTen, String action, String errorMessage, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setMaNV(maNV);
        log.setHoTen(hoTen);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setSuccess(false);
        log.setErrorMessage(errorMessage);

        if (request != null) {
            log.setIpAddress(getClientIP(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }

        auditLogRepository.save(log);
    }

    /**
     * Lấy IP của client
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}
