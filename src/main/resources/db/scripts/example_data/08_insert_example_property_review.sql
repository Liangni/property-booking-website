INSERT INTO property_review(
    property_review_cleanliness,
    property_review_accuracy,
    property_review_checkin,
    property_review_communication,
    property_review_location,
    property_review_value,
    property_review_comment,
    property_review_created_at,
    property_id,
    customer_id)
SELECT 5, 5, 5, 5, 5, 5, 'test comment', CURRENT_DATE, p.property_id, eu.ec_user_id
FROM (SELECT property_id
      FROM property
      WHERE property_title
      IN ('test property 1', 'test property 2', 'test property 3')
     ) p,
     (SELECT ec_user_id
      FROM ec_user
      WHERE ec_user_name = 'test user 2'
     ) eu;