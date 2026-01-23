import Sidebar from "../components/Sidebar";
import Topbar from "../components/Topbar";

export default function UserLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="container">
      <Sidebar mode="user" />
      <div className="main">
        <Topbar />
        <div className="page">{children}</div>
      </div>
    </div>
  );
}
