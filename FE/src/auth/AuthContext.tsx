import React, { createContext, useContext, useMemo, useState } from "react";
import { fetchLogin, fetchRegister } from "../lib/api";

type Role = "admin" | "user";
type AuthUser = { employeeId: string; role: Role } | null;

type Ctx = {
  user: AuthUser;
  login: (email: string, password: string) => Promise<{ ok: boolean; msg?: string }>;
  register: (email: string, password: string, confirmPassword: string, name: string) => Promise<{ ok: boolean; msg?: string }>;
  logout: () => void;
};

const AuthContext = createContext<Ctx | null>(null);

// Helper to get/set auth from localStorage
function getAuth(): AuthUser {
  const stored = localStorage.getItem("hr_auth");
  return stored ? JSON.parse(stored) : null;
}

function setAuth(auth: AuthUser) {
  if (auth) {
    localStorage.setItem("hr_auth", JSON.stringify(auth));
  } else {
    localStorage.removeItem("hr_auth");
  }
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser>(() => getAuth());

  const api = useMemo<Ctx>(() => ({
    user,
    async login(email, password) {
      try {
        const response = await fetchLogin(email, password);
        if (response.success) {
          const auth = { employeeId: response.employeeId, role: response.role };
          setAuth(auth);
          setUser(auth);
          return { ok: true };
        } else {
          return { ok: false, msg: response.message || "Đăng nhập thất bại" };
        }
      } catch (error) {
        console.error("Login error:", error);
        return { ok: false, msg: error instanceof Error ? error.message : "Lỗi kết nối đến server" };
      }
    },
    async register(email, password, confirmPassword, name) {
      if (!email.trim()) return { ok: false, msg: "Email không được để trống" };
      if (!password.trim()) return { ok: false, msg: "Mật khẩu không được để trống" };
      if (password !== confirmPassword) return { ok: false, msg: "Mật khẩu xác nhận không khớp" };
      if (password.length < 6) return { ok: false, msg: "Mật khẩu phải ít nhất 6 ký tự" };

      try {
        const response = await fetchRegister(email, password, name);
        if (response.success) {
          const auth = { employeeId: response.employeeId, role: response.role };
          setAuth(auth);
          setUser(auth);
          return { ok: true };
        } else {
          return { ok: false, msg: response.message || "Đăng ký thất bại" };
        }
      } catch (error) {
        console.error("Register error:", error);
        return { ok: false, msg: error instanceof Error ? error.message : "Lỗi kết nối đến server" };
      }
    },
    logout() {
      setAuth(null);
      setUser(null);
    }
  }), [user]);

  return <AuthContext.Provider value={api}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
