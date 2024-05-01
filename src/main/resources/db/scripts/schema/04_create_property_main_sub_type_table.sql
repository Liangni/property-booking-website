CREATE TABLE IF NOT EXISTS property_main_sub_type
(
    property_main_sub_type_id BIGSERIAL PRIMARY KEY,
    property_main_type_id BIGINT NOT NULL,
    property_sub_type_id BIGINT NOT NULL,
    FOREIGN KEY (property_main_type_id) REFERENCES property_main_type(property_main_type_id),
    FOREIGN KEY (property_sub_type_id) REFERENCES property_sub_type(property_sub_type_id)
);