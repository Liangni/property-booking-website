CREATE TABLE IF NOT EXISTS booking_order
(
    booking_order_id BIGSERIAL PRIMARY KEY NOT NULL,
    checkin_date DATE NOT NULL,
    checkout_date DATE NOT NULL,
    guest_name VARCHAR(150) NOT NULL,
    guest_email VARCHAR(150) NOT NULL,
    guest_phone VARCHAR(150),
    arrival_time VARCHAR(100) NOT NULL,
    guest_message TEXT,
    order_total NUMERIC NOT NULL,
    property_id BIGINT NOT NULL,
    host_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    discount_id BIGINT,
    payment_status VARCHAR(50) DEFAULT 'pending',
    FOREIGN KEY (property_id) REFERENCES property(property_id),
    FOREIGN KEY (host_id) REFERENCES ec_user(ec_user_id),
    FOREIGN KEY (customer_id) REFERENCES ec_user(ec_user_id),
    FOREIGN KEY (discount_id) REFERENCES discount(discount_id)
)