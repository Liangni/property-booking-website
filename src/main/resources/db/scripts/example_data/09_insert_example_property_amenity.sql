INSERT INTO property_amenity(amenity_id, property_id)
SELECT amenity_id, (
    SELECT
        property_id
    FROM property
    WHERE property_title = 'test property 1'
)
FROM amenity;
