INSERT INTO bedroom (
    property_id,
    num_of_double_beds,
    num_of_single_beds
)
SELECT p.property_id, 1, 1
FROM (SELECT property_id
      FROM property
      WHERE property_title
                IN ('test property 1', 'test property 2', 'test property 3')
     ) p