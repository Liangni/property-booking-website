CREATE TABLE IF NOT EXISTS amenity
(
    amenity_id BIGSERIAL NOT NULL,
    amenity_name VARCHAR(150),
    amenity_type_id BIGINT NOT NULL
)