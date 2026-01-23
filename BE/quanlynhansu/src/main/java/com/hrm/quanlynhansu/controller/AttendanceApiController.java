package com.hrm.quanlynhansu.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hrm.quanlynhansu.entity.*;
import com.hrm.quanlynhansu.repository.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceApiController {

    @Autowired
    private ChamCongRepository repoCC;
    @Autowired
    private NhanVienRepository repoNV;

    // 1. API LẤY DANH SÁCH (Sửa lại logic để Debug)
    @GetMapping
    public List<Map<String, Object>> getAttendance(@RequestParam(required = false) String date) {
        // LOG RA MÀN HÌNH ĐỂ KIỂM TRA
        System.out.println(">>> API getAttendance được gọi!");
        System.out.println(">>> Tham số date nhận được: " + date);

        List<ChamCong> list;
        
        if (date == null || date.isEmpty() || date.equals("undefined")) {
            // Nếu không chọn ngày -> Lấy TOÀN BỘ (Để test xem có dữ liệu không)
            System.out.println(">>> Không có ngày -> Lấy tất cả");
            list = repoCC.findAll();
        } else {
            // Nếu có ngày -> Tìm theo ngày
            try {
                LocalDate searchDate = LocalDate.parse(date);
                list = repoCC.findByNgayChamCong(searchDate);
                System.out.println(">>> Tìm theo ngày " + searchDate + " -> Kết quả: " + list.size() + " dòng");
            } catch (Exception e) {
                System.out.println(">>> Lỗi định dạng ngày: " + e.getMessage());
                list = new ArrayList<>();
            }
        }
        
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 2. API CHO ĐƯỜNG DẪN /date/{date} (Phòng trường hợp React gọi kiểu này)
    @GetMapping("/date/{date}")
    public List<Map<String, Object>> getAttendanceByPath(@PathVariable String date) {
        return getAttendance(date); // Gọi lại hàm trên
    }

    // ... (Giữ nguyên phần @PostMapping createAttendance như cũ) ...
    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestBody Map<String, Object> body) {
        try {
            String maNV = (String) body.get("employeeId");
            String dateStr = (String) body.get("date");

            if (repoCC.existsByNhanVien_MaNVAndNgayChamCong(maNV, LocalDate.parse(dateStr))) {
                return ResponseEntity.badRequest().body("Nhân viên này đã chấm công ngày " + dateStr);
            }

            NhanVien nv = repoNV.findById(maNV).orElse(null);
            if (nv == null) return ResponseEntity.badRequest().body("Không tìm thấy nhân viên");

            ChamCong cc = new ChamCong();
            cc.setNhanVien(nv);
            cc.setNgayChamCong(LocalDate.parse(dateStr));
            
            if (body.get("checkIn") != null && !body.get("checkIn").toString().isEmpty()) 
                cc.setGioVao(LocalTime.parse(body.get("checkIn").toString()));
            
            if (body.get("checkOut") != null && !body.get("checkOut").toString().isEmpty()) 
                cc.setGioRa(LocalTime.parse(body.get("checkOut").toString()));
            
            String status = body.get("status") != null ? body.get("status").toString() : "ontime";
            // Map status từ tiếng Việt sang code (nếu cần)
            if (status.equalsIgnoreCase("Đúng giờ")) status = "ontime";
            else if (status.equalsIgnoreCase("Đi muộn")) status = "late";
            else if (status.equalsIgnoreCase("Vắng mặt")) status = "absent";
            cc.setTrangThai(status);

            if (body.get("hours") != null) {
                 cc.setSoGioLamThem(Double.valueOf(body.get("hours").toString()));
            }

            repoCC.save(cc);
            return ResponseEntity.ok(convertToDTO(cc));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }

    private Map<String, Object> convertToDTO(ChamCong cc) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", cc.getId());
        map.put("employeeId", cc.getNhanVien().getMaNV());
        map.put("employeeName", cc.getNhanVien().getHoTen());
        map.put("date", cc.getNgayChamCong().toString());
        map.put("checkIn", cc.getGioVao() != null ? cc.getGioVao().toString() : "");
        map.put("checkOut", cc.getGioRa() != null ? cc.getGioRa().toString() : "");
        
        double hours = 0;
        if (cc.getGioVao() != null && cc.getGioRa() != null) {
            long seconds = java.time.Duration.between(cc.getGioVao(), cc.getGioRa()).getSeconds();
            hours = (double) seconds / 3600;
        }
        map.put("hours", Math.round(hours * 10.0) / 10.0);
        map.put("status", cc.getTrangThai());
        return map;
    }
}