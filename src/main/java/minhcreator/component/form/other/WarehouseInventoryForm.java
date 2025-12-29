package minhcreator.component.form.other;


import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.stock.StockStatus;
import minhcreator.component.stock.TableBadgeCellRenderer;
import minhcreator.functional.TimeManager;
import minhcreator.functional.database.DB;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import minhcreator.util.Shared;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import static minhcreator.component.form.Login.login;

public class WarehouseInventoryForm extends javax.swing.JPanel {
    private static JTable table = new JTable();
    private JPanel Rightpanel, controlPanel, searchPanel, sortPanel, bottomPanel, StockPanel;
    private JLabel lblSearch, lblSort, StockID, StockName, StockPrice, StockAmount;
    private JButton searchButton, sortButton, Add_But, deleteButton, clearButton, refreshButton, editButton, ExportButt, addButton_add_stock_dialog, CancelBut_add_stock_dialog;
    private JTextField searchField, StockIDField, StockNameField, StockPriceField, StockAmountField;
    private JComboBox<String> sortByBox;
    public static Vector<Vector<Object>> export_data = new Vector<>();
    private final Shared shared = new Shared();
    private Connection conn = null;

    public WarehouseInventoryForm() {
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

        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(450, 200));
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, render);

        TableBadgeCellRenderer.apply(table, StockStatus.class);
        OnReadyUpdate();
        return table;
    }

    public JComponent add_Util_Panel() {

        controlPanel = new JPanel(new BorderLayout());
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));

        lblSearch = new JLabel("Search");
        searchField = new JTextField(20);

        searchButton = new JButton();
        searchButton.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/search.svg"));
        searchButton.addActionListener(e -> letSearch());
        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // sort functional
        sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        lblSort = new JLabel("Sort by");
        // option for sort
        sortByBox = new JComboBox<>(new String[]{"ID", "Name stock", "Price", "Quantity"});
        sortButton = new JButton("Sort");
        sortByBox.addActionListener(e -> letSort());

        sortPanel.add(lblSort);
        sortPanel.add(sortByBox);
        sortPanel.add(sortButton);

        controlPanel.add(searchPanel, BorderLayout.WEST);
        controlPanel.add(sortPanel, BorderLayout.EAST);

        return controlPanel;
    }

    public JComponent add_Function_Panel() {
        // bottom functional panel
        Add_But = new JButton("Add");
        Add_But.addActionListener(e -> {
            addStock();
        });
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            deleteStock();
        });
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            clearStock();
        });
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> OnReadyUpdate());
        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editStock());
        ExportButt = new JButton("Export");

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        bottomPanel.add(Add_But);
        bottomPanel.add(deleteButton);
        bottomPanel.add(editButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(refreshButton);
        bottomPanel.add(ExportButt);
        return bottomPanel;
    }

    public static sessionManager get_user() {
        sessionManager get = login.getInstance().session;
        System.out.println();
        return login.getInstance().session;
    }

    private static void loadTableData_SQL(String sql) {

        try (ResultSet rs = new DB().selectSQL(sql)) {
            Vector<Vector<Object>> data = Create_DataModel(rs);
            DefaultTableModel model = modelBuilding(data);
            table.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage());
        }
    }

    public static Vector<Vector<Object>> Create_DataModel(ResultSet rs) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        if (rs != null) {
            ResultSetMetaData load_meta = rs.getMetaData();
            int columnCount = load_meta.getColumnCount();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++)
                    row.add(rs.getObject(i));
                data.add(row);
            }
            return data;
        } else {
            return data;
        }
    }

    public static DefaultTableModel modelBuilding(Vector<Vector<Object>> data) throws SQLException {
        Object[] col = {"", "ID", "Name", "Price", "Amount", "Status"};
        Vector<Object> columns = new Vector<>();

        columns.addAll(Arrays.asList(col));

        Vector<Vector<Object>> model = new Vector<>();

        if (!data.isEmpty()) {
            for (int row_data = 0; row_data < data.size(); row_data++) {
//            System.out.println(data.get(row_data));
                data.get(row_data).add(0, Boolean.FALSE);
                data.get(row_data).add(StockStatus.getStatusBage((int) data.get(row_data).get(4), 10));
                model.add(data.get(row_data));
            }
//        System.out.println(model);
            export_data = model;
            return new DefaultTableModel(model, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Make the four column (index 4, "Status") not editable
                    if (column == 4) {
                        return false;
                    }
                    // Allow editing for all other columns
                    return true;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 5) {
                        return StockStatus.class;
                    }
                    if (columnIndex == 0) {
                        return Boolean.class;
                    }
                    return super.getColumnClass(columnIndex);
                }
            };
        } else return new DefaultTableModel(model, columns); // empty row if model = null
    }

    public static Vector<Vector<Object>> exportData() {
        return export_data;
    }

    public static void OnReadyUpdate() {

//        loadTableData_SQL("SELECT * FROM users_inventory");
        loadTableData_SQL("SELECT * FROM " + get_user().getYour_inventory());
    }

    public void UpdateTableModel(ResultSet rs) {
        Vector<Vector<Object>> data = null;
        DefaultTableModel model = null;
        try {
            data = Create_DataModel(rs);
            model = modelBuilding(data);
            if (data != null && model != null) {
                table.setModel(model);
            } else OnReadyUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void addStock() {
        javax.swing.JFrame dialog = new javax.swing.JFrame();
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        // add component
        StockPanel = new JPanel();
        StockPanel.setLayout(
                new MigLayout(
                        "wrap, fillx, insets 35 45 30 45",
                        "[fill,360]"
                )
        );

        StockID = new JLabel("Stock ID");
        StockIDField = new JTextField();

        StockName = new JLabel("Stock name");
        StockNameField = new JTextField();

        StockPrice = new JLabel("Stock price");
        StockPriceField = new JTextField();

        StockAmount = new JLabel("Stock amount");
        StockAmountField = new JTextField();

        addButton_add_stock_dialog = new JButton("Confirm");
        CancelBut_add_stock_dialog = new JButton("Cancel");

        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
        addButton_add_stock_dialog.addActionListener(e -> {
            try {
                conn = DB.getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (conn == null) return;
            // insert to user inventory
            String sql = "INSERT INTO " + get_user().getYour_inventory() + " (id, name, price, amount) VALUES (?, ?, ?, ?)";
            import_goods(conn);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, StockIDField.getText().trim());
                ps.setString(2, StockNameField.getText().trim());
                ps.setString(3, StockPriceField.getText().trim());
                ps.setString(4, StockAmountField.getText().trim());
                ps.executeUpdate();
                dialog.dispose();
                OnReadyUpdate();
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Insert successful");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Insert failed: " + ex.getMessage());
            }
        });
        StockPanel.add(StockID);
        StockPanel.add(StockIDField);
        StockPanel.add(StockName);
        StockPanel.add(StockNameField);
        StockPanel.add(StockPrice);
        StockPanel.add(StockPriceField);
        StockPanel.add(StockAmount);
        StockPanel.add(StockAmountField);
        StockPanel.add(addButton_add_stock_dialog, "split 2");
        StockPanel.add(CancelBut_add_stock_dialog);
        dialog.add(StockPanel);
    }

    private void editStock() {
        javax.swing.JFrame dialog = new javax.swing.JFrame();
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        // add component
        StockPanel = new JPanel();
        StockPanel.setLayout(
                new MigLayout(
                        "wrap, fillx, insets 35 45 30 45",
                        "[fill,360]"
                )
        );
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Select a row to edit.");
            dialog.dispose();
            return;
        }

        StockID = new JLabel("Stock ID");
        StockIDField = new JTextField();
        StockIDField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
        StockIDField.setEditable(false);
        StockIDField.setFocusable(false);

        StockName = new JLabel("Stock name");
        StockNameField = new JTextField();
        StockNameField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());

        StockPrice = new JLabel("Stock price");
        StockPriceField = new JTextField();
        StockPriceField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());


        StockAmount = new JLabel("Stock amount");
        StockAmountField = new JTextField();
        StockAmountField.setText(table.getValueAt(table.getSelectedRow(), 4).toString());

        addButton_add_stock_dialog = new JButton("Confirm");
        CancelBut_add_stock_dialog = new JButton("Cancel");

        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
        addButton_add_stock_dialog.addActionListener(e -> {
            try {
                conn = new DB().getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (conn == null) return;

//            String sql = "UPDATE users_inventory SET name=?, price=?, amount=? WHERE id=?";
            String sql = "UPDATE " + get_user().getYour_inventory() + " SET name=?, price=?, amount=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, StockNameField.getText().trim());
                ps.setString(2, StockPriceField.getText().trim());
                ps.setString(3, StockAmountField.getText().trim());
                ps.setString(4, StockIDField.getText().trim());
                ps.executeUpdate();
                dialog.dispose();
                OnReadyUpdate();
                Notifications.getInstance().show(
                        Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "Update data at Stock ID " + StockIDField.getText().trim() + " successful!"
                );
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
            }
        });
        StockPanel.add(StockID);
        StockPanel.add(StockIDField);
        StockPanel.add(StockName);
        StockPanel.add(StockNameField);
        StockPanel.add(StockPrice);
        StockPanel.add(StockPriceField);
        StockPanel.add(StockAmount);
        StockPanel.add(StockAmountField);
        StockPanel.add(addButton_add_stock_dialog, "split 2");
        StockPanel.add(CancelBut_add_stock_dialog);
        dialog.add(StockPanel);
    }

    private void deleteStock() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        Vector<String> selected_ID_Rows = new Vector<>();
        boolean hasSelection = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isSelected = (boolean) model.getValueAt(i, 0);
            if (isSelected) {
                hasSelection = true;
                selected_ID_Rows.add(model.getValueAt(i, 1).toString());

            }
        }
        deleteSelectedItems(selected_ID_Rows);
        if (!hasSelection) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Please select at least one row to delete.");
            return;
        }
    }

    private void deleteSelectedItems(Vector<String> ids) {
        if (ids.isEmpty()) return;

        try {
            conn = new DB().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (conn == null) return;
        String sql;
        if (ids.size() == 1) {
            sql = "DELETE FROM " + get_user().getYour_inventory() + " WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Set each parameter
                ps.setString(1, ids.getFirst());

                int deleted = ps.executeUpdate();
                Notifications.getInstance().show(
                        Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "Deleted " + deleted + " Stock(s) successfully"
                );

                // Refresh the table
                OnReadyUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Create a string with comma-separated question marks for the prepared statement
            String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
            sql = "DELETE FROM " + get_user().getYour_inventory() + " WHERE id IN (" + placeholders + ")";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Set each parameter
                for (int i = 0; i < ids.size(); i++) {
                    ps.setString(i + 1, ids.get(i));
                }

                int deleted = ps.executeUpdate();
                Notifications.getInstance().show(
                        Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "Deleted " + deleted + " Stock(s) successfully"
                );

                // Refresh the table
                OnReadyUpdate();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage());
            }
        }
    }

    private void clearStock() {
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
    }

    private void exportStock() {
    }

    private void letSearch() {
        String getSearchText = searchField.getText().trim();
        if (getSearchText.isEmpty()) {
            OnReadyUpdate();
            return;
        }
        // simple search across a few columns
        String sql = "SELECT * FROM " + get_user().getYour_inventory() + " WHERE id LIKE ? OR name LIKE ? OR price LIKE ? OR amount LIKE ?";
        try {
            conn = new DB().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (conn == null) return;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + getSearchText + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                UpdateTableModel(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Search failed: " + ex.getMessage());
        }
    }

    private void letSort() {
        String key = (String) sortByBox.getSelectedItem();
        String col = "";
        if ("ID".equals(key)) col = "id";
        else if ("Name stock".equals(key)) col = "name";
        else if ("Price".equals(key)) col = "price";
        else if ("Quantity".equals(key)) {
            col = "amount";
        }
        loadTableData_SQL("SELECT * FROM " + get_user().getYour_inventory() + " ORDER BY " + col);
    }

    private void import_goods(Connection conn) {

        if (conn == null) return;

        String sql = "INSERT INTO " + get_user().getYour_stock_add() + " (id, name, date, price, amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, StockIDField.getText().trim());
            ps.setString(2, StockNameField.getText().trim());
            ps.setString(3, new TimeManager().TimeNowFormat("yyyy-MM-dd"));
            ps.setString(4, StockPriceField.getText().trim());
            ps.setString(5, StockAmountField.getText().trim());
            ps.executeUpdate();
            OnReadyUpdate();
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Added to report successful");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Added to report failed: " + ex.getMessage());
        }


    }

    private void export_goods() {
    }
}