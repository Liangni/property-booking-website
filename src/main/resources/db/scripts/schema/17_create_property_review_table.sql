CREATE TABLE IF NOT EXISTS property_review
(
    property_review_id BIGSERIAL PRIMARY KEY NOT NULL,
    property_review_cleanliness INT NOT NULL,
    property_review_accuracy INT NOT NULL,
    property_review_checkin INT NOT NULL,
    property_review_communication INT NOT NULL,
    property_review_location INT NOT NULL,
    property_review_value INT NOT NULL,
    property_review_comment TEXT,
    property_review_created_at TIMESTAMP NOT NULL,
    property_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id),
    FOREIGN KEY (customer_id) REFERENCES ec_user(ec_user_id)
)