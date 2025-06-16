-- Alter universities table to use a foreign key for facility admin
ALTER TABLE universities 
    ADD COLUMN IF NOT EXISTS facility_admin_user_id BIGINT,
    ADD CONSTRAINT fk_university_facility_admin FOREIGN KEY (facility_admin_user_id) REFERENCES users(id) ON DELETE SET NULL;
