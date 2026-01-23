package com.hrm.quanlynhansu.controller.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrm.quanlynhansu.entity.AuditLog;
import com.hrm.quanlynhansu.repository.AuditLogRepository;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://localhost:3000" })
public class AuditLogApiController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // GET /api/audit-logs - Lấy tất cả logs
    @GetMapping
    public ResponseEntity<List<AuditLogDTO>> getAllLogs() {
        List<AuditLog> logs = auditLogRepository.findAllByOrderByTimestampDesc();
        List<AuditLogDTO> dtos = logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/audit-logs/employee/:maNV - Lấy logs của một nhân viên
    @GetMapping("/employee/{maNV}")
    public ResponseEntity<List<AuditLogDTO>> getLogsByEmployee(@PathVariable String maNV) {
        List<AuditLog> logs = auditLogRepository.findByMaNVOrderByTimestampDesc(maNV);
        List<AuditLogDTO> dtos = logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/audit-logs/action/:action - Lấy logs theo action (LOGIN/LOGOUT)
    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLogDTO>> getLogsByAction(@PathVariable String action) {
        List<AuditLog> logs = auditLogRepository.findByActionOrderByTimestampDesc(action);
        List<AuditLogDTO> dtos = logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/audit-logs/role/:role - Lấy logs theo role (admin/user)
    @GetMapping("/role/{role}")
    public ResponseEntity<List<AuditLogDTO>> getLogsByRole(@PathVariable String role) {
        List<AuditLog> logs = auditLogRepository.findByRoleOrderByTimestampDesc(role);
        List<AuditLogDTO> dtos = logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/audit-logs/range?start=...&end=... - Lấy logs trong khoảng thời gian
    @GetMapping("/range")
    public ResponseEntity<List<AuditLogDTO>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AuditLog> logs = auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
        List<AuditLogDTO> dtos = logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Helper method
    private AuditLogDTO convertToDTO(AuditLog log) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(log.getId());
        dto.setMaNV(log.getMaNV());
        dto.setHoTen(log.getHoTen());
        dto.setRole(log.getRole());
        dto.setAction(log.getAction());
        dto.setTimestamp(log.getTimestamp().toString());
        dto.setIpAddress(log.getIpAddress());
        dto.setUserAgent(log.getUserAgent());
        dto.setSuccess(log.getSuccess());
        dto.setErrorMessage(log.getErrorMessage());
        return dto;
    }

    // DTO
    public static class AuditLogDTO {
        private Long id;
        private String maNV;
        private String hoTen;
        private String role;
        private String action;
        private String timestamp;
        private String ipAddress;
        private String userAgent;
        private Boolean success;
        private String errorMessage;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMaNV() {
            return maNV;
        }

        public void setMaNV(String maNV) {
            this.maNV = maNV;
        }

        public String getHoTen() {
            return hoTen;
        }

        public void setHoTen(String hoTen) {
            this.hoTen = hoTen;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
