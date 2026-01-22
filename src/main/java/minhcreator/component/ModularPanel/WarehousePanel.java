package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.model.Product;
import minhcreator.component.stock.StockStatus;
import minhcreator.component.stock.TableBadgeCellRenderer;
import minhcreator.functional.database.DB;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import minhcreator.service.WarehouseService;
import minhcreator.service.orderPanel;
import minhcreator.service.orderService;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static minhcreator.component.page.Login.login;

public class WarehousePanel extends JPanel {
    private JTable table = new JTable();
    private JPanel Rightpanel, controlPanel, searchPanel, sortPanel, bottomPanel, StockPanel;
    private JLabel lblSearch, lblSort, StockID, StockName, StockPrice, StockAmount, StockSellPrice, StockCategory;
    private JButton searchButton, sortButton, Add_But, deleteButton, clearButton, refreshButton, editButton, CartMenu, addButton_add_stock_dialog, CancelBut_add_stock_dialog;
    private JButton AddtoCart_butt;
    private JTextField searchField, StockIDField, StockNameField, StockPriceField, StockAmountField, StockSellPriceField, StockCategoryField;
    private JComboBox<String> sortByBox;
    private WarehouseService WS;
    private final String inv = login.getSession().getYour_inventory();
    private final String product = login.getSession().getUser_product();
    private final String purchase = login.getSession().getUser_cost_table();
    private final String sale = login.getSession().get_User_sale_table();
    private static final JComboBox<String> OptionComboBox = new JComboBox<>(new String[]{"IN", "OUT", "CHANGE"});
    private static orderPanel orderPanel;

    public WarehousePanel() {
        init();
        initComponents();
    }

    private void init() {
        setVisible(true);
        setLayout(new BorderLayout());
    }

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

    public JComponent addTable() {
        List<Product> list = WarehouseService.getAllProducts(product, inv);
        DefaultTableModel model = buildTableModel(list);

        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, render);

        TableBadgeCellRenderer.apply(table, StockStatus.class);
//        loadTableData(product, inv);
        return table;
    }

    public JComponent add_Util_Panel() {

        controlPanel = new JPanel(new BorderLayout());
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));

        lblSearch = new JLabel("Search");
        searchField = new JTextField(20);

        searchButton = new JButton();
        searchButton.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/search.svg"));
        searchButton.addActionListener(e -> Search());
        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // sort functional
        sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        lblSort = new JLabel("Sort by");
        // option for sort
        sortByBox = new JComboBox<>(new String[]{"UPID", "Product Name ", "Product Category", "Product Price", "Product Quantity"});
        sortButton = new JButton("Sort");
        sortByBox.addActionListener(e -> Sort());

        sortPanel.add(lblSort);
        sortPanel.add(sortByBox);
