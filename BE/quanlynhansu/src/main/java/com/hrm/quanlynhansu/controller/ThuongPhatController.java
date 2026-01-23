package com.hrm.quanlynhansu.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrm.quanlynhansu.entity.ThuongPhat;
import com.hrm.quanlynhansu.enums.LoaiThuongPhat;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.ThuongPhatRepository;
import com.hrm.quanlynhansu.service.ThuongPhatService;

@Controller
@RequestMapping("/thuong-phat")
public class ThuongPhatController {

    @Autowired
    private ThuongPhatRepository thuongPhatRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private ThuongPhatService thuongPhatService;

    // --- 1. HIỂN THỊ DANH SÁCH & FORM ---
    @GetMapping
    public String hienThiTrangThuongPhat(Model model) {
        // Lấy danh sách thưởng phạt để hiện bảng
        List<ThuongPhat> listTP = thuongPhatRepository.findAll();
        model.addAttribute("dsThuongPhat", listTP);

        // Lấy danh sách nhân viên để hiện trong Dropdown chọn người
        model.addAttribute("dsNhanVien", nhanVienRepository.findAll());
        
        return "thuong-phat"; // Trả về file HTML
    }

    // --- 2. XỬ LÝ THÊM MỚI ---
    @PostMapping("/luu")
    public String luuThuongPhat(@RequestParam("maNV") String maNV,
                                @RequestParam("loai") LoaiThuongPhat loai,
                                @RequestParam("soTien") Double soTien,
                                @RequestParam("lyDo") String lyDo,
                                @RequestParam("ngayQuyetDinh") LocalDate ngayQuyetDinh,
                                RedirectAttributes redirectAttributes) {
        
        String ketQua = thuongPhatService.themThuongPhat(maNV, loai, soTien, lyDo, ngayQuyetDinh);
        redirectAttributes.addFlashAttribute("thongBao", ketQua);
        
        return "redirect:/thuong-phat";
    }

    // --- 3. XỬ LÝ XÓA ---
    @GetMapping("/xoa/{id}")
    public String xoaThuongPhat(@PathVariable("id") Long id) {
        thuongPhatService.xoaThuongPhat(id);
        return "redirect:/thuong-phat";
    }
}
