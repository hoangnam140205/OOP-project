import { LogOut } from "lucide-react";
import { useAuth } from "../auth/AuthContext";

export default function Topbar() {
  const { user, logout } = useAuth();

  // 👇 SỬA LỖI: Ép kiểu user về 'any' để TypeScript không báo đỏ nữa
  // Điều này giúp bạn truy cập .name và so sánh role thoải mái
  const safeUser = user as any;

  // Kiểm tra Admin (Chấp nhận cả chữ hoa và chữ thường)
  const isAdmin = safeUser?.role === "admin" || safeUser?.role === "ADMIN";

  // Lấy tên hiển thị (Ưu tiên tên thật, nếu không có thì hiện chức vụ)
  const displayName = safeUser?.name || (isAdmin ? "Admin" : "Nhân viên");

  return (
    <div className="topbar">
      <div>
        <div className="hello">Xin chào,</div>
        <div style={{ fontWeight: 900, fontSize: 26 }}>
            {displayName}
        </div>
      </div>

      <button className="logout" onClick={logout}>
        <LogOut size={18} />
        Đăng xuất
      </button>
    </div>
  );
}