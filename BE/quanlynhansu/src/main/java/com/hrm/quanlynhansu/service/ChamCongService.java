package com.hrm.quanlynhansu.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.quanlynhansu.entity.ChamCong;
import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.repository.ChamCongRepository;
import com.hrm.quanlynhansu.repository.NhanVienRepository;

@Service
public class ChamCongService {

    @Autowired
    private ChamCongRepository chamCongRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    // Giờ quy định đi làm (Ví dụ 8:00 sáng)
    private static final LocalTime GIO_BAT_DAU = LocalTime.of(8, 0);

    // XỬ LÝ CHECK-IN
    public String chamCongVao(String maNV) {
        // 1. Kiểm tra nhân viên có tồn tại không
        NhanVien nv = nhanVienRepository.findById(maNV).orElse(null);
        if (nv == null) {
            return "Lỗi: Không tìm thấy nhân viên có mã " + maNV;
        }

        LocalDate homNay = LocalDate.now();

        // 2. Kiểm tra xem đã chấm công hôm nay chưa
        if (chamCongRepository.existsByNhanVien_MaNVAndNgayChamCong(maNV, homNay)) {
            return "Bạn đã chấm công vào ngày hôm nay rồi!";
        }

        // 3. Tạo phiếu chấm công mới
        ChamCong cc = new ChamCong();
        cc.setNhanVien(nv);
        cc.setNgayChamCong(homNay);
        cc.setGioVao(LocalTime.now());
        
        // 4. Kiểm tra đi muộn hay đúng giờ (SỬA LỖI TẠI ĐÂY)
        if (LocalTime.now().isAfter(GIO_BAT_DAU)) {
            // Thay vì dùng Enum, ta dùng String "late"
            cc.setTrangThai("late"); 
        } else {
            // Thay vì dùng Enum, ta dùng String "ontime"
            cc.setTrangThai("ontime"); 
        }

        chamCongRepository.save(cc);
        
        // Trả về thông báo (Dựa vào trạng thái vừa lưu)
        String trangThaiViet = cc.getTrangThai().equals("late") ? "Đi muộn" : "Đúng giờ";
        return "Check-in thành công! (" + trangThaiViet + ")";
    }

    // XỬ LÝ CHECK-OUT
    public String chamCongRa(String maNV) {
        LocalDate homNay = LocalDate.now();

        // 1. Tìm bản ghi chấm công hôm nay
        Optional<ChamCong> opCC = chamCongRepository.findByNhanVien_MaNVAndNgayChamCong(maNV, homNay);

        if (opCC.isEmpty()) {
            return "Lỗi: Bạn chưa Check-in hôm nay!";
        }

        ChamCong cc = opCC.get();
        cc.setGioRa(LocalTime.now());

        // Tính số giờ làm thêm nếu cần (Tạm thời để logic đơn giản)
        // cc.setSoGioLamThem(...);

        chamCongRepository.save(cc);
        return "Check-out thành công lúc " + LocalTime.now().toString().substring(0, 5);
    }
}