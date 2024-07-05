CREATE DATABASE inventory_management_system;
USE inventory_management_system;

CREATE TABLE categories(
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO categories(name)
VALUES ('electronics'),
       ('furniture'),
       ('clothing'),
       ('books'),
       ('toys');

CREATE TABLE items(
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

INSERT INTO items(name, brand, model, sku, quantity, price, category_id)
VALUES ('Laptop', 'Apple', 'MacBook Pro', 'MBP2021', 10, 235000, 1),
       ('Laptop', 'Dell', 'Inspiron', 'INS2021', 15, 35000, 1),
       ('Sofa', 'Ikea', 'Ektorp', 'SOFA123', 20, 4500, 2),
       ('T-shirt', 'Nike', 'Sportswear', 'TSHIRT001', 100, 450, 3),
       ('Children\'s Book', 'Scholastic', 'Harry Potter', 'BOOK001', 15, 150, 4);

CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL
);

INSERT INTO users(name, username, password, role)
VALUES ('Makechi Eric', 'admin', 'admin', 'ADMIN'),
       ('Makbe Mkuu', 'user', 'password', 'USER');
