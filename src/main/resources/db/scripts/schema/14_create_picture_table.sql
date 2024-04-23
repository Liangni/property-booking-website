CREATE TABLE IF NOT EXISTS picture
(
    picture_id BIGSERIAL PRIMARY KEY NOT NULL,
    picture_storage_path VARCHAR(200) NOT NULL,
    picture_is_uploaded Boolean DEFAULT false
)