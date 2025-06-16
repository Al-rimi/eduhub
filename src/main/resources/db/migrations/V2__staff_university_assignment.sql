-- Migration: Add staff-university assignment join table
CREATE TABLE IF NOT EXISTS staff_university_assignments (
    staff_id BIGINT NOT NULL,
    university_id BIGINT NOT NULL,
    PRIMARY KEY (staff_id, university_id),
    CONSTRAINT fk_staff FOREIGN KEY (staff_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_university FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE
);
