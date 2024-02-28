CREATE TABLE IF NOT EXISTS booking_availability
(
    booking_availability_id BIGSERIAL PRIMARY KEY NOT NULL,
    booking_availability_status VARCHAR(150) NOT NULL,
    booking_availability_date DATE NOT NULL,
    booking_availability_day_of_week VARCHAR(150) NOT NULL,
    property_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id)
)