DO $$
DECLARE
    衛浴_amenity_type_id INT;
    臥室和洗衣_amenity_type_id INT;
    娛樂_amenity_type_id INT;
    親子_amenity_type_id INT;
    網路和辦公_amenity_type_id INT;
    廚房和餐飲_amenity_type_id INT;
    位置特色_amenity_type_id INT;

BEGIN
    -- 獲取設備種類的 amenity_type_id
    SELECT amenity_type_id INTO 衛浴_amenity_type_id FROM amenity_type WHERE amenity_type_name = '衛浴';
    SELECT amenity_type_id INTO 臥室和洗衣_amenity_type_id FROM amenity_type WHERE amenity_type_name = '臥室和洗衣';
    SELECT amenity_type_id INTO 娛樂_amenity_type_id FROM amenity_type WHERE amenity_type_name = '娛樂';
    SELECT amenity_type_id INTO 親子_amenity_type_id FROM amenity_type WHERE amenity_type_name = '親子';
    SELECT amenity_type_id INTO 網路和辦公_amenity_type_id FROM amenity_type WHERE amenity_type_name = '網路和辦公';
    SELECT amenity_type_id INTO 廚房和餐飲_amenity_type_id FROM amenity_type WHERE amenity_type_name = '廚房和餐飲';
    SELECT amenity_type_id INTO 位置特色_amenity_type_id FROM amenity_type WHERE amenity_type_name = '位置特色';

    INSERT INTO amenity (amenity_name, amenity_type_id) VALUES
    ('吹風機', 衛浴_amenity_type_id),
    ('沐浴乳', 衛浴_amenity_type_id),
    ('洗髮露', 衛浴_amenity_type_id),
    ('浴缸', 衛浴_amenity_type_id),
    ('熱水', 衛浴_amenity_type_id),

    ('洗衣機', 臥室和洗衣_amenity_type_id),
    ('衣櫥', 臥室和洗衣_amenity_type_id),
    ('衣架', 臥室和洗衣_amenity_type_id),
    ('保險箱', 臥室和洗衣_amenity_type_id),

    ('主題房間', 娛樂_amenity_type_id),
    ('乒乓球桌', 娛樂_amenity_type_id),
    ('保齡球', 娛樂_amenity_type_id),
    ('滑板坡道', 娛樂_amenity_type_id),
    ('迷你高爾夫', 娛樂_amenity_type_id),

    ('兒童自行車', 親子_amenity_type_id),
    ('兒童遊戲室', 親子_amenity_type_id),
    ('嬰兒浴盆', 親子_amenity_type_id),
    ('戶外遊樂場', 親子_amenity_type_id),
    ('桌遊', 親子_amenity_type_id),

    ('Wi-Fi', 網路和辦公_amenity_type_id),
    ('專屬工作空間', 網路和辦公_amenity_type_id),

    ('冰箱', 廚房和餐飲_amenity_type_id),
    ('冷凍庫', 廚房和餐飲_amenity_type_id),
    ('咖啡', 廚房和餐飲_amenity_type_id),
    ('基本廚具', 廚房和餐飲_amenity_type_id),
    ('微波爐', 廚房和餐飲_amenity_type_id),

    ('湖畔', 位置特色_amenity_type_id),
    ('濱水', 位置特色_amenity_type_id),
    ('直達海灘', 位置特色_amenity_type_id),
    ('直達滑雪場', 位置特色_amenity_type_id),
    ('度假村入場證', 位置特色_amenity_type_id);

END $$;
