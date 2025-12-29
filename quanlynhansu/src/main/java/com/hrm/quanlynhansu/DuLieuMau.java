package com.hrm.quanlynhansu;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hrm.quanlynhansu.entity.NhanVien;
import com.hrm.quanlynhansu.entity.NhanVienFullTime;
import com.hrm.quanlynhansu.entity.NhanVienPartTime;
import com.hrm.quanlynhansu.entity.PhongBan;
import com.hrm.quanlynhansu.repository.NhanVienRepository;
import com.hrm.quanlynhansu.repository.PhongBanRepository;

@Configuration
public class DuLieuMau {

    // Danh sách tên mẫu để random cho phong phú
    private final String[] HO = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng"};
    private final String[] LOT = {"Văn", "Thị", "Đức", "Thành", "Ngọc", "Minh", "Quang", "Thu", "Gia", "Xuân"};
    private final String[] TEN = {"Hùng", "Lan", "Tuấn", "Mai", "Dũng", "Hương", "Hải", "Linh", "Sơn", "Hạnh", "Nam", "Trang"};

    private final Random random = new Random();

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner initDatabase(NhanVienRepository nhanVienRepository, 
                                   PhongBanRepository phongBanRepository) {
        return args -> {
            // Chỉ tạo dữ liệu khi Database còn trống
            if (phongBanRepository.count() == 0) {
                
                System.out.println("⏳ ĐANG TẠO DỮ LIỆU MẪU (30 NHÂN VIÊN)...");

                // 1. TẠO 4 PHÒNG BAN
                PhongBan pIT = new PhongBan();
                pIT.setMaPhong("IT");
                pIT.setTenPhong("Công Nghệ Thông Tin");
                PhongBan pHR = new PhongBan();
                pHR.setMaPhong("HR");
                pHR.setTenPhong("Nhân Sự");
                PhongBan pKT = new PhongBan();
                pKT.setMaPhong("KT");
                pKT.setTenPhong("Kế Toán");
                PhongBan pKD = new PhongBan();
                pKD.setMaPhong("KD");
                pKD.setTenPhong("Kinh Doanh");
                
                // Lưu phòng ban và cho vào 1 list để lát bốc random
                List<PhongBan> dsPhong = Arrays.asList(pIT, pHR, pKT, pKD);
                phongBanRepository.saveAll(dsPhong);

                // 2. CHẠY VÒNG LẶP TẠO 30 NHÂN VIÊN
                for (int i = 1; i <= 30; i++) {
                    String maNV = String.format("NV%03d", i); // Tạo mã NV001, NV002...
                    String hoTen = taoTenNgauNhien();
                    String email = "nv" + i + "@company.com";
                    String sdt = "090" + String.format("%07d", random.nextInt(9999999));
                    LocalDate ngaySinh = LocalDate.of(1985 + random.nextInt(15), 1 + random.nextInt(12), 1 + random.nextInt(28));
                    PhongBan phongRandom = dsPhong.get(random.nextInt(dsPhong.size())); // Random phòng

                    // Random loại nhân viên (70% là Fulltime, 30% là Parttime)
                    if (random.nextInt(10) < 7) {
                        // TẠO FULL-TIME
                        NhanVienFullTime nv = new NhanVienFullTime();
                        chuanBiDuLieuChung(nv, maNV, hoTen, sdt, email, ngaySinh, phongRandom);
                        
                        // Lương cứng từ 10tr - 30tr
                        double luongCB = 10000000 + (random.nextInt(20) * 1000000); 
                        nv.setLuongCoBan(luongCB);
                        nv.setHeSoLuong(1.0 + (random.nextInt(5) * 0.1)); // Hệ số 1.0 - 1.5
                        nv.setChucVu(random.nextBoolean() ? "Nhân viên" : "Chuyên viên");
                        
                        nhanVienRepository.save(nv);
                    } else {
                        // TẠO PART-TIME
                        NhanVienPartTime nv = new NhanVienPartTime();
                        chuanBiDuLieuChung(nv, maNV, hoTen, sdt, email, ngaySinh, phongRandom);
                        
                        // Lương giờ 30k - 100k
                        nv.setLuongTheoGio(30000.0 + (random.nextInt(14) * 5000));
                        
                        nhanVienRepository.save(nv);
                    }
                }
                System.out.println("✅ ĐÃ TẠO XONG 30 NHÂN VIÊN!");
            }
        };
    }

    // Hàm phụ trợ: Sinh tên tiếng Việt ngẫu nhiên
    private String taoTenNgauNhien() {
        return HO[random.nextInt(HO.length)] + " " + 
               LOT[random.nextInt(LOT.length)] + " " + 
               TEN[random.nextInt(TEN.length)];
    }

    // Hàm phụ trợ: Set các thông tin chung để đỡ viết lại code
    private void chuanBiDuLieuChung(NhanVien nv, String ma, String ten, String sdt, String email, LocalDate sinh, PhongBan phong) {
        nv.setMaNV(ma);
        nv.setHoTen(ten);
        nv.setSoDienThoai(sdt);
        nv.setEmail(email);
        nv.setNgaySinh(sinh);
        nv.setNgayVaoLam(LocalDate.now().minusMonths(random.nextInt(24))); // Vào làm từ 0-2 năm trước
        nv.setPhongBan(phong);
    }
}
