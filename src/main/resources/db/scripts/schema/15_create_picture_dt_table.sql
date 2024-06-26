CREATE TABLE IF NOT EXISTS picture_dt
(
    picture_dt_id BIGSERIAL PRIMARY KEY NOT NULL,
    picture_dt_size INTEGER NOT NULL,
    picture_dt_storage_path VARCHAR(200) NOT NULL,
    picture_dt_is_uploaded Boolean DEFAULT false,
    picture_id BIGINT NOT NULL,
    FOREIGN KEY (picture_id) REFERENCES picture(picture_id)
);