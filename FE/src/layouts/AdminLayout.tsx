import Sidebar from "../components/Sidebar";
import Topbar from "../components/Topbar";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="container">
      <Sidebar mode="admin" />
      <div className="main">
        <Topbar />
        <div className="page">{children}</div>
      </div>
    </div>
  );
}
