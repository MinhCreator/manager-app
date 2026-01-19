package minhcreator.service;

import minhcreator.component.ModularPanel.WarehousePanel;
import minhcreator.functional.database.DB;
import minhcreator.main.Application;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Order Service class provides methods for managing order operations.
 *
 * @author MinhCreatorVN
 * @version 1.0 PreTest
 */
public class orderService {
    public static DefaultTableModel modelCart = new DefaultTableModel(new String[]{"PID", "UPID", "Name", "Quantity", "Price", "Total"}, 0);
    public JTable tblCart = new JTable(modelCart);
    private static JPanel CartPanel;
    //    JLabel lblTotal;
    public static JLabel lblTotal = new JLabel("Total: $0.00");
    private static JLabel Cartlb;
    private static JTextField CartTF;
    private static JButton AddCart;
    private static JButton Cancel;
    private static orderPanel panel = new orderPanel();

    // add product to cart

    /**
     * Adds a product to the cart.
     *
     * @param pid   the product ID
     * @param upid  the user product ID
     * @param name  the product name
     * @param qty   the quantity of the product
     * @param price the price of the product
     */
    private static void addToCart(int pid, String upid, String name, int qty, double price) {
        double subtotal = qty * price;
        modelCart.addRow(new Object[]{pid, upid, name, qty, price, subtotal});
        updateTotalAmount(); // Cập nhật tổng tiền hiển thị trên giao diện

        if (panel != null) {
            panel.refreshCartDisplay();
        }
    }

    // remove product from cart

    /**
     * Removes a product from the cart.
     *
     * @throws NullPointerException if panel is null
     */
    public void removeProduct() {
        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        int rowCount = model.getRowCount();
        boolean anyDeleted = false;
        int selectedRow = tblCart.getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Select a row to delete.");
            return;
        }

