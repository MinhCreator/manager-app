package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.form.SimpleForm;
import minhcreator.functional.database.DB;
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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

import static minhcreator.component.page.Login.login;

/**
 *
 * @author Designed by Raven
 * @author Modified by MinhCreatorVN
 */
public class AnalyticsPanel extends SimpleForm {

    private final String user_inventory = login.getSession().getYour_inventory();
    private final String user_invoice_details = login.getSession().getUser_invoice_details();
    private final String user_purchase_orders = login.getSession().getUser_cost_table();
    private final String user_invoice = login.getSession().getUser_invoices();
    private JButton reloadLineChart;

    public AnalyticsPanel() {
        init();
    }


    private void init() {
//        setLayout(new MigLayout("wrap,fill,gap 10", "fill"));
        setLayout(new MigLayout(
                        "wrap,fillx,gap 10",
                        "[grow,center]",
                        "[]1[]1[]"
                )
        );
        createPieChart();
        createLineChart();
        createBarChart();
    }

    private void createPieChart() {
        ProductIncome = new PieChart();
        JLabel header1 = new JLabel("Product Income");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        ProductIncome.setHeader(header1);
        ProductIncome.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        ProductIncome.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        ProductIncome.setDataset(createPieDataIncome());
        add(ProductIncome, "split 3,w 100:355:600,height 240");

        ProductCost = new PieChart();
        JLabel header2 = new JLabel("Product Cost");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        ProductCost.setHeader(header2);
        ProductCost.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
//        ProductCost.getChartColor();
        ProductCost.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        ProductCost.setDataset(createPieDataCost());
        add(ProductCost, "w 100:355:600,height 240");

        ProductProfit = new PieChart();
        JLabel header3 = new JLabel("Product Profit");
        header3.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        ProductProfit.setHeader(header3);
        ProductProfit.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        ProductProfit.setChartType(PieChart.ChartType.DONUT_CHART);
        ProductProfit.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        ProductProfit.setDataset(createPieDataProfit());
        add(ProductProfit, "w 100:355:600,height 240");
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
        reloadLineChart.addActionListener(e -> {
            updateChartWithSelectedDates(datePicker);
        });

        JFormattedTextField calendarChooser = new JFormattedTextField();
        datePicker.setEditor(calendarChooser);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(calendarChooser, BorderLayout.WEST);
        headerPanel.add(reloadLineChart, BorderLayout.EAST);

        lineChart = new LineChart();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");

        add(headerPanel, "align left,wrap");
        add(lineChart, "w 100:1075:1500,height 240");
        updateChartWithSelectedDates(datePicker);

    }

    private void createBarChart() {
        // BarChart 1
        Income_barChart = new HorizontalBarChart();
        JLabel header1 = new JLabel("Monthly Income");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        Income_barChart.setHeader(header1);
        Income_barChart.setBarColor(Color.decode("#f97316"));
        Income_barChart.setDataset(createBarDataIncome());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        add(Income_barChart, "split 2,gap 0 20 2,w 100:500:600,height 175");

        // BarChart 2
        Expense_barChart = new HorizontalBarChart();
        JLabel header2 = new JLabel("Monthly Expense");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        Expense_barChart.setHeader(header2);
        Expense_barChart.setBarColor(Color.decode("#10b981"));
        Expense_barChart.setDataset(createBarDataExpense());
        add(Expense_barChart, "gap 0 0 2,w 100:500:600,height 175");

    }

