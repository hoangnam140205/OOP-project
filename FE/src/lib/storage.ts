import { seedAttendance, seedEmployees, seedPayrolls, Attendance, Employee, Payroll } from "../data/seed";

const K_EMP = "hr_employees";
const K_PAY = "hr_payrolls";
const K_ATT = "hr_attendance";
const K_AUTH = "hr_auth";

function ensureSeed() {
  if (!localStorage.getItem(K_EMP)) localStorage.setItem(K_EMP, JSON.stringify(seedEmployees));
  if (!localStorage.getItem(K_PAY)) localStorage.setItem(K_PAY, JSON.stringify(seedPayrolls));
  if (!localStorage.getItem(K_ATT)) localStorage.setItem(K_ATT, JSON.stringify(seedAttendance));
}
ensureSeed();

export function getEmployees(): Employee[] {
  return JSON.parse(localStorage.getItem(K_EMP) || "[]");
}
export function setEmployees(list: Employee[]) {
  localStorage.setItem(K_EMP, JSON.stringify(list));
}

export function getPayrolls(): Payroll[] {
  return JSON.parse(localStorage.getItem(K_PAY) || "[]");
}
export function setPayrolls(list: Payroll[]) {
  localStorage.setItem(K_PAY, JSON.stringify(list));
}

export function getAttendance(): Attendance[] {
  return JSON.parse(localStorage.getItem(K_ATT) || "[]");
}
export function setAttendance(list: Attendance[]) {
  localStorage.setItem(K_ATT, JSON.stringify(list));
}

export type AuthState = { employeeId: string; role: "admin" | "user" } | null;

export function getAuth(): AuthState {
  return JSON.parse(localStorage.getItem(K_AUTH) || "null");
}
export function setAuth(auth: AuthState) {
  localStorage.setItem(K_AUTH, JSON.stringify(auth));
}
