package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.form.SimpleForm;
import minhcreator.functional.database.dao.InvoiceDAO;
import minhcreator.functional.database.dao.PurchaseOrderDAO;
import minhcreator.functional.location.TimeManager;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;
import raven.datetime.DatePicker;
import raven.datetime.event.DateSelectionEvent;
import raven.datetime.event.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import static minhcreator.component.page.Login.login;

public class AnalyticsPanel extends SimpleForm {

    private final int userId;
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
    private static final TimeManager TIME_MANAGER = new TimeManager();
    private JButton reloadLineChart;

    public AnalyticsPanel() {
        this.userId = login.getSession().getUserId();
        init();
    }

    private void init() {
        setLayout(new MigLayout(
                        "wrap,fillx,gap 10",
                        "[grow,center]",
                        "[]1[]1[]"
                )
        );
        createPieChart();
        createLineChart();
    }

    private void createPieChart() {
        ProductIncome = new PieChart();
        JLabel header1 = new JLabel("Product Income");
        header1.putClientProperty(FlatClientProperties.STYLE, "font:+1");
        ProductIncome.setHeader(header1);
        ProductIncome.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        ProductIncome.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5,$Component.borderColor,,20");
        add(ProductIncome, "split 3,w 100:355:600,height 240");

        ProductCost = new PieChart();
        JLabel header2 = new JLabel("Product Cost");
        header2.putClientProperty(FlatClientProperties.STYLE, "font:+1");
        ProductCost.setHeader(header2);
        ProductCost.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        ProductCost.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5,$Component.borderColor,,20");
        add(ProductCost, "w 100:355:600,height 240");

        ProductProfit = new PieChart();
        JLabel header3 = new JLabel("Product Profit");
        header3.putClientProperty(FlatClientProperties.STYLE, "font:+1");
        ProductProfit.setHeader(header3);
        ProductProfit.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        ProductProfit.setChartType(PieChart.ChartType.DONUT_CHART);
        ProductProfit.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5,$Component.borderColor,,20");
        add(ProductProfit, "w 100:355:600,height 240");

        new Thread(() -> {
            var incomeData = createPieDataIncome();
            var costData = createPieDataCost();
            var profitData = createPieDataProfit();
            EventQueue.invokeLater(() -> {
                ProductIncome.setDataset(incomeData);
                ProductCost.setDataset(costData);
                ProductProfit.setDataset(profitData);
            });
        }).start();
    }

