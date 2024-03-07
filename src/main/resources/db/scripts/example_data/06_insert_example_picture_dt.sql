-- 為圖片1在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id, -- 從 picture 表中選擇 picture_id 欄位
       size, -- 選擇由 VALUES 子句創建的 sizes 臨時表中的size列
       CONCAT('www.fake.image1.', size, '.jpg') -- 將圖片URL組合成完整的URL
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size) -- 創建一個包含尺寸的臨時表sizes
CROSS JOIN picture -- 與 picture 表交叉組合以獲取圖片 picture_id
WHERE picture_url = 'www.fake.image1.jpg';

-- 為圖片2在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image2.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
CROSS JOIN picture
WHERE picture_url = 'www.fake.image2.jpg';

-- 為圖片3在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image3.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image3.jpg';

-- 為圖片4在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image4.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image4.jpg';

-- 為圖片5在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image5.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image5.jpg';

-- 為圖片6在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image6.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image6.jpg';

-- 為圖片7在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image7.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image7.jpg';

-- 為圖片8在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image8.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image8.jpg';

-- 為圖片9在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('www.fake.image9.', size, '.jpg')
FROM (VALUES ('large'), ('medium'), ('small')) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'www.fake.image9.jpg';