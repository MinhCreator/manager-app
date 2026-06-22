package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.Card;
import minhcreator.component.Refreshable;
import minhcreator.component.model.ModelCard;
import minhcreator.component.page.Login;
import minhcreator.component.stock.StockStatus;
import minhcreator.entity.InventoryEntity;
import minhcreator.entity.InvoiceEntity;
import minhcreator.entity.ProductEntity;
import minhcreator.functional.database.dao.InventoryDAO;
import minhcreator.functional.database.dao.InvoiceDAO;
import minhcreator.functional.database.dao.ProductDAO;
import minhcreator.functional.database.dao.PurchaseOrderDAO;
import minhcreator.functional.session.sessionManager;
import minhcreator.util.AppLogger;
import minhcreator.util.UIRefreshScheduler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashPanel extends JPanel implements Refreshable {

    public sessionManager sharedSession = Login.session;
    private final int userId;
    private JPanel contentWrapper;

    public DashPanel() {
        this.userId = Login.session.getUserId();
        setLayout(new BorderLayout());
        contentWrapper = new JPanel();
        add(contentWrapper, BorderLayout.CENTER);
        buildContent();
        UIRefreshScheduler.getInstance().register(this);
        AppLogger.info("Dashboard", "Dashboard loaded — auto-refresh enabled");
    }

    @Override
    public void refreshData() {
        AppLogger.debug("Dashboard", "Auto-refreshing dashboard data");
        buildContent();
    }

    @Override
    public int getRefreshIntervalMs() {
        return 8000;
    }

    private void buildContent() {
        contentWrapper.removeAll();
        contentWrapper.setLayout(new MigLayout("insets 12, gap 10, fillx, filly", "[grow]", "[]10[grow]"));
        contentWrapper.add(createCardsRow(), "growx, wrap");
        contentWrapper.add(createBottomSplit(), "grow, push");
        contentWrapper.revalidate();
        contentWrapper.repaint();
    }

    // ─── Top Row: 4 Metric Cards ────────────────────────────────────

    private JPanel createCardsRow() {
        JPanel row = new JPanel(new MigLayout("gap 10, fillx", "[25%][25%][25%][25%]", "[]"));

        row.add(buildWelcomeCard(), "growx, h 110!");
        row.add(buildMetricCard("Profit", getProfit(), "$", "profit.svg", new Color(103, 80, 164)), "growx, h 110!");
        row.add(buildMetricCard("Total Stock", getCurrStorage(), "", "stock.svg", new Color(30, 142, 62)), "growx, h 110!");
        row.add(buildMetricCard("Expenses", getCost(), "$", "expense.svg", new Color(179, 38, 30)), "growx, h 110!");

        return row;
    }

    private Card buildWelcomeCard() {
        Card card = new Card();
        card.setData(new ModelCard("Welcome Back", sharedSession.getUsername()));
        card.setAccentColor(new Color(0, 170, 120));
        try {
            card.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/PeopleFilled.svg", 0.6f));
        } catch (Exception ignored) {}
        return card;
    }

    private Card buildMetricCard(String title, double value, String prefix, String svg, Color accent) {
        Card card = new Card();
        card.setData(new ModelCard(title, value));
        card.setAccentColor(accent);
        try {
            card.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/" + svg, 0.6f));
        } catch (Exception ignored) {}
        return card;
    }

    // ─── Bottom Split: Table + Alerts ───────────────────────────────

    private JPanel createBottomSplit() {
        JPanel split = new JPanel(new MigLayout("gap 10, fillx, filly", "[70%][30%]", "[grow]"));
        split.add(createInvoiceTablePanel(), "grow, push, h 300!");
        split.add(createLowStockPanel(), "grow, h 300!");
        return split;
    }

    // ─── Left: Recent Invoices Table ─────────────────────────────────

    private JPanel createInvoiceTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:16;border:1,1,1,1,$Component.borderColor,,16");

        JLabel header = new JLabel("  Recent Invoices");
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(header, BorderLayout.NORTH);

        String[] cols = {"ID", "Customer", "Total", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        InvoiceDAO invDAO = new InvoiceDAO();
        List<InvoiceEntity> invoices = invDAO.findByUserId(userId);
        int displayCount = Math.min(invoices.size(), 20);

        for (int i = 0; i < displayCount; i++) {
            InvoiceEntity inv = invoices.get(i);
            String date = inv.getCreatedAt() != null
                    ? inv.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    : "";
            model.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getCustomerName() != null ? inv.getCustomerName() : "-",
                    String.format("$%,.0f", inv.getTotalAmount()),
                    date
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        double totalInvoiceAmount = invoices.stream().mapToDouble(InvoiceEntity::getTotalAmount).sum();
        JLabel summary = new JLabel(String.format("  Total: $%,.0f across %d invoices", totalInvoiceAmount, invoices.size()));
        summary.putClientProperty(FlatClientProperties.STYLE, "font:-1");
        summary.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        panel.add(summary, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Right: Low Stock Alerts ─────────────────────────────────────

    private JPanel createLowStockPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:16;border:1,1,1,1,$Component.borderColor,,16");

        JLabel header = new JLabel("  Stock Alerts");
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(header, BorderLayout.NORTH);

        ProductDAO prodDAO = new ProductDAO();
        InventoryDAO invDAO = new InventoryDAO();
        List<ProductEntity> products = prodDAO.findByUserId(userId);
        List<InventoryEntity> invList = invDAO.findByUserId(userId);

        String[] cols = {"Product", "Qty", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        int threshold = 10;
        int lowCount = 0;
        int outCount = 0;

        for (ProductEntity p : products) {
            InventoryEntity inv = invList.stream()
                    .filter(i -> i.getProductId() == p.getId())
                    .findFirst().orElse(null);
            int qty = inv != null ? inv.getQuantity() : 0;

            StockStatus status = StockStatus.getStatusBage(qty, threshold);
            if (status != StockStatus.AVAIlABLE) {
                model.addRow(new Object[]{p.getName(), qty, status});
                if (qty == 0) outCount++;
                else lowCount++;
            }
        }

        if (model.getRowCount() == 0) {
            JLabel emptyLabel = new JLabel("  ✓ All products well-stocked", SwingConstants.CENTER);
            emptyLabel.setForeground(new Color(30, 142, 62));
            emptyLabel.putClientProperty(FlatClientProperties.STYLE, "font:+1");
            panel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setShowGrid(false);
            table.getColumnModel().getColumn(2).setPreferredWidth(50);

            table.getColumnModel().getColumn(2).setCellRenderer(
                    new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable t, Object v,
                                boolean s, boolean f, int r, int c) {
                            Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                            if (v instanceof StockStatus status) {
                                setForeground(status.getColor());
                                setText(status.getText().trim());
                            }
                            return comp;
                        }
                    }
            );

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(null);
            scroll.getViewport().setOpaque(false);
            panel.add(scroll, BorderLayout.CENTER);
        }

        String alertMsg;
        Color alertColor;
        if (outCount > 0 && lowCount > 0) {
            alertMsg = String.format("⚠ %d out of stock, %d low", outCount, lowCount);
            alertColor = new Color(230, 57, 70);
        } else if (outCount > 0) {
            alertMsg = String.format("⚠ %d product(s) out of stock", outCount);
            alertColor = new Color(230, 57, 70);
        } else if (lowCount > 0) {
            alertMsg = String.format("⚠ %d product(s) low on stock", lowCount);
            alertColor = new Color(240, 150, 30);
        } else {
            alertMsg = "✓ All products well-stocked";
            alertColor = new Color(30, 142, 62);
        }

        JLabel footer = new JLabel("  " + alertMsg);
        footer.setForeground(alertColor);
        footer.putClientProperty(FlatClientProperties.STYLE, "font:-1");
        footer.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Data Methods ────────────────────────────────────────────────

    private double getProfit() {
        InvoiceDAO invDAO = new InvoiceDAO();
        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        List<Object[]> income = invDAO.getIncomeByCategory(userId);
        List<Object[]> cost = poDAO.getCostByCategory(userId);
        double rev = income.stream().mapToDouble(r -> r[1] != null ? (double) r[1] : 0).sum();
        double exp = cost.stream().mapToDouble(r -> r[1] != null ? (double) r[1] : 0).sum();
        return rev - exp;
    }

    private double getCost() {
        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        List<Object[]> cost = poDAO.getCostByCategory(userId);
        return cost.stream().mapToDouble(r -> r[1] != null ? (double) r[1] : 0).sum();
    }

    private int getCurrStorage() {
        InventoryDAO invDAO = new InventoryDAO();
        return invDAO.findByUserId(userId).stream()
                .mapToInt(InventoryEntity::getQuantity).sum();
    }
}
