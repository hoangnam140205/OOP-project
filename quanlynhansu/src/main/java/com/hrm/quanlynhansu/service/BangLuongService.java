package com.hrm.quanlynhansu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.quanlynhansu.entity.BangLuong;
import com.hrm.quanlynhansu.entity.ChamCong;
import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.entity.ThuongPhat;
import com.hrm.quanlynhansu.enums.LoaiThuongPhat;
import com.hrm.quanlynhansu.repository.BangLuongRepository;
import com.hrm.quanlynhansu.repository.ChamCongRepository;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.ThuongPhatRepository;

@Service
public class BangLuongService {

    @Autowired
    private BangLuongRepository bangLuongRepository;
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private ChamCongRepository chamCongRepository;
    @Autowired
    private ThuongPhatRepository thuongPhatRepository;

    public void tinhLuongChoThang(int thang, int nam) {
        List<NhanVien> dsNhanVien = nhanVienRepository.findAll();
        
        // Xác định ngày đầu tháng và cuối tháng để lấy dữ liệu chấm công/thưởng phạt
        LocalDate startDate = LocalDate.of(nam, thang, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        for (NhanVien nv : dsNhanVien) {
            // 1. Nếu đã tính rồi thì bỏ qua (hoặc bạn có thể chọn xóa đi tính lại)
            if (bangLuongRepository.existsByNhanVien_MaNVAndThangAndNam(nv.getMaNV(), thang, nam)) {
                continue; 
            }

            // 2. Lấy dữ liệu CHẤM CÔNG
            List<ChamCong> dsChamCong = chamCongRepository.findByNhanVien_MaNVAndNgayChamCongBetween(nv.getMaNV(), startDate, endDate);
            double soNgayDiLam = dsChamCong.size(); // Giả sử mỗi lần chấm công là 1 ngày
            // (Nếu muốn chính xác hơn cho Part-time thì phải cộng tổng giờ làm: gioRa - gioVao)
            
            // 3. Lấy dữ liệu THƯỞNG / PHẠT
            List<ThuongPhat> dsThuongPhat = thuongPhatRepository.findByNhanVien_MaNVAndNgayQuyetDinhBetween(nv.getMaNV(), startDate, endDate);
            double tienThuong = dsThuongPhat.stream().filter(tp -> tp.getLoai() == LoaiThuongPhat.THUONG).mapToDouble(ThuongPhat::getSoTien).sum();
            double tienPhat = dsThuongPhat.stream().filter(tp -> tp.getLoai() == LoaiThuongPhat.PHAT).mapToDouble(ThuongPhat::getSoTien).sum();

            // 4. TÍNH LƯƠNG CHÍNH
            // Tính tổng giờ làm từ các bản ghi chấm công (dành cho Part-time chính xác)
            double tongGioLam = dsChamCong.stream().mapToDouble(cc -> {
                if (cc.getGioVao() != null && cc.getGioRa() != null) {
                    return java.time.Duration.between(cc.getGioVao(), cc.getGioRa()).toMinutes() / 60.0;
                } else {
                    // Nếu thiếu giờ vào/ra, fallback là 4 giờ cho 1 ngày
                    return 4.0;
                }
            }).sum();

            Double luongChinhObj = nv.tinhLuongTheoThang(soNgayDiLam, tongGioLam);
            double luongChinh = (luongChinhObj != null) ? luongChinhObj : 0.0;

            // 5. TỔNG KẾT THỰC LÃNH
            double thucLanh = luongChinh + tienThuong - tienPhat;

            // 6. Lưu vào DB
            BangLuong bl = new BangLuong();
            bl.setNhanVien(nv);
            bl.setThang(thang);
            bl.setNam(nam);
            bl.setSoNgayCong(soNgayDiLam);
            bl.setLuongChinh(luongChinh);
            bl.setTongThuong(tienThuong);
            bl.setTongPhat(tienPhat);
            bl.setThucLanh(thucLanh);
            bl.setNgayTao(LocalDateTime.now());

            bangLuongRepository.save(bl);
        }
    }
}