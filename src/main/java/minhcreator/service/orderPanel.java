package minhcreator.service;

import com.formdev.flatlaf.FlatClientProperties;

import minhcreator.component.stock.StockStatus;
import minhcreator.component.stock.TableBadgeCellRenderer;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.util.concurrent.ExecutionException;

import static minhcreator.component.page.Login.login;

/**
 * Panel for displaying and managing the user's shopping cart.
 */
public class orderPanel extends JDialog {
    private JTable table = new JTable();
    private JPanel Rightpanel, controlPanel, NamePanel, sortPanel, bottomPanel, StockPanel, TotalPanel;
    private JLabel lbl_Name, lblSort, StockID, StockName, StockPrice, StockAmount, StockSellPrice, StockCategory;
    private JButton searchButton, sortButton, Confirm_But, deleteButton, clearButton, CancelButton, Sale_Butt, addButton_add_stock_dialog, CancelBut_add_stock_dialog;
    private JTextField Customer_name_Field, StockIDField, StockNameField, StockPriceField, StockAmountField, StockSellPriceField, StockCategoryField;
    private JComboBox<String> sortByBox;
    private orderService orderService = new orderService();
    private final sessionManager session;
    private final String inv;
    private final String invoice;
    private final String invoice_detail;
    private static orderPanel instance;

    public orderPanel() {
        sessionManager s = login.getSession();
        if (s == null) {
            throw new IllegalStateException("Cannot create orderPanel without an active session");
        }
        session = s;
        inv = session.getYour_inventory();
        invoice = session.getUser_invoices();
        invoice_detail = session.getUser_invoice_details();
        init();
        initComponents();
    }

    /**
     * Returns the singleton instance of the orderPanel.
     *
     * @return the singleton instance of the orderPanel
     */
    public static synchronized orderPanel getInstance() {
        if (instance == null) {
            instance = new orderPanel();
        }
        return instance;
    }

    /**
     * Initializes the orderPanel.
     */
    private void init() {
        setLocationRelativeTo(Application.getInstance());
        setSize(new Dimension(950, 550));
        setLayout(new BorderLayout());
        setTitle("Cart menu");
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

    }

    /**
     * Overrides the setVisible method to ensure that the orderPanel is centered on the screen whenever it is made
     * visible.
     *
     * @param visible whether or not the orderPanel should be made visible
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            setLocationRelativeTo(Application.getInstance());
        }
        super.setVisible(visible);
    }

    /**
     * Initializes the components of the orderPanel.
     */
    private void initComponents() {

        Rightpanel = new JPanel();

        Rightpanel.setLayout(new BorderLayout(8, 8));
        Rightpanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 0),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        JScrollPane scrollPane = new JScrollPane(addTable());
        Rightpanel.add(add_Util_Panel(), BorderLayout.NORTH);
        Rightpanel.add(scrollPane, BorderLayout.CENTER);


        add(add_Function_Panel(), BorderLayout.SOUTH);
        add(Rightpanel);
    }

    /**
     * Adds a table to the orderPanel.
     *
     * @return the JScrollPane containing the table
     */
    public JComponent addTable() {
        table = orderService.tblCart;
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, render);

        TableBadgeCellRenderer.apply(table, StockStatus.class);
        return table;
    }

    /**
     * Adds a utility panel to the orderPanel containing a text field for entering the customer's name.
     *
     * @return the utility panel
     */
    public JComponent add_Util_Panel() {
        controlPanel = new JPanel(new BorderLayout());
        NamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        lbl_Name = new JLabel("Enter Customer name");
        Customer_name_Field = new JTextField(20);
        NamePanel.add(lbl_Name);
        NamePanel.add(Customer_name_Field);
        controlPanel.add(NamePanel, BorderLayout.WEST);
        return controlPanel;
    }

    /**
     * Adds a functional panel to the orderPanel containing buttons for checking out, deleting selected products,
     * clearing the cart, and canceling the order.
     *
     * @return the functional panel
     */
    public JComponent add_Function_Panel() {
        // bottom functional panel
        Confirm_But = new JButton("Checkout");
        Confirm_But.addActionListener(e -> {
            String customer_name = Customer_name_Field.getText();
            if (!customer_name.isEmpty()) {
                Confirm_But.setEnabled(false);
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        // orderService.processCheckout(customer_name, inv, invoice, invoice_detail);
                        orderService.QRPayment(
                                orderService.getTotal(), instance, customer_name, inv, invoice, invoice_detail);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                        } catch (InterruptedException | ExecutionException ex) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Checkout failed: " + ex.getCause().getMessage());
                        } finally {
                            Confirm_But.setEnabled(true);
                            dispose();
                        }
                    }
                }.execute();
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Please not leave empty Customer name!");

            }
        });
        deleteButton = new JButton("Delete selected product");
        deleteButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "danger");
        deleteButton.addActionListener(e -> orderService.removeProduct());
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> orderService.clearCart());
        CancelButton = new JButton("Cancel");
        CancelButton.addActionListener(e -> this.setVisible(false));
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        TotalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 1));
        TotalPanel.add(orderService.lblTotal);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        bottomPanel.add(TotalPanel);
        bottomPanel.add(Confirm_But);
        bottomPanel.add(deleteButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(CancelButton);
        return bottomPanel;
    }


    /**
     * Refreshes the display of the orderPanel by updating the table model and label.
     */
    public void refreshCartDisplay() {
        // Update the table model
        table.setModel(orderService.modelCart);

        // Re-apply the table settings
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, render);

        // Update the total label
        orderService.updateTotalAmount();
    }
}
