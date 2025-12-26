package com.hrm.quanlynhansu.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrm.quanlynhansu.entity.ChamCong;
import com.hrm.quanlynhansu.repository.ChamCongRepository;
import com.hrm.quanlynhansu.service.ChamCongService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cham-cong")
public class ChamCongController {

    @Autowired
    private ChamCongService chamCongService;

    @Autowired
    private ChamCongRepository chamCongRepository;

    // --- 1. HIỂN THỊ TRANG CHẤM CÔNG ---
    @GetMapping
    public String hienThiTrangChamCong(Model model) {
        // Lấy danh sách chấm công để hiển thị lịch sử
        List<ChamCong> danhSach = chamCongRepository.findAll();
        model.addAttribute("dsChamCong", danhSach);
        return "cham-cong"; // Trả về file giao diện cham-cong.html
    }

    // --- 2. XỬ LÝ CHECK-IN (ĐIỂM DANH VÀO) ---
    @PostMapping("/check-in")
    public String checkIn(@RequestParam("maNV") String maNV, RedirectAttributes redirectAttributes) {
        String ketQua = chamCongService.chamCongVao(maNV);
        // Gửi thông báo kết quả ra màn hình
        redirectAttributes.addFlashAttribute("thongBao", ketQua);
        return "redirect:/cham-cong";
    }

    // --- 3. XỬ LÝ CHECK-OUT (ĐIỂM DANH RA) ---
    @PostMapping("/check-out")
    public String checkOut(@RequestParam("maNV") String maNV, RedirectAttributes redirectAttributes) {
        String ketQua = chamCongService.chamCongRa(maNV);
        redirectAttributes.addFlashAttribute("thongBao", ketQua);
        return "redirect:/cham-cong";
    }

    // --- 4. XUẤT FILE EXCEL ---
    @GetMapping("/xuat-excel")
    public void xuatExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Bang_Cham_Cong.xlsx";
        response.setHeader(headerKey, headerValue);

        List<ChamCong> listChamCong = chamCongRepository.findAll();

        try ( // Tạo file Excel
            Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Chấm Công");
            
            // Tạo dòng tiêu đề (Header)
            Row rowHeader = sheet.createRow(0);
            rowHeader.createCell(0).setCellValue("ID");
            rowHeader.createCell(1).setCellValue("Mã NV");
            rowHeader.createCell(2).setCellValue("Ngày");
            rowHeader.createCell(3).setCellValue("Giờ Vào");
            rowHeader.createCell(4).setCellValue("Giờ Ra");
            rowHeader.createCell(5).setCellValue("Trạng Thái");
            
            // Ghi dữ liệu
            int rowIdx = 1;
            for (ChamCong cc : listChamCong) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(cc.getId());
                // Lưu ý: Đảm bảo class NhanVien có hàm getMaNV() (hoặc getMaNv)
                row.createCell(1).setCellValue(cc.getNhanVien().getMaNV());
                row.createCell(2).setCellValue(cc.getNgayChamCong().toString());
                row.createCell(3).setCellValue(cc.getGioVao() != null ? cc.getGioVao().toString() : "");
                row.createCell(4).setCellValue(cc.getGioRa() != null ? cc.getGioRa().toString() : "");
                row.createCell(5).setCellValue(cc.getTrangThai().toString());
            }
            
            workbook.write(response.getOutputStream());
        }
    }
}