-- Migration script: Per-user tables → Unified Hibernate schema
-- 
-- The OLD schema had per-user tables named like:
--   {username}_products, {username}_inventory, {username}_purchase_orders,
--   {username}_sales_orders, {username}_invoices, {username}_invoice_details
--
-- The NEW schema uses shared tables with a user_id column.
--
-- Run this script ONCE to migrate existing data.

-- 1. Create new unified tables (if not already created by Hibernate)
--    Hibernate's hbm2ddl.auto=update handles this automatically.

-- 2. Migrate data from per-user tables to unified tables
--    Replace 'username1', 'username2' with actual usernames.

-- Example per-user migration:
-- INSERT INTO products (user_id, UPID, name)
-- SELECT u.id, p.UPID, p.name
-- FROM username1_products p
-- JOIN users u ON u.username = 'username1';
--
-- INSERT INTO inventory (product_id, user_id, category, price, selling_price, quantity)
-- SELECT p.id, u.id, i.category, i.price, i.selling_price, i.quantity
-- FROM username1_inventory i
-- JOIN products p ON ...  -- requires mapping old IDs to new ones
-- JOIN users u ON u.username = 'username1';

-- NOTE: Due to AUTO_INCREMENT ID changes, manual migration
-- per user is recommended. Or use the application's fresh start
-- by signing up again and re-adding data.

-- The Hibernate hbm2ddl.auto=update setting will auto-create
-- the unified tables on first application startup.
