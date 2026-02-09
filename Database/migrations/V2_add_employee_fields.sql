-- Migration: Thêm các trường mới vào bảng nhan_vien
-- Ngày: 2026-02-09

USE quan_ly_nhan_su;

-- Thêm các cột mới
ALTER TABLE nhan_vien 
ADD COLUMN IF NOT EXISTS dia_chi VARCHAR(255),
ADD COLUMN IF NOT EXISTS quan_ly VARCHAR(100),
ADD COLUMN IF NOT EXISTS vi_tri VARCHAR(100) DEFAULT 'Văn phòng chính';

-- Cập nhật giá trị mặc định cho các bản ghi cũ
UPDATE nhan_vien 
SET vi_tri = 'Văn phòng chính' 
WHERE vi_tri IS NULL;

-- Verify changes
SELECT 'Migration completed successfully' AS status;
