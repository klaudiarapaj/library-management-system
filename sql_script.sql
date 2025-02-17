-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS library;

-- Use the newly created database
USE library;

-- Create the 'users' table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    role ENUM('admin', 'user') NOT NULL
);

-- Create the 'library_items' table
CREATE TABLE IF NOT EXISTS library_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    year INT,
    price DOUBLE,
    type ENUM('book', 'magazine'),
    author VARCHAR(255),
    genre VARCHAR(255),
    issue_number INT,
    borrowed_status BOOLEAN DEFAULT FALSE,
    borrowed_by VARCHAR(255) DEFAULT NULL
);

-- Insert sample users (including an admin user)
INSERT INTO users (name, email, role) VALUES
    ('Admin', 'admin@gmail.com', 'admin'),
    ('Test', 'test@gmail.com', 'user');

-- Insert sample library items (books and magazines)
INSERT INTO library_items (title, year, price, type, author, genre, issue_number, borrowed_status, borrowed_by) VALUES
    ('Dune', 1965, 15.99, 'book', 'Frank Herbert', 'Science Fiction', NULL, FALSE, NULL),
    ('The Shining', 1977, 12.99, 'book', 'Stephen King', 'Horror', NULL, FALSE, NULL),
    ('Hamlet', 1600, 5.99, 'book', 'William Shakespeare', 'Tragedy', NULL, FALSE, NULL),
    ('It Ends with Us', 2016, 16.50, 'book', 'Colleen Hoover', 'Romance', NULL, FALSE, NULL),
    ('Rolling', 1967, 5.50, 'magazine', NULL, NULL, 1073, FALSE, NULL),
    ('Time', 1923, 10.50, 'magazine', NULL, NULL, 204, FALSE, NULL),
    ('Cosmopolitan', 1886, 7.00, 'magazine', NULL, NULL, 876, FALSE, NULL);

