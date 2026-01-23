import React from "react";

export default function Modal({
  open,
  title,
  children,
  onClose,
  footer,
}: {
  open: boolean;
  title: string;
  children: React.ReactNode;
  footer?: React.ReactNode;
  onClose: () => void;
}) {
  if (!open) return null;
  return (
    <div style={{
      position:"fixed", inset:0, background:"rgba(15,23,42,0.35)",
      display:"flex", alignItems:"center", justifyContent:"center", padding:18, zIndex:50
    }}
      onMouseDown={onClose}
    >
      <div
        className="card"
        style={{ width: 560, maxWidth:"100%" }}
        onMouseDown={(e)=>e.stopPropagation()}
      >
        <div style={{ display:"flex", justifyContent:"space-between", alignItems:"center" }}>
          <div style={{ fontWeight: 900, fontSize: 16 }}>{title}</div>
          <button className="btnGhost" onClick={onClose}>Đóng</button>
        </div>

        <div style={{ marginTop: 12 }}>{children}</div>

        {footer ? <div style={{ marginTop: 14, display:"flex", justifyContent:"flex-end", gap:10 }}>{footer}</div> : null}
      </div>
    </div>
  );
}