        model.removeRow(selectedRow);
        updateTotalAmount();
        Notifications.getInstance().show(
                Notifications.Type.SUCCESS,
                Notifications.Location.TOP_CENTER,
                "Selected products deleted from cart successfully"
        );
    }


    //add product to cart button

    /**
     * Adds a product to the cart when the "Add to Cart" button is clicked.
     *
     * @param pid     the product ID
     * @param upid    the user product ID
     * @param name    the product name
     * @param price   the price of the product
     * @param currQty the current quantity of the product
     */
    public static void addToCartButton(int pid, String upid, String name, double price, int currQty) {
        JDialog dialog = new JDialog();
        dialog.setSize(450, 500);
        dialog.setTitle("Add to Cart");
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        CartPanel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));

        Cartlb = new JLabel();
        Cartlb.setText("Enter Product quantity");
        CartTF = new JTextField();
        AddCart = new JButton("Add to cart");
        Cancel = new JButton("Cancel");
        if (currQty == 0) {
            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "The selected product is currently out of stock.");
            return;
        }
        AddCart.addActionListener(e -> {
            try {
                int EnterQty = Integer.parseInt(CartTF.getText().trim());
                if (EnterQty <= currQty) {
                    orderService.addToCart(pid, upid, name, EnterQty, price);
                    dialog.dispose();
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "The quantity imported exceeds the available quantity.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });
        Cancel.addActionListener(e -> dialog.dispose());
        CartPanel.add(Cartlb);
        CartPanel.add(CartTF);
        CartPanel.add(AddCart, "split 2");
        CartPanel.add(Cancel);
        dialog.add(CartPanel);
        dialog.pack();
    }

    /**
     * Clears the contents of the shopping cart.
     * <p>
     * This method creates a new default table model with the column names
     * "PID", "UPID", "Name", "Quantity", "Price", and "Total", and sets it
     * as the model for the shopping cart table. It then calls the
     * {@link #updateTotalAmount()} method to update the total amount
     * displayed on the label.
     *
     * @see #updateTotalAmount()
     * @see javax.swing.table.DefaultTableModel
     */
    public void clearCart() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"PID", "UPID", "Name", "Quantity", "Price", "Total"}, 0);
        tblCart.setModel(model);
        updateTotalAmount();
    }

    // Hàm tính tổng tiền của cả giỏ hàng (hiển thị trên Label)

    /**
     * Updates the total amount displayed on the label.
     *
     * <p>This method iterates over each row in the shopping cart table model,
     * retrieves the total amount for each row, and calculates the sum of all
     * the totals. The sum is then used to update the text of the {@code lblTotal}
     * label, which is displayed on the user interface.
     *
     * @return the total amount of all items in the shopping cart
     */
    public static double updateTotalAmount() {
        double total = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            total += (double) modelCart.getValueAt(i, 5);
        }
        lblTotal = new JLabel();
        lblTotal.setText("Total: " + String.format("$ %,.0f", total));
        return total;
    }


    /**
     * Processes the checkout.
     *
     * <p>This method performs the following steps:
     * <ol>
     * <li>Creates a new database connection.</li>
     * <li>Starts a new transaction.</li>
     * <li>Creates a new invoice (if successful).</li>
     * <li>Adds invoice details (if successful).</li>
     * <li>Updates the product quantity in the inventory (if successful).</li>
     * <li>Closes the transaction.</li>
     * <li>Commits the transaction.</li>
     * <li>Displays a success notification.</li>
     * <li>Closes the checkout panel.</li>
     * </ol>
     *
     * @param customerName         the name of the customer
     * @param user_inv             the name of the inventory table
     * @param user_invoices        the name of the invoices table
     * @param user_invoice_details the name of the invoice details table
     */
    public void processCheckout(String customerName, String user_inv, String user_invoices, String user_invoice_details) {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false); // Quan trọng: Bắt đầu giao dịch

            // BƯỚC 1: Tạo Hóa đơn tổng (Invoices)
            String sqlInvoice = "INSERT INTO " + user_invoices + " (customer_name, total_amount, created_at) VALUES (?, ?, CURDATE())";
            PreparedStatement psInv = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS);

            double finalTotal = updateTotalAmount(); // Hàm tính tổng tiền từ tblCart
            psInv.setString(1, customerName);
            psInv.setDouble(2, finalTotal);
            psInv.executeUpdate();

            // Lấy ID hóa đơn vừa tạo tự động auto get ID
            ResultSet rs = psInv.getGeneratedKeys();
            int invoiceId = 0;
            if (rs.next()) invoiceId = rs.getInt(1);

            // BƯỚC 2: Duyệt từng dòng trong JTable để lưu Chi tiết & Trừ kho
            String sqlDetail = "INSERT INTO " + user_invoice_details + " (invoice_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE " + user_inv + " SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";

            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);

            for (int i = 0; i < tblCart.getRowCount(); i++) {
                int pId = (int) tblCart.getValueAt(i, 0);
                int qty = Integer.parseInt(tblCart.getValueAt(i, 3).toString());
                double price = (double) tblCart.getValueAt(i, 4);

                // Lưu chi tiết hóa đơn
                psDetail.setInt(1, invoiceId);
                psDetail.setInt(2, pId);
                psDetail.setInt(3, qty);
                psDetail.setDouble(4, price);
                psDetail.addBatch(); // Gom lại để thực thi một lần (Batch)

                // Cập nhật kho (Trừ số lượng)
                psStock.setInt(1, qty);
                psStock.setInt(2, pId);
                psStock.setInt(3, qty); // Kiểm tra tồn kho >= số lượng xuất

                int affected = psStock.executeUpdate();
                if (affected == 0) {
                    throw new Exception("Product ID " + pId + " Insufficient stock!");
                }
            }
            psDetail.executeBatch(); // Thực thi lưu tất cả chi tiết

            conn.commit(); // Hoàn tất giao dịch
            JOptionPane.showMessageDialog(Application.getInstance(), "Shipment successful! Total amount: " + finalTotal);
            WarehousePanel WP = new WarehousePanel();
            WP.reloadUI();
            // Sau đó gọi hàm xuất file PDF hóa đơn đã học ở trên
//            exportInvoicePDF(invoiceId, customerName, getListFromCart());

        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            } // Cancel when caused error
            JOptionPane.showMessageDialog(Application.getInstance(), "Shipment error: " + e.getMessage());
        }
    }
}