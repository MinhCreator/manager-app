-- PostgreSQL/Supabase schema for Warehouse Management System
-- Run this in the Supabase SQL Editor to create tables manually,
-- or let Hibernate auto-create them via hbm2ddl.auto=update.

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    UPID VARCHAR(50),
    name VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_products_user_id ON products(user_id);

-- Inventory table
CREATE TABLE IF NOT EXISTS inventory (
    product_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(255),
    price DOUBLE PRECISION,
    selling_price DOUBLE PRECISION,
    quantity INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_inventory_user_id ON inventory(user_id);

-- Purchase orders table
CREATE TABLE IF NOT EXISTS purchase_orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT,
    quantity INT,
    import_price DOUBLE PRECISION,
    date DATE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_purchase_orders_user_id ON purchase_orders(user_id);

-- Sales orders table
CREATE TABLE IF NOT EXISTS sales_orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT,
    quantity INT,
    selling_price DOUBLE PRECISION,
    date DATE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sales_orders_user_id ON sales_orders(user_id);

-- Invoices table
CREATE TABLE IF NOT EXISTS invoices (
    invoice_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    customer_name VARCHAR(255),
    total_amount DOUBLE PRECISION,
    created_at DATE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_invoices_user_id ON invoices(user_id);

-- Invoice details table
CREATE TABLE IF NOT EXISTS invoice_details (
    detail_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    invoice_id INT,
    product_id INT,
    quantity INT,
    unit_price DOUBLE PRECISION,
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_invoice_details_invoice_id ON invoice_details(invoice_id);
CREATE INDEX IF NOT EXISTS idx_invoice_details_user_id ON invoice_details(user_id);