//        sortPanel.add(sortButton);

        controlPanel.add(searchPanel, BorderLayout.WEST);
        controlPanel.add(sortPanel, BorderLayout.EAST);

        return controlPanel;
    }

    public JComponent add_Function_Panel() {
        // bottom functional panel
        Add_But = new JButton("Add");
        Add_But.addActionListener(e -> addNewProduct(product, inv, purchase, sale));
        Add_But.putClientProperty(FlatClientProperties.STYLE_CLASS, "info");

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int confim = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete a product(s) From your inventory ?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confim != JOptionPane.YES_OPTION) {
                return;
            }

            deleteSelectedProducts(inv, product, purchase, sale, table);
        });
        deleteButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "danger");

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> cleartable());
        clearButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "warning");

        refreshButton = new JButton("Refresh");
        refreshButton.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/refresh.svg"));
        refreshButton.addActionListener(e -> reloadUI());

        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editProduct(product, inv, purchase, sale));
        editButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "success");

        AddtoCart_butt = new JButton("Add to cart");
        AddtoCart_butt.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                Notifications.getInstance().show(
                        Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "Please select a product for add to cart"
                );
                return;
            }

            // Get selected product data
            int productId = (int) table.getValueAt(selectedRow, 1); // Assuming ID is in column 1
            String upid = table.getValueAt(selectedRow, 2).toString();
            String name = table.getValueAt(selectedRow, 3).toString();
            String category = table.getValueAt(selectedRow, 4).toString();
            double sellingPrice = (double) table.getValueAt(selectedRow, 6);
            int quantity = (int) table.getValueAt(selectedRow, 7);

            // Show the order panel if not already shown
            if (orderPanel == null) {
                orderPanel = new orderPanel();
            }
            orderService.addToCartButton(productId, upid, name, sellingPrice, quantity);
            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Added " + name + " to Cart !"
            );

        });
        CartMenu = new JButton("Cart Menu");
        CartMenu.addActionListener(e -> orderPanel.getInstance().setVisible(true));

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        bottomPanel.add(Add_But);
        bottomPanel.add(AddtoCart_butt);
        bottomPanel.add(CartMenu);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(refreshButton);
        return bottomPanel;
    }

    private void cleartable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        table.setModel(model);
    }

    public static sessionManager get_user() {
        sessionManager get = login.getInstance().session;
        System.out.println();
        return get;
    }

    private void Search() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshTable(product, inv);
            return;
        }

        // Search in all fields: name, category, and UPID
        List<Product> searchResults = buildsearchProducts(product, inv, searchText);
        DefaultTableModel model = buildTableModel(searchResults);
        table.setModel(model);
    }

    public List<Product> buildsearchProducts(String user_product, String user_inv,
                                             String searchText) {
        List<Product> list = new ArrayList<>();

        // Map UI field names to database column names

        String sql = "SELECT p.id, p.UPID, p.name, i.category, i.price, i.selling_price, i.quantity " +
                "FROM " + user_product + " p " +
                "LEFT JOIN " + user_inv + " i ON p.id = i.product_id " +
                "WHERE 1=1";

        if (searchText != null && !searchText.trim().isEmpty()) {
            sql += " AND (p.name LIKE ? OR i.category LIKE ? OR p.UPID LIKE ?)";
        }
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set search parameters if search text is provided
            if (searchText != null && !searchText.trim().isEmpty()) {
                String searchPattern = "%" + searchText.trim() + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                ps.setString(3, searchPattern);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("id"),
                            rs.getString("UPID"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getDouble("selling_price"),
                            rs.getInt("quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void Sort() {
        String sortBy = (String) sortByBox.getSelectedItem();
        String sortField = "id"; // Default sort field

        // Map UI sort options to database column names
        if (sortBy != null) {
            switch (sortBy) {
                case "Product Name":
                    sortField = "name";
                    break;
                case "Product Category":
                    sortField = "category";
                    break;
                case "Product Price":
                    sortField = "price";
                    break;
                case "Product Quantity":
                    sortField = "quantity";
                    break;
                case "UPID":
                default:
                    sortField = "UPID";
                    break;
            }
        }

        // Get current search text to maintain search while sorting
        String searchText = searchField.getText().trim();
        List<Product> sortedResults;

        if (searchText.isEmpty()) {
            sortedResults = sortProducts(product, inv, sortField, "ASC");
        } else {
            sortedResults = buildsearchProducts(product, inv, searchText);
        }

        DefaultTableModel model = buildTableModel(sortedResults);
        table.setModel(model);
    }

    public void addNewProduct(String user_product, String user_inventory, String user_purchase, String user_sale) {
        JDialog dialog = new JDialog();
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        dialog.setTitle("Import Product");
        StockPanel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));


        // Setup form fields
        StockID = new JLabel("Product UPID");
        StockIDField = new JTextField();

        StockName = new JLabel("Product name");
        StockNameField = new JTextField();

        StockCategory = new JLabel("Product Category");
        StockCategoryField = new JTextField();

        StockPrice = new JLabel("Product price");
        StockPriceField = new JTextField();

        StockSellPrice = new JLabel("Product sell price");
        StockSellPriceField = new JTextField();

        StockAmount = new JLabel("Product amount");
        StockAmountField = new JTextField();

        addButton_add_stock_dialog = new JButton("Add Product");
        CancelBut_add_stock_dialog = new JButton("Cancel");

        // setup fucntion for button
        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
        addButton_add_stock_dialog.addActionListener(e -> {
            try {
                String upid = StockIDField.getText().trim();
                String name = StockNameField.getText().trim();
                String category = StockCategoryField.getText().trim();
                double price = Double.parseDouble(StockPriceField.getText().trim());
                double sellingPrice = Double.parseDouble(StockSellPriceField.getText().trim());
                int quantity = Integer.parseInt(StockAmountField.getText().trim());

                int productId = WarehouseService.addNewProduct(
                        upid, name, category, price, sellingPrice, quantity,
                        user_inventory, user_product, user_purchase, user_sale
                );

                if (productId != -1) {
                    Notifications.getInstance().show(
                            Notifications.Type.SUCCESS,
                            Notifications.Location.TOP_CENTER,
                            "Product added successfully!"
                    );
//                    loadTableData(user_product, user_inventory);
                    refreshTable(user_product, user_inventory);
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Please enter valid numbers for price and quantity"
                );
            } catch (Exception ex) {
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Error adding product: " + ex.getMessage()
                );
            }
            refreshTable(user_product, user_inventory);
            dialog.dispose();
        });

        addButton_add_stock_dialog.putClientProperty(FlatClientProperties.STYLE_CLASS, "success");
        CancelBut_add_stock_dialog.putClientProperty(FlatClientProperties.STYLE_CLASS, "warning");

        // add form compounent
        StockPanel.add(StockID);
        StockPanel.add(StockIDField);
        StockPanel.add(StockName);
        StockPanel.add(StockNameField);
        StockPanel.add(StockCategory);
        StockPanel.add(StockCategoryField);
        StockPanel.add(StockPrice);
        StockPanel.add(StockPriceField);
        StockPanel.add(StockSellPrice);
        StockPanel.add(StockSellPriceField);
        StockPanel.add(StockAmount);
        StockPanel.add(StockAmountField);
        StockPanel.add(addButton_add_stock_dialog, "split 2");
        StockPanel.add(CancelBut_add_stock_dialog);
        dialog.add(StockPanel);
    }

    public void editProduct(String user_product, String user_inventory, String user_purchase, String user_sale) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Product");
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        StockPanel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Please select a product to edit"
            );
            return;
        }

        // Get selected product data
        int productId = (int) table.getValueAt(selectedRow, 1); // Assuming ID is in column 1
        String upid = table.getValueAt(selectedRow, 2).toString();
        String name = table.getValueAt(selectedRow, 3).toString();
        String category = table.getValueAt(selectedRow, 4).toString();
        double price = (double) table.getValueAt(selectedRow, 5);
        double sellingPrice = (double) table.getValueAt(selectedRow, 6);
        int quantity = (int) table.getValueAt(selectedRow, 7);

        // setup fields
        StockID = new JLabel("Product UPID");
        StockIDField = new JTextField();
        StockIDField.setText(upid);

        StockName = new JLabel("Product name");
        StockNameField = new JTextField();
        StockNameField.setText(name);

        StockCategory = new JLabel("Product Category");
        StockCategoryField = new JTextField();
        StockCategoryField.setText(category);

        StockPrice = new JLabel("Product price");
        StockPriceField = new JTextField();
        StockPriceField.setText(String.valueOf(price).trim());

        StockSellPrice = new JLabel("Product sell price");
        StockSellPriceField = new JTextField();
        StockSellPriceField.setText(String.valueOf(sellingPrice).trim());


        StockAmount = new JLabel("Product amount");
        StockAmountField = new JTextField();
        StockAmountField.setText(String.valueOf(quantity).trim());

        addButton_add_stock_dialog = new JButton("Confirm");
        addButton_add_stock_dialog.putClientProperty(FlatClientProperties.STYLE_CLASS, "success");

        CancelBut_add_stock_dialog = new JButton("Cancel");
        CancelBut_add_stock_dialog.putClientProperty(FlatClientProperties.STYLE_CLASS, "warning");

        // convert string to int
        int quantity_str_to_int = Integer.parseInt(StockAmountField.getText().trim());
        double price_str_to_double = Double.parseDouble(StockPriceField.getText().trim());
        double Sellprice_str_to_double = Double.parseDouble(StockSellPriceField.getText().trim());

        // setup function for button
        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
        addButton_add_stock_dialog.addActionListener(e -> {
            try {
                int INT_IN = 0;
                int INT_OUT = 1;
                int INT_CHANGE = 2;
                if (OptionComboBox.getSelectedIndex() == INT_IN) {
                    if (quantity_str_to_int > 0) {
//                        WarehouseService.processStock(productId, quantity_str_to_int, price_str_to_double, "IN", user_inventory, user_purchase, user_sale);
                        WarehouseService.processStock(productId, Integer.parseInt(StockAmountField.getText().trim()), price_str_to_double, "IN", user_inventory, user_purchase, user_sale);

                    } else Notifications.getInstance().show(
                            Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Quantity must be greater than current product quantity!"
                    );

                } else if (OptionComboBox.getSelectedIndex() == INT_OUT) {
                    Notifications.getInstance().show(
                            Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "This Option is not available for edit function!"
                    );
                } else if (OptionComboBox.getSelectedIndex() == INT_CHANGE) {
                    StockAmountField.setEditable(false);
                    WarehouseService.UpdateName_Category_UPID(
                            productId,
                            StockIDField.getText().trim(),
                            StockNameField.getText().trim(),
                            StockCategoryField.getText().trim(),
                            user_inventory,
                            user_product
                    );
                    WarehouseService.UpdateSellPrice(productId, Sellprice_str_to_double, user_inventory);
                    WarehouseService.UpdatePrice(productId, price_str_to_double, user_inventory);

                }
                // After successful update:
                refreshTable(user_product, user_inventory);
                dialog.dispose();
                Notifications.getInstance().show(
                        Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Product updated successfully!"
                );
            } catch (Exception ex) {
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Error updating product: " + ex.getMessage()
                );
            }
        });

        //add to panel
        StockPanel.add(StockID);
        StockPanel.add(StockIDField);

        StockPanel.add(StockName);
        StockPanel.add(StockNameField);

        StockPanel.add(StockCategory);
        StockPanel.add(StockCategoryField);

        StockPanel.add(StockPrice);
        StockPanel.add(StockPriceField);

        StockPanel.add(StockSellPrice);
        StockPanel.add(StockSellPriceField);

        StockPanel.add(StockAmount);
        StockPanel.add(StockAmountField);

        StockPanel.add(OptionComboBox);

        StockPanel.add(addButton_add_stock_dialog, "split 2");
        StockPanel.add(CancelBut_add_stock_dialog);
        dialog.add(StockPanel);
    }

    public void deleteSelectedProducts(String user_inv, String user_product, String user_purchase, String user_sale, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        boolean anyDeleted = false;

        for (int i = rowCount - 1; i >= 0; i--) {
            if ((Boolean) model.getValueAt(i, 0)) { // If checkbox is checked
                int productId = (int) model.getValueAt(i, 1); // Assuming ID is in column 1
                try {
                    boolean success = WarehouseService.deleteProduct(
                            productId,
                            user_inv,
                            user_purchase,
                            user_sale,
                            user_product
                    );
                    if (success) {
                        model.removeRow(i);
                        anyDeleted = true;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Notifications.getInstance().show(
                            Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Error deleting product ID " + productId + ": " + ex.getMessage()
                    );
                }
            }
        }

        if (anyDeleted) {
            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Selected products deleted successfully"
            );
            loadTableData(user_product, user_inv);
        } else {
            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "No products selected or deletion failed"
            );
        }
    }

    // table function
    public void refreshTable(String user_product, String user_inventory) {
        loadTableData(user_product, user_inventory);
    }

    public void loadTableData(String user_product, String user_inventory) {
        try {
            List<Product> products = WarehouseService.getAllProducts(user_product, user_inventory);
            DefaultTableModel model = buildTableModel(products);
            SwingUtilities.invokeLater(() -> {
                table.setModel(model);
                // Re-apply the cell renderer after setting the model
                TableBadgeCellRenderer.apply(table, StockStatus.class);
                // Re-apply the cell alignment
                DefaultTableCellRenderer render = new DefaultTableCellRenderer();
                render.setHorizontalAlignment(SwingConstants.CENTER);
                table.setDefaultRenderer(Object.class, render);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage());
        }
    }

    public DefaultTableModel buildTableModel(List<Product> products) {
        Object[] columns = {"", "PID", "UPID", "Product Name", "Category", "Price", "Sell Price", "Amount", "Status"};
        Vector<Object> columnVector = new Vector<>();
        columnVector.addAll(Arrays.asList(columns));
        Vector<Vector<Object>> data = new Vector<>();

        for (Product product : products) {
            Vector<Object> row = new Vector<>();
            row.add(Boolean.FALSE); // Checkbox column
            row.add(product.getId());
            row.add(product.getUPID());
            row.add(product.getName());
            row.add(product.getCategory());
            row.add(product.getPrice());
            row.add(product.getSelling_price());
            row.add(product.getQuantity());
            row.add(StockStatus.getStatusBage(product.getQuantity(), 10)); // Assuming 10 is the threshold
            data.add(row);
        }

        return new DefaultTableModel(data, columnVector) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the column 0 can editable
                if (column == 0) {
                    return true;
                }
                // Not Allowed editing for all other columns
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8) {
                    return StockStatus.class;
                }
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

    }

    public List<Product> sortProducts(String user_product, String user_inv, String sortField, String sortOrder) {
        String col;
        if ("name".equals(sortField)) col = "name";
        else if ("category".equals(sortField)) col = "category";
        else if ("price".equals(sortField)) col = "price";
        else if ("quantity".equals(sortField)) col = "quantity";
        else if ("UPID".equals(sortField)) col = "UPID";
        else col = "id";

        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.UPID, p.name, i.category, i.price, i.selling_price, i.quantity " +
                "FROM " + user_product + " p " +
                "LEFT JOIN " + user_inv + " i ON p.id = i.product_id "
                + "ORDER BY " + col + " " + sortOrder;

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Product(

                        rs.getInt("id"),
                        rs.getString("UPID"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getDouble("selling_price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
//        loadTableData("SELECT * FROM sdata ORDER BY " + col + " DESC");
    }

    public void reloadUI() {
        // Clear any existing selections
        table.clearSelection();

        // Clear the search field
        searchField.setText("");

        // Reset the sort combo box to default
        sortByBox.setSelectedIndex(0);

        // Force a complete refresh of the table data
        refreshTable(product, inv);

        // Force update of the UI
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();

            // Show a notification that the UI has been reloaded
            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "UI reloaded successfully"
            );
        });
    }

}