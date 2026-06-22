package minhcreator.service;

import minhcreator.component.ModularPanel.WarehousePanel;
import minhcreator.entity.InvoiceDetailEntity;
import minhcreator.entity.InvoiceEntity;
import minhcreator.functional.database.HibernateUtil;
import minhcreator.main.Application;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

import static minhcreator.component.page.Login.login;

public class orderService {
    public static DefaultTableModel modelCart = new DefaultTableModel(new String[]{"PID", "UPID", "Name", "Quantity", "Price", "Total"}, 0);
    public JTable tblCart = new JTable(modelCart);
    private static JPanel CartPanel;
    public static JLabel lblTotal = new JLabel("Total: $0.00");
    private static JLabel Cartlb;
    private static JTextField CartTF;
    private static JButton AddCart;
    private static JButton Cancel;
    private static orderPanel panel = new orderPanel();

    private static void addToCart(int pid, String upid, String name, int qty, double price) {
        double subtotal = qty * price;
        modelCart.addRow(new Object[]{pid, upid, name, qty, price, subtotal});
        updateTotalAmount();

        if (panel != null) {
            panel.refreshCartDisplay();
        }
    }

    public void removeProduct() {
        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        int selectedRow = tblCart.getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Select a row to delete.");
            return;
        }
        model.removeRow(selectedRow);
        updateTotalAmount();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Selected products deleted from cart successfully");
    }

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
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "The selected product is currently out of stock.");
            return;
        }
        AddCart.addActionListener(e -> {
            try {
                int EnterQty = Integer.parseInt(CartTF.getText().trim());
                if (EnterQty <= currQty) {
                    addToCart(pid, upid, name, EnterQty, price);
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

    public void clearCart() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"PID", "UPID", "Name", "Quantity", "Price", "Total"}, 0);
        tblCart.setModel(model);
        updateTotalAmount();
    }

    public static double updateTotalAmount() {
        double total = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            total += (double) modelCart.getValueAt(i, 5);
        }
        lblTotal = new JLabel();
        lblTotal.setText("Total: " + String.format("$ %,.0f", total));
        return total;
    }

    public void processCheckout(String customerName, String user_inv, String user_invoices, String user_invoice_details) {
        int userId = login.getSession().getUserId();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            double finalTotal = updateTotalAmount();
            InvoiceEntity invoice = new InvoiceEntity(userId, customerName, finalTotal, LocalDate.now());
            session.persist(invoice);

            String sqlUpdateStock = "UPDATE InventoryEntity SET quantity = quantity - :qty WHERE productId = :pid AND quantity >= :qty";

            for (int i = 0; i < tblCart.getRowCount(); i++) {
                int pId = (int) tblCart.getValueAt(i, 0);
                int qty = Integer.parseInt(tblCart.getValueAt(i, 3).toString());
                double price = (double) tblCart.getValueAt(i, 4);

                InvoiceDetailEntity detail = new InvoiceDetailEntity(userId, invoice.getInvoiceId(), pId, qty, price);
                session.persist(detail);

                int affected = session.createQuery(sqlUpdateStock)
                        .setParameter("qty", qty)
                        .setParameter("pid", pId)
                        .executeUpdate();
                if (affected == 0) {
                    throw new Exception("Product ID " + pId + " Insufficient stock!");
                }
            }

            tx.commit();
            JOptionPane.showMessageDialog(Application.getInstance(), "Shipment successful! Total amount: " + finalTotal);
            WarehousePanel WP = new WarehousePanel();
            WP.reloadUI();

        } catch (Exception e) {
            if (tx != null) try { tx.rollback(); } catch (Exception ignored) {}
            JOptionPane.showMessageDialog(Application.getInstance(), "Shipment error: " + e.getMessage());
        }
    }
}
