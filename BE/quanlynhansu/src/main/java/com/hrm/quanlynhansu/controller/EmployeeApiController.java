package com.hrm.quanlynhansu.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Import cho hàm xóa
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.entity.NhanVienFullTime;
import com.hrm.quanlynhansu.entity.NhanVienPartTime;
import com.hrm.quanlynhansu.entity.PhongBan;
import com.hrm.quanlynhansu.repository.BangLuongRepository;
import com.hrm.quanlynhansu.repository.ChamCongRepository;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.PhongBanRepository;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeApiController {

    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private PhongBanRepository phongBanRepository;

    // Repository phụ trợ để xóa dữ liệu liên quan
    @Autowired
    private BangLuongRepository bangLuongRepository;
    @Autowired
    private ChamCongRepository chamCongRepository;

    // 1. LẤY DANH SÁCH NHÂN VIÊN
    @GetMapping
    public List<Map<String, Object>> getAllEmployees() {
        return nhanVienRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 1.5 LẤY 1 NHÂN VIÊN THEO ID (FIX CHO USER PROFILE)
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {
        NhanVien nv = nhanVienRepository.findById(id).orElse(null);
        if (nv == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(nv));
    }

    // 2. LẤY PHÒNG BAN (Dropdown)
    @GetMapping("/departments")
    public List<PhongBan> getAllDepartments() {
        return phongBanRepository.findAll();
    }

    // 3. TẠO MỚI NHÂN VIÊN
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Map<String, Object> body) {
        try {
            String type = "FULL_TIME";
            if (body.get("fullTimeType") != null)
                type = body.get("fullTimeType").toString();
            if (body.get("loaiNhanVien") != null)
                type = body.get("loaiNhanVien").toString();

            NhanVien nv;
            if ("PART_TIME".equalsIgnoreCase(type) || "Thời vụ".equals(type)) {
                nv = new NhanVienPartTime();
            } else {
                nv = new NhanVienFullTime();
            }

            updateEntityFromMap(nv, body);

            if (nv.getPassword() == null)
                nv.setPassword("123456");
            if (nv.getRole() == null)
                nv.setRole("user");

            nhanVienRepository.save(nv);
            return ResponseEntity.ok(convertToDTO(nv));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi tạo nhân viên: " + e.getMessage());
        }
    }

    // 4. CẬP NHẬT NHÂN VIÊN
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody Map<String, Object> body) {
        NhanVien nv = nhanVienRepository.findById(id).orElse(null);
        if (nv == null)
            return ResponseEntity.notFound().build();

        try {
            updateEntityFromMap(nv, body);
            nhanVienRepository.save(nv);
            return ResponseEntity.ok(convertToDTO(nv));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi cập nhật: " + e.getMessage());
        }
    }

    // 5. XÓA NHÂN VIÊN (Đã sửa lỗi Transactional)
    @DeleteMapping("/{id}")
    @Transactional // ✅ Bắt buộc có để xóa nhiều bảng
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        if (!nhanVienRepository.existsById(id))
            return ResponseEntity.notFound().build();

        try {
            // Xóa dữ liệu liên quan trước (Lương & Chấm công)
            if (bangLuongRepository != null)
                bangLuongRepository.deleteByNhanVien_MaNV(id);
            if (chamCongRepository != null)
                chamCongRepository.deleteByNhanVien_MaNV(id);

            // Sau đó xóa nhân viên
            nhanVienRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    // --- HÀM HỖ TRỢ MAP DỮ LIỆU ---
    private void updateEntityFromMap(NhanVien nv, Map<String, Object> body) {
        if (body.get("maNV") != null)
            nv.setMaNV(body.get("maNV").toString());
        else if (body.get("code") != null)
            nv.setMaNV(body.get("code").toString());

        if (body.get("hoTen") != null)
            nv.setHoTen(body.get("hoTen").toString());
        else if (body.get("name") != null)
            nv.setHoTen(body.get("name").toString());

        if (body.get("email") != null)
            nv.setEmail(body.get("email").toString());

        if (body.get("soDienThoai") != null)
            nv.setSoDienThoai(body.get("soDienThoai").toString());
        else if (body.get("phone") != null)
            nv.setSoDienThoai(body.get("phone").toString());

        // XỬ LÝ LƯƠNG (QUAN TRỌNG: Full-time lưu vào luongCoBan, Part-time lưu vào
        // luongTheoGio)
        Double luongInput = 0.0;
        if (body.get("luongCoBan") != null)
            luongInput = Double.valueOf(body.get("luongCoBan").toString());
        else if (body.get("baseSalary") != null)
            luongInput = Double.valueOf(body.get("baseSalary").toString());
        else if (body.get("luong") != null)
            luongInput = Double.valueOf(body.get("luong").toString());

        if (nv instanceof NhanVienPartTime) {
            nv.setLuongTheoGio(luongInput); // Lưu vào lương theo giờ
            nv.setLuongCoBan(0.0);
        } else {
            nv.setLuongCoBan(luongInput); // Lưu vào lương cơ bản
            nv.setLuongTheoGio(0.0);
        }

        // CHỨC VỤ
        Object chucVu = body.get("chucVu");
        if (chucVu == null)
            chucVu = body.get("position");
        if (chucVu == null)
            chucVu = body.get("title");
        if (chucVu != null)
            nv.setChucVu(chucVu.toString());

        // XỬ LÝ PHÒNG BAN (FIX LỖI KHÔNG LƯU ĐƯỢC)
        Object pbId = body.get("maPhong");
        if (pbId == null)
            pbId = body.get("departmentId");

        if (pbId != null && !pbId.toString().isEmpty()) {
            PhongBan pb = phongBanRepository.findById(pbId.toString()).orElse(null);
            nv.setPhongBan(pb);
        } else {
            nv.setPhongBan(null);
        }

        // NGÀY
        if (body.get("ngaySinh") != null && !body.get("ngaySinh").toString().isEmpty()) {
            nv.setNgaySinh(LocalDate.parse(body.get("ngaySinh").toString()));
        }
    }

    private Map<String, Object> convertToDTO(NhanVien nv) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nv.getMaNV());
        map.put("code", nv.getMaNV());
        map.put("name", nv.getHoTen());
        map.put("email", nv.getEmail());
        map.put("phone", nv.getSoDienThoai());

        if (nv.getPhongBan() != null) {
            map.put("department", nv.getPhongBan().getTenPhong());
            map.put("departmentId", nv.getPhongBan().getMaPhong());
        } else {
            map.put("department", "Chưa phân bổ");
            map.put("departmentId", "");
        }

        map.put("position", nv.getChucVu());
        map.put("chucVu", nv.getChucVu());
        map.put("title", nv.getChucVu() != null ? nv.getChucVu() : "Nhân viên");

        // FIX LỖI HIỂN THỊ LƯƠNG: Nếu lương cơ bản null/0 thì lấy lương theo giờ
        Double luong = nv.getLuongCoBan();
        if (luong == null || luong == 0) {
            luong = nv.getLuongTheoGio(); // Lấy lương Part-time
        }

        map.put("baseSalary", luong != null ? luong : 0.0);
        map.put("luong", luong != null ? luong : 0.0);

        map.put("role", nv.getRole());

        // === THÊM CÁC TRƯỜNG MỚI CHO USER PROFILE ===
        map.put("manager", nv.getQuanLy() != null ? nv.getQuanLy() : "—");
        map.put("location", nv.getViTri() != null ? nv.getViTri() : "Văn phòng chính");
        map.put("address", nv.getDiaChi() != null ? nv.getDiaChi() : "");
        map.put("dob", nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
        map.put("startDate", nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().toString() : "");

        return map;
    }
}