import React from "react";

export default function Card({
  title,
  desc,
  right,
  children,
}: {
  title: string;
  desc?: string;
  right?: React.ReactNode;
  children?: React.ReactNode;
}) {
  return (
    <div className="card">
      <div className="cardHeader">
        <div>
          <div className="cardTitle">{title}</div>
          {desc ? <div className="cardDesc">{desc}</div> : null}
        </div>
        {right}
      </div>
      {children}
    </div>
  );
}
