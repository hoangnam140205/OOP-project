export type Role = "admin" | "user";

export type Employee = {
  id: string;
  code: string;
  name: string;
  email: string;
  phone: string;
  dob: string;
  address: string;
  department: string;
  title: string;
  manager: string;
  location: string;
  startDate: string;
  status: "active" | "inactive";
  baseSalary: number;
  password: string;
  role: Role;
};

export type Payroll = {
  id: string;
  employeeId: string;
  month: string; // YYYY-MM
  baseSalary: number;
  bonus: number;
  allowance: number;
  insurance: number;
  pit: number;
  status: "paid" | "pending";
  payDate?: string; // YYYY-MM-DD
};

export type Attendance = {
  id: string;
  employeeId: string;
  date: string; // YYYY-MM-DD
  checkIn?: string; // HH:mm
  checkOut?: string; // HH:mm
  hours?: number;
  status: "ontime" | "late" | "absent" | "leave";
};

export const seedEmployees: Employee[] = [
  {
    id: "e1",
    code: "NV001",
    name: "Nguyễn Văn An",
    email: "an.nguyen@company.com",
    phone: "0901234567",
    dob: "1995-05-15",
    address: "123 Đường ABC, Quận 1, TP.HCM",
    department: "Phát triển phần mềm",
    title: "Lập trình viên Senior",
    manager: "Lê Hoàng Cường",
    location: "Tầng 5, Tòa nhà XYZ",
    startDate: "2023-01-15",
    status: "active",
    baseSalary: 15000000,
    password: "123456",
    role: "user",
  },
  {
    id: "e2",
    code: "NV002",
    name: "Trần Thị Bình",
    email: "binh.tran@company.com",
    phone: "0902222333",
    dob: "1997-11-08",
    address: "45 Đường DEF, Quận 3, TP.HCM",
    department: "Marketing",
    title: "Thiết kế đồ họa",
    manager: "Lê Hoàng Cường",
    location: "Tầng 3, Tòa nhà XYZ",
    startDate: "2022-06-10",
    status: "active",
    baseSalary: 12000000,
    password: "123456",
    role: "user",
  },
  {
    id: "e3",
    code: "NV003",
    name: "Lê Hoàng Cường",
    email: "cuong.le@company.com",
    phone: "0909999888",
    dob: "1990-02-02",
    address: "78 Đường GHI, Quận 7, TP.HCM",
    department: "Quản lý",
    title: "Quản lý dự án",
    manager: "—",
    location: "Tầng 6, Tòa nhà XYZ",
    startDate: "2020-03-01",
    status: "active",
    baseSalary: 20000000,
    password: "123456",
    role: "user",
  },
  {
    id: "admin",
    code: "AD001",
    name: "Admin",
    email: "admin@company.com",
    phone: "0900000000",
    dob: "1990-01-01",
    address: "—",
    department: "Quản trị",
    title: "Admin",
    manager: "—",
    location: "—",
    startDate: "2020-01-01",
    status: "active",
    baseSalary: 0,
    password: "admin123",
    role: "admin",
  },
];

export const seedPayrolls: Payroll[] = [
  { id:"p1", employeeId:"e1", month:"2024-07", baseSalary:15000000, bonus:2000000, allowance:1000000, insurance:1000000, pit:1500000, status:"paid", payDate:"2024-07-25" },
  { id:"p2", employeeId:"e1", month:"2024-08", baseSalary:15000000, bonus:2500000, allowance:1000000, insurance:1000000, pit:1500000, status:"paid", payDate:"2024-08-25" },
  { id:"p3", employeeId:"e1", month:"2024-09", baseSalary:15000000, bonus:3000000, allowance:1000000, insurance:1000000, pit:1500000, status:"paid", payDate:"2024-09-25" },
  { id:"p4", employeeId:"e1", month:"2024-10", baseSalary:15000000, bonus:2000000, allowance:1000000, insurance:1000000, pit:1500000, status:"paid", payDate:"2024-10-25" },
  { id:"p5", employeeId:"e1", month:"2024-11", baseSalary:15000000, bonus:2500000, allowance:1000000, insurance:1000000, pit:1500000, status:"paid", payDate:"2024-11-25" },
  { id:"p6", employeeId:"e1", month:"2024-12", baseSalary:15000000, bonus:3000000, allowance:1000000, insurance:1000000, pit:1500000, status:"paid", payDate:"2024-12-25" },

  { id:"p7", employeeId:"e2", month:"2024-12", baseSalary:12000000, bonus:2000000, allowance:500000, insurance:800000, pit:1200000, status:"paid", payDate:"2024-12-25" },
  { id:"p8", employeeId:"e3", month:"2024-12", baseSalary:20000000, bonus:5000000, allowance:1000000, insurance:1200000, pit:2000000, status:"pending" },
];

export const seedAttendance: Attendance[] = [
  { id:"a1", employeeId:"e1", date:"2025-12-27", status:"ontime", checkIn:"08:30", checkOut:"17:30", hours:8 },
  { id:"a2", employeeId:"e2", date:"2025-12-27", status:"late", checkIn:"09:05", checkOut:"17:30", hours:7.5 },
];
