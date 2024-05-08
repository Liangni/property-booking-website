CREATE TABLE IF NOT EXISTS wish_property
(
    wish_property_id BIGSERIAL PRIMARY KEY NOT NULL,
    property_id BIGINT NOT NULL,
    ec_user_id BIGINT NOT NULL,
    checkin_date DATE,
    checkout_date DATE,
    FOREIGN KEY (property_id) REFERENCES property(property_id),
    FOREIGN KEY (ec_user_id) REFERENCES ec_user(ec_user_id)
)