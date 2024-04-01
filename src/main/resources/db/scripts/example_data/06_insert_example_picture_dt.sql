-- 為圖片11在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id, -- 從 picture 表中選擇 picture_id 欄位
       size, -- 選擇由 VALUES 子句創建的 sizes 臨時表中的size列
       CONCAT('www.fake.image11.', size, '.jpg') -- 將圖片URL組合成完整的URL
FROM (VALUES (1), (2), (3)) AS sizes(size) -- 創建一個包含尺寸的臨時表sizes
CROSS JOIN picture -- 與 picture 表交叉組合以獲取圖片 picture_id
WHERE picture_url = 'www.fake.image11.jpg';

-- 為圖片12在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image12.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
CROSS JOIN picture
WHERE picture_url = 'www.fake.image12.jpg';

-- 為圖片13在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image13.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image13.jpg';

-- 為圖片21在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image21.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image21.jpg';

-- 為圖片22在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image22.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image22.jpg';

-- 為圖片23在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image23.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image23.jpg';

-- 為圖片31在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image31.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image31.jpg';

-- 為圖片32在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image32.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image32.jpg';

-- 為圖片33在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image33.', size, '.jpg')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image33.jpg';