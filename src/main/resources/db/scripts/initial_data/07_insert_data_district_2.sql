-- 新增縣、市的 district 資料
DO $$
DECLARE
    縣市_administrative_area_id INT;
BEGIN
    -- 獲取 administrative_area_name 為 '縣-市' 的 administrative_area_id
    SELECT administrative_area_id INTO 縣市_administrative_area_id FROM administrative_area WHERE administrative_area_name = '縣-市';
    -- 新增縣市的資料至 district 表
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
     ('金門縣', 縣市_administrative_area_id, null),
     ('連江縣', 縣市_administrative_area_id, null),
     ('基隆市', 縣市_administrative_area_id, null),
     ('新竹市', 縣市_administrative_area_id, null),
     ('嘉義市', 縣市_administrative_area_id, null);
END $$;

-- 新增縣、市管轄的鄉-鎮-區-縣轄市資料至 district 表
DO $$
DECLARE
    鄉鎮市區_administrative_area_id INT;

    宜蘭縣_district_id INT;
    新竹縣_district_id INT;
    苗栗縣_district_id INT;
    彰化縣_district_id INT;
    南投縣_district_id INT;
    雲林縣_district_id INT;
    嘉義縣_district_id INT;
    屏東縣_district_id INT;
    臺東縣_district_id INT;
    花蓮縣_district_id INT;
    澎湖縣_district_id INT;
    金門縣_district_id INT;
    連江縣_district_id INT;

    基隆市_district_id INT;
    新竹市_district_id INT;
    嘉義市_district_id INT;
