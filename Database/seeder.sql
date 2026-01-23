-- ============================================
-- DATABASE SEEDER FOR QUAN LY NHAN SU (FINAL)
-- ============================================

DROP DATABASE IF EXISTS quan_ly_nhan_su;
CREATE DATABASE quan_ly_nhan_su CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quan_ly_nhan_su;

-- ============================================
-- 1. PHONG BAN (Departments)
-- ============================================
CREATE TABLE phong_ban (
    ma_phong VARCHAR(10) PRIMARY KEY,
    ten_phong VARCHAR(100) NOT NULL
);

INSERT INTO phong_ban (ma_phong, ten_phong) VALUES
('PB001', 'Phát triển phần mềm'),
('PB002', 'Marketing'),
('PB003', 'Quản lý'),
('PB004', 'Nhân sự'),
('PB005', 'Kế toán');

-- ============================================
-- 2. NHAN VIEN (Employees)
-- ============================================
CREATE TABLE nhan_vien (
    ma_nv VARCHAR(20) PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    ngay_sinh DATE,
    so_dien_thoai VARCHAR(15),
    ngay_vao_lam DATE,
    ma_phong VARCHAR(10),
    loai_nhan_vien VARCHAR(20) NOT NULL, -- FULL_TIME or PART_TIME
    
    -- Các cột dùng chung và riêng
    he_so_luong DOUBLE DEFAULT 1.0,      -- Dùng cho cả 2 loại
    chuc_vu VARCHAR(50),                 -- Dùng cho cả 2 loại
    
    luong_co_ban DOUBLE DEFAULT 0,       -- Full-time
    so_gio_lam INT DEFAULT 0,            -- Part-time
    luong_theo_gio DOUBLE DEFAULT 0,     -- Part-time
    
    password VARCHAR(255) DEFAULT '123456',
    role VARCHAR(20) DEFAULT 'user',
    
    FOREIGN KEY (ma_phong) REFERENCES phong_ban(ma_phong)
);

-- Insert Full-time employees (Đã có sẵn hệ số và chức vụ)
INSERT INTO nhan_vien (ma_nv, ho_ten, email, ngay_sinh, so_dien_thoai, ngay_vao_lam, ma_phong, loai_nhan_vien, he_so_luong, luong_co_ban, chuc_vu, role) VALUES
('NV001', 'Nguyễn Văn An', 'an@company.com', '1995-01-01', '0901234567', '2023-01-15', 'PB001', 'FULL_TIME', 2.5, 6000000, 'Dev Senior', 'user'),
('NV002', 'Trần Thị Bình', 'binh@company.com', '1998-05-20', '0902222333', '2022-06-10', 'PB002', 'FULL_TIME', 2.0, 6000000, 'Designer', 'user'),
('NV003', 'Lê Hoàng Cường', 'cuong@company.com', '1990-11-15', '0909999888', '2020-03-01', 'PB003', 'FULL_TIME', 3.0, 8000000, 'Project Manager', 'user'),
('NV004', 'Phạm Thị Diệu', 'dieu@company.com', '1996-08-08', '0903456789', '2023-05-20', 'PB004', 'FULL_TIME', 1.8, 6000000, 'HR Specialist', 'user'),
('NV005', 'Hoàng Văn Em', 'em@company.com', '1997-12-12', '0904567890', '2023-08-15', 'PB005', 'FULL_TIME', 2.2, 6000000, 'Accountant', 'user'),
('ADMIN', 'Administrator', 'admin@company.com', '1985-01-01', '0900000000', '2020-01-01', 'PB003', 'FULL_TIME', 5.0, 10000000, 'Admin', 'admin');

-- Insert Part-time employees (ĐÃ BỔ SUNG: he_so_luong, chuc_vu)
INSERT INTO nhan_vien (ma_nv, ho_ten, email, ngay_sinh, so_dien_thoai, ngay_vao_lam, ma_phong, loai_nhan_vien, so_gio_lam, luong_theo_gio, he_so_luong, chuc_vu, role) VALUES
('PT001', 'Nguyễn Thị Giang', 'giang@company.com', '2000-02-02', '0905678901', '2024-01-10', 'PB001', 'PART_TIME', 80, 50000, 1.0, 'Thực tập sinh Dev', 'user'),
('PT002', 'Trần Văn Hùng', 'hung@company.com', '2001-03-03', '0906789012', '2024-02-15', 'PB002', 'PART_TIME', 100, 45000, 1.0, 'Cộng tác viên TK', 'user');

