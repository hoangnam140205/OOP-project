# Debug Guide: User Profile Edit Not Working

## Checklist

### 1. ✅ Đã Restart Backend?

**QUAN TRỌNG**: Backend PHẢI restart để load code mới!

```bash
# In backend terminal:
Ctrl + C  (stop)
.\mvnw.cmd spring-boot:run  (restart)
```

Chờ đến khi thấy: "Started QuanlynhansuApplication"

### 2. ✅ Kiểm Tra Console Errors

**Frontend Console** (F12):
```
- Có error message không?
- API call có success không?
- Response trả về gì?
```

**Backend Console**:
```
- Có exception không?
- Có log request POST/PUT không?
```

### 3. ✅ Kiểm Tra Network Tab

1. F12 → Network tab
2. Edit profile → Save
3. Tìm request: `PUT /api/employees/{id}`
4. Check:
   - Status: 200? 404? 500?
   - Request payload: có data không?
   - Response: có data trả về không?

### 4. ✅ Fields Nào Được Support?

**Fields ĐƯỢC lưu** (có trong backend entity):
- ✅ `name` (họ tên)
- ✅ `phone` (số điện thoại)
- ✅ `department` (phòng ban) - sau khi fix
- ✅ `title` (chức vụ) - chỉ FullTime
- ✅ `baseSalary` (lương) - chỉ FullTime

**Fields KHÔNG được lưu** (backend entity không có):
- ❌ `email`
- ❌ `dob` (ngày sinh)
- ❌ `address` (địa chỉ)

→ Nếu bạn chỉ sửa email/dob/address, backend sẽ không lưu!

### 5. ✅ Test Step by Step

**Test 1**: Edit Tên
1. Login as user
2. Profile → Click "Chỉnh sửa"
3. Đổi tên: "ABC" → "XYZ"
4. Save
5. Kiểm tra: Tên có đổi không?
6. Reload page (F5)
7. Tên vẫn là "XYZ"?

**Test 2**: Edit SĐT
1. Đổi SĐT: "0901234567" → "0987654321"
2. Save
3. Kiểm tra hiển thị
4. Reload
5. Verify

**Test 3**: Edit Email (sẽ KHÔNG work)
1. Đổi email
2. Save
3. → Email sẽ KHÔNG thay đổi (backend không support)

## Debugging Steps

### Step 1: Check Browser Console

```javascript
// Mở Console (F12), khi save xem log
console.log('Saving...', form);
```

### Step 2: Check Network Request

```
Request URL: http://localhost:8080/api/employees/NV001
Request Method: PUT
Status Code: 200 OK

Request Payload:
{
  "id": "NV001",
  "name": "New Name",  ← Giá trị mới
  "phone": "0987654321",
  ...
}

Response:
{
  "id": "NV001",
  "name": "New Name",  ← Phải là giá trị mới
  ...
}
```

### Step 3: Check Database

```sql
USE quan_ly_nhan_su;

-- Check giá trị trong DB
SELECT ma_nv, ho_ten, so_dien_thoai 
FROM nhan_vien 
WHERE ma_nv = 'NV001';  -- Thay NV001 bằng mã của bạn
```

## Common Issues

### Issue 1: Backend Not Restarted
**Symptom**: Code mới không chạy
**Solution**: Restart backend

### Issue 2: Editing Unsupported Fields
**Symptom**: Email/DOB/Address không lưu
**Solution**: Chỉ edit name/phone (supported fields)

### Issue 3: API Error
**Symptom**: Network tab shows error
**Solution**: Check backend logs for exception

### Issue 4: Wrong Employee ID
**Symptom**: Update không tìm thấy employee
**Solution**: Verify `user.employeeId` đúng

## Quick Test

Mở Browser Console, paste code này:

```javascript
// Check current user
const auth = JSON.parse(localStorage.getItem('hr_auth'));
console.log('Current user:', auth);

// Manually test API
fetch('http://localhost:8080/api/employees/' + auth.employeeId, {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    id: auth.employeeId,
    name: 'TEST NAME',
    phone: '0999999999',
    email: 'test@test.com',
    department: 'Công Nghệ Thông Tin',
    title: 'Test',
    baseSalary: 10000000
  })
})
.then(r => r.json())
.then(data => console.log('Update result:', data))
.catch(err => console.error('Error:', err));
```

Nếu code này work → Frontend có issue
Nếu code này fail → Backend có issue

## Summary

**Most likely cause**: Backend chưa restart

**Action**: 
1. Restart backend
2. Clear browser cache (Ctrl+Shift+R)
3. Test lại với name hoặc phone (không dùng email/dob/address)
