/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.RoundPanel;
import minhcreator.component.model.ModelCard;
import minhcreator.component.page.Login;
import minhcreator.functional.database.DB;
import minhcreator.functional.session.sessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Designed by Raven
 * @author MinhCreatorVN
 */
public class DashPanel extends javax.swing.JPanel {

    private Connection conn;
    private Statement stmt;
    public sessionManager sharedSession = Login.session;


    /**
     * Creates new form DashPanel
     */
    public DashPanel() {
        initComponents();
        init();
    }

    private void initComponents() {

        StaticPanel = new javax.swing.JPanel();
        ProfitCard = new minhcreator.component.Card();
        TotalStock = new minhcreator.component.Card();
        Expense = new minhcreator.component.Card();
        welcomeUserCard = new minhcreator.component.Card();
        RoundTable = new RoundPanel();
        TableScrollPanel = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        LatestStockAddButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(600, 700));

        javax.swing.GroupLayout StaticPanelLayout = new javax.swing.GroupLayout(StaticPanel);
        StaticPanel.setLayout(StaticPanelLayout);
        StaticPanelLayout.setHorizontalGroup(
                StaticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(StaticPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ProfitCard, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TotalStock, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addGap(18, 18, 18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Expense, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addGap(18, 18, 18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(welcomeUserCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        StaticPanelLayout.setVerticalGroup(
                StaticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(StaticPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(StaticPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(welcomeUserCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Expense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TotalStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ProfitCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TableScrollPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        TableScrollPanel.setViewportView(table);

        LatestStockAddButton.setText("New stock added");
        LatestStockAddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LatestStockAddButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        javax.swing.GroupLayout RoundTableLayout = new javax.swing.GroupLayout(RoundTable);
        RoundTable.setLayout(RoundTableLayout);
        RoundTableLayout.setHorizontalGroup(
                RoundTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(RoundTableLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(RoundTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(TableScrollPanel)
                                        .addGroup(RoundTableLayout.createSequentialGroup()
                                                .addComponent(LatestStockAddButton)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        RoundTableLayout.setVerticalGroup(
                RoundTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RoundTableLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(LatestStockAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(RoundTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(StaticPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(StaticPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RoundTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    private void init() {
        welcomeUserCard.setDataNoIcon(new ModelCard("Welcome Back", sharedSession.getUsername()));
        welcomeUserCard.setGradientColor(Color.decode("#00ffcc"));

        // init import stock table
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, render);
        loadTableData();

        // trying get profit, total current stock and cost
        try {
            ProfitCard.setDataSvg(new ModelCard("Profit", getProfit(), new FlatSVGIcon("minhcreator/assets/functional_icon/profit.svg")));
            TotalStock.setDataSvg(new ModelCard("Current storage", getCurrStorage(), new FlatSVGIcon("minhcreator/assets/functional_icon/stock.svg")));
            Expense.setDataSvg(new ModelCard("Expense", getCost(), new FlatSVGIcon("minhcreator/assets/functional_icon/expense.svg")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ProfitCard.setGradientColor(Color.decode("#fff200"));
        TotalStock.setGradientColor(Color.decode("#4c00ff"));
        Expense.setGradientColor(Color.decode("#fff870"));


    }


    private void loadTableData() {
        loadTableData("SELECT * FROM " + sharedSession.getUser_cost_table() + " ORDER BY date DESC");
    }

    private void loadTableData(String sql) {
        try {
            conn = DB.getConnection();
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
            return;
        }

        if (stmt == null) return;
        try (ResultSet rs = stmt.executeQuery(sql)) {
            DefaultTableModel model = buildTableModel(rs);
            table.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        Vector<String> columnNames = new Vector<>();
        for (int model_Label = 1; model_Label <= columnCount; model_Label++)
            columnNames.add(meta.getColumnLabel(model_Label));

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int DataModel = 1; DataModel <= columnCount; DataModel++) row.add(rs.getObject(DataModel));
            data.add(row);
        }
        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
    }

    // Tính lợi nhuận: Doanh thu - Giá vốn trung bình
    private ResultSet getProfitReport(String user_product, String user_purchase, String user_invoices_details) throws Exception {
        Connection conn = DB.getConnection();
        String revenue_cost = "SELECT p.name, " +
                "SUM(so.quantity * so.unit_price) AS revenue, " +
                "SUM(so.quantity * (SELECT IFNULL(SUM(import_price * quantity)/SUM(quantity), 0) " +
                "FROM " + user_purchase + " WHERE product_id = p.id)) AS cost " +
                "FROM " + user_product + " p JOIN " + user_invoices_details + " so ON p.id = so.product_id GROUP BY p.id";


        return conn.prepareStatement(revenue_cost).executeQuery();
    }

    // cal profit
    private double getProfit() throws Exception {
        List<Double> revenueList = new ArrayList<>();
        List<Double> costList = new ArrayList<>();
        ResultSet rs = getProfitReport(
                sharedSession.getUser_product(),
                sharedSession.getUser_cost_table(),
                sharedSession.getUser_invoice_details()
        );

        try {
            while (rs.next()) {
                String productName = rs.getString("name");
                double revenue = rs.getDouble("revenue");
                double cost = rs.getDouble("cost");
                revenueList.add(revenue);
                costList.add(cost);
            }

            // Using Java 8+ Stream API
            double TotalRevenue = revenueList.stream().mapToDouble(Double::doubleValue).sum();
            double TotalCost = costList.stream().mapToDouble(Double::doubleValue).sum();
            return TotalRevenue - TotalCost;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            // Always close the ResultSet when done
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // cal cost your spend
    private double getCost() throws Exception {
        List<Double> costList = new ArrayList<>();
        ResultSet rs = getProfitReport(
                sharedSession.getUser_product(),
                sharedSession.getUser_cost_table(),
                sharedSession.getUser_invoice_details()
        );

        try {
            while (rs.next()) {
                double cost = rs.getDouble("cost");
                costList.add(cost);
            }
            return costList.stream().mapToDouble(Double::doubleValue).sum();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            // Always close the ResultSet when done
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // get total quantity of product in current storage
    private int getCurrStorage() throws Exception {
        Connection conn = DB.getConnection();
        String currentStorage = "SELECT " + " quantity " + "FROM " + sharedSession.getYour_inventory();
        ResultSet rs = conn.prepareStatement(currentStorage).executeQuery();
        List<Integer> qty = new ArrayList<>();
        while (rs.next()) {
            qty.add(rs.getInt("quantity"));
        }

        return qty.stream().mapToInt(Integer::intValue).sum();
    }

    private minhcreator.component.Card Expense;
    private minhcreator.component.Card ProfitCard;
    private RoundPanel RoundTable;
    private javax.swing.JPanel StaticPanel;
    private javax.swing.JScrollPane TableScrollPanel;
    private minhcreator.component.Card TotalStock;
    private javax.swing.JButton LatestStockAddButton;
    private javax.swing.JTable table;
    private minhcreator.component.Card welcomeUserCard;
}