    private void createLineChart() {
        DatePicker datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setSeparator(" to ");
        datePicker.setUsePanelOption(true);
        datePicker.setDateSelectionAble(localDate -> !localDate.isAfter(LocalDate.now()));

        LocalDate now = LocalDate.now();
        LocalDate fiveDaysAgo = now.minusDays(5);
        datePicker.setSelectedDateRange(fiveDaysAgo, now);
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateSelectionEvent dateSelectionEvent) {
                updateChartWithSelectedDates(datePicker);
            }
        });

        reloadLineChart = new JButton();
        reloadLineChart.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/refresh.svg"));
        reloadLineChart.addActionListener(e -> updateChartWithSelectedDates(datePicker));

        JFormattedTextField calendarChooser = new JFormattedTextField();
        datePicker.setEditor(calendarChooser);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(calendarChooser, BorderLayout.WEST);
        headerPanel.add(reloadLineChart, BorderLayout.EAST);

        lineChart = new LineChart();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5,$Component.borderColor,,20");

        add(headerPanel, "align left,wrap");
        add(lineChart, "w 100:1075:1500,height 400");
        updateChartWithSelectedDates(datePicker);
    }

    private void createBarChart() {
        Income_barChart = new HorizontalBarChart();
        JLabel header1 = new JLabel("Monthly Income");
        header1.putClientProperty(FlatClientProperties.STYLE, "font:+1;border:0,0,5,0");
        Income_barChart.setHeader(header1);
        Income_barChart.setBarColor(Color.decode("#f97316"));
        Income_barChart.setDataset(createBarDataIncome());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5,$Component.borderColor,,20");
        add(Income_barChart, "split 2,gap 0 20 2,w 100:500:600,height 175");

        Expense_barChart = new HorizontalBarChart();
        JLabel header2 = new JLabel("Monthly Expense");
        header2.putClientProperty(FlatClientProperties.STYLE, "font:+1;border:0,0,5,0");
        Expense_barChart.setHeader(header2);
        Expense_barChart.setBarColor(Color.decode("#10b981"));
        Expense_barChart.setDataset(createBarDataExpense());
        add(Expense_barChart, "gap 0 0 2,w 100:500:600,height 175");
    }

    private DefaultPieDataset createPieDataIncome() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<Object[]> rows = invoiceDAO.getIncomeByCategory(userId);
        for (Object[] row : rows) {
            String category = (String) row[0];
            double total = row[1] != null ? (double) row[1] : 0.0;
            dataset.setValue(category, total);
        }
        return dataset;
    }

    private DefaultPieDataset createPieDataCost() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<Object[]> rows = purchaseOrderDAO.getCostByCategory(userId);
        for (Object[] row : rows) {
            String category = (String) row[0];
            double total = row[1] != null ? (double) row[1] : 0.0;
            dataset.setValue(category, total);
        }
        return dataset;
    }

    private DefaultPieDataset createPieDataProfit() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<Object[]> rows = invoiceDAO.getProfitByCategory(userId);
        for (Object[] row : rows) {
            String category = (String) row[0];
            double total = row[1] != null ? (double) row[1] : 0.0;
            dataset.setValue(category, total);
        }
        return dataset;
    }

    private DefaultPieDataset<String> createBarDataIncome() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        TimeManager time = new TimeManager();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");
        Calendar cal = Calendar.getInstance();
        String currentMonthSQL = time.TimeNowFormat("yyyy-MM");

        try {
            List<Object[]> rows = invoiceDAO.getMonthlyIncome(userId, currentMonthSQL);
            for (Object[] row : rows) {
                String month = monthFormat.format(java.sql.Date.valueOf(row[0].toString() + "-01"));
                double income = row[1] != null ? (double) row[1] : 0.0;
                if (income > 0) {
                    dataset.setValue("Income (" + month + ")", income);
                }
            }
            if (dataset.getKeys().isEmpty()) {
                cal.setTime(new java.util.Date());
                for (int i = 0; i < 6; i++) {
                    String month = monthFormat.format(cal.getTime());
                    dataset.setValue("Income (" + month + ")", 1000 * (6 - i));
                    cal.add(Calendar.MONTH, -1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            cal.setTime(new java.util.Date());
            for (int i = 0; i < 6; i++) {
                String month = monthFormat.format(cal.getTime());
                dataset.setValue("Income (" + month + ")", 1000 * (6 - i));
                cal.add(Calendar.MONTH, -1);
            }
        }
        return dataset;
    }

    private DefaultPieDataset<String> createBarDataExpense() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        TimeManager time = new TimeManager();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");
        Calendar cal = Calendar.getInstance();
        String currentMonthSQL = time.TimeNowFormat("yyyy-MM");

        try {
            List<Object[]> rows = purchaseOrderDAO.getMonthlyExpense(userId, currentMonthSQL);
            for (Object[] row : rows) {
                String month = monthFormat.format(java.sql.Date.valueOf(row[0].toString() + "-01"));
                double expense = row[1] != null ? (double) row[1] : 0.0;
                if (expense > 0) {
                    dataset.setValue("Expense (" + month + ")", expense);
                }
            }
            if (dataset.getKeys().isEmpty()) {
                cal.setTime(new java.util.Date());
                for (int i = 0; i < 6; i++) {
                    String month = monthFormat.format(cal.getTime());
                    dataset.setValue("Expense (" + month + ")", 600 * (6 - i));
                    cal.add(Calendar.MONTH, -1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            cal.setTime(new java.util.Date());
            for (int i = 0; i < 6; i++) {
                String month = monthFormat.format(cal.getTime());
                dataset.setValue("Expense (" + month + ")", 600 * (6 - i));
                cal.add(Calendar.MONTH, -1);
            }
        }
        return dataset;
    }

    private void createLineChartData(String startDate, String endDate) {
        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        TimeManager tm = TIME_MANAGER;
        int backTraceRange = 5;
        if (startDate == null || endDate == null) {
            endDate = tm.TimeNowFormat("yyyy-MM-dd");
            startDate = tm.wayBackMachine(endDate, "yyyy-MM-dd", backTraceRange);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        try {
            List<Object[]> incomeRows = invoiceDAO.getDailyIncomeBetween(userId, startDate, endDate);
            for (Object[] row : incomeRows) {
                String date = sdf.format(java.sql.Date.valueOf(row[0].toString()));
                double total = row[1] != null ? (double) row[1] : 0.0;
                dataset.addValue(total, "Income", date);
            }

            List<Object[]> expenseRows = purchaseOrderDAO.getDailyExpenseBetween(userId, startDate, endDate);
            for (Object[] row : expenseRows) {
                String date = sdf.format(java.sql.Date.valueOf(row[0].toString()));
                double total = row[1] != null ? (double) row[1] : 0.0;
                dataset.addValue(total, "Expense", date);
            }

            for (int i = 0; i < dataset.getColumnCount(); i++) {
                String date = dataset.getColumnKey(i);
                double income = dataset.getValue("Income", date) != null ?
                        dataset.getValue("Income", date).doubleValue() : 0;
                double expense = dataset.getValue("Expense", date) != null ?
                        dataset.getValue("Expense", date).doubleValue() : 0;
                dataset.addValue(income - expense, "Profit", date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 7; i++) {
                String date = sdf.format(cal.getTime());
                dataset.addValue(0, "Income", date);
                dataset.addValue(0, "Expense", date);
                dataset.addValue(0, "Profit", date);
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
        }

        lineChart.setCategoryDataset(dataset);
        lineChart.getChartColor().addColor(
                Color.decode("#38bdf8"),
                Color.decode("#fb7185"),
                Color.decode("#34d399")
        );

        JLabel header = new JLabel("Financial analyse (" + startDate + " to " + endDate + ")");
        header.putClientProperty(FlatClientProperties.STYLE, "font:+1;border:0,0,5,0");
        lineChart.setHeader(header);
    }

    private void updateChartWithSelectedDates(DatePicker datePicker) {
        LocalDate[] dates = datePicker.getSelectedDateRange();
        DateTimeFormatter df_getDatabase = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (dates != null && dates.length == 2) {
            String startDate = df_getDatabase.format(dates[0]);
            String endDate = df_getDatabase.format(dates[1]);
            createLineChartData(startDate, endDate);
        } else {
            createLineChartData(null, null);
        }
    }

    private LineChart lineChart;
    private HorizontalBarChart Income_barChart;
    private HorizontalBarChart Expense_barChart;
    private PieChart ProductIncome;
    private PieChart ProductCost;
    private PieChart ProductProfit;
}
