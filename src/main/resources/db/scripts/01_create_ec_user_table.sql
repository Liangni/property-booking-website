CREATE TABLE IF NOT EXISTS ec_user
(
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    email VARCHAR(30) NOT NULL,
    password VARCHAR(100) NOT NULL,
    city VARCHAR(60),
    country_id BIGINT
);