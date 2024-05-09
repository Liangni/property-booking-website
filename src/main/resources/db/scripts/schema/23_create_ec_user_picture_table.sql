CREATE TABLE IF NOT EXISTS ec_user_picture
(
    ec_user_picture_id BIGSERIAL PRIMARY KEY NOT NULL,
    ec_user_id BIGINT NOT NULL,
    picture_id BIGINT NOT NULL,
    FOREIGN KEY (picture_id) REFERENCES picture(picture_id),
    FOREIGN KEY (ec_user_id) REFERENCES ec_user(ec_user_id)
)