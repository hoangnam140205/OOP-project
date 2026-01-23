import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export default function ProtectedRoute({
  children,
  allow,
}: {
  children: React.ReactNode;
  allow: ("admin" | "user")[];
}) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!allow.includes(user.role)) return <Navigate to={user.role === "admin" ? "/admin/employees" : "/me/profile"} replace />;
  return <>{children}</>;
}
