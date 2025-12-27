package com.hrm.quanlynhansu.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "nhan_vien")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Gom hết con cái vào 1 bảng
@DiscriminatorColumn(name = "loai_nhan_vien") // Cột này để phân biệt FullTime hay PartTime
@Data

public abstract class NhanVien { // Thêm abstract vì sơ đồ lớp quy định
    @Id
    @Column(name = "ma_nv")
    private String maNV;    // ID nhân viên (String)

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "so_dien_thoai")

    private String soDienThoai;

    @Column(name = "email")
    private String email;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "ngay_vao_lam")
    private LocalDate ngayVaoLam;

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    // Thêm liên kết với Phòng Ban
    @ManyToOne
    @JoinColumn(name = "ma_phong") // Khóa ngoại liên kết sang bảng PhongBan
    private PhongBan phongBan;

    // Hàm tính lương trừu tượng (các con sẽ tự viết chi tiết)
    public abstract Double tinhLuong();
    // Tính lương theo tháng (dựa vào số ngày đi làm). Các lớp con sẽ implement.
    public abstract Double tinhLuongTheoThang(double soNgayLam);

    // Tính lương theo tháng dựa trên số ngày và tổng giờ làm (dành cho Part-time có giờ cụ thể)
    // Mặc định gọi về phương thức 1 tham số để tương thích với các class cũ.
    public Double tinhLuongTheoThang(double soNgayLam, double tongGioLam) {
        return tinhLuongTheoThang(soNgayLam);
    }
    // Hàm trừu tượng: Các class con bắt buộc phải trả lời xem mình là loại gì
    public abstract String getTenLoai();
}