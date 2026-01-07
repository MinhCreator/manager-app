/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.model.ModelCard;
import minhcreator.component.page.Login;
import minhcreator.functional.database.DB;
import minhcreator.functional.session.sessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
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
        RoundTable = new com.raven.swing.RoundPanel();
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
                                .addComponent(ProfitCard, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TotalStock, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Expense, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
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

        ProfitCard.setDataSvg(new ModelCard("Profit", getTotalProfit(), new FlatSVGIcon("minhcreator/assets/functional_icon/profit.svg")));
        ProfitCard.setGradientColor(Color.decode("#fff200"));

        TotalStock.setGradientColor(Color.decode("#4c00ff"));
        TotalStock.setDataSvg(new ModelCard("Current total stock",
                getTotalStock(sharedSession.getYour_inventory(), "amount", "TotalStock"),
                new FlatSVGIcon("minhcreator/assets/functional_icon/stock.svg")));

        Expense.setGradientColor(Color.decode("#fff870"));
        Expense.setDataSvg(new ModelCard("Expense", getExpense(), new FlatSVGIcon("minhcreator/assets/functional_icon/expense.svg")));
    }


    private void loadTableData() {
        loadTableData("SELECT * FROM " + sharedSession.getYour_stock_add() + " ORDER BY date ASC");
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

    private int getTotalStock(String stockTable, String column_name, String alias) {
        String query = "SELECT COUNT(DISTINCT " + column_name + " ) AS " + alias + " FROM " + stockTable;
        try {
            conn = DB.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(alias);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private double getTotalProfit() {
        String query = "SELECT SUM(amount * (sellprice - price)) AS TotalProfit FROM " + sharedSession.getYour_export_table();
        try {
            conn = DB.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getDouble("TotalProfit");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private double getExpense() {
        String query = "SELECT SUM(amount * price) AS Expense FROM " + sharedSession.getYour_stock_add();
        try {
            conn = DB.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getDouble("Expense");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }


    private minhcreator.component.Card Expense;
    private minhcreator.component.Card ProfitCard;
    private com.raven.swing.RoundPanel RoundTable;
    private javax.swing.JPanel StaticPanel;
    private javax.swing.JScrollPane TableScrollPanel;
    private minhcreator.component.Card TotalStock;
    private javax.swing.JButton LatestStockAddButton;
    private javax.swing.JTable table;
    private minhcreator.component.Card welcomeUserCard;
}