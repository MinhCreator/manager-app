# User Guide — Warehouse Manager

## Getting Started

### 1. Login / Sign Up

When you launch the application, you'll see the login screen:

- **First time?** Click "Sign up" to create an account (username, email, password)
- **Returning user?** Enter your email and password, check "I agree to our User Agreement", then click "Login"

> Passwords are securely hashed with BCrypt. Your credentials are never stored in plain text.

### 2. Navigating the Application

After login, use the **left sidebar menu** to navigate between sections:

| Menu Item | Description |
|---|---|
| **Dashboard** | Overview cards (profit, stock value, expenses) + recent stock additions |
| **Inventory** | Manage products: add, edit, delete, search, sort |
| **Analytics** | Charts: income/cost/profit pies, financial trend line |
| **Invoice** | View invoices, see invoice details, print PDF |
| **Settings** | Update your profile, change password, delete account, log out |
| **Log Viewer** | Real-time application events (info, warnings, errors) |
| **Configuration** | Edit database connection and app settings |

### 3. Inventory Management

**Add a product:**
1. Go to **Inventory**
2. Click **Add**
3. Fill in: UPID (unique product ID), Name, Category, Price, Selling Price, Quantity
4. Click **Add Product**

**Edit a product:**
1. Select a product row in the table
2. Click **Edit**
3. Modify fields. Use the dropdown to choose:
   - **IN** — Add stock quantity
   - **CHANGE** — Update name, category, UPID, prices
4. Click **Confirm**

**Delete a product:**
1. Check the checkbox(es) for product(s) to delete
2. Click **Delete**
3. Confirm deletion

**Search/Sort:**
- Use the **Search** field to find products by name, UPID, or category
- Use the **Sort by** dropdown to reorder the list

### 4. Creating an Invoice / Checkout

1. Go to **Inventory**
2. Select a product and click **Add to Cart**
3. Enter the quantity and confirm
4. Click **Cart Menu** to open the cart
5. Enter the **Customer Name**
6. Click **Checkout**
7. The invoice is saved and stock is deducted automatically

### 5. Viewing Invoices

1. Go to **Invoice**
2. All invoices are listed on the left. Click one to see details on the right.
3. **Print** — Generates a PDF invoice file
4. **Delete** — Removes the invoice (with confirmation)

### 6. Analytics Dashboard

1. Go to **Analytics**
2. Three pie charts show your Income, Cost, and Profit by product category
3. The line chart shows financial trends — use the date picker to select a range

### 7. Log Viewer

1. Go to **Log Viewer** (under System menu)
2. All application events are displayed in real-time
3. Use the filter dropdown to show only INFO, WARNING, ERROR, or DEBUG messages
4. Click **Clear** to empty the log

### 8. Configuration

1. Go to **Configuration** (under System menu)
2. Edit database settings (URL, user, password) or window settings (title, dimensions)
3. Click **Save Configuration** — changes take effect after restart

### 9. Theme Settings

- Use the **light/dark toggle** at the bottom of the sidebar menu
- Click the **accent color** button to change the UI accent color

### 10. Managing Your Account

- Go to **Settings** to update your email, username, or password
- Click **Log out** to return to the login screen
- Click **Delete me** to permanently delete your account and all associated data
