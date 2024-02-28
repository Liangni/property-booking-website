CREATE TABLE IF NOT EXISTS property_picture
(
    property_picture_id BIGSERIAL PRIMARY KEY NOT NULL,
    property_id BIGINT NOT NULL,
    picture_id BIGINT NOT NULL,
    property_picture_order BIGINT NOT NULL,
    FOREIGN KEY (property_id) REFERENCES property(property_id),
    FOREIGN KEY (picture_id) REFERENCES picture(picture_id)
)