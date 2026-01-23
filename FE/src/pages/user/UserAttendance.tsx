import Table from "../../components/Table";
import Badge from "../../components/Badge";
import Card from "../../components/Card";
import { useEffect, useMemo, useState } from "react";
import { Attendance, createAttendance, fetchAttendanceByEmployee, updateAttendance } from "../../lib/api";
import { useAuth } from "../../auth/AuthContext";

export default function UserAttendancePage() {
    const { user } = useAuth();
    const [list, setList] = useState<Attendance[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [q, setQ] = useState("");

    const today = new Date().toISOString().split("T")[0];
    const todayAttendance = list.find(a => a.date === today);

    const thisMonth = useMemo(() => {
        const now = new Date();
        const month = now.getMonth();
        const year = now.getFullYear();
        return list.filter(a => {
            const d = new Date(a.date);
            return d.getMonth() === month && d.getFullYear() === year;
        });
    }, [list]);

    const filtered = useMemo(() => {
        const s = q.trim().toLowerCase();
        if (!s) return list;
        return list.filter(a => a.date.includes(s));
    }, [q, list]);

    const totalHours = thisMonth.reduce((sum, a) => sum + (a.hours || 0), 0);
    const ontimeCount = thisMonth.filter(a => a.status === "ontime").length;
    const lateCount = thisMonth.filter(a => a.status === "late").length;
    const absentCount = thisMonth.filter(a => a.status === "absent").length;

    async function loadData() {
        if (!user?.employeeId) return;
        try {
            setLoading(true);
            setError(null);
            const data = await fetchAttendanceByEmployee(user.employeeId);
            setList(data);
        } catch (err) {
            setError(err instanceof Error ? err.message : "Lỗi tải dữ liệu");
            console.error("Error loading attendance:", err);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadData();
    }, [user]);

    async function handleCheckIn() {
        if (!user?.employeeId) return;
        const now = new Date();
        const timeStr = now.toTimeString().split(" ")[0].substring(0, 5);
        const newAttendance: Partial<Attendance> = {
            id: `att_${Date.now()}`,
            employeeId: user.employeeId,
            date: today,
            checkIn: timeStr,
            status: "ontime",
        };
        try {
            await createAttendance(newAttendance);
            await loadData();
        } catch (err) {
            alert("Lỗi chấm công vào: " + (err instanceof Error ? err.message : "Unknown error"));
        }
    }

    async function handleCheckOut() {
        if (!todayAttendance) return;
        const now = new Date();
        const timeStr = now.toTimeString().split(" ")[0].substring(0, 5);
        const checkInTime = todayAttendance.checkIn ? new Date(`2000-01-01T${todayAttendance.checkIn}`) : null;
        const checkOutTime = new Date(`2000-01-01T${timeStr}`);
        let hours = 0;
        if (checkInTime) {
            hours = Math.round((checkOutTime.getTime() - checkInTime.getTime()) / (1000 * 60 * 60) * 10) / 10;
        }
        try {
            await updateAttendance(todayAttendance.id, {
                checkOut: timeStr,
                hours: hours
            });
            await loadData();
        } catch (err) {
            alert("Lỗi chấm công ra: " + (err instanceof Error ? err.message : "Unknown error"));
        }
    }

    function tone(st: Attendance["status"]) {
        if (st === "ontime") return { t: "green" as const, label: "Đúng giờ" };
        if (st === "late") return { t: "orange" as const, label: "Đi muộn" };
        if (st === "absent") return { t: "red" as const, label: "Vắng mặt" };
        return { t: "blue" as const, label: "Nghỉ phép" };
    }

    if (loading) {
        return <div style={{ textAlign: "center", padding: "40px", color: "#64748b" }}>Đang tải dữ liệu...</div>;
    }

    if (error) {
        return <div style={{ textAlign: "center", padding: "40px", color: "#d11a2a" }}>{error}</div>;
    }

    return (
        <>
            <h1 className="h1">Chấm công</h1>
            <p className="sub">Quản lý chấm công của bạn</p>

            <div className="grid4">
                <Card title="Tổng giờ tháng này" right={<div className="iconBubble" style={{ background: "#DBEAFE", fontSize: 24 }}>⏰</div>}>
                    <div style={{ fontWeight: 900, fontSize: 18 }}>{totalHours.toFixed(1)}h</div>
                </Card>
                <Card title="Đúng giờ" right={<div className="iconBubble" style={{ background: "#DCFCE7", fontSize: 24 }}>✅</div>}>
                    <div style={{ fontWeight: 900, fontSize: 18 }}>{ontimeCount}</div>
                </Card>
                <Card title="Đi muộn" right={<div className="iconBubble" style={{ background: "#FFEDD5", fontSize: 24 }}>🕒</div>}>
                    <div style={{ fontWeight: 900, fontSize: 18 }}>{lateCount}</div>
                </Card>
                <Card title="Vắng mặt" right={<div className="iconBubble" style={{ background: "#FEE2E2", fontSize: 24 }}>❌</div>}>
                    <div style={{ fontWeight: 900, fontSize: 18 }}>{absentCount}</div>
                </Card>
            </div>

            <div className="card" style={{ marginTop: 16 }}>
                <div style={{ display: "flex", gap: 12, marginBottom: 16 }}>
                    {!todayAttendance ? (
                        <button className="btnPrimary" onClick={handleCheckIn}>
                            ✅ Chấm công vào
                        </button>
                    ) : !todayAttendance.checkOut ? (
                        <button className="btnPrimary" onClick={handleCheckOut}>
                            🏁 Chấm công ra
                        </button>
                    ) : (
                        <div style={{ color: "#10b981", fontWeight: 600 }}>✅ Đã hoàn thành chấm công hôm nay</div>
                    )}
                </div>

                <div style={{ marginBottom: 12 }}>
                    <div className="label">Tìm kiếm theo ngày</div>
                    <input
                        className="input"
                        placeholder="Nhập ngày (YYYY-MM-DD)..."
                        value={q}
                        onChange={(e) => setQ(e.target.value)}
                    />
                </div>

                <Table>
                    <table>
                        <thead>
                            <tr>
                                <th>Ngày</th>
                                <th>Giờ vào</th>
                                <th>Giờ ra</th>
                                <th>Số giờ làm</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filtered.length ? filtered.map(a => {
                                const st = tone(a.status);
                                return (
                                    <tr key={a.id}>
                                        <td style={{ fontWeight: 900 }}>{a.date}</td>
                                        <td>{a.checkIn || "—"}</td>
                                        <td>{a.checkOut || "—"}</td>
                                        <td>{a.hours ?? "—"}</td>
                                        <td><Badge tone={st.t} text={st.label} /></td>
                                    </tr>
                                );
                            }) : (
                                <tr><td colSpan={5} style={{ textAlign: "center", color: "#64748b" }}>Chưa có dữ liệu chấm công</td></tr>
                            )}
                        </tbody>
                    </table>
                </Table>
            </div>
        </>
    );
}
