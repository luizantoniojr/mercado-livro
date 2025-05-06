CREATE TABLE purchases(
    id int auto_increment primary key,
    customer_id int not null,
    nfe varchar(255),
    price DECIMAL(15,2) NOT NULL,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE purchase_books(
    purchase_id int not null,
    book_id int not null,
    FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    PRIMARY KEY (purchase_id, book_id)
);