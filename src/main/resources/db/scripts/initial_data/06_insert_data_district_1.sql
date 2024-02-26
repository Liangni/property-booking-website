-- 處理直轄市的 district 資料
DO $$
DECLARE
    直轄市_administrative_area_id INT;
BEGIN
    --  獲取 administrative_area_name 為 '直轄市' 的 administrative_area_id
    SELECT administrative_area_id INTO 直轄市_administrative_area_id FROM administrative_area WHERE administrative_area_name = '直轄市';
    -- 新增直轄市的資料至 district table
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('新北市', 直轄市_administrative_area_id, null),
    ('臺北市', 直轄市_administrative_area_id, null),
    ('桃園市', 直轄市_administrative_area_id, null),
    ('臺中市', 直轄市_administrative_area_id, null),
    ('臺南市', 直轄市_administrative_area_id, null),
    ('高雄市', 直轄市_administrative_area_id, null);
END $$;

-- 處理縣、市的 district 資料
DO $$
DECLARE
    縣市_administrative_area_id INT;
BEGIN
    -- 獲取 administrative_area_name 為 '縣-市' 的 administrative_area_id
    SELECT administrative_area_id INTO 縣市_administrative_area_id FROM administrative_area WHERE administrative_area_name = '縣-市';
    -- 新增縣市的資料至 district table
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('宜蘭縣', 縣市_administrative_area_id, null),
    ('新竹縣', 縣市_administrative_area_id, null),
    ('苗栗縣', 縣市_administrative_area_id, null),
    ('彰化縣', 縣市_administrative_area_id, null),
    ('南投縣', 縣市_administrative_area_id, null),
    ('雲林縣', 縣市_administrative_area_id, null),
    ('嘉義縣', 縣市_administrative_area_id, null),
    ('屏東縣', 縣市_administrative_area_id, null),
    ('花蓮縣', 縣市_administrative_area_id, null),
    ('臺東縣', 縣市_administrative_area_id, null),
    ('澎湖縣', 縣市_administrative_area_id, null),
    ('基隆市', 縣市_administrative_area_id, null),
    ('新竹市', 縣市_administrative_area_id, null),
    ('嘉義市', 縣市_administrative_area_id, null);
END $$;

