package minhcreator.component.ModularPanel;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.form.SimpleForm;
import minhcreator.functional.database.HibernateUtil;
import minhcreator.util.AppLogger;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigPanel extends SimpleForm {

    private JTextField dbUrlField;
    private JTextField dbUserField;
    private JPasswordField dbPassField;
    private JTextField appTitleField;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private Properties props;

    public ConfigPanel() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap 2, fillx, insets 20", "[right][grow,250]"));

        props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/minhcreator/config/appConfig.properties")) {
            if (is != null) props.load(is);
        } catch (Exception e) {
            AppLogger.error("Config", "Failed to load config: " + e.getMessage());
        }

        add(createHeader("Database Configuration"), "span 2, gapbottom 10");
        addField("DB URL:", dbUrlField = new JTextField(props.getProperty("db.url", "")), "span 2, growx");
        addField("DB User:", dbUserField = new JTextField(props.getProperty("db.user", "")), "span 2, growx");
        addField("DB Password:", dbPassField = new JPasswordField(props.getProperty("db.password", "")), "span 2, growx");

        add(createHeader("Application Settings"), "span 2, gapbottom 10, gaptop 15");
        addField("Window Title:", appTitleField = new JTextField(props.getProperty("title", "Warehouse Management System")), "span 2, growx");

        add(new JLabel("Width:"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(
                Integer.parseInt(props.getProperty("width", "1366")), 800, 3840, 10));
        add(widthSpinner, "growx");

        add(new JLabel("Height:"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(
                Integer.parseInt(props.getProperty("height", "768")), 600, 2160, 10));
        add(heightSpinner, "growx");

        JButton saveButton = new JButton("Save Configuration");
        saveButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "success");
        saveButton.addActionListener(e -> saveConfig());
        add(saveButton, "span 2, gaptop 20, align center");

        JButton resetButton = new JButton("Reset to Defaults");
        resetButton.addActionListener(e -> resetDefaults());
        add(resetButton, "span 2, align center");

        AppLogger.info("Config", "Configuration panel opened");
    }

    private void addField(String label, JComponent field, String constraints) {
        add(new JLabel(label));
        add(field, constraints);
    }

    private JLabel createHeader(String text) {
        JLabel header = new JLabel(text);
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");
        return header;
    }

    private void saveConfig() {
        try {
            props.setProperty("db.url", dbUrlField.getText().trim());
            props.setProperty("db.user", dbUserField.getText().trim());
            props.setProperty("db.password", new String(dbPassField.getPassword()));
            props.setProperty("title", appTitleField.getText().trim());
            props.setProperty("width", widthSpinner.getValue().toString());
            props.setProperty("height", heightSpinner.getValue().toString());

            String path = getClass().getResource("/minhcreator/config/appConfig.properties").getPath();
            try (OutputStream os = new FileOutputStream(path)) {
                props.store(os, "Warehouse Management System Configuration");
            }

            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Configuration saved. Restart to apply changes."
            );
            AppLogger.info("Config", "Configuration saved successfully");
        } catch (Exception e) {
            AppLogger.error("Config", "Failed to save config: " + e.getMessage());
            Notifications.getInstance().show(
                    Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Error saving configuration: " + e.getMessage()
            );
        }
    }

    private void resetDefaults() {
        dbUrlField.setText("jdbc:mysql://localhost/warehouse");
        dbUserField.setText("root");
        dbPassField.setText("");
        appTitleField.setText("Warehouse Management System");
        widthSpinner.setValue(1366);
        heightSpinner.setValue(768);
        AppLogger.info("Config", "Configuration reset to defaults");
    }
}
