CREATE TABLE IF NOT EXISTS address
(
    address_id BIGSERIAL PRIMARY KEY,
    apartment_floor VARCHAR(150),
    street VARCHAR(150) NOT NULL,
    city VARCHAR(150) NOT NULL,
    state VARCHAR(150),
    postal_code BIGINT,
    country_id BIGINT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
)