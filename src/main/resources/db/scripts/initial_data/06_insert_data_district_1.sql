-- 新增直轄市的 district 資料
DO $$
DECLARE
    直轄市_administrative_area_id INT;
BEGIN
    -- 獲取 administrative_area_name 為 '直轄市' 的 administrative_area_id
    SELECT administrative_area_id INTO 直轄市_administrative_area_id FROM administrative_area WHERE administrative_area_name = '直轄市';
    -- 新增直轄市的資料至 district 表
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('新北市', 直轄市_administrative_area_id, null),
    ('臺北市', 直轄市_administrative_area_id, null),
    ('桃園市', 直轄市_administrative_area_id, null),
    ('臺中市', 直轄市_administrative_area_id, null),
    ('臺南市', 直轄市_administrative_area_id, null),
    ('高雄市', 直轄市_administrative_area_id, null);
END $$;

-- 新增直轄市所管轄的鄉-鎮-區-縣轄市資料至 district 表
DO $$
    DECLARE
        鄉鎮市區_administrative_area_id INT;
        新北市_district_id INT;
        臺北市_district_id INT;
        桃園市_district_id INT;
        臺中市_district_id INT;
        臺南市_district_id INT;
        高雄市_district_id INT;

    BEGIN
        -- 獲取 administrative_area_name 為 '鄉-鎮-區-縣轄市' 的 administrative_area_id
        SELECT administrative_area_id INTO 鄉鎮市區_administrative_area_id FROM administrative_area WHERE administrative_area_name = '鄉-鎮-區-縣轄市';

        -- 獲取各直轄市的 administrative_area_id
        SELECT district_id INTO 新北市_district_id FROM district WHERE district_name = '新北市';
        SELECT district_id INTO 臺北市_district_id FROM district WHERE district_name = '臺北市';
        SELECT district_id INTO 桃園市_district_id FROM district WHERE district_name = '桃園市';
        SELECT district_id INTO 臺中市_district_id FROM district WHERE district_name = '臺中市';
        SELECT district_id INTO 臺南市_district_id FROM district WHERE district_name = '臺南市';
        SELECT district_id INTO 高雄市_district_id FROM district WHERE district_name = '高雄市';

    -- 新增直轄市管轄的鄉-鎮-區-縣轄市
    -- 新北市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('板橋區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('三重區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('中和區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('永和區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('新莊區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('新店區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('樹林區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('鶯歌區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('三峽區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('淡水區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('汐止區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('瑞芳區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('土城區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('蘆洲區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('五股區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('泰山區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('林口區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('深坑區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('石碇區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('坪林區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('三芝區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('石門區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('八里區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('平溪區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('雙溪區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('貢寮區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('金山區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('萬里區', 鄉鎮市區_administrative_area_id, 新北市_district_id),
    ('烏來區', 鄉鎮市區_administrative_area_id, 新北市_district_id);

    -- 臺北市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('松山區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('信義區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('大安區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('中山區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('中正區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('大同區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('萬華區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('文山區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('南港區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('內湖區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('士林區', 鄉鎮市區_administrative_area_id, 臺北市_district_id),
    ('北投區', 鄉鎮市區_administrative_area_id, 臺北市_district_id);

    -- 桃園市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('桃園區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('中壢區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('大溪區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('楊梅區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('蘆竹區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('大園區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('龜山區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('八德區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('龍潭區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('平鎮區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('新屋區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('觀音區', 鄉鎮市區_administrative_area_id, 桃園市_district_id),
    ('復興區', 鄉鎮市區_administrative_area_id, 桃園市_district_id);

    -- 臺中市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('中區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('東區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('南區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('西區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('北區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('西屯區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('南屯區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('北屯區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('豐原區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('東勢區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('大甲區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('清水區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('沙鹿區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('梧棲區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('后里區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('神岡區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('潭子區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('大雅區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('新社區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('石岡區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('外埔區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('大安區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('烏日區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('大肚區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('龍井區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('霧峰區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('太平區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('大里區', 鄉鎮市區_administrative_area_id, 臺中市_district_id),
    ('和平區', 鄉鎮市區_administrative_area_id, 臺中市_district_id);

    -- 臺南市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('新營區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('鹽水區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('白河區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('柳營區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('後壁區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('東山區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('麻豆區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('下營區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('六甲區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('官田區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('大內區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('佳里區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('學甲區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('西港區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('七股區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('將軍區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('北門區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('新化區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('善化區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('新市區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('安定區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('山上區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('玉井區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('楠西區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('南化區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('左鎮區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('仁德區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('歸仁區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('關廟區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('龍崎區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('永康區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('東區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('南區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('北區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('安南區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('安平區', 鄉鎮市區_administrative_area_id, 臺南市_district_id),
    ('中西區', 鄉鎮市區_administrative_area_id, 臺南市_district_id);

    -- 高雄市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('鹽埕區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('鼓山區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('左營區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('楠梓區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('三民區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('新興區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('前金區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('苓雅區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('前鎮區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('旗津區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('小港區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('鳳山區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('林園區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('大寮區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('大樹區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('大社區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('仁武區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('鳥松區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('岡山區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('橋頭區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('燕巢區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('田寮區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('阿蓮區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('路竹區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('湖內區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('茄萣區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('永安區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('彌陀區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('梓官區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('旗山區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('美濃區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('六龜區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('甲仙區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('杉林區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('內門區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('茂林區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('桃源區', 鄉鎮市區_administrative_area_id, 高雄市_district_id),
    ('那瑪夏區', 鄉鎮市區_administrative_area_id, 高雄市_district_id);
END $$;

