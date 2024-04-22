INSERT INTO property_picture(property_id, picture_id, property_picture_order)
SELECT pro.property_id, pic.picture_id, subquery.order_num
FROM (
         SELECT 'properties/1/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653811.png' AS picture_url, 1 AS order_num, 'test property 1' AS property_title
         UNION ALL
         SELECT 'properties/1/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653812.png', 2, 'test property 1'
         UNION ALL
         SELECT 'properties/1/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653813.png', 3, 'test property 1'
         UNION ALL
         SELECT 'properties/2/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653821.png', 1, 'test property 2'
         UNION ALL
         SELECT 'properties/2/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653822.png', 2, 'test property 2'
         UNION ALL
         SELECT 'properties/2/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653823.png', 3, 'test property 2'
         UNION ALL
         SELECT 'properties/3/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653831.png', 1, 'test property 3'
         UNION ALL
         SELECT 'properties/3/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653832.png', 2, 'test property 3'
         UNION ALL
         SELECT 'properties/3/size-original/7f3b28ab-a802-43cc-bca6-14b0c6653833.png', 3, 'test property 3'
     ) AS subquery
         JOIN property pro ON pro.property_title = subquery.property_title
         JOIN picture pic ON pic.picture_url = subquery.picture_url;