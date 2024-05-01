DO $$
DECLARE
    公寓_property_main_type_id INT;
    獨棟房屋_property_main_type_id INT;
    附屬建築_property_main_type_id INT;
    獨特房源_property_main_type_id INT;
    BnB_property_main_type_id INT;
    精品旅館_property_main_type_id INT;
BEGIN
    SELECT property_main_type_id INTO 公寓_property_main_type_id FROM property_main_type WHERE property_main_type_name = '公寓';
    SELECT property_main_type_id INTO 獨棟房屋_property_main_type_id FROM property_main_type WHERE property_main_type_name = '獨棟房屋';
    SELECT property_main_type_id INTO 附屬建築_property_main_type_id FROM property_main_type WHERE property_main_type_name = '附屬建築';
    SELECT property_main_type_id INTO 獨特房源_property_main_type_id FROM property_main_type WHERE property_main_type_name = '獨特房源';
    SELECT property_main_type_id INTO BnB_property_main_type_id FROM property_main_type WHERE property_main_type_name = 'B&B';
    SELECT property_main_type_id INTO 精品旅館_property_main_type_id FROM property_main_type WHERE property_main_type_name = '精品旅館';

    -- Insert values into property_main_type_sub table using variables
    INSERT INTO property_main_sub_type (property_main_type_id, property_sub_type_id) VALUES
    -- 公寓
    (公寓_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '出租房源' )),
    (公寓_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '私有公寓' )),
    (公寓_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '服務式公寓' )),
    (公寓_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = 'Loft空間' )),

    -- 獨棟房屋
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '房源' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '連棟房屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '平房' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '小木屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '度假小木屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '生態屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '小屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '燈塔' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '別墅' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '圓頂屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '村舍' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '農場住宿' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '船屋' )),
    (獨棟房屋_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '迷你屋' )),

    -- 附屬建築
    (附屬建築_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '待客小屋' )),
    (附屬建築_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '客用套房' )),
    (附屬建築_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '農場住宿' )),

    -- 獨特房源
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '船' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '城堡' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '洞穴' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '冰制圓頂屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '島嶼' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '飛機' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '露營車/ 休旅車' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '帳篷' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '印地安帳篷' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '火車' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '樹屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '蒙古包' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '其他' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '穀倉' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '營地' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '風車' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '巴士' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '休閒公園' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '牧場' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '宗教建築' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '貨櫃屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '塔樓' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '生態屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '小屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '燈塔' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '圓頂屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '農場住宿' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '船屋' )),
    (獨特房源_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '迷你屋' )),

    -- B&B
    (BnB_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = 'B&B' )),
    (BnB_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '自然小屋' )),
    (BnB_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '臺灣民宿' )),
    (BnB_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '農場住宿' )),

    -- 精品旅館
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '精品旅館' )),
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '自然小屋' )),
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '青年旅館' )),
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '服務式公寓' )),
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '公寓式飯店' )),
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '飯店' )),
    (精品旅館_property_main_type_id,  (SELECT property_sub_type_id FROM property_sub_type WHERE property_sub_type_name = '度假村' ));
END $$;