ALTER TABLE ec_user
ADD CONSTRAINT fk_country
FOREIGN KEY (country_id)
REFERENCES country(country_id)
ON DELETE RESTRICT;