-- ============================================
-- 3. CHAM CONG (Attendance)
-- ============================================
CREATE TABLE cham_cong (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nv VARCHAR(20) NOT NULL,
    ngay_cham_cong DATE NOT NULL,
    gio_vao TIME,
    gio_ra TIME,
    trang_thai VARCHAR(20) DEFAULT 'CO_MAT',
    so_gio_lam_them DOUBLE DEFAULT 0.0,
    ghi_chu TEXT,
    
    FOREIGN KEY (ma_nv) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (ma_nv, ngay_cham_cong)
);

-- Insert attendance records
INSERT INTO cham_cong (ma_nv, ngay_cham_cong, gio_vao, gio_ra, trang_thai, so_gio_lam_them) VALUES
('NV001', '2025-12-01', '08:30', '17:30', 'CO_MAT', 0),
('NV001', '2025-12-02', '08:25', '17:35', 'CO_MAT', 0),
('NV001', '2025-12-03', '09:05', '17:30', 'DI_MUON', 0),
('NV001', '2025-12-27', '08:30', '17:30', 'CO_MAT', 0),
('NV002', '2025-12-27', '09:05', '17:30', 'DI_MUON', 0),
('NV003', '2025-12-27', '08:00', '17:30', 'CO_MAT', 0);

-- ============================================
-- 4. BANG LUONG (Payroll)
-- ============================================
CREATE TABLE bang_luong (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nv VARCHAR(20) NOT NULL,
    thang INT NOT NULL,
    nam INT NOT NULL,
    so_ngay_cong DOUBLE DEFAULT 0,
    so_gio_lam DOUBLE DEFAULT 0,
    luong_chinh DOUBLE DEFAULT 0,
    tong_thuong DOUBLE DEFAULT 0,
    tong_phat DOUBLE DEFAULT 0,
    thuc_lanh DOUBLE DEFAULT 0,
    trang_thai VARCHAR(20) DEFAULT 'PENDING',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (ma_nv) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE,
    UNIQUE KEY unique_payroll (ma_nv, thang, nam)
);

-- Insert payroll records
INSERT INTO bang_luong (ma_nv, thang, nam, so_ngay_cong, luong_chinh, tong_thuong, tong_phat, thuc_lanh, trang_thai) VALUES
('NV001', 1, 2025, 26, 6000000, 500000, 0, 6500000, 'PAID'),
('NV002', 1, 2025, 26, 6000000, 200000, 0, 6200000, 'PENDING'),
('ADMIN', 1, 2025, 26, 15000000, 2000000, 0, 17000000, 'PENDING');

-- ============================================
-- 5. THUONG PHAT (Rewards & Penalties)
-- ============================================
CREATE TABLE thuong_phat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nv VARCHAR(20) NOT NULL,
    loai VARCHAR(20) NOT NULL, 
    ly_do TEXT,
    so_tien DOUBLE NOT NULL,
    ngay_ap_dung DATE NOT NULL,
    
    FOREIGN KEY (ma_nv) REFERENCES nhan_vien(ma_nv) ON DELETE CASCADE
);

INSERT INTO thuong_phat (ma_nv, loai, ly_do, so_tien, ngay_ap_dung) VALUES
('NV001', 'THUONG', 'Hoàn thành xuất sắc dự án X', 2000000, '2024-12-15'),
('NV002', 'THUONG', 'Sáng tạo trong thiết kế', 1500000, '2024-12-10'),
('NV003', 'THUONG', 'Quản lý tốt team', 3000000, '2024-12-20'),
('PT001', 'PHAT', 'Đi muộn nhiều lần', 200000, '2024-12-05');

-- ============================================
-- 6. AUDIT LOG (Login/Logout Tracking)
-- ============================================
CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nv VARCHAR(20),
    ho_ten VARCHAR(100),
    role VARCHAR(20),
    action VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    success BOOLEAN DEFAULT TRUE,
    error_message TEXT,
    
    INDEX idx_ma_nv (ma_nv),
    INDEX idx_action (action),
    INDEX idx_timestamp (timestamp)
);

-- ============================================
-- 7. QUY LUONG
-- ============================================
CREATE TABLE quy_luong (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    thang INT NOT NULL,
    nam INT NOT NULL,
    ngan_sach DOUBLE DEFAULT 0,
    UNIQUE KEY unique_budget (thang, nam)
);
INSERT INTO quy_luong (thang, nam, ngan_sach) VALUES (1, 2025, 200000000);