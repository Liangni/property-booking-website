CREATE TABLE IF NOT EXISTS address
(
    address_id BIGSERIAL PRIMARY KEY,
    apartment_floor VARCHAR(150),
    street VARCHAR(150) NOT NULL,
    admin_area_level_3_district_id BIGINT,
    FOREIGN KEY (admin_area_level_3_district_id) REFERENCES district(district_id)
)