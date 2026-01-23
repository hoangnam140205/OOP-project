import Table from "../../components/Table";
import Badge from "../../components/Badge";
import Modal from "../../components/Modal";
import { Pencil, Plus, CheckCircle2, Clock, XCircle, Calendar } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { 
  Attendance, 
  Employee, 
  fetchAttendanceByDate, 
  fetchEmployees, 
  createAttendance 
} from "../../lib/api";
import Card from "../../components/Card";

export default function AttendancePage() {
  // Mặc định ngày có dữ liệu mẫu
  const [date, setDate] = useState("2025-12-27");
  
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [list, setList] = useState<Attendance[]>([]);
  const [loading, setLoading] = useState(true);
  const [q, setQ] = useState("");
  
  const [open, setOpen] = useState(false);
  const [mode, setMode] = useState<"create" | "edit">("create");
  
  const [form, setForm] = useState<any>({
    employeeId: "",
    date: "2025-12-27",
    checkIn: "08:00",
    checkOut: "17:00",
    hours: 8,
    status: "ontime"
  });

  async function loadData() {
    setLoading(true);
    try {
      const [attData, empData] = await Promise.all([
        fetchAttendanceByDate(date),
        fetchEmployees() 
      ]);
      setList(attData || []); // Thêm || [] để tránh lỗi nếu null
      setEmployees(empData || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, [date]);

  const rows = useMemo(() => {
    const s = q.trim().toLowerCase();
    return list.map(a => {
      const emp = employees.find(e => e.id === a.employeeId);
      return { 
         ...a, 
         employeeName: emp ? emp.name : (a.employeeName || "Không rõ"), 
         employeeCode: emp ? emp.code : "" 
      };
    })
    .filter(x => !s || x.employeeName.toLowerCase().includes(s));
  }, [list, q, employees]);

  // 👇 1. SỬA PHẦN THỐNG KÊ (Bắt cả tiếng Việt từ Database)
  const stats = {
    ontime: list.filter(x => ['ontime', 'co_mat', 'present', 'có mặt'].includes((x.status||'').toLowerCase())).length,
    late: list.filter(x => ['late', 'di_muon', 'đi muộn'].includes((x.status||'').toLowerCase())).length,
    absent: list.filter(x => ['absent', 'vang', 'vắng', 'nghi_khong_phep'].includes((x.status||'').toLowerCase())).length,
    leave: list.filter(x => ['leave', 'nghi_phep', 'nghỉ phép'].includes((x.status||'').toLowerCase())).length,
  };

  // 👇 2. SỬA HÀM HIỂN THỊ MÀU (Bắt cả tiếng Việt từ Database)
  function tone(st: string) {
    const s = (st || "").toLowerCase();
    if (['ontime', 'co_mat', 'present', 'có mặt'].includes(s)) return { t: "green" as const, label: "Có mặt" };
    if (['late', 'di_muon', 'đi muộn'].includes(s)) return { t: "orange" as const, label: "Đi muộn" };
    if (['leave', 'nghi_phep', 'nghỉ phép'].includes(s)) return { t: "blue" as const, label: "Nghỉ phép" };
    // Mặc định là vắng
    return { t: "red" as const, label: "Vắng / Nghỉ" };
  }

  function onCreate() {
    setMode("create");
    setForm({
      employeeId: "",
      date: date,
      checkIn: "08:00",
      checkOut: "17:00",
      hours: 8,
      status: "ontime"
    });
    setOpen(true);
  }

  function onEdit(item: any) {
    setMode("edit");
    setForm({ ...item });
    setOpen(true);
  }

  async function onSave() {
    try {
      await createAttendance(form);
      setOpen(false);
      loadData();
      alert("Lưu thành công!");
    } catch (err) {
      alert("Lỗi: " + err);
    }
  }

  return (
    <>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <div>
          <h1 className="h1">Quản lý chấm công</h1>
          <p className="sub">Theo dõi theo ngày</p>
        </div>
        <button className="btnPrimary" onClick={onCreate}>
          <Plus size={18} /> Thêm chấm công
        </button>
      </div>

      <div className="grid4" style={{ marginTop: 20, display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 16 }}>
        <Card title="Có mặt" right={<div className="iconBubble" style={{background: "#DCFCE7"}}><CheckCircle2 size={24} color="#16a34a"/></div>}>
           <div style={{ fontWeight: 900, fontSize: 18 }}>{stats.ontime}</div>
        </Card>
        <Card title="Đi muộn" right={<div className="iconBubble" style={{background: "#FFEDD5"}}><Clock size={24} color="#ea580c"/></div>}>
           <div style={{ fontWeight: 900, fontSize: 18 }}>{stats.late}</div>
        </Card>
        <Card title="Vắng mặt" right={<div className="iconBubble" style={{background: "#FEE2E2"}}><XCircle size={24} color="#dc2626"/></div>}>
           <div style={{ fontWeight: 900, fontSize: 18 }}>{stats.absent}</div>
        </Card>
        <Card title="Nghỉ phép" right={<div className="iconBubble" style={{background: "#DBEAFE"}}><Calendar size={24} color="#2563eb"/></div>}>
           <div style={{ fontWeight: 900, fontSize: 18 }}>{stats.leave}</div>
        </Card>
      </div>

      <div className="card" style={{ marginTop: 20 }}>
        <div className="grid2" style={{ marginTop: 0, marginBottom: 20 }}>
          <div>
            <div className="label">Chọn ngày xem</div>
            <input className="input" type="date" value={date} onChange={e => setDate(e.target.value)} />
          </div>
          <div>
            <div className="label">Tìm nhân viên</div>
            <input className="input" placeholder="Nhập tên..." value={q} onChange={e => setQ(e.target.value)} />
          </div>
        </div>

        <Table>
          <table>
            <thead>
              <tr>
                <th>Nhân viên</th>
                <th>Ngày</th>
                <th>Giờ vào</th>
                <th>Giờ ra</th>
                <th>Số giờ</th>
                <th>Trạng thái</th>
                <th>Sửa</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                 <tr><td colSpan={7} style={{textAlign: 'center', padding: 20}}>Đang tải...</td></tr>
              ) : rows.length === 0 ? (
                 <tr><td colSpan={7} style={{textAlign: 'center', padding: 20, color: '#888'}}>Không có dữ liệu</td></tr>
              ) : (
                rows.map((item: any) => {
                  const st = tone(item.status);
                  return (
                    <tr key={item.id}>
                      <td style={{ fontWeight: 900 }}>
                        {item.employeeName} 
                        <span style={{fontWeight: 400, color: '#666', fontSize: 12, marginLeft: 5}}>
                          ({item.employeeCode || item.employeeId})
                        </span>
                      </td>
                      <td>{item.date}</td>
                      <td style={{fontFamily: 'monospace'}}>{item.checkIn || "—"}</td>
                      <td style={{fontFamily: 'monospace'}}>{item.checkOut || "—"}</td>
                      <td>{item.hours}</td>
                      <td><Badge tone={st.t} text={st.label} /></td>
                      <td>
                        <button className="iconBtn" onClick={() => onEdit(item)}>
                          <Pencil size={16} />
                        </button>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </Table>
      </div>

      <Modal
        open={open}
        title={mode === "create" ? "Thêm chấm công" : "Cập nhật chấm công"}
        onClose={() => setOpen(false)}
        footer={
          <>
            <button className="btnGhost" onClick={() => setOpen(false)}>Hủy</button>
            <button className="btnPrimary" onClick={onSave}>Lưu</button>
          </>
        }
      >
        <div className="grid1">
          <div>
            <div className="label">Nhân viên</div>
            <select 
              className="input" 
              value={form.employeeId} 
              onChange={e => setForm({...form, employeeId: e.target.value})}
              disabled={mode === 'edit'}
            >
              <option value="">-- Chọn nhân viên --</option>
              {employees.map(emp => (
                <option key={emp.id} value={emp.id}>
                  {emp.name} - {emp.code}
                </option>
              ))}
            </select>
          </div>

          <div className="grid2">
            <div>
              <div className="label">Ngày</div>
              <input type="date" className="input" value={form.date} onChange={e => setForm({...form, date: e.target.value})} />
            </div>
            <div>
              <div className="label">Giờ vào</div>
              <input type="time" className="input" value={form.checkIn} onChange={e => setForm({...form, checkIn: e.target.value})} />
            </div>
          </div>

          <div className="grid2">
            <div>
              <div className="label">Giờ ra</div>
              <input type="time" className="input" value={form.checkOut} onChange={e => setForm({...form, checkOut: e.target.value})} />
            </div>
            <div>
              <div className="label">Số giờ làm</div>
              <input type="number" className="input" value={form.hours} onChange={e => setForm({...form, hours: Number(e.target.value)})} />
            </div>
          </div>

          <div>
            <div className="label">Trạng thái</div>
            {/* Cập nhật value option để gửi đúng dữ liệu lên DB nếu cần */}
            <select className="input" value={form.status} onChange={e => setForm({...form, status: e.target.value})}>
              <option value="ontime">Đúng giờ (Ontime)</option>
              <option value="late">Đi muộn (Late)</option>
              <option value="absent">Vắng mặt (Absent)</option>
              <option value="leave">Nghỉ phép (Leave)</option>
            </select>
          </div>
        </div>
      </Modal>
    </>
  );
}