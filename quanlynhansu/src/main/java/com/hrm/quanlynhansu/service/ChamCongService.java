package com.hrm.quanlynhansu.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.quanlynhansu.entity.ChamCong;
import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.enums.AttendanceStatus;
import com.hrm.quanlynhansu.repository.ChamCongRepository;
import com.hrm.quanlynhansu.repository.NhanVienRepository;

@Service
public class ChamCongService {

    @Autowired
    private ChamCongRepository chamCongRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    // --- HÀM 1: CHẤM CÔNG VÀO (CHECK-IN) ---
    public String chamCongVao(String maNV) {
        LocalDate homNay = LocalDate.now();

        // 1. Kiểm tra xem nhân viên có tồn tại không
        Optional<NhanVien> nhanVienOpt = nhanVienRepository.findById(maNV);
        if (nhanVienOpt.isEmpty()) {
            return "Lỗi: Không tìm thấy nhân viên có mã " + maNV;
        }

        // 2. Kiểm tra xem hôm nay đã chấm công chưa
        if (chamCongRepository.existsByNhanVien_MaNVAndNgayChamCong(maNV, homNay)) {
            return "Thông báo: Bạn đã chấm công vào ngày hôm nay rồi!";
        }

        // 3. Tạo bản ghi chấm công mới
        ChamCong chamCong = new ChamCong();
        chamCong.setNhanVien(nhanVienOpt.get());
        chamCong.setNgayChamCong(homNay);
        chamCong.setGioVao(LocalTime.now()); // Lấy giờ hiện tại
        
        // Logic đơn giản: Nếu check-in sau 8h30 thì tính là đi muộn (Bạn có thể sửa giờ này)
        if (LocalTime.now().isAfter(LocalTime.of(8, 30))) {
            chamCong.setTrangThai(AttendanceStatus.DI_MUON);
        } else {
            chamCong.setTrangThai(AttendanceStatus.CO_MAT);
        }

        chamCongRepository.save(chamCong);
        return "Thành công: Đã chấm công vào lúc " + LocalTime.now();
    }

    // --- HÀM 2: CHẤM CÔNG RA (CHECK-OUT) ---
    public String chamCongRa(String maNV) {
        LocalDate homNay = LocalDate.now();

        // 1. Tìm bản ghi chấm công của hôm nay
        Optional<ChamCong> chamCongOpt = chamCongRepository.findByNhanVien_MaNVAndNgayChamCong(maNV, homNay);

        if (chamCongOpt.isEmpty()) {
            return "Lỗi: Bạn chưa chấm công vào (Check-in) nên không thể chấm công ra!";
        }

        // 2. Cập nhật giờ ra
        ChamCong chamCong = chamCongOpt.get();
        chamCong.setGioRa(LocalTime.now());

        // (Tùy chọn) Tính toán giờ làm thêm hoặc cập nhật lại trạng thái nếu cần ở đây
        
        chamCongRepository.save(chamCong);
        return "Thành công: Đã chấm công ra lúc " + LocalTime.now();
    }

    public ChamCongRepository getChamCongRepository() {
        return chamCongRepository;
    }

    public void setChamCongRepository(ChamCongRepository chamCongRepository) {
        this.chamCongRepository = chamCongRepository;
    }

    public NhanVienRepository getNhanVienRepository() {
        return nhanVienRepository;
    }

    public void setNhanVienRepository(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }
}
