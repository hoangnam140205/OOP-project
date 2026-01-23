import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ProtectedRoute from "./auth/ProtectedRoute";

import AdminLayout from "./layouts/AdminLayout";
import UserLayout from "./layouts/UserLayout";

import Employees from "./pages/admin/Employees";
import Payroll from "./pages/admin/Payroll";
import Attendance from "./pages/admin/Attendance";
import Reports from "./pages/admin/Reports";

import Profile from "./pages/user/Profile";
import Salary from "./pages/user/Salary";
import UserAttendance from "./pages/user/UserAttendance";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route
        path="/me/profile"
        element={
          <ProtectedRoute allow={["user"]}>
            <UserLayout><Profile /></UserLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/me/salary"
        element={
          <ProtectedRoute allow={["user"]}>
            <UserLayout><Salary /></UserLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/me/attendance"
        element={
          <ProtectedRoute allow={["user"]}>
            <UserLayout><UserAttendance /></UserLayout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/employees"
        element={
          <ProtectedRoute allow={["admin"]}>
            <AdminLayout><Employees /></AdminLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/payroll"
        element={
          <ProtectedRoute allow={["admin"]}>
            <AdminLayout><Payroll /></AdminLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/attendance"
        element={
          <ProtectedRoute allow={["admin"]}>
            <AdminLayout><Attendance /></AdminLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/reports"
        element={
          <ProtectedRoute allow={["admin"]}>
            <AdminLayout><Reports /></AdminLayout>
          </ProtectedRoute>
        }
      />

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}
