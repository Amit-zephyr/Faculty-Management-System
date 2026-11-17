CREATE DATABASE IF NOT EXISTS faculty_leave_db;
USE faculty_leave_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('Faculty', 'Admin / HOD', 'Substitute Faculty') NOT NULL,
    email VARCHAR(100),
    contact_no VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS leave_applications (
    leave_id INT AUTO_INCREMENT PRIMARY KEY,
    faculty_id INT NOT NULL,
    leave_type VARCHAR(50) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    reason TEXT,
    contact_no VARCHAR(15),
    email VARCHAR(100),
    status ENUM('Pending', 'Approved', 'Rejected') NOT NULL DEFAULT 'Pending',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_leave_faculty FOREIGN KEY (faculty_id) REFERENCES users(user_id),
    CONSTRAINT chk_leave_dates CHECK (to_date >= from_date)
);

CREATE TABLE IF NOT EXISTS substitution (
    substitution_id INT AUTO_INCREMENT PRIMARY KEY,
    leave_id INT NULL,
    absent_faculty_id INT NOT NULL,
    substitute_faculty_id INT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    substitution_date DATE NOT NULL,
    period VARCHAR(30) NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sub_leave FOREIGN KEY (leave_id) REFERENCES leave_applications(leave_id),
    CONSTRAINT fk_sub_absent FOREIGN KEY (absent_faculty_id) REFERENCES users(user_id),
    CONSTRAINT fk_sub_substitute FOREIGN KEY (substitute_faculty_id) REFERENCES users(user_id)
);

-- Demo accounts for testing only. Change passwords before real deployment.
INSERT INTO users (name, username, password_hash, role, email, contact_no)
VALUES
('Amit Sharma', 'amit', 'amit123', 'Faculty', 'amit@gmail.com', '9876543210'),
('Rahul Sharma', 'rahul', 'rahul123', 'Substitute Faculty', 'rahul@gmail.com', '9876543211'),
('Neha Verma', 'neha', 'neha123', 'Faculty', 'neha@gmail.com', '9876543212'),
('Priya Singh', 'admin', 'admin123', 'Admin / HOD', 'priya@gmail.com', '9876543213')
ON DUPLICATE KEY UPDATE username = VALUES(username);

SELECT * FROM users;
SELECT * FROM leave_applications;
SELECT * FROM substitution;
