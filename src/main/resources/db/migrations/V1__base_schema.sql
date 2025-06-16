-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(32) NOT NULL
);

-- Universities table
CREATE TABLE IF NOT EXISTS universities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    image_url VARCHAR(255),
    facility_admin_user_id BIGINT,
    CONSTRAINT fk_university_facility_admin FOREIGN KEY (facility_admin_user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Majors table
CREATE TABLE IF NOT EXISTS majors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    university_id BIGINT NOT NULL,
    CONSTRAINT fk_major_university FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE
);

-- Applications table
CREATE TABLE IF NOT EXISTS applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    university_id BIGINT NOT NULL,
    major_id BIGINT NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    application_data TEXT,
    CONSTRAINT fk_application_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_application_university FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE,
    CONSTRAINT fk_application_major FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE
);

-- University requirements (as a collection table)
CREATE TABLE IF NOT EXISTS university_requirements (
    university_id BIGINT NOT NULL,
    requirement TEXT,
    CONSTRAINT fk_requirement_university FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE
);
