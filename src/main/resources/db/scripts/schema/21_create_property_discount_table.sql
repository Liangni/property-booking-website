CREATE TABLE IF NOT EXISTS property_discount
(
    property_discount_id BIGSERIAL PRIMARY KEY NOT NULL,
    property_id BIGINT NOT NULL,
    discount_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id),
    FOREIGN KEY (discount_id) REFERENCES discount(discount_id)
)