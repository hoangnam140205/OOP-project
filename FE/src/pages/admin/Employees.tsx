import { useEffect, useState } from "react";
import { User, Mail, Briefcase, DollarSign, Calendar, Search, Plus, Pencil, Trash2 } from "lucide-react";
import { Employee, fetchEmployees } from "../../lib/api";
import { vnd, formatDate } from "../../lib/format"; // Đảm bảo import đúng
import Modal from "../../components/Modal";
import Card from "../../components/Card";
import Badge from "../../components/Badge";

const API_URL = "http://localhost:8080/api/employees";

// Giá trị mặc định cho form để tránh lỗi "Uncontrolled input"
const defaultForm = {
    code: "", // Mã NV
    name: "",
    email: "",
    phone: "",
    departmentId: "", // ID phòng ban để gửi lên server
    position: "",
    baseSalary: 0,
    fullTimeType: "FULL_TIME", // Mặc định là Full-time
    dob: "",
    startDate: new Date().toISOString().split('T')[0]
};

export default function Employees() {
  const [data, setData] = useState<Employee[]>([]);
  const [departments, setDepartments] = useState<any[]>([]);
  const [search, setSearch] = useState("");
  
  // State cho Modal
  const [open, setOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false); // Cờ để phân biệt Thêm hay Sửa
  const [form, setForm] = useState(defaultForm);

  // Tải dữ liệu
  async function loadData() {
    const res = await fetchEmployees();
    setData(res);
    
    // Tải danh sách phòng ban để nạp vào dropdown
    try {
        const depRes = await fetch(`${API_URL}/departments`);
        if (depRes.ok) setDepartments(await depRes.json());
    } catch (e) { console.error("Lỗi tải phòng ban"); }
  }

  useEffect(() => { loadData(); }, []);

  // Hàm mở Modal THÊM MỚI
  function onAdd() {
      setForm(defaultForm); // Reset form về rỗng
      setIsEditing(false);  // Đặt chế độ là Thêm mới
      setOpen(true);
  }

  // Hàm mở Modal CHỈNH SỬA
  function onEdit(emp: Employee) {
      setForm({
          code: emp.code || emp.maNV || "", // Lấy mã NV
          name: emp.name || emp.hoTen || "",
          email: emp.email || "",
          phone: emp.phone || "",
          departmentId: emp.departmentId || "", // Binding phòng ban
          position: emp.position || emp.chucVu || "",
          baseSalary: emp.baseSalary || emp.luong || 0,
          fullTimeType: emp.fullTimeType || "FULL_TIME",
          dob: emp.dob || "",
          startDate: emp.startDate || ""
      });
      setIsEditing(true); // Đặt chế độ là Chỉnh sửa
      setOpen(true);
  }

  // Hàm LƯU (Quan trọng: Phân biệt POST và PUT)
  async function onSave() {
      try {
          const method = isEditing ? 'PUT' : 'POST';
          // Nếu đang sửa -> Gửi vào /api/employees/{code}
          // Nếu đang thêm -> Gửi vào /api/employees
          const url = isEditing ? `${API_URL}/${form.code}` : API_URL;

          const res = await fetch(url, {
              method: method,
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                  ...form,
                  // Mapping lại tên trường cho khớp với Backend
                  maNV: form.code,
                  hoTen: form.name,
                  soDienThoai: form.phone,
                  maPhong: form.departmentId,
                  chucVu: form.position,
                  luongCoBan: form.baseSalary, // Gửi lương lên
                  loaiNhanVien: form.fullTimeType,
                  ngaySinh: form.dob,
                  ngayVaoLam: form.startDate
              })
          });

          if (res.ok) {
              alert(isEditing ? "Cập nhật thành công!" : "Thêm mới thành công!");
              setOpen(false);
              loadData(); // Tải lại danh sách
          } else {
              const txt = await res.text();
              alert("Lỗi: " + txt);
          }
      } catch (e) {
          alert("Lỗi kết nối Server");
      }
  }

  // Hàm XÓA
  async function onDelete(id: string) {
      if (!confirm("Bạn có chắc chắn muốn xóa nhân viên này?")) return;
      try {
          const res = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
          if (res.ok) {
              alert("Đã xóa thành công!");
              loadData();
          } else {
              alert("Không thể xóa (Có thể do ràng buộc dữ liệu lương/công)");
          }
      } catch (e) {
          alert("Lỗi kết nối");
      }
  }

  // Lọc tìm kiếm
  const filtered = data.filter(e => 
      (e.name?.toLowerCase() || "").includes(search.toLowerCase()) ||
      (e.code?.toLowerCase() || "").includes(search.toLowerCase())
  );

  return (
    <>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 24 }}>
        <div>
           <h1 className="h1">Quản lý nhân viên</h1>
           <div className="subText">Tổng số: {data.length} nhân viên</div>
        </div>
        <button className="btnPrimary" onClick={onAdd}><Plus size={18}/> Thêm nhân viên</button>
      </div>

      <div className="card">
        <div style={{ padding: 16, borderBottom: '1px solid #f1f5f9', display: 'flex', gap: 10 }}>
            <div className="inputGroup" style={{ flex: 1 }}>
                <Search size={18} style={{ color: '#94a3b8' }} />
                <input 
                    placeholder="Tìm theo Mã NV, Tên..." 
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                    style={{ border: 'none', outline: 'none', width: '100%' }}
                />
            </div>
        </div>

        <table className="table">
            <thead>
                <tr>
                    <th>Mã NV</th>
                    <th>Họ tên</th>
                    <th>Email</th>
                    <th>Phòng ban</th>
                    <th>Chức vụ</th>
                    <th>Lương</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
              {filtered.map(e => (
                <tr key={e.id}>
                  <td><Badge tone="blue" text={e.code || e.maNV || ""} /></td>
                  <td style={{ fontWeight: 600 }}>{e.name || e.hoTen}</td>
                  <td>{e.email}</td>
                  <td>{e.department || e.tenPhong || "—"}</td>
                  <td>{e.position || e.chucVu || "—"}</td>
                  {/* Sử dụng || 0 để an toàn cho hàm vnd */}
                  <td style={{ fontFamily: 'monospace' }}>
                      {vnd(e.baseSalary || e.luong || 0)}
                  </td>
                  <td>
                    <div className="actions">
                      <button className="iconBtn" onClick={() => onEdit(e)}><Pencil size={16} /></button>
                      <button className="iconBtn danger" onClick={() => onDelete(e.id)}><Trash2 size={16} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
        </table>
      </div>

      {/* MODAL FORM */}
      <Modal open={open} title={isEditing ? "Cập nhật nhân viên" : "Thêm nhân viên mới"} onClose={() => setOpen(false)}
        footer={<>
            <button className="btnGhost" onClick={() => setOpen(false)}>Hủy</button> 
            <button className="btnPrimary" onClick={onSave}>{isEditing ? "Lưu thay đổi" : "Tạo mới"}</button>
        </>}
      >
        <div className="grid2">
            <div><div className="label">Mã NV</div>
                <input className="input" value={form.code} 
                       disabled={isEditing} // Không cho sửa mã khi đang edit
                       onChange={e => setForm({...form, code: e.target.value})} />
            </div>
            <div><div className="label">Họ tên</div>
                <input className="input" value={form.name} onChange={e => setForm({...form, name: e.target.value})} />
            </div>
            <div><div className="label">Email</div>
                <input className="input" value={form.email} onChange={e => setForm({...form, email: e.target.value})} />
            </div>
            <div><div className="label">Số điện thoại</div>
                <input className="input" value={form.phone} onChange={e => setForm({...form, phone: e.target.value})} />
            </div>
            
            {/* Dropdown Phòng Ban */}
            <div><div className="label">Phòng ban</div>
                <select className="input" value={form.departmentId} onChange={e => setForm({...form, departmentId: e.target.value})}>
                    <option value="">-- Chọn phòng ban --</option>
                    {departments.map(d => (
                        <option key={d.maPhong} value={d.maPhong}>{d.tenPhong}</option>
                    ))}
                </select>
            </div>

            <div><div className="label">Chức vụ</div>
                <input className="input" value={form.position} onChange={e => setForm({...form, position: e.target.value})} />
            </div>

            {/* Dropdown Loại Nhân viên */}
            <div><div className="label">Loại hợp đồng</div>
                <select className="input" value={form.fullTimeType} onChange={e => setForm({...form, fullTimeType: e.target.value})}>
                    <option value="FULL_TIME">Toàn thời gian (Full-time)</option>
                    <option value="PART_TIME">Thời vụ (Part-time)</option>
                </select>
            </div>

            <div><div className="label">Lương (CB/Giờ)</div>
                <input className="input" type="number" value={form.baseSalary} onChange={e => setForm({...form, baseSalary: Number(e.target.value)})} />
            </div>

            <div><div className="label">Ngày sinh</div>
                <input className="input" type="date" value={form.dob} onChange={e => setForm({...form, dob: e.target.value})} />
            </div>
            <div><div className="label">Ngày vào làm</div>
                <input className="input" type="date" value={form.startDate} onChange={e => setForm({...form, startDate: e.target.value})} />
            </div>
        </div>
      </Modal>
    </>
  );
}