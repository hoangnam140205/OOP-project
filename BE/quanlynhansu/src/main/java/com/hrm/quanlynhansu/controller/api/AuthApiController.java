package com.hrm.quanlynhansu.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://localhost:3000" })
public class AuthApiController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private AuditService auditService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        // Tìm nhân viên theo email trực tiếp
        NhanVien employee = nhanVienRepository.findAll().stream()
                .filter(e -> e.getEmail() != null && e.getEmail().equalsIgnoreCase(request.getEmail()))
                .findFirst()
                .orElse(null);

        if (employee == null) {
            // Ghi log thất bại
            auditService.logFailure(null, request.getEmail(), "LOGIN", "Email không tồn tại", httpRequest);
            return ResponseEntity.ok(new LoginResponse(false, "Email không tồn tại", null, null, null));
        }

        // Trong thực tế, bạn cần thêm trường password vào entity NhanVien
        // Hiện tại check mặc định
        boolean passwordMatch = false;
        if (request.getEmail().equalsIgnoreCase("admin@company.com") && request.getPassword().equals("admin123")) {
            passwordMatch = true;
        } else if (request.getPassword().equals("123456")) {
            passwordMatch = true;
        }

        if (!passwordMatch) {
            // Ghi log thất bại
            auditService.logFailure(employee.getMaNV(), employee.getHoTen(), "LOGIN", "Mật khẩu không đúng",
                    httpRequest);
            return ResponseEntity.ok(new LoginResponse(false, "Mật khẩu không đúng", null, null, null));
        }

        // Determine role - check if ADMIN or regular user
        String role = employee.getMaNV().equals("ADMIN") ? "admin" : "user";

        // Ghi log thành công
        auditService.logSuccess(employee.getMaNV(), employee.getHoTen(), role, "LOGIN", httpRequest);

        EmployeeDTO empDTO = new EmployeeDTO();
        empDTO.setId(employee.getMaNV());
        empDTO.setName(employee.getHoTen());
        empDTO.setEmail(request.getEmail());
        empDTO.setRole(role);

        return ResponseEntity.ok(new LoginResponse(true, "Đăng nhập thành công",
                employee.getMaNV(), role, empDTO));
    }

    // POST /api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request, HttpServletRequest httpRequest) {
        // Tìm nhân viên
        NhanVien employee = nhanVienRepository.findById(request.getEmployeeId()).orElse(null);

        if (employee != null) {
            String role = employee.getMaNV().equals("ADMIN") ? "admin" : "user";
            // Ghi log logout thành công
            auditService.logSuccess(employee.getMaNV(), employee.getHoTen(), role, "LOGOUT", httpRequest);
        }

        return ResponseEntity.ok(new LogoutResponse(true, "Đăng xuất thành công"));
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        // Validate input
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            auditService.logFailure(null, request.getEmail(), "REGISTER", "Email không được để trống", httpRequest);
            return ResponseEntity.ok(new LoginResponse(false, "Email không được để trống", null, null, null));
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            auditService.logFailure(null, request.getEmail(), "REGISTER", "Mật khẩu phải có ít nhất 6 ký tự",
                    httpRequest);
            return ResponseEntity.ok(new LoginResponse(false, "Mật khẩu phải có ít nhất 6 ký tự", null, null, null));
        }

        // Check if email already exists
        String searchName = request.getEmail().replace("@company.com", "");
        boolean exists = nhanVienRepository.findAll().stream()
                .anyMatch(e -> e.getHoTen().equalsIgnoreCase(searchName));

        if (exists) {
            auditService.logFailure(null, request.getEmail(), "REGISTER", "Email đã tồn tại", httpRequest);
            return ResponseEntity.ok(new LoginResponse(false, "Email đã tồn tại", null, null, null));
        }

        // Create new employee
        com.hrm.quanlynhansu.entity.NhanVienFullTime newEmployee = new com.hrm.quanlynhansu.entity.NhanVienFullTime();

        // Generate employee ID
        long count = nhanVienRepository.count();
        String newId = "NV" + String.format("%03d", count + 1);

        newEmployee.setMaNV(newId);
        newEmployee.setHoTen(request.getName() != null && !request.getName().trim().isEmpty()
                ? request.getName()
                : searchName);
        newEmployee.setSoDienThoai("");
        newEmployee.setNgayVaoLam(java.time.LocalDate.now());
        newEmployee.setChucVu("Nhân viên");
        newEmployee.setLuongCoBan(10000000.0);
        newEmployee.setHeSoLuong(1.5);

        // Save to database
        NhanVien saved = nhanVienRepository.save(newEmployee);

        // Log successful registration
        auditService.logSuccess(saved.getMaNV(), saved.getHoTen(), "user", "REGISTER", httpRequest);

        // Return success with employee info
        EmployeeDTO empDTO = new EmployeeDTO();
        empDTO.setId(saved.getMaNV());
        empDTO.setName(saved.getHoTen());
        empDTO.setEmail(request.getEmail());
        empDTO.setRole("user");

        return ResponseEntity.ok(new LoginResponse(true, "Đăng ký thành công",
                saved.getMaNV(), "user", empDTO));
    }

    // DTOs
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LogoutRequest {
        private String employeeId;

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }
    }

    public static class RegisterRequest {
        private String email;
        private String password;
        private String name;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class LoginResponse {
        private boolean success;
        private String message;
        private String employeeId;
        private String role;
        private EmployeeDTO employee;

        public LoginResponse(boolean success, String message, String employeeId, String role, EmployeeDTO employee) {
            this.success = success;
            this.message = message;
            this.employeeId = employeeId;
            this.role = role;
            this.employee = employee;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public EmployeeDTO getEmployee() {
            return employee;
        }

        public void setEmployee(EmployeeDTO employee) {
            this.employee = employee;
        }
    }

    public static class LogoutResponse {
        private boolean success;
        private String message;

        public LogoutResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class EmployeeDTO {
        private String id;
        private String name;
        private String email;
        private String role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
