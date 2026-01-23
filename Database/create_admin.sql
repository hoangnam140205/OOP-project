-- =============================================
-- Script: Tạo Admin Account
-- Description: Insert admin user vào database
-- =============================================

USE quan_ly_nhan_su;

-- Insert ADMIN account
INSERT INTO nhan_vien (
    ma_nv,
    ho_ten, 
    so_dien_thoai,
    ngay_vao_lam,
    loai_nhan_vien,
    chuc_vu,
    luong_co_ban,
    he_so_luong,
    ma_phong
) VALUES (
    'ADMIN',                    -- ma_nv
    'Administrator',            -- ho_ten  
    '0999999999',              -- so_dien_thoai
    '2024-01-01',              -- ngay_vao_lam
    'FULL_TIME',               -- loai_nhan_vien
    'Quản trị viên',           -- chuc_vu
    50000000,                  -- luong_co_ban (50 triệu)
    2.0,                       -- he_so_luong
    'IT'                       -- ma_phong
);

-- Verify admin đã được tạo
SELECT ma_nv, ho_ten, chuc_vu, luong_co_ban 
FROM nhan_vien 
WHERE ma_nv = 'ADMIN';

-- Login credentials:
-- Email: admin@company.com
-- Password: admin123