BEGIN
    -- 獲取 administrative_area_name 為 '鄉-鎮-區-縣轄市' 的 administrative_area_id
    SELECT administrative_area_id INTO 鄉鎮市區_administrative_area_id FROM administrative_area WHERE administrative_area_name = '鄉-鎮-區-縣轄市';

    -- 獲取各縣的 administrative_area_id
    SELECT district_id INTO 宜蘭縣_district_id FROM district WHERE district_name = '宜蘭縣';
    SELECT district_id INTO 新竹縣_district_id FROM district WHERE district_name = '新竹縣';
    SELECT district_id INTO 苗栗縣_district_id FROM district WHERE district_name = '苗栗縣';
    SELECT district_id INTO 彰化縣_district_id FROM district WHERE district_name = '彰化縣';
    SELECT district_id INTO 南投縣_district_id FROM district WHERE district_name = '南投縣';
    SELECT district_id INTO 雲林縣_district_id FROM district WHERE district_name = '雲林縣';
    SELECT district_id INTO 嘉義縣_district_id FROM district WHERE district_name = '嘉義縣';
    SELECT district_id INTO 屏東縣_district_id FROM district WHERE district_name = '屏東縣';
    SELECT district_id INTO 臺東縣_district_id FROM district WHERE district_name = '臺東縣';
    SELECT district_id INTO 花蓮縣_district_id FROM district WHERE district_name = '花蓮縣';
    SELECT district_id INTO 澎湖縣_district_id FROM district WHERE district_name = '澎湖縣';
    SELECT district_id INTO 金門縣_district_id FROM district WHERE district_name = '金門縣';
    SELECT district_id INTO 連江縣_district_id FROM district WHERE district_name = '連江縣';

    -- 獲取各市的 administrative_area_id
    SELECT district_id INTO 基隆市_district_id FROM district WHERE district_name = '基隆市';
    SELECT district_id INTO 新竹市_district_id FROM district WHERE district_name = '新竹市';
    SELECT district_id INTO 嘉義市_district_id FROM district WHERE district_name = '嘉義市';

    -- 新增縣管轄的鄉-鎮-區-縣轄市
    -- 宜蘭縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('宜蘭市', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('羅東鎮', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('蘇澳鎮', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('頭城鎮', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('礁溪鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('壯圍鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('員山鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('冬山鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('五結鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('三星鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('大同鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id),
    ('南澳鄉', 鄉鎮市區_administrative_area_id, 宜蘭縣_district_id);

    -- 新竹縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('竹北市', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('關西鎮', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('新埔鎮', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('竹東鎮', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('湖口鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('橫山鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('新豐鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('芎林鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('寶山鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('北埔鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('峨眉鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('尖石鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id),
    ('五峰鄉', 鄉鎮市區_administrative_area_id, 新竹縣_district_id);

    -- 苗栗縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('苗栗市', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('頭份市', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('苑裡鎮', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('通霄鎮', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('竹南鎮', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('後龍鎮', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('卓蘭鎮', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('大湖鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('公館鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('銅鑼鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('南鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('頭屋鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('三義鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('西湖鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('造橋鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('三灣鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('獅潭鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id),
    ('泰安鄉', 鄉鎮市區_administrative_area_id, 苗栗縣_district_id);


    -- 彰化縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('彰化市', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('員林市', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('鹿港鎮', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('和美鎮', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('北斗鎮', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('溪湖鎮', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('田中鎮', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('二林鎮', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('線西鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('伸港鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('福興鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('秀水鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('花壇鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('芬園鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('大村鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('埔鹽鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('埔心鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('永靖鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('社頭鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('二水鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('田尾鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('埤頭鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('芳苑鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('大城鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('竹塘鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id),
    ('溪州鄉', 鄉鎮市區_administrative_area_id, 彰化縣_district_id);

    -- 南投縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('南投市', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('埔里鎮', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('草屯鎮', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('竹山鎮', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('集集鎮', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('名間鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('鹿谷鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('中寮鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('魚池鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('國姓鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('水里鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('信義鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id),
    ('仁愛鄉', 鄉鎮市區_administrative_area_id, 南投縣_district_id);

    -- 雲林縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('斗六市', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('斗南鎮', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('虎尾鎮', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('西螺鎮', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('土庫鎮', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('北港鎮', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('古坑鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('大埤鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('莿桐鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('林內鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('二崙鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('崙背鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('麥寮鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('東勢鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('褒忠鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('臺西鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('元長鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('四湖鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('口湖鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id),
    ('水林鄉', 鄉鎮市區_administrative_area_id, 雲林縣_district_id);

    -- 嘉義縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('太保市', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('朴子市', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('布袋鎮', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('大林鎮', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('民雄鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('溪口鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('新港鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('六腳鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('東石鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('義竹鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('鹿草鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('水上鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('中埔鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('竹崎鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('梅山鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('番路鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('大埔鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id),
    ('阿里山鄉', 鄉鎮市區_administrative_area_id, 嘉義縣_district_id);

    -- 屏東縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('屏東市', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('潮州鎮', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('東港鎮', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('恆春鎮', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('萬丹鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('長治鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('麟洛鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('九如鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('里港鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('鹽埔鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('高樹鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('萬巒鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('內埔鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('竹田鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('新埤鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('枋寮鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('新園鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('崁頂鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('林邊鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('南州鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('佳冬鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('琉球鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('車城鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('滿州鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('枋山鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('三地門鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('霧臺鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('瑪家鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('泰武鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('來義鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('春日鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('獅子鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id),
    ('牡丹鄉', 鄉鎮市區_administrative_area_id, 屏東縣_district_id);

    -- 臺東縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('臺東市', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('成功鎮', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('關山鎮', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('卑南鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('大武鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('太麻里鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('東河鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('長濱鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('鹿野鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('池上鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('綠島鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('延平鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('海端鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('達仁鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('金峰鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id),
    ('蘭嶼鄉', 鄉鎮市區_administrative_area_id, 臺東縣_district_id);

    -- 澎湖縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('馬公市', 鄉鎮市區_administrative_area_id, 澎湖縣_district_id),
    ('湖西鄉', 鄉鎮市區_administrative_area_id, 澎湖縣_district_id),
    ('白沙鄉', 鄉鎮市區_administrative_area_id, 澎湖縣_district_id),
    ('西嶼鄉', 鄉鎮市區_administrative_area_id, 澎湖縣_district_id),
    ('望安鄉', 鄉鎮市區_administrative_area_id, 澎湖縣_district_id),
    ('七美鄉', 鄉鎮市區_administrative_area_id, 澎湖縣_district_id);

    -- 金門縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('金城鎮', 鄉鎮市區_administrative_area_id, 金門縣_district_id),
    ('金湖鎮', 鄉鎮市區_administrative_area_id, 金門縣_district_id),
    ('金沙鎮', 鄉鎮市區_administrative_area_id, 金門縣_district_id),
    ('金寧鄉', 鄉鎮市區_administrative_area_id, 金門縣_district_id),
    ('烈嶼鄉', 鄉鎮市區_administrative_area_id, 金門縣_district_id),
    ('烏坵鄉', 鄉鎮市區_administrative_area_id, 金門縣_district_id);

    -- 連江縣
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('南竿鄉', 鄉鎮市區_administrative_area_id, 連江縣_district_id),
    ('北竿鄉', 鄉鎮市區_administrative_area_id, 連江縣_district_id),
    ('莒光鄉', 鄉鎮市區_administrative_area_id, 連江縣_district_id),
    ('東引鄉', 鄉鎮市區_administrative_area_id, 連江縣_district_id);

    -- 新增市管轄的鄉-鎮-區-縣轄市
    -- 基隆市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('中正區', 鄉鎮市區_administrative_area_id, 基隆市_district_id),
    ('七堵區', 鄉鎮市區_administrative_area_id, 基隆市_district_id),
    ('暖暖區', 鄉鎮市區_administrative_area_id, 基隆市_district_id),
    ('仁愛區', 鄉鎮市區_administrative_area_id, 基隆市_district_id),
    ('中山區', 鄉鎮市區_administrative_area_id, 基隆市_district_id),
    ('安樂區', 鄉鎮市區_administrative_area_id, 基隆市_district_id),
    ('信義區', 鄉鎮市區_administrative_area_id, 基隆市_district_id);

    -- 新竹市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('東區', 鄉鎮市區_administrative_area_id, 新竹市_district_id),
    ('北區', 鄉鎮市區_administrative_area_id, 新竹市_district_id),
    ('香山區', 鄉鎮市區_administrative_area_id, 新竹市_district_id);

    -- 嘉義市
    INSERT INTO district (district_name, administrative_area_id, parent_district_id) VALUES
    ('東區', 鄉鎮市區_administrative_area_id, 嘉義市_district_id),
    ('西區', 鄉鎮市區_administrative_area_id, 嘉義市_district_id);
END $$;