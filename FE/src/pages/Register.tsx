import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { User } from "lucide-react";
import { useAuth } from "../auth/AuthContext";
export default function Register() {
  const { register } = useAuth();
  const nav = useNavigate();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [err, setErr] = useState<string | null>(null);
  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    const res = await register(email, password, confirmPassword, name);
    if (!res.ok) return setErr(res.msg || "Đăng ký thất bại");
    setErr(null);
    nav("/me/profile");
  }

  return (
    <div className="loginBg">
      <div className="registerCard">
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div className="iconBubble" style={{ background: "#EEF2FF", width: 60, height: 60 }}>
            <User />
          </div>
        </div>

        <div className="loginTitle">Đăng ký</div>
        <div className="loginSub">Hệ thống quản lý nhân viên</div>

        <form onSubmit={onSubmit}>
          <div className="label">Họ và tên</div>
          <input className="input" value={name} onChange={(e) => setName(e.target.value)} />

          <div className="label">Email</div>
          <input className="input" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />

          <div className="label">Mật khẩu</div>
          <input className="input" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

          <div className="label">Xác nhận mật khẩu</div>
          <input className="input" type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />

          {err ? <div style={{ color: "#d11a2a", marginTop: 10, fontSize: 13, fontWeight: 700 }}>{err}</div> : null}
          <button
            type="submit"
            className="btnSecondary"
            style={{ width: "100%", marginTop: 14, justifyContent: "center", cursor: "pointer" }}
          >
            Đăng kí
          </button>
        </form>
      </div>
    </div>
  );
}