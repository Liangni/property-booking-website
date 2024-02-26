CREATE TABLE IF NOT EXISTS administrative_area
(
    administrative_area_id BIGSERIAL PRIMARY KEY,
    administrative_area_name VARCHAR(150),
    administrative_area_level BIGINT NOT NULL
)