package com.hrm.quanlynhansu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrm.quanlynhansu.entity.PhongBan;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.PhongBanRepository;

@Controller
public class PhongBanController {

    @Autowired
    private PhongBanRepository repoPB;

    @Autowired
    private NhanVienRepository repoNV;

    // 1. DANH SÁCH PHÒNG BAN
    @GetMapping("/phong-ban")
    public String danhSachPhongBan(Model model) {
        model.addAttribute("dsPhongBan", repoPB.findAll());
        model.addAttribute("phongBan", new PhongBan()); // Object rỗng cho form thêm mới
        model.addAttribute("isEdit", false);
        return "quan-ly-phong-ban";
    }

    // 2. LƯU (THÊM HOẶC SỬA)
    @PostMapping("/phong-ban/luu")
    public String luuPhongBan(PhongBan phongBan, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra trùng mã nếu là thêm mới (bạn có thể thêm logic existsById như bài Nhân Viên)
            repoPB.save(phongBan);
            redirectAttributes.addFlashAttribute("success", "Lưu phòng ban thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: Mã phòng ban có thể đã tồn tại!");
        }
        return "redirect:/phong-ban";
    }

    // 3. XÓA PHÒNG BAN (Có kiểm tra điều kiện)
    @GetMapping("/phong-ban/xoa")
    public String xoaPhongBan(@RequestParam String maPhong, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem phòng này có nhân viên không
        long soNhanVien = repoNV.countByPhongBan_MaPhong(maPhong);
        
        if (soNhanVien > 0) {
            redirectAttributes.addFlashAttribute("error", 
                "Không thể xóa! Phòng này đang có " + soNhanVien + " nhân viên. Vui lòng điều chuyển họ trước.");
        } else {
            repoPB.deleteById(maPhong);
            redirectAttributes.addFlashAttribute("success", "Đã xóa phòng ban!");
        }
        return "redirect:/phong-ban";
    }

    // 4. CHỌN SỬA
    @GetMapping("/phong-ban/sua")
    public String suaPhongBan(@RequestParam String maPhong, Model model) {
        model.addAttribute("dsPhongBan", repoPB.findAll());
        model.addAttribute("phongBan", repoPB.findById(maPhong).get());
        model.addAttribute("isEdit", true); // Bật chế độ sửa
        return "quan-ly-phong-ban";
    }

    // 5. XEM CHI TIẾT NHÂN VIÊN TRONG PHÒNG (Chức năng điều chuyển nằm ở đây)
    @GetMapping("/phong-ban/chi-tiet")
    public String chiTietPhongBan(@RequestParam String maPhong, Model model) {
        // Lấy thông tin phòng
        PhongBan pb = repoPB.findById(maPhong).orElse(null);
        model.addAttribute("phongBan", pb);

        // Lấy danh sách nhân viên thuộc phòng này
        model.addAttribute("dsNhanVien", repoNV.findByPhongBan_MaPhong(maPhong));
        
        return "chi-tiet-phong-ban";
    }
}
