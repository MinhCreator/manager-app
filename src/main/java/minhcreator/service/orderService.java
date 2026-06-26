package minhcreator.service;

import static minhcreator.component.page.Login.login;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.formdev.flatlaf.FlatClientProperties;

import minhcreator.entity.InvoiceDetailEntity;
import minhcreator.entity.InvoiceEntity;
import minhcreator.functional.database.HibernateUtil;
import minhcreator.main.Application;
import minhcreator.util.UIRefreshScheduler;
import minhcreator.util.global;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

public class orderService {
    private static final String[] COLUMNS = { "PID", "UPID", "Name", "Quantity", "Price", "Total" };
    static DefaultTableModel modelCart = new DefaultTableModel(COLUMNS, 0);
    public JTable tblCart = new JTable(modelCart);
    private static JPanel CartPanel;
    public static JLabel lblTotal = new JLabel("Total: $0.00");
    private static JLabel Cartlb, nameProduct;
    private static JTextField CartTF;
    private static JButton AddCart;
    private static JButton Cancel;
    private static double total;

    private static synchronized void addToCart(int pid, String upid, String name, int qty, double price) {
        double subtotal = qty * price;
        modelCart.addRow(new Object[] { pid, upid, name, qty, price, subtotal });
        updateTotalAmount();
        UIRefreshScheduler.getInstance().markDataChanged();
    }

    public void removeProduct() {
        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        int selectedRow = tblCart.getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                    "Select a row to delete.");
            return;
        }
        model.removeRow(selectedRow);
        updateTotalAmount();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER,
                "Selected products deleted from cart successfully");
    }

    public static void addToCartButton(int pid, String upid, String name, double price, int currQty) {
        if (currQty <= 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                    "The selected product is currently out of stock.");
            return;
        }
        JDialog dialog = new JDialog();
        dialog.setSize(450, 500);
        dialog.setTitle("Add to Cart");
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        CartPanel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));

        Cartlb = new JLabel();
        Cartlb.setText("Enter Product quantity");
        nameProduct = new JLabel("Product: " + name);
        CartTF = new JTextField();
        AddCart = new JButton("Add to cart");
        Cancel = new JButton("Cancel");
        AddCart.addActionListener(e -> {
            try {
                int EnterQty = Integer.parseInt(CartTF.getText().trim());
                if (EnterQty <= 0) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                            "Quantity must be greater than zero.");
                    return;
                }
                if (EnterQty <= currQty) {
                    addToCart(pid, upid, name, EnterQty, price);
                    dialog.dispose();
                    Notifications.getInstance().show(
                            Notifications.Type.SUCCESS,
                            Notifications.Location.TOP_CENTER,
                            "Added " + name + " to Cart !");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                            "The quantity imported exceeds the available quantity.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        Cancel.addActionListener(e -> dialog.dispose());
        CartPanel.add(nameProduct, "center");
        CartPanel.add(Cartlb);
        CartPanel.add(CartTF);
        CartPanel.add(AddCart, "split 2");
        CartPanel.add(Cancel);
        dialog.add(CartPanel);
        dialog.pack();
        dialog.setVisible(true);
    }

    public void clearCart() {
        modelCart.setRowCount(0);
        tblCart.setModel(modelCart);
        updateTotalAmount();
    }

    public static synchronized double updateTotalAmount() {
        total = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            total += (double) modelCart.getValueAt(i, 5);
        }
        lblTotal.setText("Total: " + String.format("$ %,.0f", total));
        return total;
    }

    public static int getTotal() {
        return USDToVND(total);
    }

    public void processCheckout(String customerName, String user_inv, String user_invoices,
            String user_invoice_details) {
        int userId = login.getSession().getUserId();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            double finalTotal = updateTotalAmount();
            InvoiceEntity invoice = new InvoiceEntity(userId, customerName, finalTotal, LocalDate.now());
            session.persist(invoice);
            session.flush();

            String sqlUpdateStock = "UPDATE InventoryEntity SET quantity = quantity - :qty WHERE productId = :pid AND quantity >= :qty";

            for (int i = 0; i < modelCart.getRowCount(); i++) {
                int pId = (int) modelCart.getValueAt(i, 0);
                int qty = Integer.parseInt(modelCart.getValueAt(i, 3).toString());
                double price = (double) modelCart.getValueAt(i, 4);

                InvoiceDetailEntity detail = new InvoiceDetailEntity(userId, invoice.getInvoiceId(), pId, qty, price);
                session.persist(detail);

                int affected = session.createQuery(sqlUpdateStock)
                        .setParameter("qty", qty)
                        .setParameter("pid", pId)
                        .executeUpdate();

                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER,
                        "Checkout successful!");

                if (affected == 0) {
                    throw new Exception("Product ID " + pId + " Insufficient stock!");
                }
            }

            tx.commit();
            modelCart.setRowCount(0);
            updateTotalAmount();
            UIRefreshScheduler.getInstance().markDataChanged();

        } catch (Exception e) {
            if (tx != null)
                try {
                    tx.rollback();
                } catch (Exception ignored) {
                }
            throw new RuntimeException("Shipment error: " + e.getMessage(), e);
        }
    }

    public void QRPayment(int totalAmount, JDialog parent, String customerName, String user_inv, String user_invoices,
            String user_invoice_details) {
        JDialog qrDialog = new JDialog(parent,
                "VietQR payment", true);
        qrDialog.setLayout(new MigLayout("wrap, fillx, insets 30 20 20 30", "fill,250:280"));
        qrDialog.setSize(400, 500);
        qrDialog.setLocationRelativeTo(parent);
        qrDialog.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(" Scan the QR code to pay", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(90, 70, 61));
        qrDialog.add(lblTitle);

        JLabel lblQR = new JLabel("Loading QR code...", SwingConstants.CENTER);
        qrDialog.add(lblQR);

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                String encodedName = URLEncoder.encode(global.ACCOUNTNAME, StandardCharsets.UTF_8.toString()).replace(
                        "+",
                        "%20");
                String encodedInfo = URLEncoder.encode(global.ADDINFO, StandardCharsets.UTF_8.toString()).replace("+",
                        "%20");

                String urlString = String.format(
                        "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%d&addInfo=%s&accountName=%s",
                        global.BANKCODE, global.ACCOUNTNUMBER, totalAmount, encodedInfo, encodedName);

                URL url = new URL(urlString);
                Image img = ImageIO.read(url);
                Image scaledImg = img.getScaledInstance(300, 350, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }

            @Override
            protected void done() {
                try {
                    lblQR.setText("");
                    lblQR.setIcon(get());
                } catch (Exception e) {
                    lblQR.setText("Network Error: Can't load QR code");
                }
            }
        }.execute();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton btnConfirm = new JButton("Confirm money received");
        JButton btnCancel = new JButton("Cancel Payment");
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:0; focusWidth:0; padding:5,10,5,10");
        btnCancel.putClientProperty(FlatClientProperties.STYLE,
                "background: #ff1100; foreground: #ffff; arc:15; borderWidth:0; focusWidth:0; padding:5,10,5,10");

        btnConfirm.addActionListener(e -> {
            processCheckout(customerName, user_inv, user_invoices, user_invoice_details);
        });
        btnCancel.addActionListener(e -> {
            qrDialog.dispose();
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER,
                    "This Payment was canceled");
        });

        bottomPanel.add(btnConfirm, "split 2");
        bottomPanel.add(btnCancel);
        qrDialog.add(bottomPanel);

        qrDialog.setVisible(true);
    }

    private static int USDToVND(double usd) {
        double currentVND = 26317.5;

        return (int) Math.round(usd * currentVND);
    }
}
