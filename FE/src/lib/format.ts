export const vnd = (price: number | string | undefined | null) => {
  // Nếu không có giá trị, trả về 0đ ngay lập tức
  if (price === undefined || price === null || price === "") return "0 ₫";
  try {
    return Number(price).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
  } catch (e) {
    return "0 ₫";
  }
};

export const monthLabel = (dateStr: string) => {
    if (!dateStr) return "";
    try {
        const [year, month] = dateStr.split("-");
        return `Tháng ${month}/${year}`;
    } catch (e) { return dateStr; }
};

export const formatDate = (dateStr: string) => {
    if (!dateStr) return "";
    try {
        return new Intl.DateTimeFormat('vi-VN').format(new Date(dateStr));
    } catch (e) { return dateStr; }
};