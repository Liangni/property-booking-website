INSERT INTO property_discount (
    property_id,
    discount_id
)
SELECT p.property_id, d.discount_id
FROM (SELECT property_id
      FROM property
      WHERE property_title
        IN ('test property 1', 'test property 2', 'test property 3')
     ) p,
     (SELECT discount_id
      FROM discount
      WHERE (least_num_of_booking_days = 7
          AND discount_value = 10)
          OR (least_num_of_booking_days = 14
          AND discount_value = 15)
     ) d;