package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import minhcreator.functional.database.DB;
import minhcreator.functional.location.TimeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static minhcreator.component.page.Login.login;

public class invoicesPanel extends javax.swing.JPanel {
    private JPanel MainPanel;
    private JButton SearchBut, refreshBut;
    private JLabel Total_Money, discount, Total_Money_Sum;
    private JButton printing, delete_But;
    private DefaultTableModel model, invoice_detail_model;
    private JTable table_invoices, table_invoice_detail;
    private JTextField txfSearch;
    private static invoicesPanel instance;
    private final String user_product = login.getSession().getUser_product();
    private final String user_invoice_details = login.getSession().getUser_invoice_details();
    private final String user_invoice = login.getSession().getUser_invoices();
    private static final Font FONT_ITEM = new Font("SansSerif", Font.BOLD, 12);
    private static final Color COLOR = Color.decode("#4B6EAF");

    public invoicesPanel() {
        init();
//        FontManager.LoadFont("defaultFont", FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 13);
        getDataInvoice();

        table_invoices.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table_invoices.getSelectedRow();
                if (row >= 0) {
                    String invoiceId = model.getValueAt(row, 0).toString();
                    getDataInvoiceDetail(invoiceId);
                }
                updateTotalMoney(row);
            }
        });

        // Add refresh button action
        refreshBut.addActionListener(e -> {
            getDataInvoice();
            invoice_detail_model.setRowCount(0); // Clear details when refreshing
        });

        // Add search functionality
        SearchBut.addActionListener(e -> searchInvoices(txfSearch.getText().trim()));

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table_invoices.setDefaultRenderer(Object.class, render);
        table_invoice_detail.setDefaultRenderer(Object.class, render);
    }


    public synchronized invoicesPanel GetInstance() {
        if (instance == null) {
            instance = new invoicesPanel();
        }
        return instance;
    }


    private void init() {
        setLayout(new BorderLayout());
        MainPanel = new JPanel(new BorderLayout(10, 10));
        MainPanel.add(SearchPanel(), BorderLayout.NORTH);
        MainPanel.add(TableComponent(), BorderLayout.CENTER);
        MainPanel.add(PanelSouth(), BorderLayout.SOUTH);
        add(MainPanel, BorderLayout.CENTER);
    }

    private JPanel SearchPanel() {
        JPanel SearchComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        SearchComponent.setBackground(COLOR);

        JLabel ID_invoices_lb = new JLabel("ID invoices");
        ID_invoices_lb.setFont(FONT_ITEM);
//        ID_invoices_lb.setForeground(Color.WHITE);


        JTextField ID_invoices_field = new JTextField(20);
        txfSearch = ID_invoices_field;

        SearchBut = new JButton("Search");
        SearchBut.setFont(FONT_ITEM);
        SearchBut.putClientProperty(FlatClientProperties.STYLE_CLASS, "info");

        refreshBut = new JButton(" Refresh");
        refreshBut.setFont(FONT_ITEM);
        refreshBut.putClientProperty(FlatClientProperties.STYLE_CLASS, "secondary");

        SearchComponent.add(ID_invoices_lb);
        SearchComponent.add(ID_invoices_field);
        SearchComponent.add(SearchBut);
        SearchComponent.add(refreshBut);
        return SearchComponent;

    }

    private JPanel TableComponent() {
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        JPanel invoice_Panel = new JPanel(new BorderLayout());

        String[] col_invoice = {"ID invoices", "Created at", "Total"};
        model = new DefaultTableModel(col_invoice, 0);
        table_invoices = new JTable(model);
        table_invoices.setRowHeight(28);
        JTableHeader header = table_invoices.getTableHeader();
        header.setForeground(COLOR);
        header.setFont(FONT_ITEM);
        JLabel title = new JLabel("Invoice");
        title.setForeground(Color.decode("#4527a3"));
        title.setFont(new Font("Cascadia Code", Font.BOLD, 20));

        invoice_Panel.add(title, BorderLayout.PAGE_START);
        invoice_Panel.add(new JScrollPane(table_invoices), BorderLayout.CENTER);
        invoice_Panel.setPreferredSize(new Dimension(400, 0));

        tablePanel.add(invoice_Panel, BorderLayout.WEST);

        JPanel invoice_detailPanel = new JPanel(new BorderLayout());

        String[] col_invoice_detail = {"ID invoices", "Product name", "Quantity", "Unit price", "Total"};
        invoice_detail_model = new DefaultTableModel(col_invoice_detail, 0);
        table_invoice_detail = new JTable(invoice_detail_model);
        table_invoice_detail.setRowHeight(28);
        JTableHeader header1 = table_invoice_detail.getTableHeader();
        header1.setForeground(COLOR);
        header1.setFont(FONT_ITEM);

        JLabel title_detail = new JLabel("Invoice detail");
        title_detail.setForeground(Color.decode("#29384a"));
        title_detail.setFont(new Font("Cascadia Code", Font.BOLD, 20));

        invoice_detailPanel.add(title_detail, BorderLayout.PAGE_START);
        invoice_detailPanel.add(new JScrollPane(table_invoice_detail), BorderLayout.CENTER);

        tablePanel.add(invoice_detailPanel, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel PanelSouth() {
        JPanel panel3 = new JPanel(new BorderLayout(10, 10));

        JPanel MoneyPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        Total_Money = new JLabel(" Total: ");
        Total_Money.setFont(new Font("SansSerif", Font.BOLD, 14));


        Total_Money_Sum = new JLabel(" Invoice total: ");
        Total_Money_Sum.setFont(new Font("SansSerif", Font.BOLD, 14));

        MoneyPanel.add(Total_Money, "gap 0 20 10 0");
        MoneyPanel.add(Total_Money_Sum, "gap 0 20 10 0");

        panel3.add(MoneyPanel, BorderLayout.WEST);

        //panel button
        JPanel ButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        printing = new JButton("Print invoice");
        printing.setFont(FONT_ITEM);

        delete_But = new JButton("Delete invoice");
        delete_But.setFont(FONT_ITEM);

        printing.addActionListener(e -> InvoicePDF_Printing());
        delete_But.addActionListener(e -> delete_invoice());

        printing.putClientProperty(FlatClientProperties.STYLE_CLASS, "success");
        delete_But.putClientProperty(FlatClientProperties.STYLE_CLASS, "danger");

        ButtonPanel.add(printing);
        ButtonPanel.add(delete_But);

        panel3.add(ButtonPanel, BorderLayout.EAST);

        return panel3;
    }

    private void getDataInvoice() {
        // Clear existing data
        model.setRowCount(0);

        String query = "SELECT invoice_id, created_at, total_amount FROM " + user_invoice + " ORDER BY created_at DESC";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("invoice_id");
                String createdAt = rs.getString("created_at");
                int totalAmount = rs.getInt("total_amount");

                // Format the date if needed
                // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // String formattedDate = sdf.format(rs.getTimestamp("created_at"));

                model.addRow(new Object[]{
                        id,
                        new TimeManager().TimeDateFormated(createdAt, "yyyy-MM-dd"), // or formattedDate if you format it
                        totalAmount
                });
            }

            // Update the total money label
//            updateTotalMoney();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading invoices: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getDataInvoiceDetail(String invoiceId) {
        // Clear existing data
        invoice_detail_model.setRowCount(0);

        if (invoiceId == null || invoiceId.isEmpty()) {
            return;
        }

        String query = "SELECT p.name, id.quantity, id.unit_price, (id.quantity * id.unit_price) as total " +
                "FROM " + user_invoice_details + " id " +
                "JOIN " + user_product + " p ON id.product_id = p.id " +
                "WHERE id.invoice_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, invoiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");
                    double total = rs.getDouble("total");

                    invoice_detail_model.addRow(new Object[]{
                            invoiceId,
                            productName,
                            quantity,
                            String.format("$%.2f", unitPrice),
                            String.format("$%.2f", total)
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading invoice details: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotalMoney(int index) {
        double total = 0;
//        for (int i = 0; i < model.getRowCount(); i++) {
        String amountStr = model.getValueAt(index, 2).toString().replace("$", "").replace(",", "");
        total += Double.parseDouble(amountStr);
//        }

        Total_Money_Sum.setText(String.format("Invoice total: $%.2f", total));
    }

    private void searchInvoices(String searchText) {
        // Clear existing data
        model.setRowCount(0);

        String query = "SELECT invoice_id, created_at, total_amount FROM " + user_invoice +
                " WHERE invoice_id LIKE ? ORDER BY created_at DESC";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Add wildcards to search for partial matches
            stmt.setString(1, "%" + searchText + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String id = rs.getString("invoice_id");
                    String createdAt = rs.getString("created_at");
                    double totalAmount = rs.getDouble("total_amount");

                    model.addRow(new Object[]{
                            id,
                            new TimeManager().TimeDateFormated(createdAt, "yyyy-MM-dd"),
                            String.format("$%.2f", totalAmount)
                    });
                }

                if (!hasResults) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No invoices found matching: " + searchText,
                            "No Results",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }

//            updateTotalMoney();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error searching invoices: " + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void InvoicePDF_Printing() {
        try {
            int row = table_invoices.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please choose invoice before printing");
                return;
            }

            int InvoiceID = Integer.parseInt(table_invoices.getValueAt(row, 0).toString());

            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontVN = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream("Invoice_" + InvoiceID + ".pdf"));

            document.open();

            document.add(new com.itextpdf.text.Phrase("Invoice", fontVN));
            document.add(new Paragraph(" "));
            document.add(new com.itextpdf.text.Phrase("Invoice ID: " + InvoiceID, fontVN));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.addCell(new com.itextpdf.text.Phrase("Product", fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Quantity", fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Unit price", fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Total", fontVN));

//            ResultSet rs = DB.getInstance().selectSQL("select p.name, i_detail.quantity, i_detail.unit_price, (i_detail.quantity * i_detail.unit_price) as total " +
//                    "from " + user_invoice_details + " i_detail " +
//                    "join " + user_product + " p on i_detail.product_id = p.id " +
//                    "where i_detail.detail_id = " + InvoiceID);
            String query = "SELECT p.name, id.quantity, id.unit_price, (id.quantity * id.unit_price) as total " +
                    "FROM " + user_invoice_details + " id " +
                    "JOIN " + user_product + " p ON id.product_id = p.id " +
                    "WHERE id.invoice_id = ?";

            int TotalSum = 0;

            try (Connection conn = DB.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, InvoiceID);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    pdfTable.addCell(new com.itextpdf.text.Phrase(rs.getString("name"), fontVN));
                    pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(rs.getInt("quantity")), fontVN));
                    pdfTable.addCell(new com.itextpdf.text.Phrase("$" + String.valueOf(rs.getDouble("unit_price")), fontVN));
                    pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(rs.getInt("total")), fontVN));
                    TotalSum += rs.getInt("total");
//                    System.out.println(rs.getString("name"));
                }

                document.add(pdfTable);
                document.add(new com.itextpdf.text.Phrase("Total: $" + TotalSum, fontVN));

                document.close();

                JOptionPane.showMessageDialog(this, "Invoice PDF have generated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete_invoice() {
        int row = table_invoices.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "You choose invoice before delete");
            return;
        }
        int Invoice_id = Integer.parseInt(table_invoices.getValueAt(row, 0).toString());
        int confim = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel the invoice " + Invoice_id + " ?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );
        if (confim != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            DB.getInstance().executionSQL("delete from " + user_invoice_details + " where invoice_id=" + Invoice_id);
            DB.getInstance().executionSQL("delete from " + user_invoice_details + " where invoice_id=" + Invoice_id);

            JOptionPane.showMessageDialog(this, "Cancelled successfully");
            invoice_detail_model.setRowCount(0);
            getDataInvoice();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}