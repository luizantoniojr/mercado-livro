CREATE TABLE customer_roles (
    customer_id INT NOT NULL,
    roles VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
