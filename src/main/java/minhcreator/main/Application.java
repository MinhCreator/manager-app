package minhcreator.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import minhcreator.component.form.MainForm;
import minhcreator.component.page.Login;
import minhcreator.component.page.Sign_up;
import minhcreator.functional.fontRender.FontManager;
import minhcreator.util.global;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;

/**
 *
 * @author Raven
 * @author Modified and added new feature by MinhCreatorVN
 */
public class Application extends JFrame {
    private static Application app;
    private final MainForm mainForm;
    private final Login loginForm;
    private final Sign_up signUpForm;

    public Application() {
        this.Config = global.Config("/minhcreator/config", "appConfig.properties");
        this.width = Integer.parseInt(Config.get("width"));
        this.height = Integer.parseInt(Config.get("height"));
//        this.setResizable(false);
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
        FlatJetBrainsMonoFont.install();
//        FlatArcIJTheme.setup();
        FlatLaf.registerCustomDefaultsSource("minhcreator.themes");
//        javax.swing.UIManager.put(
//                "defaultFont",
//                new Font(FlatJetBrainsMonoFont.FAMILY,
//                        Font.PLAIN, 13
//                )
//        );

        FontManager.LoadFont("defaultFont", FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 13);
        FlatMacLightLaf.setup();
        //FlatLightLaf.setup();
//        EventQueue.invokeLater(() -> {
//            app = new Application();
//            app.setVisible(true);
//        });

        java.awt.EventQueue.invokeLater(() -> {
            app = new Application();
//            app.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            app.setVisible(true);
        });

    }

    private final Dictionary<String, String> Config;
    private final int width;
    private final int height;
    private static final FontManager fontSupport = new FontManager();
}