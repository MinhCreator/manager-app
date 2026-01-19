package minhcreator.debugFeature.UIManager;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import minhcreator.main.Application;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Raven
 * @author Modified by MinhCreatorVN
 */

public class UIManager {
    private Application app;
    private static UIManager instance;

    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    private UIManager() {
    }

    public void initApplication(Application application) {
        this.app = application;
    }

    public void showForm(JComponent Component) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            app.setContentPane(Component);
            app.revalidate();
            app.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}