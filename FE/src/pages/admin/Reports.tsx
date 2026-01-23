import Card from "../../components/Card";
import Table from "../../components/Table";
import { getEmployees, getPayrolls, getAttendance } from "../../lib/storage";
import { BarChart, Bar, XAxis, YAxis, Tooltip, PieChart, Pie, Cell, ResponsiveContainer, LineChart, Line, CartesianGrid, Legend } from "recharts";

export default function Reports() {
  const emps = getEmployees().filter(e=>e.role!=="admin");
  const payrolls = getPayrolls();
  const attendance = getAttendance();

  const totalEmployees = emps.length;

  const avgSalary = Math.round(
    emps.reduce((s,e)=>s+e.baseSalary,0) / Math.max(emps.length,1)
  );

  const month = "2025-12";
  const monthAtt = attendance.filter(a=>a.date.startsWith(month));
  const present = monthAtt.filter(a=>a.status==="ontime" || a.status==="late").length;
  const rate = monthAtt.length ? Math.round((present/monthAtt.length)*100) : 0;

  const totalPayroll = payrolls
    .filter(p=>p.month.startsWith("2024-"))
    .reduce((s,p)=>s+(p.baseSalary+p.bonus+p.allowance-p.insurance-p.pit),0);

  const deptCount = Object.entries(
    emps.reduce((m, e)=>{ m[e.department]=(m[e.department]||0)+1; return m; }, {} as Record<string,number>)
  ).map(([name,value])=>({ name, value }));

  const statusData = [
    { name:"Hoạt động", value: emps.filter(e=>e.status==="active").length },
    { name:"Nghỉ phép", value: Math.max(1, Math.round(totalEmployees*0.09)) },
  ];

  const attTrend = ["07","08","09","10","11","12"].map((m, idx)=>({
    m: `T${Number(m)}`,
    ontime: 96 + (idx%3),
    late: 3 - (idx%2),
    absent: 1,
  }));

  const payrollTrend = ["07","08","09","10","11","12"].map((m, idx)=>({
    m: `T${Number(m)}`,
    total: 420_000_000 + idx*15_000_000,
  }));

  const deptSalary = deptCount.map(d => {
    const deptEmps = emps.filter(e=>e.department===d.name);
    const avg = Math.round(deptEmps.reduce((s,e)=>s+e.baseSalary,0)/Math.max(deptEmps.length,1));
    const sum = deptEmps.reduce((s,e)=>s+e.baseSalary,0);
    return { dept: d.name, count: deptEmps.length, avg, sum };
  });

  const pieColors = ["#10b981", "#3b82f6", "#f97316", "#ef4444"];

  return (
    <>
      <div style={{ display:"flex", justifyContent:"space-between", alignItems:"flex-start" }}>
        <div>
          <h1 className="h1">Báo cáo & Thống kê</h1>
          <p className="sub">Xem tổng quan và phân tích dữ liệu nhân sự</p>
        </div>
        <select className="btnGhost">
          <option>Tháng này</option>
          <option>6 tháng</option>
          <option>Năm nay</option>
        </select>
      </div>

      <div className="grid3">
        <Card title="Tổng nhân viên" desc="+5% so với tháng trước" right={<div className="iconBubble" style={{ background:"#E8EDFF", fontSize: 24 }}>👥</div>}>
          <div style={{ fontWeight: 900, fontSize: 18 }}>{totalEmployees}</div>
        </Card>
        <Card title="Lương TB" desc="+3% so với tháng trước" right={<div className="iconBubble" style={{ background:"#DCFCE7", fontSize: 24 }}>💲</div>}>
          <div style={{ fontWeight: 900, fontSize: 18 }}>{Math.round(avgSalary/1_000_000)} Tr đ</div>
        </Card>
        <Card title="Tỷ lệ chấm công" desc="+2% so với tháng trước" right={<div className="iconBubble" style={{ background:"#DBEAFE", fontSize: 24 }}>📅</div>}>
          <div style={{ fontWeight: 900, fontSize: 18 }}>{rate}%</div>
        </Card>
        <Card title="Tổng lương" desc="+3.2% so với tháng trước" right={<div className="iconBubble" style={{ background:"#FFEDD5", fontSize: 24 }}>💰</div>}>
          <div style={{ fontWeight: 900, fontSize: 18 }}>{Math.round(totalPayroll/1_000_000)} Tr đ</div>
        </Card>
      </div>

      <div className="grid2">
        <div className="card">
          <div style={{ fontWeight: 900, marginBottom: 10 }}>Nhân viên theo phòng ban</div>
          <div style={{ height: 260 }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={deptCount}>
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="value" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="card">
          <div style={{ fontWeight: 900, marginBottom: 10 }}>Tình trạng nhân viên</div>
          <div style={{ height: 260 }}>
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={statusData} dataKey="value" nameKey="name" outerRadius={90} label>
                  {statusData.map((_, i) => <Cell key={i} fill={pieColors[i % pieColors.length]} />)}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      <div className="grid2">
        <div className="card">
          <div style={{ fontWeight: 900, marginBottom: 10 }}>Xu hướng chấm công 6 tháng</div>
          <div style={{ height: 260 }}>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={attTrend}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="m" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="ontime" strokeWidth={2} dot />
                <Line type="monotone" dataKey="late" strokeWidth={2} dot />
                <Line type="monotone" dataKey="absent" strokeWidth={2} dot />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="card">
          <div style={{ fontWeight: 900, marginBottom: 10 }}>Xu hướng tổng lương 6 tháng</div>
          <div style={{ height: 260 }}>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={payrollTrend}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="m" />
                <YAxis />
                <Tooltip />
                <Line type="monotone" dataKey="total" strokeWidth={2} dot />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      <div className="card" style={{ marginTop: 16 }}>
        <div style={{ fontWeight: 900, marginBottom: 10 }}>Lương trung bình theo phòng ban</div>

        <Table>
          <table>
            <thead>
              <tr>
                <th>Phòng ban</th>
                <th>Số nhân viên</th>
                <th>Lương TB</th>
                <th>Tổng lương</th>
              </tr>
            </thead>
            <tbody>
              {deptSalary.map(r => (
                <tr key={r.dept}>
                  <td style={{ fontWeight: 900 }}>{r.dept}</td>
                  <td>{r.count}</td>
                  <td>{Math.round(r.avg/1_000_000)} Tr đ</td>
                  <td>{Math.round(r.sum/1_000_000)} Tr đ</td>
                </tr>
              ))}
            </tbody>
          </table>
        </Table>
      </div>
    </>
  );
}
