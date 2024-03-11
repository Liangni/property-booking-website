INSERT INTO property_picture(property_id, picture_id, property_picture_order)
SELECT pro.property_id, pic.picture_id, subquery.order_num
FROM (
         SELECT 'www.fake.image11.jpg' AS picture_url, 1 AS order_num, 'test property 1' AS property_title
         UNION ALL
         SELECT 'www.fake.image12.jpg', 2, 'test property 1'
         UNION ALL
         SELECT 'www.fake.image13.jpg', 3, 'test property 1'
         UNION ALL
         SELECT 'www.fake.image21.jpg', 1, 'test property 2'
         UNION ALL
         SELECT 'www.fake.image22.jpg', 2, 'test property 2'
         UNION ALL
         SELECT 'www.fake.image23.jpg', 3, 'test property 2'
         UNION ALL
         SELECT 'www.fake.image31.jpg', 1, 'test property 3'
         UNION ALL
         SELECT 'www.fake.image32.jpg', 2, 'test property 3'
         UNION ALL
         SELECT 'www.fake.image33.jpg', 3, 'test property 3'
     ) AS subquery
         JOIN property pro ON pro.property_title = subquery.property_title
         JOIN picture pic ON pic.picture_url = subquery.picture_url;