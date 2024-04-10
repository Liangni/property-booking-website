CREATE TABLE IF NOT EXISTS discount
(
    discount_id BIGSERIAL PRIMARY KEY NOT NULL,
    discount_name VARCHAR(150) NOT NULL,
    least_num_of_booking_days INTEGER NOT NULL DEFAULT 0,
    discount_value DOUBLE PRECISION NOT NULL,
    discount_unit VARCHAR(150) NOT NULL,
    discount_is_active BOOLEAN NOT NULL DEFAULT true,
    discount_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    property_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id)
)