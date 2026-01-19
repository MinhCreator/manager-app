package minhcreator.component.page;

import minhcreator.component.ModularPanel.invoicesPanel;

import javax.swing.*;

/**
 *
 * @author MinhCreatorVN
 */
public class invoice extends javax.swing.JPanel {
    public invoice() {
        init();
        initComponent();
    }

    private void init() {
        setVisible(true);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    private void initComponent() {
        add(new invoicesPanel());
    }
}