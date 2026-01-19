CREATE DATABASE warehouse;


CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    UPID VARCHAR(50) UNIQUE, -- unique product id
    name VARCHAR(255)
);

CREATE TABLE inventory (
    product_id INT PRIMARY KEY,
    UPID VARCHAR(255) UNIQUE,
    category VARCHAR(255),
    price DOUBLE,
    selling_price DOUBLE,
    quantity INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (UPID) REFERENCES products(UPID)
);

-- Lưu chi phí (Cost)
CREATE TABLE purchase_orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    quantity INT,
    import_price DOUBLE,
    date DATE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Lưu doanh thu (Revenue)
CREATE TABLE sales_orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    quantity INT,
    selling_price DOUBLE,
    date DATE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Bảng chính của Hóa đơn
CREATE TABLE invoices (
    invoice_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(255),
    total_amount DOUBLE,
    created_at DATE
);

-- Chi tiết từng dòng trong hóa đơn
CREATE TABLE invoice_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    product_id INT,
    quantity INT,
    unit_price DOUBLE, -- Giá bán thực tế lúc đó (đã trừ chiết khấu nếu có)
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);