package com.hrm.quanlynhansu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrm.quanlynhansu.entity.BangLuong;
import com.hrm.quanlynhansu.repository.BangLuongRepository;
import com.hrm.quanlynhansu.service.BangLuongService;

@Controller
@RequestMapping("/bang-luong")
public class BangLuongController {

    @Autowired
    private BangLuongService bangLuongService;

    @Autowired
    private BangLuongRepository bangLuongRepository;

    // --- 1. HIỂN THỊ DANH SÁCH BẢNG LƯƠNG ---
    @GetMapping
    public String hienThiBangLuong(Model model) {
        // Lấy toàn bộ bảng lương trong database ra hiển thị
        // (Thực tế nên lọc theo tháng/năm, nhưng làm đơn giản trước)
        List<BangLuong> danhSach = bangLuongRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "nam", "thang"));
        model.addAttribute("dsBangLuong", danhSach);
        return "bang-luong"; // Trả về file bang-luong.html
    }

    // --- 2. TÍNH LƯƠNG CHO THÁNG (KHI BẤM NÚT) ---
    @PostMapping("/tinh-toan")
    public String tinhLuong(@RequestParam("thang") int thang,
                            @RequestParam("nam") int nam,
                            RedirectAttributes redirectAttributes) {
        try {
            // Gọi Service để tính toán cho tất cả nhân viên
            bangLuongService.tinhLuongChoThang(thang, nam);
            redirectAttributes.addFlashAttribute("thongBao", "Thành công: Đã tính lương xong cho tháng " + thang + "/" + nam);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBao", "Lỗi: " + e.getMessage());
        }
        return "redirect:/bang-luong";
    }
}