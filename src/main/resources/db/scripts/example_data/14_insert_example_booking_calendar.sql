-- 插入未來30天的預訂狀態
WITH dates AS (
    SELECT current_date + s AS date
    FROM generate_series(0, 29) AS s -- 生成日期序列，從當前日期開始計算未來30天的日期
)

-- 插入預訂日期資料
-- 為房源1 ~ 3 插入未來30天預定日期
INSERT INTO booking_calendar (booking_date, property_id)
SELECT  date, 1
FROM dates
UNION ALL
SELECT date, 2
FROM dates
UNION ALL
SELECT date, 3
FROM dates;

-- 更新未來第二周的可預訂性
WITH second_week_dates AS (
    SELECT s AS date
    FROM generate_series(current_date + INTERVAL '7 days', current_date + INTERVAL '13 days', '1 day') AS s
),

-- 更新未來第四周的可預訂性
     forth_week_dates AS (
         SELECT s AS date
         FROM generate_series(current_date + INTERVAL '21 days', current_date + INTERVAL '25 days', '1 day') AS s
     )

-- 更新預訂可預訂性表
-- 將房源 1 未來第二、四週，房源 2 未來第四周的日期標記為「可預訂」
DELETE FROM booking_calendar
WHERE (booking_date >= (SELECT min(date) FROM second_week_dates)
    AND booking_date <= (SELECT max(date) FROM second_week_dates)
    AND property_id = 1) OR
    (booking_date >= (SELECT min(date) FROM forth_week_dates)
        AND booking_date <= (SELECT max(date) FROM forth_week_dates)
        AND property_id = 1) OR
    (booking_date >= (SELECT min(date) FROM second_week_dates)
        AND booking_date <= (SELECT max(date) FROM second_week_dates)
        AND property_id = 2);


-- 更新未來第三周的可預訂性
WITH third_week_dates AS (
    SELECT s2 AS date
    FROM generate_series(current_date + INTERVAL '14 days', current_date + INTERVAL '20 days', '1 day') AS s2
)

-- 更新預訂可預訂性表
-- 將房源 3 未來第三周的日期標記為「可預訂」
DELETE FROM booking_calendar
WHERE booking_date >= (SELECT min(date) FROM third_week_dates)
  AND booking_date <= (SELECT max(date) FROM third_week_dates)
  AND property_id = 3;


