package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.form.other.SimpleForm;
import minhcreator.functional.location.TimeManager;
import minhcreator.util.DateCalculator;
import net.miginfocom.swing.MigLayout;
import raven.chart.ChartLegendRenderer;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;
import raven.datetime.DatePicker;
import raven.datetime.DateSelectionAble;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Designed by Raven
 * @author Modified by MinhCreatorVN
 */
public class AnalyticsPanel extends SimpleForm {

    public AnalyticsPanel() {
        init();
    }

    @Override
    public void formRefresh() {
        lineChart.startAnimation();
        ProductIncome.startAnimation();
        ProductCost.startAnimation();
        ProductProfit.startAnimation();
        barChart1.startAnimation();
        barChart2.startAnimation();
    }

    @Override
    public void formInitAndOpen() {
        System.out.println("init and open");
    }

    @Override
    public void formOpen() {
        System.out.println("Open");
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
        ProductIncome.setDataset(createPieData());
        add(ProductIncome, "split 3,w 100:355:600,height 240");

        ProductCost = new PieChart();
        JLabel header2 = new JLabel("Product Cost");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        ProductCost.setHeader(header2);
        ProductCost.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        ProductCost.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        ProductCost.setDataset(createPieData());
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
        ProductProfit.setDataset(createPieData());
        add(ProductProfit, "w 100:355:600,height 240");
    }

    private void createLineChart() {
        DatePicker datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setSeparator(" to date ");
        datePicker.setUsePanelOption(true);
        datePicker.setDateSelectionAble(new DateSelectionAble() {
            @Override
            public boolean isDateSelectedAble(LocalDate localDate) {
                return !localDate.isAfter(LocalDate.now());
            }
        });

        JFormattedTextField calenderChooser = new JFormattedTextField();
        datePicker.setEditor(calenderChooser);

        lineChart = new LineChart();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
//        add(lineChart, "w 800:750:900,height 240");
        add(calenderChooser, "align left,width 250,wrap");
        add(lineChart, "w 100:1075:1500,height 240");
        createLineChartData();
    }

    private void createBarChart() {
        // BarChart 1
        barChart1 = new HorizontalBarChart();
        JLabel header1 = new JLabel("Monthly Income");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart1.setHeader(header1);
        barChart1.setBarColor(Color.decode("#f97316"));
        barChart1.setDataset(createBarData());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        add(barChart1, "split 2,gap 0 20 2,w 100:500:600,height 175");

        // BarChart 2
        barChart2 = new HorizontalBarChart();
        JLabel header2 = new JLabel("Monthly Expense");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart2.setHeader(header2);
        barChart2.setBarColor(Color.decode("#10b981"));
        barChart2.setDataset(createBarData());
        add(barChart2, "gap 0 0 2,w 100:500:600,height 175");
//        add(panel1, "split 2,gap 0 20");

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

    private void createLineChartData() {
        DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        Random ran = new Random();
        int randomDate = 30;
        for (int i = 1; i <= randomDate; i++) {
            String date = df.format(cal.getTime());
            System.out.println(date);
            categoryDataset.addValue(ran.nextInt(700) + 5, "Income", date);
            categoryDataset.addValue(ran.nextInt(700) + 5, "Expense", date);
            categoryDataset.addValue(ran.nextInt(700) + 5, "Profit", date);

            cal.add(Calendar.DATE, 1);
        }

        /**
         * Control the legend we do not show all legend
         */
        try {
            Date date = df.parse(categoryDataset.getColumnKey(0));
            Date dateEnd = df.parse(categoryDataset.getColumnKey(categoryDataset.getColumnCount() - 1));


            DateCalculator dcal = new DateCalculator(date, dateEnd);
            long diff = dcal.getDifferenceDays();

            double d = Math.ceil((diff / 10f));
            lineChart.setLegendRenderer(new ChartLegendRenderer() {
                @Override
                public Component getLegendComponent(Object legend, int index) {
                    if (index % d == 0) {
                        return super.getLegendComponent(legend, index);
                    } else {
                        return null;
                    }
                }
            });
        } catch (ParseException e) {
            System.err.println(e);
        }

        lineChart.setCategoryDataset(categoryDataset);
        lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
        JLabel header = new JLabel("Income Data");
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        lineChart.setHeader(header);
    }


    private LineChart lineChart;
    private HorizontalBarChart barChart1;
    private HorizontalBarChart barChart2;
    private PieChart ProductIncome;
    private PieChart ProductCost;
    private PieChart ProductProfit;
}