package minhcreator.component.page;

import minhcreator.component.ModularPanel.DashPanel;
import minhcreator.functional.session.sessionManager;

import javax.swing.*;

/**
 *
 * @author MinhCreatorVN
 */
public class Dashboard extends javax.swing.JPanel {

    public Dashboard(sessionManager session) {
        init();

    }

    private void init() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new DashPanel());
    }

}