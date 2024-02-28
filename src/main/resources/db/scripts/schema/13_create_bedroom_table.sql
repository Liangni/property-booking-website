CREATE TABLE IF NOT EXISTS bedroom
(
    bedroom_id BIGSERIAL PRIMARY KEY NOT NULL,
    num_of_single_beds INT NOT NULL DEFAULT 0,
    num_of_double_beds INT NOT NULL DEFAULT 0,
    property_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id)
)