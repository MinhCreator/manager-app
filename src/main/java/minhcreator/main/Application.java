package minhcreator.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import minhcreator.component.form.MainForm;
import minhcreator.component.page.Login;
import minhcreator.component.page.Sign_up;
import minhcreator.functional.database.HibernateUtil;
import minhcreator.functional.fontRender.FontManager;
import minhcreator.util.AppLogger;
import minhcreator.util.ThemeManager;
import minhcreator.util.global;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;

public class Application extends JFrame {
    private static Application app;
    private final MainForm mainForm;
    private final Login loginForm;
    private final Sign_up signUpForm;

    public Application() {
        this.Config = global.Config("/minhcreator/config", "appConfig.properties");
        this.width = Integer.parseInt(Config.get("width"));
        this.height = Integer.parseInt(Config.get("height"));
        mainForm = new MainForm();
        loginForm = new Login();
        signUpForm = new Sign_up();
        init();
        setSize(this.width, this.height);
        setLocationRelativeTo(null);
    }

    private void init() {
        setTitle(Config.get("title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        initComponents();
        setContentPane(loginForm);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, false);
        Notifications.getInstance().setJFrame(this);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }

    public static void login() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.mainForm);
        app.mainForm.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0);
        app.mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginForm);
        app.loginForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void signup() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.signUpForm);
        app.signUpForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.signUpForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void setSelectedMenu(int index, int subIndex) {
        app.mainForm.setSelectedMenu(index, subIndex);
    }

    public static Application getInstance() {
        return app;
    }

    public static void restartUI() {
        SwingUtilities.updateComponentTreeUI(app);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }

    static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.err.println("Hibernate initialization failed: " + e.getMessage());
        }

        FlatJetBrainsMonoFont.install();
        FlatLaf.registerCustomDefaultsSource("minhcreator.themes");

        FontManager.LoadFont("defaultFont", FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 13);

        String savedTheme = loadSavedTheme();
        ThemeManager.applyTheme(savedTheme);

        AppLogger.info("Application", "Starting with theme: " + savedTheme);

        java.awt.EventQueue.invokeLater(() -> {
            app = new Application();
            app.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
    }

    public static String loadSavedTheme() {
        var config = global.Config("/minhcreator/config", "appConfig.properties");
        String theme = config.get("theme");
        if (theme == null || theme.isBlank()) {
            theme = ThemeManager.THEME_MATERIAL_LIGHT;
        }
        return theme;
    }

    private final Dictionary<String, String> Config;
    private final int width;
    private final int height;
    private static final FontManager fontSupport = new FontManager();
}
