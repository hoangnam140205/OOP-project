import Card from "../../components/Card";
import Table from "../../components/Table";
import { useAuth } from "../../auth/AuthContext";
import { useEffect, useState } from "react";
import { Employee, Payroll, fetchEmployee, fetchPayrollsByEmployee } from "../../lib/api";
import { monthLabel, vnd } from "../../lib/format";
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";

export default function Salary() {
  const { user } = useAuth();
  const [me, setMe] = useState<Employee | null>(null);
  const [payrolls, setPayrolls] = useState<Payroll[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadSalary() {
      if (!user) return;
      try {
        setLoading(true);
        const [employeeData, payrollsData] = await Promise.all([
          fetchEmployee(user.employeeId),
          fetchPayrollsByEmployee(user.employeeId)
        ]);
        setMe(employeeData);
        setPayrolls(payrollsData.sort((a, b) => a.month.localeCompare(b.month)));
      } catch (err) {
        console.error("Error loading salary:", err);
      } finally {
        setLoading(false);
      }
    }
    loadSalary();
  }, [user]);

  if (loading || !me) {
    return <div style={{ textAlign: "center", padding: "40px", color: "#64748b" }}>Đang tải...</div>;
  }

  const current = payrolls[payrolls.length - 1];

  const last6 = payrolls.slice(-6);
  const chartData = last6.map(p => ({
    name: monthLabel(p.month),
    value: p.baseSalary + p.bonus + p.allowance - p.insurance - p.pit,
  }));

  const avg6 = Math.round(chartData.reduce((s, x) => s + x.value, 0) / Math.max(chartData.length, 1));
  const totalYear = payrolls
    .filter(p => p.month.startsWith("2024-"))
    .reduce((s, p) => s + (p.baseSalary + p.bonus + p.allowance - p.insurance - p.pit), 0);

  return (
    <>
      <h1 className="h1">Lương & Thu nhập</h1>
      <p className="sub">Xem thông tin lương và thu nhập của bạn</p>

      <div className="grid3">
        <Card
          title="Lương tháng này"
          desc={current?.payDate ? `Ngày thanh toán: ${current.payDate}` : "—"}
          right={<span className="badge green">Đã thanh toán</span>}
        >
          <div style={{ fontWeight: 900, fontSize: 18, color: "#503EF4" }}>
            {current ? vnd(current.baseSalary) : "—"}
          </div>
        </Card>

        <Card
          title="Lương trung bình 6 tháng"
          desc="+8.5% so với 6 tháng trước"
          right={<div className="iconBubble" style={{ background: "#DCFCE7" }}>📈</div>}
        >
          <div style={{ fontWeight: 900, fontSize: 18, color: "#16a34a" }}>{vnd(avg6)}</div>
        </Card>

        <Card
          title="Tổng thu nhập năm nay"
          desc="6 tháng gần nhất"
          right={<div className="iconBubble" style={{ background: "#FFEDD5" }}>🗓️</div>}
        >
          <div style={{ fontWeight: 900, fontSize: 18, color: "#f97316" }}>{vnd(totalYear)}</div>
        </Card>
      </div>

      <div className="card" style={{ marginTop: 16 }}>
        <div style={{ fontWeight: 900, marginBottom: 10 }}>Biểu đồ thu nhập 6 tháng</div>
        <div style={{ height: 260 }}>
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="value" strokeWidth={2} dot />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      {current ? (
        <div className="card" style={{ marginTop: 16 }}>
          <div style={{ fontWeight: 900 }}>Chi tiết lương tháng hiện tại</div>

          <div className="kv" style={{ marginTop: 8 }}>
            <div>
              <div className="row"><span>Lương cơ bản</span><b>{vnd(current.baseSalary)}</b></div>
              <div className="row"><span>Thưởng</span><b style={{ color: "#16a34a" }}>+{vnd(current.bonus)}</b></div>
              <div className="row"><span>Phụ cấp</span><b style={{ color: "#16a34a" }}>+{vnd(current.allowance)}</b></div>
              <div className="row"><span>Bảo hiểm</span><b style={{ color: "#ef4444" }}>-{vnd(current.insurance)}</b></div>
              <div className="row"><span>Thuế TNCN</span><b style={{ color: "#ef4444" }}>-{vnd(current.pit)}</b></div>
              <div className="row">
                <span><b>Thực nhận</b></span>
                <b style={{ color: "#503EF4" }}>
                  {vnd(current.baseSalary + current.bonus + current.allowance - current.insurance - current.pit)}
                </b>
              </div>
            </div>
          </div>

          <div style={{ fontWeight: 900, marginTop: 16 }}>Lịch sử lương</div>
          <Table>
            <table>
              <thead>
                <tr>
                  <th>Tháng</th>
                  <th>Lương cơ bản</th>
                  <th>Thưởng</th>
                  <th>Phụ Cấp</th>
                  <th>Bảo hiểm</th>
                  <th>Thuế TNCN</th>
                  <th>Tổng lương</th>
                </tr>
              </thead>
              <tbody>
                {payrolls.slice(-6).reverse().map(p => {
                  const total = p.baseSalary + p.bonus + p.allowance - p.insurance - p.pit;
                  return (
                    <tr key={p.id}>
                      <td>{monthLabel(p.month)}</td>
                      <td>{vnd(p.baseSalary)}</td>
                      <td style={{ color: "#16a34a", fontWeight: 800 }}>+{vnd(p.bonus)}</td>
                      <td style={{ color: "#16a34a", fontWeight: 800 }}>+{vnd(p.allowance)}</td>
                      <td style={{ color: "#ef4444", fontWeight: 800 }}>-{vnd(p.insurance)}</td>
                      <td style={{ color: "#ef4444", fontWeight: 800 }}>-{vnd(p.pit)}</td>
                      <td style={{ fontWeight: 900 }}>{vnd(total)}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </Table>
        </div>
      ) : null}
    </>
  );
}
