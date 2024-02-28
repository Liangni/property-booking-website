CREATE TABLE IF NOT EXISTS amenity
(
    amenity_id BIGSERIAL PRIMARY KEY NOT NULL,
    amenity_name VARCHAR(150),
    amenity_type_id BIGINT NOT NULL
)