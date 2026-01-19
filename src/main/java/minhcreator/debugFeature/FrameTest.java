package minhcreator.debugFeature;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import minhcreator.functional.database.DB;
import minhcreator.functional.location.TimeManager;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.datetime.DateSelectionAble;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author MinhCreatorVN
 */
public class FrameTest extends javax.swing.JFrame {
    private static FrameTest app;

    public FrameTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());
//        add(new invoicesPanel());
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));
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
        JButton tesbt = new JButton();
//        System.out.println(calenderChooser.getText());
        tesbt.addActionListener(e -> System.out.println(calenderChooser.getText()));
        panel.add(calenderChooser);
        panel.add(tesbt);
        add(panel);

    }

    private static ResultSet test() throws Exception {
        String sql = "SELECT created_at FROM admin_invoices";
        try {
            Connection conn = DB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void get() throws Exception {
        ResultSet rs = test();
        TimeManager time = new TimeManager();
        while (rs.next()) {
            String getDate = rs.getString("created_at");
            String format = time.TimeDateFormated(getDate, "HH:mm");

            System.out.println(format);
        }
    }

    public static void main(String[] args) {
        FlatMacLightLaf.setup();
//        new FrameTest();
        java.awt.EventQueue.invokeLater(() -> {
            app = new FrameTest();
//            app.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            app.setVisible(true);
        });

    }
}