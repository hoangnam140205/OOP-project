# Database Seeder Guide

## File Location
[`seeder.sql`](file:///c:/Users/HoangNam/project-OOP/Database/seeder.sql)

## What's Included

### 1. Database Schema
- **phong_ban** (Departments) - 5 departments
- **nhan_vien** (Employees) - Single table inheritance for Full-time & Part-time
- **cham_cong** (Attendance) - Daily attendance records
- **bang_luong** (Payroll) - Monthly salary records
- **thuong_phat** (Rewards & Penalties) - Bonus and deductions

### 2. Sample Data

#### Departments (5 records)
- PB001: Phát triển phần mềm
- PB002: Marketing
- PB003: Quản lý
- PB004: Nhân sự
- PB005: Kế toán

#### Employees (8 records)
**Full-time (6):**
- NV001: Nguyễn Văn An - Lập trình viên Senior (15M)
- NV002: Trần Thị Bình - Thiết kế đồ họa (12M)
- NV003: Lê Hoàng Cường - Quản lý dự án (20M)
- NV004: Phạm Thị Diệu - Nhân viên nhân sự
- NV005: Hoàng Văn Em - Kế toán viên
- ADMIN: Administrator (50M)

**Part-time (2):**
- PT001: Nguyễn Thị Giang - 80h @ 50k/h
- PT002: Trần Văn Hùng - 100h @ 45k/h

#### Attendance Records
- December 2025 attendance for NV001, NV002, NV003
- Various statuses: CO_MAT, DI_MUON, NGHI_PHEP

#### Payroll Records
- 6 months of salary for NV001 (Jul-Dec 2024)
- December 2024 for NV002 and NV003
- Includes bonuses

#### Rewards & Penalties (4 records)
- Bonuses for NV001, NV002, NV003
- Penalty for PT001

## How to Run

### Method 1: MySQL Command Line
```bash
mysql -u root -p < c:\Users\HoangNam\project-OOP\Database\seeder.sql
```

### Method 2: MySQL Workbench
1. Open MySQL Workbench
2. Connect to your server
3. File → Open SQL Script → Select `seeder.sql`
4. Execute (⚡ icon or Ctrl+Shift+Enter)

### Method 3: Copy & Paste
1. Open the seeder.sql file
2. Copy all content
3. Paste into MySQL Workbench query window
4. Execute

## Important Notes

⚠️ **Warning**: This script will:
- DROP the existing `quan_ly_nhan_su` database
- CREATE a new database
- INSERT fresh sample data

💡 **Tip**: Make sure your Spring Boot application is configured to connect to:
- Database: `quan_ly_nhan_su`
- Username: `root` (or your MySQL user)
- Password: As configured in `application.properties`

## Verification

After running the seeder, the script will show:
1. Record counts for each table
2. Top 3 employees
3. Recent attendance records

Expected output:
```
Table_Name     | Count
---------------|------
Phòng ban      | 5
Nhân viên      | 8
Chấm công      | ~14
Bảng lương     | 8
Thưởng phạt    | 4
```

## Testing with Backend

After seeding the database:

1. **Start Backend**:
```bash
cd c:\Users\HoangNam\project-OOP\BE\quanlynhansu
.\mvnw spring-boot:run
```

2. **Test Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"Nguyễn Văn An@company.com\",\"password\":\"123456\"}"
```

3. **Test Get Employees**:
```bash
curl http://localhost:8080/api/employees
```

## Schema Structure

```
phong_ban (Departments)
  └─ nhan_vien (Employees)
       ├─ cham_cong (Attendance)
       ├─ bang_luong (Payroll)
       └─ thuong_phat (Rewards/Penalties)
```

## Troubleshooting

**Error: Access denied for user 'root'**
- Update password in `application.properties`

**Error: Unknown database 'quan_ly_nhan_su'**
- Run the seeder script first

**Error: Table doesn't exist**
- Check if JPA auto-create is enabled: `spring.jpa.hibernate.ddl-auto=update`

## Next Steps

✅ Database is ready  
✅ Backend API is ready  
✅ Frontend is ready  

You can now:
1. Login with any employee name
2. View and manage employee data
3. Track attendance
4. Manage payroll
