package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import minhcreator.component.form.SimpleForm;
import minhcreator.util.AppLogger;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LogViewerPanel extends SimpleForm {

    private JComboBox<String> filterCombo;
    private JButton clearButton;
    private JButton refreshButton;
    private JTable logTable;
    private DefaultTableModel tableModel;

    public LogViewerPanel() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(10, 10));

        add(createToolbar(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new MigLayout("insets 5, fillx", "[][][][]push[]"));

        JLabel filterLabel = new JLabel("Filter:");
        filterCombo = new JComboBox<>(new String[]{"All", "INFO", "WARNING", "ERROR", "DEBUG"});
        filterCombo.addActionListener(e -> applyFilter());

        clearButton = new JButton("Clear");
        clearButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "danger");
        clearButton.addActionListener(e -> AppLogger.clear());

        refreshButton = new JButton();
        refreshButton.setIcon(new FlatSVGIcon("minhcreator/assets/functional_icon/refresh.svg"));
        refreshButton.addActionListener(e -> applyFilter());

        toolbar.add(filterLabel);
        toolbar.add(filterCombo);
        toolbar.add(refreshButton);
        toolbar.add(clearButton, "right");

        return toolbar;
    }

    private JScrollPane createTablePanel() {
        String[] cols = {"Time", "Level", "Source", "Message"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        AppLogger.bindTable(tableModel);

        logTable = new JTable(tableModel);
        logTable.setRowHeight(28);
        logTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        logTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        logTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        logTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        logTable.getColumnModel().getColumn(3).setPreferredWidth(500);

        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        logTable.getColumnModel().getColumn(0).setCellRenderer(render);
        logTable.getColumnModel().getColumn(1).setCellRenderer(render);
        logTable.getColumnModel().getColumn(2).setCellRenderer(render);

        AppLogger.info("LogViewer", "Log viewer opened");
        return new JScrollPane(logTable);
    }

    private void applyFilter() {
        String selected = (String) filterCombo.getSelectedItem();
        AppLogger.Level level = null;
        if (selected != null && !selected.equals("All")) {
            level = AppLogger.Level.valueOf(selected.toUpperCase());
        }
        tableModel.setRowCount(0);
        var logs = level == null ? AppLogger.getLogs() : AppLogger.getLogsByLevel(level);
        var fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        for (var entry : logs) {
            tableModel.addRow(new Object[]{
                    fmt.format(entry.getTimestamp()),
                    entry.getLevel(),
                    entry.getSource(),
                    entry.getMessage()
            });
        }
    }
}
