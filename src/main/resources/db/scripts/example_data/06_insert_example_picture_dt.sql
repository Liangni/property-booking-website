-- 為圖片11在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id, -- 從 picture 表中選擇 picture_id 欄位
       size, -- 選擇由 VALUES 子句創建的 sizes 臨時表中的size列
       CONCAT('properties/1/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248f1', size ,'.png') -- 將圖片URL組合成完整的URL
FROM (VALUES (1), (2), (3)) AS sizes(size) -- 創建一個包含尺寸的臨時表sizes
CROSS JOIN picture -- 與 picture 表交叉組合以獲取圖片 picture_id
WHERE picture_url = 'properties/1/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653811.png';

-- 為圖片12在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/1/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248g1', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
CROSS JOIN picture
WHERE picture_url = 'properties/1/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653812.png';

-- 為圖片13在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/1/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248h1', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/1/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653813.png';

-- 為圖片21在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/2/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248f2', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/2/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653821.png';

-- 為圖片22在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/2/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248g2', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/2/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653822.png';

-- 為圖片23在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/2/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248h2', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/2/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653823.png';

-- 為圖片31在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/3/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248f3', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/3/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653831.png';

-- 為圖片32在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/3/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248g3', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/3/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653832.png';

-- 為圖片33在 picture_dt 表插入詳細資訊（大、中、小）
INSERT INTO picture_dt (picture_id, picture_dt_size, picture_dt_url)
SELECT picture_id,
       size,
       CONCAT('properties/3/size-', size, '/d7640f50-66d4-4c3a-bb4f-f34837248h3', size ,'.png')
FROM (VALUES (1), (2), (3)) AS sizes(size)
         CROSS JOIN picture
WHERE picture_url = 'properties/3/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653833.png';