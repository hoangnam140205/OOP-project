package com.hrm.quanlynhansu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_nv")
    private String maNV;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "role")
    private String role;

    @Column(name = "action")
    private String action; // LOGIN, LOGOUT

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "error_message")
    private String errorMessage;
}
