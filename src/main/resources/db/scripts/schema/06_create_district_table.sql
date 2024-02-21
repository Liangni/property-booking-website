CREATE TABLE IF NOT EXISTS district
(
    district_id BIGSERIAL PRIMARY KEY,
    district_name VARCHAR(150),
    parent_district_id BIGINT NOT NULL,
    FOREIGN KEY (parent_district_id) REFERENCES district(district_id)
)