    private DefaultPieDataset createBarData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Random random = new Random();
        TimeManager time = new TimeManager();
        String getMonth = time.TimeNowFormat("MMM");
        dataset.addValue(getMonth + " (ongoing)", random.nextInt(100));
        dataset.addValue("June", random.nextInt(100));
        dataset.addValue("May", random.nextInt(100));
        dataset.addValue("April", random.nextInt(100));
        dataset.addValue("March", random.nextInt(100));
        dataset.addValue("February", random.nextInt(100));
        return dataset;
    }

    private DefaultPieDataset createPieDataIncome() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String query = "SELECT i.category, SUM(id.quantity * id.unit_price) as total " +
                "FROM " + user_invoice_details + " id " +
                "JOIN " + user_inventory + " i ON id.product_id = i.product_id " +
                "GROUP BY i.category";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String category = rs.getString("category");
                double total = rs.getDouble("total");
                dataset.setValue(category, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Add some default data or show error message
            dataset.setValue("Error", 1);
        }

        return dataset;

    }

    private DefaultPieDataset createPieDataCost() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String query = "SELECT i.category, SUM(po.quantity * po.import_price) as total_cost " +
                "FROM " + user_purchase_orders + " po " +
                "JOIN " + user_inventory + " i ON po.product_id = i.product_id " +
                "GROUP BY i.category";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String category = rs.getString("category");
                double total = rs.getDouble("total_cost");
                dataset.setValue(category, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Add some default data or show error message
            dataset.setValue("Error", 1);
        }

        return dataset;

    }

    private DefaultPieDataset createPieDataProfit() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String query = "SELECT " +
                "i.category, " +
                "SUM((so.quantity * so.unit_price) - (po.quantity * po.import_price)) as profit " +
                "FROM " + user_invoice_details + " so " +
                "JOIN " + user_purchase_orders + " po ON so.product_id = po.product_id " +
                "JOIN " + user_inventory + " i ON so.product_id = i.product_id " +
                "GROUP BY i.category";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String category = rs.getString("category");
                double total = rs.getDouble("profit");
                dataset.setValue(category, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Add some default data or show error message
            dataset.setValue("Error", 1);
        }

        return dataset;

    }

    private DefaultPieDataset createPieData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Random random = new Random();
        dataset.addValue("Bags", random.nextInt(100) + 50);
        dataset.addValue("Hats", random.nextInt(100) + 50);
        dataset.addValue("Glasses", random.nextInt(100) + 50);
        dataset.addValue("Watches", random.nextInt(100) + 50);
        dataset.addValue("Jewelry", random.nextInt(100) + 50);
        return dataset;
    }

    private DefaultPieDataset<String> createBarDataIncome() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        TimeManager time = new TimeManager();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");
        Calendar cal = Calendar.getInstance();

        // Get current month and year
        String currentMonth = time.TimeNowFormat("MMM yyyy");
        String currentMonthSQL = time.TimeNowFormat("yyyy-MM");

        try (Connection conn = DB.getConnection()) {
            // Query for monthly income data
            String incomeQuery = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, " +
                    "SUM(total_amount) as total " +
                    "FROM " + user_invoice + " " +
                    "WHERE created_at >= DATE_SUB(STR_TO_DATE(?, '%Y-%m-01'), INTERVAL 5 MONTH) " +
                    "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
                    "ORDER BY month DESC LIMIT 6";

            // Get income data
            try (PreparedStatement stmt = conn.prepareStatement(incomeQuery)) {
                stmt.setString(1, currentMonthSQL);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String month = monthFormat.format(rs.getDate("month"));
                        double income = rs.getDouble("total");
                        if (income > 0) {
                            dataset.setValue("Income (" + month + ")", income);
                        }
                    }
                }
            }

            // Ensure we have at least some data
            if (dataset.getKeys().isEmpty()) {
                // Add sample data if no real data exists
                cal.setTime(new java.util.Date());
                for (int i = 0; i < 6; i++) {
                    String month = monthFormat.format(cal.getTime());
                    dataset.setValue("Income (" + month + ")", 1000 * (6 - i));
                    cal.add(Calendar.MONTH, -1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to sample data if there's an error
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

        // Get current month and year
        String currentMonth = time.TimeNowFormat("MMM yyyy");
        String currentMonthSQL = time.TimeNowFormat("yyyy-MM");

        try (Connection conn = DB.getConnection()) {
            // Query for monthly expense data
            String expenseQuery = "SELECT DATE_FORMAT(po.date, '%Y-%m') as month, " +
                    "SUM(po.quantity * po.import_price) as total " +
                    "FROM " + user_purchase_orders + " po " +
                    "WHERE po.date >= DATE_SUB(STR_TO_DATE(?, '%Y-%m-01'), INTERVAL 5 MONTH) " +
                    "GROUP BY DATE_FORMAT(po.date, '%Y-%m') " +
                    "ORDER BY month DESC LIMIT 6";

            // Get expense data
            try (PreparedStatement stmt = conn.prepareStatement(expenseQuery)) {
                stmt.setString(1, currentMonthSQL);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String month = monthFormat.format(rs.getDate("month"));
                        double expense = rs.getDouble("total");
                        if (expense > 0) {
                            dataset.setValue("Expense (" + month + ")", expense);
                        }
                    }
                }
            }

            // Ensure we have at least some data
            if (dataset.getKeys().isEmpty()) {
                // Add sample data if no real data exists
                cal.setTime(new java.util.Date());
                for (int i = 0; i < 6; i++) {
                    String month = monthFormat.format(cal.getTime());
                    dataset.setValue("Expense (" + month + ")", 600 * (6 - i));
                    cal.add(Calendar.MONTH, -1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to sample data if there's an error
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

        // Get the selected date range from the calendar chooser
        TimeManager tm = new TimeManager();

        // If no date range is selected, default to last 5 days
        int backTraceRange = 5;
        if (startDate == null || endDate == null) {
            endDate = tm.TimeNowFormat("yyyy-MM-dd");
            startDate = tm.wayBackMachine(endDate, "yyyy-MM-dd", backTraceRange);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Query for income data
            String incomeQuery = "SELECT DATE(created_at) as date, SUM(total_amount) as total " +
                    "FROM " + user_invoice + " " +
                    "WHERE created_at BETWEEN ? AND ? " +
                    "GROUP BY DATE(created_at) " +
                    "ORDER BY date";

            // Query for expense data (purchase orders)
            String expenseQuery = "SELECT DATE(po.date) as date, SUM(po.quantity * po.import_price) as total " +
                    "FROM " + user_purchase_orders + " po " +
                    "WHERE po.date BETWEEN ? AND ? " +
                    "GROUP BY DATE(po.date) " +
                    "ORDER BY date";

            // Execute income query
            try (Connection conn = DB.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(incomeQuery)) {

                stmt.setString(1, startDate);
                stmt.setString(2, endDate);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String date = sdf.format(rs.getDate("date"));
                        double total = rs.getDouble("total");
                        dataset.addValue(total, "Income", date);
                    }
                }
            }

            // Execute expense query
            try (Connection conn = DB.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(expenseQuery)) {

                stmt.setString(1, startDate);
                stmt.setString(2, endDate);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String date = sdf.format(rs.getDate("date"));
                        double total = rs.getDouble("total");
                        dataset.addValue(total, "Expense", date);
                    }
                }
            }

            // Calculate profit for each day
            for (int i = 0; i < dataset.getColumnCount(); i++) {
                String date = dataset.getColumnKey(i);
                double income = dataset.getValue("Income", date) != null ?
                        dataset.getValue("Income", date).doubleValue() : 0;
                double expense = dataset.getValue("Expense", date) != null ?
                        dataset.getValue("Expense", date).doubleValue() : 0;
                double profit = income - expense;
                dataset.addValue(profit, "Profit", date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Add some default data in case of error
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 7; i++) {
                String date = sdf.format(cal.getTime());
                dataset.addValue(0, "Income", date);
                dataset.addValue(0, "Expense", date);
                dataset.addValue(0, "Profit", date);
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
        }

        // Configure the line chart
        lineChart.setCategoryDataset(dataset);
        lineChart.getChartColor().addColor(
                Color.decode("#38bdf8"),  // Blue for Income
                Color.decode("#fb7185"),  // Red for Expense
                Color.decode("#34d399")   // Green for Profit
        );

        JLabel header = new JLabel("Financial Trend (" +
                startDate + " to " + endDate + ")");
        header.putClientProperty(FlatClientProperties.STYLE,
                "font:+1;border:0,0,5,0");
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