package com.hrm.quanlynhansu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrm.quanlynhansu.entity.BangLuong;
import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.entity.QuyLuong;
import com.hrm.quanlynhansu.repository.BangLuongRepository;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.QuyLuongRepository;

@RestController
@RequestMapping("/api/payrolls")
@CrossOrigin(origins = "http://localhost:5173")
public class PayrollApiController {

    @Autowired private BangLuongRepository repoBL;
    @Autowired private QuyLuongRepository repoQL;
    @Autowired private NhanVienRepository repoNV;

    // 1. LẤY DỮ LIỆU
    @GetMapping
    public Map<String, Object> getPayrollData(@RequestParam(required = false, defaultValue = "1") int month, 
                                              @RequestParam(required = false, defaultValue = "2025") int year) {
        List<BangLuong> list = repoBL.findByThangAndNam(month, year);
        QuyLuong ql = repoQL.findByThangAndNam(month, year);
        
        Double budget = (ql != null && ql.getNganSach() != null) ? ql.getNganSach() : 0.0;
        
        // Tính tổng đã trả (PAID)
        double totalPaid = list.stream()
                .filter(b -> "PAID".equals(b.getTrangThai()))
                .mapToDouble(b -> b.getThucLanh() != null ? b.getThucLanh() : 0.0)
                .sum();
                
        // Tính tổng chờ trả (PENDING)
        double totalPending = list.stream()
                .filter(b -> !"PAID".equals(b.getTrangThai()))
                .mapToDouble(b -> b.getThucLanh() != null ? b.getThucLanh() : 0.0)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("list", list.stream().map(this::convertToDTO).collect(Collectors.toList()));
        response.put("budget", budget);
        response.put("totalPaid", totalPaid);
        response.put("totalPending", totalPending);
        return response;
    }

