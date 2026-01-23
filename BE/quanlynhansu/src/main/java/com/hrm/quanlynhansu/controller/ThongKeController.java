package com.hrm.quanlynhansu.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hrm.quanlynhansu.repository.BangLuongRepository;
import com.hrm.quanlynhansu.repository.NhanVienRepository;

@Controller
@RequestMapping("/thong-ke")
public class ThongKeController {

    @Autowired
    private BangLuongRepository bangLuongRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @GetMapping
    public String hienThiBaoCao(Model model, 
                                @RequestParam(value = "thang", defaultValue = "12") int thang,
                                @RequestParam(value = "nam", defaultValue = "2025") int nam) {
        
        // 1. THỐNG KÊ LƯƠNG
        Double tongLuong = bangLuongRepository.tinhTongLuongThang(thang, nam);
        if (tongLuong == null) tongLuong = 0.0; // Tránh lỗi nếu chưa có dữ liệu

        // 2. THỐNG KÊ BIẾN ĐỘNG NHÂN SỰ (Tăng mới)
        LocalDate startMonth = LocalDate.of(nam, thang, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        
        long nhanVienMoi = nhanVienRepository.countByNgayVaoLamBetween(startMonth, endMonth);
        
        // Lấy tổng số nhân viên hiện tại để so sánh
        long tongNhanVien = nhanVienRepository.count();

        // 3. Gửi sang View
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        model.addAttribute("tongLuong", tongLuong);
        model.addAttribute("nhanVienMoi", nhanVienMoi);
        model.addAttribute("tongNhanVien", tongNhanVien);

        return "bao-cao";
    }
}