CREATE TABLE IF NOT EXISTS booking_calendar
(
    booking_calendar_id BIGSERIAL PRIMARY KEY NOT NULL,
    booking_date DATE NOT NULL,
    property_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id)
)