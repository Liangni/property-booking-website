INSERT INTO booking_order(
    checkin_date,
    checkout_date,
    guest_name,
    guest_email,
    guest_phone,
    arrival_time,
    order_total,
    property_id,
    host_id,
    customer_id
) SELECT current_date + INTERVAL '7 days',
         current_date + INTERVAL '10 days',
         'test guest name',
         'test@test.com',
         '0928825252',
         '11:00-13:00',
         9000,
         property_id,
         host_id,
         (host_id + 1)
    FROM property where property_title = 'test property 1'