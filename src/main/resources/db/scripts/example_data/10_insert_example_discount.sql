INSERT INTO discount (
    discount_name,
    discount_value,
    discount_unit,
    least_num_of_booking_days,
    discount_is_active,
    property_id
)
VALUES (
           '7-days-discount',
           10,
           'percent',
           7,
            true,
           (SELECT property_id FROM property WHERE property_title = 'test property 1')
       ),
       (
           '14-days-discount',
           15,
           'percent',
           14,
            false,
           (SELECT property_id FROM property WHERE property_title = 'test property 1')
       )
