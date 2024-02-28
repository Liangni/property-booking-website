CREATE TABLE property_amenity
(
    property_amenity_id BIGSERIAL PRIMARY KEY NOT NULL,
    property_id BIGINT NOT NULL,
    amenity_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id),
    FOREIGN KEY (amenity_id) REFERENCES amenity(amenity_id)
);