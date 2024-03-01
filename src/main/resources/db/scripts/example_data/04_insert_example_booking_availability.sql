-- 插入未來30天的預訂狀態
WITH dates AS (
    SELECT current_date + s AS date
    FROM generate_series(0, 29) AS s -- 生成日期序列，從當前日期開始計算未來30天的日期
)

-- 插入預訂可用性資料
-- 將房源1和房源2的預訂日期標記為「已預訂」
INSERT INTO booking_availability (booking_availability_status, booking_availability_date, property_id)
SELECT 'booked', date, 1
FROM dates
UNION ALL
SELECT 'booked', date, 2
FROM dates;


-- 更新第二周的可用性
WITH second_week_dates AS (
    SELECT s AS date
    FROM generate_series(current_date + INTERVAL '7 days', current_date + INTERVAL '13 days', '1 day') AS s
)

-- 更新預訂可用性表
-- 將房源1第二周的日期標記為「可用」
UPDATE booking_availability
SET booking_availability_status = 'available'
WHERE booking_availability_date >= (SELECT min(date) FROM second_week_dates)
  AND booking_availability_date <= (SELECT max(date) FROM second_week_dates)
  AND property_id = 1;

-- 更新第二周的可用性
WITH third_week_dates AS (
    SELECT s2 AS date
    FROM generate_series(current_date + INTERVAL '14 days', current_date + INTERVAL '20 days', '1 day') AS s2
)

-- 更新預訂可用性表
-- 將房源2第三周的日期標記為「可用」
UPDATE booking_availability
SET booking_availability_status = 'available'
WHERE booking_availability_date >= (SELECT min(date) FROM third_week_dates)
  AND booking_availability_date <= (SELECT max(date) FROM third_week_dates)
  AND property_id = 2;




