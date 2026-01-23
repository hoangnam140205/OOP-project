import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { User } from "lucide-react";
import { useAuth } from "../auth/AuthContext";

export default function Login() {
  const { login, user } = useAuth();
  const nav = useNavigate();
  const [email, setEmail] = useState("admin@company.com");
  const [password, setPassword] = useState("admin123");
  const [err, setErr] = useState<string | null>(null);

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    const res = await login(email, password);
    if (!res.ok) return setErr(res.msg || "Đăng nhập thất bại");
    setErr(null);

    // Wait a bit for user state to update, then check role
    setTimeout(() => {
      const currentUser = JSON.parse(localStorage.getItem("hr_auth") || "null");
      if (currentUser?.role === "admin") {
        nav("/admin/employees");
      } else {
        nav("/me/profile");
      }
    }, 100);
  }

  return (
    <div className="loginBg">
      <div className="loginCard">
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div className="iconBubble" style={{ background: "#EEF2FF", width: 60, height: 60 }}>
            <User />
          </div>
        </div>

        <div className="loginTitle">Đăng nhập</div>
        <div className="loginSub">Hệ thống quản lý nhân viên</div>

        <form onSubmit={onSubmit}>
          <div className="label">Tên đăng nhập</div>
          <input className="input" value={email} onChange={(e) => setEmail(e.target.value)} />

          <div className="label">Mật khẩu</div>
          <input className="input" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

          {err ? <div style={{ color: "#d11a2a", marginTop: 10, fontSize: 13, fontWeight: 700 }}>{err}</div> : null}

          <button className="btnPrimary" style={{ width: "100%", marginTop: 14, justifyContent: "center" }}>
            Đăng nhập
          </button>
          <button
            type="button"
            className="btnSecondary"
            style={{ width: "100%", marginTop: 14, justifyContent: "center" }}
            onClick={() => nav("/register")}
          >
            Đăng kí
          </button>
        </form>
      </div>
    </div>
  );
}
