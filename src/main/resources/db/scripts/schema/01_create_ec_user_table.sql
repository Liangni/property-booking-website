CREATE TABLE IF NOT EXISTS ec_user
(
    ec_user_id BIGSERIAL PRIMARY KEY,
    ec_user_name VARCHAR(30) NOT NULL,
    ec_user_email VARCHAR(30) NOT NULL,
    ec_user_hashed_password VARCHAR(100) NOT NULL,
    ec_user_introduction TEXT
);