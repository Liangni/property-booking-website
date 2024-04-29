CREATE TABLE IF NOT EXISTS discount
(
    discount_id BIGSERIAL PRIMARY KEY NOT NULL,
    least_num_of_booking_days INTEGER NOT NULL DEFAULT 0,
    discount_value DOUBLE PRECISION NOT NULL
)