DO $$
DECLARE
    公寓_group_id INT;
    獨棟房屋_group_id INT;
    附屬建築_group_id INT;
    獨特房源_group_id INT;
    BnB_group_id INT;
    精品旅館_group_id INT;
BEGIN
    SELECT property_group_id INTO 公寓_group_id FROM property_group WHERE name = '公寓';
    SELECT property_group_id INTO 獨棟房屋_group_id FROM property_group WHERE name = '獨棟房屋';
    SELECT property_group_id INTO 附屬建築_group_id FROM property_group WHERE name = '附屬建築';
    SELECT property_group_id INTO 獨特房源_group_id FROM property_group WHERE name = '獨特房源';
    SELECT property_group_id INTO BnB_group_id FROM property_group WHERE name = 'B&B';
    SELECT property_group_id INTO 精品旅館_group_id FROM property_group WHERE name = '精品旅館';

    -- Insert values into property_group_type table using variables
    INSERT INTO property_group_type (property_group_id, property_type_id) VALUES
    -- 公寓
    (公寓_group_id,  (SELECT property_type_id FROM property_type WHERE name = '出租房源' )),
    (公寓_group_id,  (SELECT property_type_id FROM property_type WHERE name = '私有公寓' )),
    (公寓_group_id,  (SELECT property_type_id FROM property_type WHERE name = '服務式公寓' )),
    (公寓_group_id,  (SELECT property_type_id FROM property_type WHERE name = 'Loft空間' )),

    -- 獨棟房屋
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '房源' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '連棟房屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '平房' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '小木屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '度假小木屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '生態屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '小屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '燈塔' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '別墅' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '圓頂屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '村舍' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '農場住宿' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '船屋' )),
    (獨棟房屋_group_id,  (SELECT property_type_id FROM property_type WHERE name = '迷你屋' )),

    -- 附屬建築
    (附屬建築_group_id,  (SELECT property_type_id FROM property_type WHERE name = '待客小屋' )),
    (附屬建築_group_id,  (SELECT property_type_id FROM property_type WHERE name = '客用套房' )),
    (附屬建築_group_id,  (SELECT property_type_id FROM property_type WHERE name = '農場住宿' )),

    -- 獨特房源
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '船' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '城堡' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '洞穴' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '冰制圓頂屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '島嶼' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '飛機' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '露營車/ 休旅車' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '帳篷' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '印地安帳篷' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '火車' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '樹屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '蒙古包' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '其他' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '穀倉' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '營地' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '風車' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '巴士' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '休閒公園' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '牧場' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '宗教建築' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '貨櫃屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '塔樓' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '生態屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '小屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '燈塔' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '圓頂屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '農場住宿' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '船屋' )),
    (獨特房源_group_id,  (SELECT property_type_id FROM property_type WHERE name = '迷你屋' )),

    -- B&B
    (BnB_group_id,  (SELECT property_type_id FROM property_type WHERE name = 'B&B' )),
    (BnB_group_id,  (SELECT property_type_id FROM property_type WHERE name = '自然小屋' )),
    (BnB_group_id,  (SELECT property_type_id FROM property_type WHERE name = '臺灣民宿' )),
    (BnB_group_id,  (SELECT property_type_id FROM property_type WHERE name = '農場住宿' )),

    -- 精品旅館
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '精品旅館' )),
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '自然小屋' )),
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '青年旅館' )),
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '服務式公寓' )),
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '公寓式飯店' )),
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '飯店' )),
    (精品旅館_group_id,  (SELECT property_type_id FROM property_type WHERE name = '度假村' ));
END $$;