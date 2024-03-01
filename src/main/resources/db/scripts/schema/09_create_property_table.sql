CREATE TABLE IF NOT EXISTS property
(
    property_id BIGSERIAL PRIMARY KEY NOT NULL,
    property_title VARCHAR(150) NOT NULL,
    property_description TEXT,
    max_num_of_guests INT,
    num_of_bedrooms INT,
    num_of_beds INT,
    num_of_bathrooms INT,
    price_on_weekdays BIGINT,
    price_on_weekends BIGINT,
    is_published BOOLEAN NOT NULL DEFAULT false,
    average_rating DOUBLE PRECISION,
    property_group_type_id BIGINT,
    property_share_type_id BIGINT,
    address_id BIGINT,
    host_id BIGINT NOT NULL,
    FOREIGN KEY (property_group_type_id) REFERENCES property_group_type(property_group_type_id),
    FOREIGN KEY (property_share_type_id) REFERENCES property_share_type(property_share_type_id),
    FOREIGN KEY (address_id) REFERENCES address(address_id),
    FOREIGN KEY (host_id) REFERENCES ec_user(ec_user_id)
);