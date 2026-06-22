package minhcreator.component.page;

import minhcreator.component.ModularPanel.ConfigPanel;

import javax.swing.*;

public class AppConfigPage extends JPanel {
    public AppConfigPage() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new ConfigPanel());
    }
}
