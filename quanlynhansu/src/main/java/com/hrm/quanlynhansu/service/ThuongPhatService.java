package com.hrm.quanlynhansu.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.entity.ThuongPhat;
import com.hrm.quanlynhansu.enums.LoaiThuongPhat;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.ThuongPhatRepository;

@Service
public class ThuongPhatService {

    @Autowired
    private ThuongPhatRepository thuongPhatRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    // Hàm thêm mới Thưởng/Phạt
    public String themThuongPhat(String maNV, LoaiThuongPhat loai, Double soTien, String lyDo, LocalDate ngayQuyetDinh) {
        // 1. Tìm nhân viên
        Optional<NhanVien> nvOpt = nhanVienRepository.findById(maNV);
        if (nvOpt.isEmpty()) {
            return "Lỗi: Không tìm thấy nhân viên có mã " + maNV;
        }

        // 2. Tạo đối tượng mới
        ThuongPhat tp = new ThuongPhat();
        tp.setNhanVien(nvOpt.get());
        tp.setLoai(loai);
        tp.setSoTien(soTien);
        tp.setLyDo(lyDo);
        tp.setNgayQuyetDinh(ngayQuyetDinh);

        // 3. Lưu vào DB
        thuongPhatRepository.save(tp);
        return "Thành công: Đã lưu khoản " + loai + " cho nhân viên " + nvOpt.get().getHoTen();
    }

    // Hàm xóa (nếu nhập sai)
    public void xoaThuongPhat(Long id) {
        thuongPhatRepository.deleteById(id);
    }
}
