CREATE TABLE IF NOT EXISTS address
(
    address_id BIGSERIAL PRIMARY KEY,
    apartment_floor VARCHAR(150),
    street VARCHAR(150) NOT NULL,
    district_id BIGINT NOT NULL,
    FOREIGN KEY (district_id) REFERENCES district(district_id)
)