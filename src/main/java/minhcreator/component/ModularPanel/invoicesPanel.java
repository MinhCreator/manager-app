package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import minhcreator.component.Refreshable;
import minhcreator.entity.InvoiceEntity;
import minhcreator.functional.database.dao.InvoiceDAO;
import minhcreator.functional.location.TimeManager;
import minhcreator.util.AppLogger;
import minhcreator.util.UIRefreshScheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static minhcreator.component.page.Login.login;

public class invoicesPanel extends javax.swing.JPanel implements Refreshable {
    private JPanel MainPanel;
    private JButton SearchBut, refreshBut;
    private JLabel Total_Money_Sum;
    private JButton printing, delete_But;
    private DefaultTableModel model, invoice_detail_model;
    private JTable table_invoices, table_invoice_detail;
    private JTextField txfSearch;
    private static invoicesPanel instance;
    private final int userId;
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private static final Font FONT_ITEM = new Font("SansSerif", Font.BOLD, 12);
    private static final Color COLOR = Color.decode("#4B6EAF");

    public invoicesPanel() {
        this.userId = login.getSession().getUserId();
        init();
        getDataInvoice();
        UIRefreshScheduler.getInstance().register(this);

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

        refreshBut.addActionListener(e -> {
            getDataInvoice();
            invoice_detail_model.setRowCount(0);
        });

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
        JLabel ID_invoices_lb = new JLabel("ID invoices");
        ID_invoices_lb.setFont(FONT_ITEM);
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

        String[] col_invoice = {"ID invoices", "Customer", "Created at", "Total"};
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
        Total_Money_Sum = new JLabel(" Invoice total: ");
        Total_Money_Sum.setFont(new Font("SansSerif", Font.BOLD, 14));
        MoneyPanel.add(Total_Money_Sum, "gap 0 20 10 0");
        panel3.add(MoneyPanel, BorderLayout.WEST);

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

    private static final DateTimeFormatter INVOICE_DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final TimeManager TIME_MANAGER = new TimeManager();

    private void getDataInvoice() {
        model.setRowCount(0);
        List<InvoiceEntity> invoices = invoiceDAO.findByUserId(userId);
        for (InvoiceEntity inv : invoices) {
            model.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getCustomerName() != null ? inv.getCustomerName() : "-",
                    TIME_MANAGER.TimeDateFormated(inv.getCreatedAt().toString(), "yyyy-MM-dd"),
                    inv.getTotalAmount()
            });
        }
    }

    private void getDataInvoiceDetail(String invoiceId) {
        invoice_detail_model.setRowCount(0);
        if (invoiceId == null || invoiceId.isEmpty()) return;

        SwingWorker<Void, Object[]> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                List<Object[]> details = invoiceDAO.getInvoiceDetails(Integer.parseInt(invoiceId), userId);
                for (Object[] row : details) {
                    publish(row);
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                for (Object[] row : chunks) {
                    invoice_detail_model.addRow(new Object[]{
                            invoiceId,
                            (String) row[0],
                            (int) row[1],
                            String.format("$%.2f", (double) row[2]),
                            String.format("$%.2f", (double) row[3])
                    });
                }
            }
        };
        worker.execute();
    }

    private void updateTotalMoney(int index) {
        double total = 0;
        String amountStr = model.getValueAt(index, 3).toString().replace("$", "").replace(",", "");
        total += Double.parseDouble(amountStr);
        Total_Money_Sum.setText(String.format("Invoice total: $%.2f", total));
    }

    private void searchInvoices(String searchText) {
        model.setRowCount(0);
        List<InvoiceEntity> invoices = invoiceDAO.searchByUserId(userId, searchText);
        for (InvoiceEntity inv : invoices) {
            model.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getCustomerName() != null ? inv.getCustomerName() : "-",
                    TIME_MANAGER.TimeDateFormated(inv.getCreatedAt().toString(), "yyyy-MM-dd"),
                    String.format("$%.2f", inv.getTotalAmount())
            });
        }
        if (invoices.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No invoices found matching: " + searchText,
                    "No Results", JOptionPane.INFORMATION_MESSAGE);
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

            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(
                    "C:\\Windows\\Fonts\\Arial.ttf", com.itextpdf.text.pdf.BaseFont.IDENTITY_H,
                    com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontVN = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Invoice_" + InvoiceID + ".pdf"));
            document.open();
            document.add(new com.itextpdf.text.Phrase("INVOICE", fontVN));
            document.add(new Paragraph(" "));
            document.add(new com.itextpdf.text.Phrase("Invoice ID: " + InvoiceID, fontVN));
            String customerName = table_invoices.getValueAt(row, 1).toString();
            document.add(new Paragraph(" "));
            document.add(new com.itextpdf.text.Phrase("Customer: " + customerName, fontVN));
            document.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.addCell(new com.itextpdf.text.Phrase("Product", fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Quantity", fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Unit price", fontVN));
            pdfTable.addCell(new com.itextpdf.text.Phrase("Total", fontVN));

            List<Object[]> details = invoiceDAO.getInvoiceDetails(InvoiceID, userId);
            int TotalSum = 0;
            for (Object[] det : details) {
                String name = (String) det[0];
                int qty = (int) det[1];
                double up = (double) det[2];
                double tot = (double) det[3];
                pdfTable.addCell(new com.itextpdf.text.Phrase(name, fontVN));
                pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(qty), fontVN));
                pdfTable.addCell(new com.itextpdf.text.Phrase("$" + up, fontVN));
                pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf((int) tot), fontVN));
                TotalSum += (int) tot;
            }

            document.add(pdfTable);
            document.add(new com.itextpdf.text.Phrase("Total: $" + TotalSum, fontVN));
            document.close();
            JOptionPane.showMessageDialog(this, "Invoice PDF have generated");
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
        int confim = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel the invoice " + Invoice_id + " ?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confim != JOptionPane.YES_OPTION) return;

        invoiceDAO.deleteInvoice(Invoice_id);
        JOptionPane.showMessageDialog(this, "Cancelled successfully");
        invoice_detail_model.setRowCount(0);
        getDataInvoice();
    }

    // ─── Refreshable ────────────────────────────────────────────────

    @Override
    public void refreshData() {
        EventQueue.invokeLater(() -> {
            getDataInvoice();
            invoice_detail_model.setRowCount(0);
            revalidate();
            repaint();
            AppLogger.debug("Invoices", "Auto-refreshed invoice list");
        });
    }

    @Override
    public int getRefreshIntervalMs() {
        return 15000;
    }
}
