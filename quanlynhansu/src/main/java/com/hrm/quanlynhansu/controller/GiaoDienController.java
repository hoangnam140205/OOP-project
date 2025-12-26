package com.hrm.quanlynhansu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.entity.NhanVienFullTime;
import com.hrm.quanlynhansu.entity.NhanVienPartTime;
import com.hrm.quanlynhansu.entity.PhongBan;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.PhongBanRepository;

@Controller
public class GiaoDienController {

    @Autowired
    private NhanVienRepository repoNV;

    @Autowired
    private PhongBanRepository repoPB;

    // 1. TRANG CHỦ
    @GetMapping("/")
    public String trangChu(@RequestParam(required = false) String tuKhoa, Model model) {
        List<NhanVien> ds;
        if (tuKhoa != null && !tuKhoa.isEmpty()) {
            ds = repoNV.findByHoTenContainingOrMaNVContaining(tuKhoa, tuKhoa);
        } else {
            ds = repoNV.findAll();
        }
        model.addAttribute("danhSachNhanVien", ds);
        model.addAttribute("tuKhoa", tuKhoa);
        return "danh-sach";
    }

    // 2. MỞ FORM THÊM
    @GetMapping("/them-moi")
    public String hienThiFormThem(Model model) {
        model.addAttribute("danhSachPhongBan", repoPB.findAll());
        model.addAttribute("nhanVien", new NhanVienFullTime());
        model.addAttribute("isEdit", false);
        return "them-nhan-vien";
    }

    // 3. XỬ LÝ LƯU (Đã sửa lỗi Duplicate entry và thêm logic Update)
    @PostMapping("/luu")
    @SuppressWarnings("CallToPrintStackTrace")
    public String xuLyLuu(@RequestParam String maNV,
                          @RequestParam String hoTen,
                          @RequestParam String soDienThoai,
                          @RequestParam String maPhong,
                          @RequestParam String loaiNhanVien,
                          @RequestParam(required = false) String heSoLuongStr,
                          @RequestParam(required = false) String luongCoBanStr,
                          @RequestParam(required = false) String chucVu,
                          @RequestParam(required = false) String soGioLamStr,
                          @RequestParam(required = false) String luongTheoGioStr,
                          Model model) { // Thêm Model để báo lỗi

        try {
            PhongBan pb = repoPB.findById(maPhong).orElse(null);

            // Chuyển đổi dữ liệu an toàn
            Double heSoLuong = chuyenDoiSo(heSoLuongStr);
            Double luongCoBan = chuyenDoiSo(luongCoBanStr);
            Double luongTheoGio = chuyenDoiSo(luongTheoGioStr);
            Integer soGioLam = 0;
            try {
                if (soGioLamStr != null && !soGioLamStr.isEmpty()) soGioLam = Integer.valueOf(soGioLamStr);
            } catch (NumberFormatException e) { soGioLam = 0; }

            if (repoNV.existsById(maNV)) {
                
            }

            if ("FULL_TIME".equals(loaiNhanVien)) {
                NhanVienFullTime ft = new NhanVienFullTime();
                ft.setMaNV(maNV);
                ft.setHoTen(hoTen);
                ft.setSoDienThoai(soDienThoai);
                ft.setPhongBan(pb);
                ft.setHeSoLuong(heSoLuong);
                ft.setLuongCoBan(luongCoBan);
                ft.setChucVu(chucVu);
                repoNV.save(ft);
            } else {
                NhanVienPartTime pt = new NhanVienPartTime();
                pt.setMaNV(maNV);
                pt.setHoTen(hoTen);
                pt.setSoDienThoai(soDienThoai);
                pt.setPhongBan(pb);
                pt.setSoGioLam(soGioLam);
                pt.setLuongTheoGio(luongTheoGio);
                repoNV.save(pt);
            }

            return "redirect:/";

        } catch (DataIntegrityViolationException e) {
            // BẮT LỖI TRÙNG MÃ NHÂN VIÊN TẠI ĐÂY
            e.printStackTrace();
            model.addAttribute("error", "Lỗi: Mã nhân viên " + maNV + " đã tồn tại hoặc dữ liệu không hợp lệ!");
            model.addAttribute("danhSachPhongBan", repoPB.findAll()); // Load lại DS phòng ban
            
            return "them-nhan-vien"; 
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đã xảy ra lỗi không xác định: " + e.getMessage());
            model.addAttribute("danhSachPhongBan", repoPB.findAll());
            return "them-nhan-vien";
        }
    }

    // Hàm phụ trợ để chuyển đổi String -> Double an toàn
    private Double chuyenDoiSo(String str) {
        try {
            if (str != null && !str.isEmpty()) {
                return Double.valueOf(str);
            }
        } catch (NumberFormatException e) {
            // Log error if needed
        }
        return 0.0;
    }

    // 4. XÓA
    @GetMapping("/xoa-nhan-vien")
    public String xuLyXoaNhanVien(@RequestParam String maNV) {
        repoNV.deleteById(maNV);
        return "redirect:/";
    }

    // 5. MỞ FORM SỬA
    @GetMapping("/sua-nhan-vien")
    public String hienThiFormSua(@RequestParam String maNV, Model model) {
        NhanVien nv = repoNV.findById(maNV).orElse(null);
        if (nv == null) return "redirect:/";

        model.addAttribute("nv", nv);
        model.addAttribute("danhSachPhongBan", repoPB.findAll());
        
        // Gửi Mã NV ra để form hiển thị (quan trọng)
        model.addAttribute("maNV", nv.getMaNV());
        model.addAttribute("hoTen", nv.getHoTen());
        model.addAttribute("soDienThoai", nv.getSoDienThoai());
        if(nv.getPhongBan() != null) {
             model.addAttribute("maPhong", nv.getPhongBan().getMaPhong());
        }

        // Java 17+ pattern matching switch
        switch (nv) {
            case NhanVienFullTime ft -> {
                model.addAttribute("loaiNhanVien", "FULL_TIME");
                model.addAttribute("heSoLuong", ft.getHeSoLuong());
                model.addAttribute("luongCoBan", ft.getLuongCoBan());
                model.addAttribute("chucVu", ft.getChucVu());
            }
            case NhanVienPartTime pt -> {
                model.addAttribute("loaiNhanVien", "PART_TIME");
                model.addAttribute("soGioLam", pt.getSoGioLam());
                model.addAttribute("luongTheoGio", pt.getLuongTheoGio());
            }
            default -> {}
        }

        model.addAttribute("isEdit", true);
        return "them-nhan-vien"; // Tái sử dụng form thêm để sửa (hoặc view sua-nhan-vien tùy bạn)
    }
}