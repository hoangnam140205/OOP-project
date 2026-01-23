import Card from "../../components/Card";
import Modal from "../../components/Modal";
import { useAuth } from "../../auth/AuthContext";
import { useEffect, useState } from "react";
import { Employee, Attendance, fetchEmployee, fetchAttendance, updateEmployee } from "../../lib/api";
import { Pencil } from "lucide-react";

export default function Profile() {
  const { user } = useAuth();
  const [me, setMe] = useState<Employee | null>(null);
  const [attendance, setAttendance] = useState<Attendance[]>([]);
  const [loading, setLoading] = useState(true);
  const [editOpen, setEditOpen] = useState(false);
  const [form, setForm] = useState<Employee | null>(null);

  async function loadProfile() {
    if (!user) return;
    try {
      setLoading(true);
      const [employeeData, attendanceData] = await Promise.all([
        fetchEmployee(user.employeeId),
        fetchAttendance()
      ]);
      setMe(employeeData);
      setAttendance(attendanceData.filter(a => a.employeeId === user.employeeId));
    } catch (err) {
      console.error("Error loading profile:", err);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProfile();
  }, [user]);

  const handleEdit = () => {
    if (me) {
      console.log('Opening edit with data:', me); // Debug log
      setForm({ ...me });
      setEditOpen(true);
    }
  };

  const handleClose = () => {
    setEditOpen(false);
    setForm(null); // Reset form when closing
  };

  const handleSave = async () => {
    if (!form) return;
    try {
      await updateEmployee(form.id, form);
      setEditOpen(false);
      await loadProfile();
    } catch (err) {
      alert("Lỗi cập nhật thông tin: " + (err instanceof Error ? err.message : "Unknown error"));
    }
  };

  if (loading || !me) {
    return <div style={{ textAlign: "center", padding: "40px", color: "#64748b" }}>Đang tải...</div>;
  }

  const thisMonth = new Date().toISOString().slice(0, 7);
  const monthAtt = attendance.filter(a => a.date.startsWith(thisMonth));
  const ontime = monthAtt.filter(a => a.status === "ontime").length;
  const late = monthAtt.filter(a => a.status === "late").length;
  const absent = monthAtt.filter(a => a.status === "absent").length;
  const leave = monthAtt.filter(a => a.status === "leave").length;

  return (
    <>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <div>
          <h1 className="h1">Thông tin cá nhân</h1>
          <p className="sub">Xem và quản lý thông tin của bạn</p>
        </div>
        <button className="btnPrimary" onClick={handleEdit}>
          <Pencil size={18} /> Chỉnh sửa
        </button>
      </div>

      <div className="card" style={{ marginTop: 16, display: "flex", gap: 16, alignItems: "center" }}>
        <div className="iconBubble" style={{ width: 64, height: 64, background: "#E8EDFF" }}>
          <span style={{ fontWeight: 900, color: "#503EF4" }}>👤</span>
        </div>
        <div>
          <div style={{ fontWeight: 900, fontSize: 26 }}>{me.name}</div>
          <div style={{ color: "#64748b", fontSize: 20 }}>{me.title}</div>
          <div style={{ marginTop: 6, color: "#64748b", fontSize: 18 }}>
            🏢 {me.department} &nbsp;&nbsp; 📅 Tham gia: {me.startDate.split("-").reverse().join("/")}
          </div>
        </div>
      </div>

      <div className="grid2">
        <Card title="Thông tin cơ bản">
          <div className="kv">
            <div>
              <div className="row"><span>Mã nhân viên</span><b>{me.code}</b></div>
              <div className="row"><span>Email</span><b>{me.email}</b></div>
              <div className="row"><span>Số điện thoại</span><b>{me.phone}</b></div>
              <div className="row"><span>Ngày sinh</span><b>{me.dob ? me.dob.split("-").reverse().join("/") : "—"}</b></div>
              <div className="row"><span>Địa chỉ</span><b>{me.address || "—"}</b></div>
            </div>
          </div>
        </Card>

        <Card title="Thông tin công việc">
          <div className="kv">
            <div>
              <div className="row"><span>Chức vụ</span><b>{me.title}</b></div>
              <div className="row"><span>Phòng ban</span><b>{me.department}</b></div>
              <div className="row"><span>Quản lý trực tiếp</span><b>{me.manager}</b></div>
              <div className="row"><span>Vị trí làm việc</span><b>{me.location}</b></div>
              <div className="row"><span>Ngày bắt đầu</span><b>{me.startDate.split("-").reverse().join("/")}</b></div>
            </div>
          </div>
        </Card>
      </div>

      <div className="card" style={{ marginTop: 16 }}>
        <div style={{ fontWeight: 900, fontSize: 26 }}>Thống kê chấm công tháng này</div>
        <div className="grid3" style={{ marginTop: 12 }}>
          <div className="card"><div style={{ color: "#64748b", fontSize: 20 }}>Tổng ngày</div><div style={{ fontWeight: 900, fontSize: 18 }}>{monthAtt.length}</div></div>
          <div className="card"><div style={{ color: "#64748b", fontSize: 20 }}>Đi làm</div><div style={{ fontWeight: 900, fontSize: 18 }}>{ontime}</div></div>
          <div className="card"><div style={{ color: "#64748b", fontSize: 20 }}>Đi muộn</div><div style={{ fontWeight: 900, fontSize: 18 }}>{late}</div></div>
          <div className="card"><div style={{ color: "#64748b", fontSize: 20 }}>Vắng</div><div style={{ fontWeight: 900, fontSize: 18 }}>{absent}</div></div>
          <div className="card"><div style={{ color: "#64748b", fontSize: 20 }}>Nghỉ phép</div><div style={{ fontWeight: 900, fontSize: 18 }}>{leave}</div></div>
        </div>
      </div>

      <Modal
        key={editOpen ? `edit-${me?.id}` : 'closed'}
        open={editOpen}
        title="Chỉnh sửa thông tin cá nhân"
        onClose={handleClose}
        footer={
          <>
            <button className="btnGhost" onClick={handleClose}>Hủy</button>
            <button className="btnPrimary" onClick={handleSave}>Lưu</button>
          </>
        }
      >
        {form ? (
          <div className="grid2" style={{ marginTop: 0 }}>
            <div>
              <div className="label">Họ tên</div>
              <input className="input" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            </div>
            <div>
              <div className="label">Email</div>
              <input className="input" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
            </div>

            <div>
              <div className="label">Số điện thoại</div>
              <input className="input" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
            </div>
            <div>
              <div className="label">Ngày sinh</div>
              <input className="input" type="date" value={form.dob} onChange={(e) => setForm({ ...form, dob: e.target.value })} />
            </div>

            <div style={{ gridColumn: "1 / -1" }}>
              <div className="label">Địa chỉ</div>
              <input className="input" value={form.address} onChange={(e) => setForm({ ...form, address: e.target.value })} />
            </div>
          </div>
        ) : null}
      </Modal>
    </>
  );
}
