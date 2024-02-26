CREATE TABLE IF NOT EXISTS district
(
    district_id BIGSERIAL PRIMARY KEY,
    district_name VARCHAR(150),
    administrative_area_id BIGINT NOT NULL,
    parent_district_id BIGINT,
    FOREIGN KEY (parent_district_id) REFERENCES district(district_id),
    FOREIGN KEY (administrative_area_id) REFERENCES administrative_area(administrative_area_id)
)