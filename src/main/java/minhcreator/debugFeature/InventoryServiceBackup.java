package minhcreator.debugFeature;

import minhcreator.component.stock.StockStatus;
import minhcreator.functional.database.DB;
import minhcreator.main.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class InventoryServiceBackup {

    private static void loadTableData_SQL(String sql) {

        try (ResultSet rs = new DB().selectSQL(sql)) {
            Vector<Vector<Object>> data = Create_DataModel(rs);
            DefaultTableModel model = modelBuilding(data);
//            table.setModel(model);

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
        Object[] col = {"", "PID", "Product Name", "ProductCategory", "Product Price", "Product Sell price ", "Product Amount", "Product Status"};
        Vector<Object> columns = new Vector<>();

        columns.addAll(Arrays.asList(col));

        Vector<Vector<Object>> model = new Vector<>();

        if (!data.isEmpty()) {
            for (int row_data = 0; row_data < data.size(); row_data++) {
//            System.out.println(data.get(row_data));
                data.get(row_data).addFirst(Boolean.FALSE);
                data.get(row_data).add(StockStatus.getStatusBage((int) data.get(row_data).get(6), 10));
                model.add(data.get(row_data));
            }
//        System.out.println(model);
//            export_data = model;
            return new DefaultTableModel(model, columns) {
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
                    if (columnIndex == 7) {
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

    public static void OnReadyUpdate() {

//        loadTableData_SQL("SELECT * FROM users_inventory");
//        loadTableData_SQL("SELECT * FROM " + get_user().getYour_inventory());
    }

    public void UpdateTableModel(ResultSet rs) {
        Vector<Vector<Object>> data = null;
        DefaultTableModel model = null;
        try {
            data = Create_DataModel(rs);
            model = modelBuilding(data);
            if (data != null && model != null) {
//                table.setModel(model);
            } else OnReadyUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void addStock() {
        JFrame dialog = new JFrame();
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(Application.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        // add component
//        StockPanel = new JPanel();
//        StockPanel.setLayout(
//                new MigLayout(
//                        "wrap, fillx, insets 35 45 30 45",
//                        "[fill,360]"
//                )
//        );

//        StockID = new JLabel("Stock ID");
//        StockIDField = new JTextField();
//
//        StockName = new JLabel("Stock name");
//        StockNameField = new JTextField();
//
//        StockCategory = new JLabel("Category");
//        StockCategoryField = new JTextField();
//
//        StockPrice = new JLabel("Stock price");
//        StockPriceField = new JTextField();
//
//        StockSellPrice = new JLabel("Stock sell price");
//        StockSellPriceField = new JTextField();
//
//        StockAmount = new JLabel("Stock amount");
//        StockAmountField = new JTextField();
//
//        addButton_add_stock_dialog = new JButton("Confirm");
//        CancelBut_add_stock_dialog = new JButton("Cancel");
//
//        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
//        addButton_add_stock_dialog.addActionListener(e -> {
//
//        });
//        StockPanel.add(StockID);
//        StockPanel.add(StockIDField);
//        StockPanel.add(StockName);
//        StockPanel.add(StockNameField);
//        StockPanel.add(StockCategory);
//        StockPanel.add(StockCategoryField);
//        StockPanel.add(StockPrice);
//        StockPanel.add(StockPriceField);
//        StockPanel.add(StockSellPrice);
//        StockPanel.add(StockSellPriceField);
//        StockPanel.add(StockAmount);
//        StockPanel.add(StockAmountField);
//        StockPanel.add(addButton_add_stock_dialog, "split 2");
//        StockPanel.add(CancelBut_add_stock_dialog);
//        dialog.add(StockPanel);
    }

//    private void editStock() {
//        JFrame dialog = new JFrame();
//        dialog.setSize(450, 500);
//        dialog.setLocationRelativeTo(Application.getInstance());
//        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        dialog.setVisible(true);
//        // add component
//        StockPanel = new JPanel();
//        StockPanel.setLayout(
//                new MigLayout(
//                        "wrap, fillx, insets 35 45 30 45",
//                        "[fill,360]"
//                )
//        );
//        int viewRow = table.getSelectedRow();
//        if (viewRow == -1) {
//            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Select a row to edit.");
//            dialog.dispose();
//            return;
//        }
//
//        StockID = new JLabel("Stock ID");
//        StockIDField = new JTextField();
//        StockIDField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
//        StockIDField.setEditable(false);
//        StockIDField.setFocusable(false);
//
//        StockName = new JLabel("Stock name");
//        StockNameField = new JTextField();
//        StockNameField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
//
//        StockCategory = new JLabel("Category");
//        StockCategoryField = new JTextField();
//        StockCategoryField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
//
//        StockPrice = new JLabel("Stock price");
//        StockPriceField = new JTextField();
//        StockPriceField.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
//
//        StockSellPrice = new JLabel("Stock sell price");
//        StockSellPriceField = new JTextField();
//        StockSellPriceField.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
//
//
//        StockAmount = new JLabel("Stock amount");
//        StockAmountField = new JTextField();
//        StockAmountField.setText(table.getValueAt(table.getSelectedRow(), 6).toString());
//
//        addButton_add_stock_dialog = new JButton("Confirm");
//        CancelBut_add_stock_dialog = new JButton("Cancel");
//
//        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
//        addButton_add_stock_dialog.addActionListener(e -> {
//
//        });
//        StockPanel.add(StockID);
//        StockPanel.add(StockIDField);
//
//        StockPanel.add(StockName);
//        StockPanel.add(StockNameField);
//
//        StockPanel.add(StockCategory);
//        StockPanel.add(StockCategoryField);
//
//        StockPanel.add(StockPrice);
//        StockPanel.add(StockPriceField);
//
//        StockPanel.add(StockSellPrice);
//        StockPanel.add(StockSellPriceField);
//
//        StockPanel.add(StockAmount);
//        StockPanel.add(StockAmountField);
//
//        StockPanel.add(addButton_add_stock_dialog, "split 2");
//        StockPanel.add(CancelBut_add_stock_dialog);
//        dialog.add(StockPanel);
//    }
//
//    private void deleteStock() {
//        DefaultTableModel model = (DefaultTableModel) table.getModel();
//
//        Vector<String> selected_ID_Rows = new Vector<>();
//        boolean hasSelection = false;
//        for (int i = 0; i < model.getRowCount(); i++) {
//            boolean isSelected = (boolean) model.getValueAt(i, 0);
//            if (isSelected) {
//                hasSelection = true;
//                selected_ID_Rows.add(model.getValueAt(i, 1).toString());
//
//            }
//        }
//        deleteSelectedItems(selected_ID_Rows);
//        if (!hasSelection) {
//            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Please select at least one row to delete.");
//            return;
//        }
//    }
//
//    private void deleteSelectedItems(Vector<String> ids) {
//        if (ids.isEmpty()) return;
//
//        try {
//            conn = new DB().getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        if (conn == null) return;
//        String sql;
//        if (ids.size() == 1) {
//            sql = "DELETE FROM " + get_user().getYour_inventory() + " WHERE id = ?";
//            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//                // Set each parameter
//                ps.setString(1, ids.getFirst());
//
//                int deleted = ps.executeUpdate();
//                Notifications.getInstance().show(
//                        Notifications.Type.INFO,
//                        Notifications.Location.TOP_CENTER,
//                        "Deleted " + deleted + " Stock(s) successfully"
//                );
//
//                // Refresh the table
//                OnReadyUpdate();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            // Create a string with comma-separated question marks for the prepared statement
//            String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
//            sql = "DELETE FROM " + get_user().getYour_inventory() + " WHERE id IN (" + placeholders + ")";
//
//            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//                // Set each parameter
//                for (int i = 0; i < ids.size(); i++) {
//                    ps.setString(i + 1, ids.get(i));
//                }
//
//                int deleted = ps.executeUpdate();
//                Notifications.getInstance().show(
//                        Notifications.Type.INFO,
//                        Notifications.Location.TOP_CENTER,
//                        "Deleted " + deleted + " Stock(s) successfully"
//                );
//
//                // Refresh the table
//                OnReadyUpdate();
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage());
//            }
//        }
//    }
//
//    private void clearStock() {
//        DefaultTableModel model = new DefaultTableModel();
//        table.setModel(model);
//    }
//
//    private void letSearch() {
//        String getSearchText = searchField.getText().trim();
//        if (getSearchText.isEmpty()) {
//            OnReadyUpdate();
//            return;
//        }
//        // simple search across a few columns
//        String sql = "SELECT * FROM " + get_user().getYour_inventory() + " WHERE id LIKE ? OR category LIKE ? OR name LIKE ? OR price LIKE ? OR sellprice LIKE ? OR amount LIKE ?";
//        try {
//            conn = new DB().getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        if (conn == null) return;
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            String like = "%" + getSearchText + "%";
//            ps.setString(1, like);
//            ps.setString(2, like);
//            ps.setString(3, like);
//            ps.setString(4, like);
//            ps.setString(5, like);
//            ps.setString(6, like);
//            try (ResultSet rs = ps.executeQuery()) {
//                UpdateTableModel(rs);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Search failed: " + ex.getMessage());
//        }
//    }
//
//    private void letSort() {
//        // "PID", "Product Name ", "Product Category", "Product Price", "Product Quantity"
//        String key = (String) sortByBox.getSelectedItem();
//        String col = "";
//        if ("PID".equals(key)) col = "id";
//        else if ("Product Category".equals(key)) col = "category";
//        else if ("Product Name".equals(key)) col = "name";
//        else if ("Product Price".equals(key)) col = "price";
//        else if ("Product Quantity".equals(key)) {
//            col = "amount";
//        }
//        loadTableData_SQL("SELECT * FROM " + get_user().getYour_inventory() + " ORDER BY " + col);
//    }
//
//    private void SellStock() {
//        JFrame dialog = new JFrame();
//        dialog.setSize(450, 500);
//        dialog.setLocationRelativeTo(Application.getInstance());
//        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        dialog.setVisible(true);
//        // add component
//        StockPanel = new JPanel();
//        StockPanel.setLayout(
//                new MigLayout(
//                        "wrap, fillx, insets 35 45 30 45",
//                        "[fill,360]"
//                )
//        );
//        int viewRow = table.getSelectedRow();
//        if (viewRow == -1) {
//            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Select a stock to sales.");
//            dialog.dispose();
//            return;
//        }
//
//        StockID = new JLabel("Stock ID");
//        StockIDField = new JTextField();
//        StockIDField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
//        StockIDField.setEditable(false);
//        StockIDField.setFocusable(false);
//
//        StockName = new JLabel("Stock name");
//        StockNameField = new JTextField();
//        StockNameField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
//        StockNameField.setEditable(false);
//        StockNameField.setFocusable(false);
//
//        StockCategory = new JLabel("Category");
//        StockCategoryField = new JTextField();
//        StockCategoryField.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
//        StockCategoryField.setEditable(false);
//        StockCategoryField.setFocusable(false);
//
//        StockPrice = new JLabel("Stock price");
//        StockPriceField = new JTextField();
//        StockPriceField.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
//        StockPriceField.setEditable(false);
//        StockPriceField.setFocusable(false);
//
//        StockSellPrice = new JLabel("Stock sell price");
//        StockSellPriceField = new JTextField();
//        StockSellPriceField.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
//        StockSellPriceField.setEditable(false);
//        StockSellPriceField.setFocusable(false);
//
//        StockAmount = new JLabel("Stock amount");
//        StockAmountField = new JTextField();
//
//        addButton_add_stock_dialog = new JButton("Confirm");
//        CancelBut_add_stock_dialog = new JButton("Cancel");
//
//        CancelBut_add_stock_dialog.addActionListener(e -> dialog.dispose());
//        addButton_add_stock_dialog.addActionListener(e -> {
//        });
//        StockPanel.add(StockID);
//        StockPanel.add(StockIDField);
//
//        StockPanel.add(StockName);
//        StockPanel.add(StockNameField);
//
//        StockPanel.add(StockCategory);
//        StockPanel.add(StockCategoryField);
//
//        StockPanel.add(StockPrice);
//        StockPanel.add(StockPriceField);
//
//        StockPanel.add(StockSellPrice);
//        StockPanel.add(StockSellPriceField);
//
//        StockPanel.add(StockAmount);
//        StockPanel.add(StockAmountField);
//
//        StockPanel.add(addButton_add_stock_dialog, "split 2");
//        StockPanel.add(CancelBut_add_stock_dialog);
//        dialog.add(StockPanel);
//    }

}