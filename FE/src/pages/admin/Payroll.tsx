import { useEffect, useState } from "react";
import { vnd } from "../../lib/format"; 
import { fetchEmployees, Employee } from "../../lib/api"; 
import Modal from "../../components/Modal";
import { Pencil, DollarSign, CheckCircle2, Clock, AlertTriangle, Wallet, CalendarRange, UserPlus } from "lucide-react"; 
import Card from "../../components/Card";
import Badge from "../../components/Badge";

const API_URL = "http://localhost:8080/api/payrolls";

export default function PayrollPage() {
  const [month, setMonth] = useState("2025-01"); 
  const [data, setData] = useState<any[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [stats, setStats] = useState({ budget: 0, paid: 0, pending: 0 });
  const [loading, setLoading] = useState(false);
  
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState<any>(null);
  const [openAdd, setOpenAdd] = useState(false);
  const [addForm, setAddForm] = useState({
      employeeId: "",
      baseSalary: 6000000,
      bonus: 0,
      pit: 0
  });

  async function loadData() {
    setLoading(true);
    try {
      const [y, m] = month.split("-");
      // Gọi API lấy lương
      const res = await fetch(`${API_URL}?month=${m}&year=${y}`);
      
      // ✅ FIX LỖI: Kiểm tra nếu Server lỗi thì không chạy tiếp
      if (!res.ok) {
          console.error("Server Error:", res.status);
          setData([]); // Xóa dữ liệu cũ để tránh lỗi render
          return;
      }

      const json = await res.json();
      
      // ✅ FIX LỖI: Kiểm tra json trước khi dùng
      if (json) {
          setData(json.list || []);
          setStats({
            budget: json.budget || 0,
            paid: json.totalPaid || 0,
            pending: json.totalPending || 0
          });
      }

      const empList = await fetchEmployees();
      setEmployees(empList);
    } catch (e) {
      console.error("Lỗi tải dữ liệu:", e);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { loadData(); }, [month]);

  async function updateBudget() {
    const newBudget = prompt("Nhập ngân sách quỹ lương mới:", stats.budget.toString());
    if (newBudget) {
      const [y, m] = month.split("-");
      await fetch(`${API_URL}/budget`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ month: m, year: y, amount: newBudget })
      });
      loadData();
    }
  }

  async function payAll() {
    const totalNeeded = stats.pending;
    if (stats.paid + totalNeeded > stats.budget) {
      alert(`Không đủ ngân sách! Cần thêm ${vnd((stats.paid + totalNeeded) - stats.budget)} nữa.`);
      return;
    }
    if (!confirm(`Xác nhận thanh toán toàn bộ lương tháng ${month}?`)) return;

    const [y, m] = month.split("-");
    const res = await fetch(`${API_URL}/pay-all`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ month: m, year: y })
    });
    
    if (res.ok) {
        alert("Thanh toán thành công!");
        loadData();
    } else {
        const txt = await res.text();
        alert("Lỗi: " + txt);
    }
  }

  async function onAddEmployee() {
      if (!addForm.employeeId) {
          alert("Vui lòng chọn nhân viên!");
          return;
      }
      try {
          const [y, m] = month.split("-");
          const res = await fetch(API_URL, {
              method: 'POST',
              headers: {'Content-Type': 'application/json'},
              body: JSON.stringify({ ...addForm, month: m, year: y })
          });

          if (res.ok) {
              setOpenAdd(false);
              loadData();
              alert("Đã thêm nhân viên vào bảng lương!");
          } else {
              const txt = await res.text();
              alert("Lỗi: " + txt);
          }
      } catch (err) {
          alert("Lỗi kết nối");
      }
  }

  async function onSave() {
    await fetch(`${API_URL}/${form.id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(form)
    });
    setOpen(false);
    loadData();
  }

  const percentUsed = stats.budget > 0 ? ((stats.paid + stats.pending) / stats.budget) * 100 : 0;
  const isOverBudget = stats.paid + stats.pending > stats.budget;

  return (
    <>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "end", marginBottom: 24 }}>
        <div>
           <h1 className="h1">Quản lý Lương thưởng</h1>
           <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginTop: 8, color: '#64748b' }}>
              <CalendarRange size={18} />
              <span>Kỳ lương:</span>
              <input 
                type="month" 
                className="input" 
                style={{ width: 'auto', padding: '4px 8px', height: 36, fontWeight: 500 }}
                value={month}
                onChange={e => setMonth(e.target.value)}
              />
           </div>
        </div>
        
        <div style={{display: 'flex', gap: 10}}>
            <button className="btnGhost" onClick={() => setOpenAdd(true)} style={{border: '1px solid #cbd5e1', background: '#fff'}}>
                <UserPlus size={18} /> Thêm nhân viên
            </button>

            {stats.pending > 0 && (
                <button 
                    className="btnPrimary" 
                    onClick={payAll}
                    style={{ background: isOverBudget ? '#ef4444' : undefined, borderColor: isOverBudget ? '#ef4444' : undefined }}
                    disabled={isOverBudget}
                >
                    <DollarSign size={18} /> 
                    {isOverBudget ? "Không đủ quỹ" : `Thanh toán tất cả`}
                </button>
            )}
        </div>
      </div>

      <div className="grid3" style={{ alignItems: 'stretch' }}>
        <div className="card" onClick={updateBudget} style={{ cursor: 'pointer', position: 'relative', borderLeft: '4px solid #3b82f6' }}>
           <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'start'}}>
               <div>
                   <div className="statTitle" style={{color: '#3b82f6', marginBottom: 4}}>NGÂN SÁCH (QUỸ LƯƠNG)</div>
                   <div className="statValue" style={{color: '#1e3a8a'}}>{vnd(stats.budget)}</div>
               </div>
               <div className="iconBubble" style={{background: '#eff6ff', color: '#3b82f6'}}><Wallet size={20}/></div>
           </div>
           <div style={{marginTop: 12}}>
               <div style={{display: 'flex', justifyContent: 'space-between', fontSize: 12, marginBottom: 4, color: '#64748b'}}>
                   <span>Đã dùng: {Math.round(percentUsed)}%</span>
                   <span style={{display: 'flex', alignItems: 'center', gap: 4}}><Pencil size={10}/> Sửa</span>
               </div>
               <div style={{height: 6, background: '#e2e8f0', borderRadius: 3, overflow: 'hidden'}}>
                   <div style={{
                       height: '100%', 
                       width: `${Math.min(percentUsed, 100)}%`, 
                       background: isOverBudget ? '#ef4444' : '#3b82f6',
                       transition: 'width 0.3s ease'
                   }}></div>
               </div>
           </div>
        </div>
        
        <Card title="ĐÃ THANH TOÁN" right={<div className="iconBubble green"><CheckCircle2 size={24}/></div>}>
           <div className="statValue" style={{color: '#15803d'}}>{vnd(stats.paid)}</div>
        </Card>

        <div className="card" style={{ borderLeft: isOverBudget ? '4px solid #ef4444' : '4px solid #f97316' }}>
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'start'}}>
               <div>
                   <div className="statTitle" style={{color: '#ea580c', marginBottom: 4}}>CHỜ THANH TOÁN</div>
                   <div className="statValue" style={{color: '#c2410c'}}>{vnd(stats.pending)}</div>
               </div>
               <div className="iconBubble orange"><Clock size={24}/></div>
           </div>
           {isOverBudget ? (
                <div style={{marginTop: 10, padding: '6px 10px', background: '#fef2f2', color: '#dc2626', borderRadius: 6, fontSize: 13, fontWeight: 500, display: 'flex', alignItems: 'center', gap: 6}}>
                    <AlertTriangle size={16}/> Vượt quỹ {vnd((stats.paid + stats.pending) - stats.budget)}
                </div>
           ) : (
                <div style={{fontSize: 13, color: '#fdba74', marginTop: 4}}>Đang chờ duyệt</div>
           )}
        </div>
      </div>

      <div className="card" style={{ marginTop: 24 }}>
        <table className="table">
            <thead>
                <tr>
                    <th>Nhân viên</th>
                    <th>Kỳ lương</th>
                    <th style={{textAlign: 'right'}}>Lương cứng</th>
                    <th style={{textAlign: 'right'}}>Thưởng</th>
                    <th style={{textAlign: 'right'}}>Khấu trừ</th>
                    <th style={{textAlign: 'right'}}>Thực lãnh</th>
                    <th style={{textAlign: 'center'}}>Trạng thái</th>
                    <th style={{textAlign: 'center'}}>Sửa</th>
                </tr>
            </thead>
            <tbody>
                {loading ? (
                    <tr><td colSpan={8} style={{textAlign: 'center', padding: 20}}>Đang tải...</td></tr>
                ) : data.length === 0 ? (
                    <tr><td colSpan={8} style={{textAlign: 'center', padding: 20, color: '#888'}}>Không có dữ liệu hoặc lỗi tải</td></tr>
                ) : data.map(item => (
                    <tr key={item.id}>
                        <td>
                            <div style={{fontWeight: 600}}>{item.employeeName}</div>
                            <div style={{fontSize: 12, color: '#94a3b8'}}>{item.employeeId}</div>
                        </td>
                        <td>{item.month}</td>
                        <td style={{textAlign: 'right', fontFamily: 'monospace'}}>{vnd(item.baseSalary)}</td>
                        <td style={{textAlign: 'right', fontFamily: 'monospace', color: '#16a34a'}}>+{vnd(item.bonus)}</td>
                        <td style={{textAlign: 'right', fontFamily: 'monospace', color: '#ef4444'}}>{item.pit > 0 ? `-${vnd(item.pit)}` : '0'}</td>
                        <td style={{textAlign: 'right', fontWeight: 700, fontSize: 15, color: '#1e293b'}}>
                            {vnd(item.total)}
                        </td>
                        <td style={{textAlign: 'center'}}>
                            {item.status === 'PAID' ? <Badge tone="green" text="Đã trả" /> : <Badge tone="orange" text="Chờ trả" />}
                        </td>
                        <td style={{textAlign: 'center'}}>
                            <button className="iconBtn" onClick={() => { setForm(item); setOpen(true); }}>
                                <Pencil size={16} />
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
      </div>

      <Modal open={open} title="Điều chỉnh lương" onClose={() => setOpen(false)}
        footer={<><button className="btnGhost" onClick={() => setOpen(false)}>Hủy bỏ</button> <button className="btnPrimary" onClick={onSave}>Lưu thay đổi</button></>}
      >
        {form && (
            <div className="grid2" style={{marginTop: 0}}>
                <div><div className="label">Kỳ lương</div><input className="input" type="month" value={form.month} onChange={e => setForm({...form, month: e.target.value})} /></div>
                <div><div className="label">Trạng thái</div><select className="input" value={form.status} onChange={e => setForm({...form, status: e.target.value})}><option value="PENDING">⏳ Chờ thanh toán</option><option value="PAID">✅ Đã thanh toán</option></select></div>
                <div><div className="label">Lương cứng</div><input className="input" type="number" value={form.baseSalary} onChange={e => setForm({...form, baseSalary: e.target.value})} /></div>
                <div><div className="label">Thưởng</div><input className="input" type="number" value={form.bonus} onChange={e => setForm({...form, bonus: e.target.value})} /></div>
                <div style={{gridColumn: '1 / -1'}}><div className="label">Khấu trừ</div><input className="input" type="number" value={form.pit} onChange={e => setForm({...form, pit: e.target.value})} /></div>
            </div>
        )}
      </Modal>

      <Modal open={openAdd} title="Thêm nhân viên vào bảng lương" onClose={() => setOpenAdd(false)}
        footer={<><button className="btnGhost" onClick={() => setOpenAdd(false)}>Hủy</button> <button className="btnPrimary" onClick={onAddEmployee}>Thêm vào bảng lương</button></>}
      >
          <div className="grid1">
              <div>
                  <div className="label">Chọn nhân viên</div>
                  <select className="input" value={addForm.employeeId} onChange={e => setAddForm({...addForm, employeeId: e.target.value})}>
                      <option value="">-- Chọn nhân viên --</option>
                      {employees.map(e => (
                          <option key={e.id} value={e.id}>{e.name} - {e.code}</option>
                      ))}
                  </select>
              </div>
              <div className="grid2">
                  <div>
                      <div className="label">Lương cơ bản</div>
                      <input className="input" type="number" value={addForm.baseSalary} onChange={e => setAddForm({...addForm, baseSalary: Number(e.target.value)})}/>
                  </div>
                  <div>
                      <div className="label">Thưởng</div>
                      <input className="input" type="number" value={addForm.bonus} onChange={e => setAddForm({...addForm, bonus: Number(e.target.value)})}/>
                  </div>
              </div>
              <div>
                  <div className="label">Khấu trừ (Phạt, Thuế...)</div>
                  <input className="input" type="number" value={addForm.pit} onChange={e => setAddForm({...addForm, pit: Number(e.target.value)})}/>
              </div>
          </div>
      </Modal>
    </>
  );
}