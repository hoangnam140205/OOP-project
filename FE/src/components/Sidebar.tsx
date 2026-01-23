import { NavLink } from "react-router-dom";
import { BarChart3, DollarSign, Users, ClipboardCheck, User } from "lucide-react";

export default function Sidebar({ mode }: { mode: "admin" | "user" }) {
  return (
    <aside className="sidebar">
      <div className="brand">
        {mode === "admin" ? "Quản lý nhân viên" : "Cổng thông tin nhân viên"}
        <small>{mode === "admin" ? "Dashboard Admin" : "Dashboard cá nhân"}</small>
      </div>

      <div className="nav">
        {mode === "user" ? (
          <>
            <NavLink to="/me/profile" className={({ isActive }) => isActive ? "active" : ""}>
              <User size={18} /> Thông tin cá nhân
            </NavLink>
            <NavLink to="/me/salary" className={({ isActive }) => isActive ? "active" : ""}>
              <DollarSign size={18} /> Lương & Thu nhập
            </NavLink>
            <NavLink to="/me/attendance" className={({ isActive }) => isActive ? "active" : ""}>
              <ClipboardCheck size={18} /> Chấm công
            </NavLink>
          </>
        ) : (
          <>
            <NavLink to="/admin/employees" className={({ isActive }) => isActive ? "active" : ""}>
              <Users size={18} /> Quản lý nhân viên
            </NavLink>
            <NavLink to="/admin/payroll" className={({ isActive }) => isActive ? "active" : ""}>
              <DollarSign size={18} /> Lương thưởng
            </NavLink>
            <NavLink to="/admin/attendance" className={({ isActive }) => isActive ? "active" : ""}>
              <ClipboardCheck size={18} /> Chấm công
            </NavLink>
            <NavLink to="/admin/reports" className={({ isActive }) => isActive ? "active" : ""}>
              <BarChart3 size={18} /> Báo cáo & Thống kê
            </NavLink>
          </>
        )}
      </div>
    </aside>
  );
}
