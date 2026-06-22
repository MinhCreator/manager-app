package minhcreator.component.page;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import minhcreator.entity.UserEntity;
import minhcreator.functional.database.TableInitializer;
import minhcreator.functional.database.dao.UserDAO;
import minhcreator.functional.session.sessionManager;
import minhcreator.main.Application;
import minhcreator.util.AppLogger;
import minhcreator.util.ThemeManager;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class settings extends JPanel {
    private JTabbedPane tabbedPane;
    public sessionManager session_SignUp = Sign_up.session;
    public sessionManager session_Login = Login.session;
    private Properties appProps;

    public settings() {
        init();
        initComponent();
    }

    private void init() {
        setVisible(true);
        setLayout(new BorderLayout());
        appProps = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/minhcreator/config/appConfig.properties")) {
            if (is != null) appProps.load(is);
        } catch (Exception e) {
            AppLogger.error("Settings", "Failed to load config: " + e.getMessage());
        }
    }

    private void initComponent() {
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty(FlatClientProperties.STYLE, "tabType:underlined;tabAreaAlignment:fill");

        tabbedPane.addTab("Account", createAccountPanel());
        tabbedPane.addTab("Appearance", createAppearancePanel());
        tabbedPane.addTab("System", createSystemPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ─── Tab 1: Account ─────────────────────────────────────────────

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 2, fillx, insets 30 45 30 45", "[right][grow,300]"));

        JLabel header = new JLabel("Account Settings");
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");
        panel.add(header, "span 2, gapbottom 15");

        JLabel emailLb = new JLabel("Email");
        JTextField emailTf = new JTextField(session_Login.getEmail());
        panel.add(emailLb);
        panel.add(emailTf, "growx");

        JLabel userLb = new JLabel("Username");
        JTextField userTf = new JTextField(session_Login.getUsername());
        panel.add(userLb);
        panel.add(userTf, "growx");

        JLabel passLb = new JLabel("Password");
        JPasswordField passTf = new JPasswordField(session_Login.getPassword());
        passTf.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        panel.add(passLb);
        panel.add(passTf, "growx");

        JButton updateBut = new JButton("Update Account");
        updateBut.putClientProperty(FlatClientProperties.STYLE_CLASS, "success");
        updateBut.addActionListener(e -> {
            letUpdate(userTf, emailTf, passTf);
        });
        panel.add(updateBut, "span 2, gaptop 10, align center");

        return panel;
    }

    // ─── Tab 2: Appearance ───────────────────────────────────────────

    private JPanel createAppearancePanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 2, fillx, insets 30 45 30 45", "[right][grow,300]"));

        JLabel header = new JLabel("Appearance");
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");
        panel.add(header, "span 2, gapbottom 15");

        // Theme selector
        JLabel themeLb = new JLabel("Theme");
        String[] themes = ThemeManager.getAvailableThemes();
        JComboBox<String> themeCombo = new JComboBox<>();
        String currentTheme = appProps.getProperty("theme", ThemeManager.THEME_MATERIAL_LIGHT);
        for (String t : themes) {
            themeCombo.addItem(ThemeManager.getThemeDisplayName(t));
            if (t.equals(currentTheme)) {
                themeCombo.setSelectedItem(ThemeManager.getThemeDisplayName(t));
            }
        }
        themeCombo.addActionListener(e -> {
            String selected = themeCombo.getSelectedItem().toString();
            String themeKey = getThemeKey(selected);
            appProps.setProperty("theme", themeKey);
            saveConfigProps();
            ThemeManager.applyTheme(themeKey, Application.getInstance());
            AppLogger.info("Settings", "Theme changed to " + selected);
        });
        panel.add(themeLb);
        panel.add(themeCombo, "growx");

        // Accent color label
        JLabel accentLb = new JLabel("Accent Color");
        JPanel accentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        Color accent = UIManager.getColor("Component.accentColor");
        JLabel accentPreview = new JLabel("  ");
        accentPreview.setOpaque(true);
        accentPreview.setBackground(accent != null ? accent : new Color(103, 80, 164));
        accentPreview.setPreferredSize(new Dimension(24, 24));
        JButton resetAccent = new JButton("Reset to Default");
        resetAccent.addActionListener(e -> {
            FlatLaf.setGlobalExtraDefaults(null);
            FlatLaf.updateUI();
            AppLogger.info("Settings", "Accent color reset to default");
        });
        accentPanel.add(accentPreview);
        accentPanel.add(Box.createHorizontalStrut(8));
        accentPanel.add(resetAccent);
        panel.add(accentLb);
        panel.add(accentPanel, "growx");

        // Font size
        JLabel fontLb = new JLabel("Font Size");
        JSpinner fontSpinner = new JSpinner(new SpinnerNumberModel(13, 10, 24, 1));
        panel.add(fontLb);
        panel.add(fontSpinner, "growx, w 100");

        // Apply font button
        JButton applyFont = new JButton("Apply Font Size");
        applyFont.addActionListener(e -> {
            int size = (int) fontSpinner.getValue();
            UIManager.put("defaultFont", new Font(UIManager.getFont("defaultFont").getFamily(), Font.PLAIN, size));
            FlatLaf.updateUI();
            AppLogger.info("Settings", "Font size changed to " + size);
        });
        panel.add(applyFont, "span 2, gaptop 10, align center");

        return panel;
    }

    // ─── Tab 3: System ───────────────────────────────────────────────

    private JPanel createSystemPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap 2, fillx, insets 30 45 30 45", "[right][grow,300]"));

        JLabel header = new JLabel("System");
        header.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");
        panel.add(header, "span 2, gapbottom 15");

        JLabel infoLb = new JLabel("Logged in as:");
        JLabel infoVal = new JLabel(session_Login.getUsername() + " (" + session_Login.getEmail() + ")");
        infoVal.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        panel.add(infoLb);
        panel.add(infoVal, "growx");

        JLabel versionLb = new JLabel("Version");
        JLabel versionVal = new JLabel("1.0.0");
        panel.add(versionLb);
        panel.add(versionVal, "growx");

        JSeparator sep = new JSeparator();
        panel.add(sep, "span 2, growx, gaptop 10, gapbottom 10");

        JButton logOutBut = new JButton("Log out");
        logOutBut.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");
        logOutBut.addActionListener(e -> {
            letLogOut();
            session_Login.clearSession();
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "You have logged out");
        });
        panel.add(logOutBut, "span 2, align center, split 3");

        JButton delBut = new JButton("Delete Account");
        delBut.putClientProperty(FlatClientProperties.STYLE_CLASS, "danger");
        delBut.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete your account? All data will be permanently lost.",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) return;

            TableInitializer.dropAllTables(session_Login.getUsername());
            deleteMyAccount();
            session_Login.clearSession();
            letLogOut();
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Your account has been deleted");
        });
        panel.add(delBut);

        return panel;
    }

    // ─── Actions ────────────────────────────────────────────────────

    private void letLogOut() {
        Application.logout();
    }

    private void letUpdate(JTextField userTf, JTextField emailTf, JPasswordField passTf) {
        UserDAO userDAO = new UserDAO();
        int userId = Integer.parseInt(session_Login.getId());
        String username = userTf.getText().trim();
        String email = emailTf.getText().trim();
        String pass = new String(passTf.getPassword());
        String hashed = BCrypt.withDefaults().hashToString(12, pass.toCharArray());

        UserEntity user = userDAO.findById(userId);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(hashed);
            userDAO.update(user);

            session_Login.login(String.valueOf(userId), username, email, hashed);
            AppLogger.info("Settings", "Account updated for user " + username);

            Notifications.getInstance().show(
                    Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Account updated successfully!"
            );
        }
    }

    private void deleteMyAccount() {
        UserDAO userDAO = new UserDAO();
        int currentUserId = Integer.parseInt(session_Login.getId().trim());
        userDAO.delete(currentUserId);
    }

    private void saveConfigProps() {
        String path = getClass().getResource("/minhcreator/config/appConfig.properties").getPath();
        try (OutputStream os = new FileOutputStream(path)) {
            appProps.store(os, "Warehouse Management System Configuration");
        } catch (Exception e) {
            AppLogger.error("Settings", "Failed to save config: " + e.getMessage());
        }
    }

    private String getThemeKey(String displayName) {
        for (String t : ThemeManager.getAvailableThemes()) {
            if (ThemeManager.getThemeDisplayName(t).equals(displayName)) {
                return t;
            }
        }
        return ThemeManager.THEME_MATERIAL_LIGHT;
    }
}
