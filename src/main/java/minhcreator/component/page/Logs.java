package minhcreator.component.page;

import minhcreator.component.ModularPanel.LogViewerPanel;

import javax.swing.*;

public class Logs extends JPanel {
    public Logs() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new LogViewerPanel());
    }
}
