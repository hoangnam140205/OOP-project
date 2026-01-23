// API Configuration
const API_BASE = 'http://localhost:8080';

// Helper function for API calls
async function apiCall<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const response = await fetch(`${API_BASE}${endpoint}`, {
        headers: {
            'Content-Type': 'application/json',
            ...options?.headers,
        },
        ...options,
    });

    if (!response.ok) {
        const error = await response.text();
        throw new Error(error || `HTTP error! status: ${response.status}`);
    }

    return response.json();
}

// ============ AUTH API (GIỮ NGUYÊN) ============
export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    success: boolean;
    message?: string;
    employeeId: string;
    role: 'admin' | 'user';
    employee: any;
}

export async function fetchLogin(email: string, password: string): Promise<LoginResponse> {
    return apiCall('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
    });
}

export async function fetchRegister(email: string, password: string, name: string): Promise<LoginResponse> {
    return apiCall('/api/auth/register', {
        method: 'POST',
        body: JSON.stringify({ email, password, name }),
    });
}

// ============ EMPLOYEE API (ĐÃ CẬP NHẬT ĐỂ SỬA LỖI) ============

// 1. Thêm Interface Department (Mới)
export interface Department {
    maPhong: string;
    tenPhong: string;
}

// 2. Cập nhật Interface Employee (Cho khớp với Backend mới)
export interface Employee {
    id: string;
    code: string;
    name: string;
    email: string;
    phone?: string;
    department?: string;
  
    // --- CÁC TRƯỜNG MỚI BỔ SUNG ĐỂ KHỚP VỚI BACKEND & FORM ---
    maNV?: string;        // Backend trả về
    hoTen?: string;       // Backend trả về
    tenPhong?: string;    // Backend trả về
    departmentId?: string; // Dùng cho form dropdown
  
    chucVu?: string;
    position?: string;
    title?: string;
  
    luong?: number;
    baseSalary?: number;
  
    fullTimeType?: string; // Dùng cho form chọn loại nhân viên
    dob?: string;          // Ngày sinh
    startDate?: string;    // Ngày vào làm
  
    role?: string;
}

// 3. Các hàm API Employee
export async function fetchEmployees(): Promise<Employee[]> {
    return apiCall('/api/employees');
}

// Hàm mới: Lấy danh sách phòng ban (Để hiển thị Dropdown)
export async function fetchDepartments(): Promise<Department[]> {
    return apiCall('/api/employees/departments');
}

export async function fetchEmployee(id: string): Promise<Employee> {
    return apiCall(`/api/employees/${id}`);
}

export async function createEmployee(data: Partial<Employee>): Promise<Employee> {
    return apiCall('/api/employees', {
        method: 'POST',
        body: JSON.stringify(data),
    });
}

export async function updateEmployee(id: string, data: Partial<Employee>): Promise<Employee> {
    return apiCall(`/api/employees/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
}

export async function deleteEmployee(id: string): Promise<void> {
    return apiCall(`/api/employees/${id}`, {
        method: 'DELETE',
    });
}

// ============ PAYROLL API (GIỮ NGUYÊN) ============
export interface Payroll {
    id: string;
    employeeId: string;
    month: string;
    baseSalary: number;
    bonus: number;
    allowance: number;
    insurance: number;
    pit: number;
    status: 'paid' | 'pending';
    payDate?: string;
}

export async function fetchPayrolls(): Promise<Payroll[]> {
    return apiCall('/api/payrolls');
}

export async function fetchPayrollsByEmployee(employeeId: string): Promise<Payroll[]> {
    return apiCall(`/api/payrolls/employee/${employeeId}`);
}

export async function updatePayroll(id: string, data: Partial<Payroll>): Promise<Payroll> {
    return apiCall(`/api/payrolls/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
}

// ============ ATTENDANCE API (GIỮ NGUYÊN) ============
export interface Attendance {
    id: string;
    employeeId: string;
    employeeName?: string;
    date: string;
    checkIn?: string;
    checkOut?: string;
    hours?: number;
    status: 'ontime' | 'late' | 'absent' | 'leave';
}

export async function fetchAttendance(): Promise<Attendance[]> {
    return apiCall('/api/attendance');
}

export async function fetchAttendanceByDate(date: string): Promise<Attendance[]> {
    return apiCall(`/api/attendance/date/${date}`);
}

export async function fetchAttendanceByEmployee(employeeId: string): Promise<Attendance[]> {
    return apiCall(`/api/attendance/employee/${employeeId}`);
}

export async function createAttendance(data: Partial<Attendance>): Promise<Attendance> {
    return apiCall('/api/attendance', {
        method: 'POST',
        body: JSON.stringify(data),
    });
}

export async function updateAttendance(id: string, data: Partial<Attendance>): Promise<Attendance> {
    return apiCall(`/api/attendance/${id}`, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
}

// ============ REPORTS API (GIỮ NGUYÊN) ============
export interface ReportSummary {
    totalEmployees: number;
    avgSalary: number;
    attendanceRate: number;
    totalPayroll: number;
    newEmployeesThisMonth: number;
}

export async function fetchReportsSummary(): Promise<ReportSummary> {
    return apiCall('/api/reports/summary');
}