    // 1.5 LẤY LƯƠNG THEO NHÂN VIÊN (Cho trang User xem lương)
    @GetMapping("/employee/{employeeId}")
    public List<Map<String, Object>> getPayrollsByEmployee(@PathVariable String employeeId) {
        List<BangLuong> list = repoBL.findByNhanVien_MaNV(employeeId);
        return list.stream().map(bl -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", String.valueOf(bl.getId()));
            map.put("employeeId", employeeId);
            // Format month as YYYY-MM
            String month = bl.getNam() + "-" + (bl.getThang() < 10 ? "0" + bl.getThang() : bl.getThang());
            map.put("month", month);
            map.put("baseSalary", bl.getLuongChinh() != null ? bl.getLuongChinh() : 0.0);
            map.put("bonus", bl.getTongThuong() != null ? bl.getTongThuong() : 0.0);
            map.put("allowance", 0.0); // Không có trong database, trả về 0
            map.put("insurance", 0.0); // Không có trong database, trả về 0
            map.put("pit", bl.getTongPhat() != null ? bl.getTongPhat() : 0.0);
            map.put("status", bl.getTrangThai() != null ? bl.getTrangThai().toLowerCase() : "pending");
            map.put("payDate", ""); // Không có trong database
            return map;
        }).collect(Collectors.toList());
    }

    // 2. THÊM MỚI NHÂN VIÊN VÀO BẢNG LƯƠNG
    @PostMapping
    public ResponseEntity<?> createPayroll(@RequestBody Map<String, Object> body) {
        try {
            // Lấy ID an toàn
            Object empIdObj = body.get("employeeId");
            if (empIdObj == null) return ResponseEntity.badRequest().body("Thiếu ID nhân viên");
            String maNV = String.valueOf(empIdObj);

            int month = Integer.parseInt(body.get("month").toString());
            int year = Integer.parseInt(body.get("year").toString());

            NhanVien nv = repoNV.findById(maNV).orElse(null);
            if (nv == null) return ResponseEntity.badRequest().body("Không tìm thấy nhân viên!");

            // Kiểm tra trùng
            BangLuong exist = repoBL.findByThangAndNam(month, year).stream()
                .filter(b -> b.getNhanVien().getMaNV().equals(maNV))
                .findFirst().orElse(null);
            
            if (exist != null) return ResponseEntity.badRequest().body("Nhân viên này đã có trong bảng lương tháng " + month + "/" + year);

            // Tạo mới
            BangLuong bl = new BangLuong();
            bl.setNhanVien(nv);
            bl.setThang(month);
            bl.setNam(year);
            bl.setTrangThai("PENDING");

            // Xử lý lương (phòng null)
            double luongCoBan = 0.0;
            if (body.get("baseSalary") != null) {
                luongCoBan = Double.parseDouble(body.get("baseSalary").toString());
            } else if (nv.getLuongCoBan() != null) {
                luongCoBan = nv.getLuongCoBan();
            }
            
            double thuong = body.get("bonus") != null ? Double.parseDouble(body.get("bonus").toString()) : 0.0;
            double phat = body.get("pit") != null ? Double.parseDouble(body.get("pit").toString()) : 0.0;

            bl.setLuongChinh(luongCoBan);
            bl.setTongThuong(thuong);
            bl.setTongPhat(phat);
            bl.setThucLanh(luongCoBan + thuong - phat);
            
            // Set mặc định để tránh lỗi database null
            bl.setSoNgayCong(26.0); 
            bl.setSoGioLam(0.0);    

            repoBL.save(bl);
            return ResponseEntity.ok("Thêm nhân viên thành công");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }

    // 3. CẬP NHẬT QUỸ LƯƠNG
    @PostMapping("/budget")
    public ResponseEntity<?> setBudget(@RequestBody Map<String, Object> body) {
        try {
            int month = Integer.parseInt(body.get("month").toString());
            int year = Integer.parseInt(body.get("year").toString());
            Double amount = Double.parseDouble(body.get("amount").toString());

            QuyLuong ql = repoQL.findByThangAndNam(month, year);
            if (ql == null) {
                ql = new QuyLuong();
                ql.setThang(month);
                ql.setNam(year);
            }
            ql.setNganSach(amount);
            repoQL.save(ql);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật quỹ lương");
        }
    }

    // 4. THANH TOÁN TẤT CẢ
    @PostMapping("/pay-all")
    public ResponseEntity<?> payAll(@RequestBody Map<String, Object> body) {
        int month = Integer.parseInt(body.get("month").toString());
        int year = Integer.parseInt(body.get("year").toString());
        List<BangLuong> list = repoBL.findByThangAndNam(month, year);
        QuyLuong ql = repoQL.findByThangAndNam(month, year);
        
        double totalNeeded = list.stream().filter(b -> !"PAID".equals(b.getTrangThai()))
                                 .mapToDouble(b -> b.getThucLanh() != null ? b.getThucLanh() : 0.0).sum();
        double currentPaid = list.stream().filter(b -> "PAID".equals(b.getTrangThai()))
                                 .mapToDouble(b -> b.getThucLanh() != null ? b.getThucLanh() : 0.0).sum();
        double budget = (ql != null && ql.getNganSach() != null) ? ql.getNganSach() : 0.0;

        if (currentPaid + totalNeeded > budget) {
            return ResponseEntity.badRequest().body("Vượt quá quỹ lương! Cần thêm: " + (currentPaid + totalNeeded - budget));
        }

        for (BangLuong bl : list) {
            if (!"PAID".equals(bl.getTrangThai())) {
                bl.setTrangThai("PAID");
                repoBL.save(bl);
            }
        }
        return ResponseEntity.ok("Done");
    }

    // 5. CẬP NHẬT 1 BẢN GHI
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayroll(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BangLuong bl = repoBL.findById(id).orElse(null);
        if (bl == null) return ResponseEntity.notFound().build();

        if (body.get("month") != null) {
            String[] parts = body.get("month").toString().split("-");
            bl.setNam(Integer.parseInt(parts[0]));
            bl.setThang(Integer.parseInt(parts[1]));
        }
        if (body.get("status") != null) bl.setTrangThai(body.get("status").toString());
        
        if (body.get("baseSalary") != null) bl.setLuongChinh(Double.parseDouble(body.get("baseSalary").toString()));
        if (body.get("bonus") != null) bl.setTongThuong(Double.parseDouble(body.get("bonus").toString()));
        if (body.get("pit") != null) bl.setTongPhat(Double.parseDouble(body.get("pit").toString()));

        double luong = bl.getLuongChinh() != null ? bl.getLuongChinh() : 0.0;
        double thuong = bl.getTongThuong() != null ? bl.getTongThuong() : 0.0;
        double phat = bl.getTongPhat() != null ? bl.getTongPhat() : 0.0;
        
        bl.setThucLanh(luong + thuong - phat);

        repoBL.save(bl);
        return ResponseEntity.ok(convertToDTO(bl));
    }

    private Map<String, Object> convertToDTO(BangLuong bl) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", bl.getId());
        if (bl.getNhanVien() != null) {
            map.put("employeeName", bl.getNhanVien().getHoTen());
            map.put("employeeId", bl.getNhanVien().getMaNV());
        }
        map.put("month", bl.getNam() + "-" + (bl.getThang() < 10 ? "0" + bl.getThang() : bl.getThang()));
        map.put("baseSalary", bl.getLuongChinh());
        map.put("bonus", bl.getTongThuong());
        map.put("pit", bl.getTongPhat());
        map.put("total", bl.getThucLanh());
        map.put("status", bl.getTrangThai() != null ? bl.getTrangThai() : "PENDING");
        return map;
    }
}
