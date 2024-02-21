CREATE TABLE IF NOT EXISTS property_group_type
(
    property_group_type_id BIGSERIAL PRIMARY KEY,
    property_group_id BIGINT NOT NULL,
    property_type_id BIGINT NOT NULL,
    FOREIGN KEY (property_group_id) REFERENCES property_group(property_group_id),
    FOREIGN KEY (property_type_id) REFERENCES property_type(property_type_id)
);