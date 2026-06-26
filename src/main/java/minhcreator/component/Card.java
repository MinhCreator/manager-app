package minhcreator.component;

import com.formdev.flatlaf.FlatClientProperties;
import minhcreator.component.model.ModelCard;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Card extends JPanel {

    private Color accentColor;
    private JLabel lbIcon;
    private JLabel lbTitle;
    private JLabel lbValues;
    private JPanel contentPanel;

    public Card() {
        init();
    }

    private void init() {
        setOpaque(false);
        accentColor = new Color(103, 80, 164);

        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        GridBagConstraints gbc = new GridBagConstraints();

        lbIcon = new JLabel();
        lbIcon.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(lbIcon, gbc);

        lbTitle = new JLabel();
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold -1");
        lbTitle.setForeground(UIManager.getColor("Label.disabledForeground"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(lbTitle, gbc);

        lbValues = new JLabel();
        lbValues.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
        gbc.gridy = 1;
        contentPanel.add(lbValues, gbc);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setData(ModelCard data) {
        lbTitle.setText(data.getTitle());
        if (data.getUserName() != null && !data.getUserName().isEmpty()) {
            lbValues.setText(data.getUserName());
        } else {
            DecimalFormat df = new DecimalFormat("#,##0.##");
            lbValues.setText(df.format(data.getValues()));
        }
    }

    public void setIcon(Icon icon) {
        lbIcon.setIcon(icon);
    }

    public void setAccentColor(Color color) {
        this.accentColor = color;
        repaint();
    }

    public Color getAccentColor() {
        return accentColor;
    }

    private transient GradientPaint cachedGradient;
    private transient Color cachedLighter;
    private transient Dimension lastSize;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = UIManager.getColor("Panel.background");
        Dimension size = getSize();
        if (!size.equals(lastSize)) {
            lastSize = size;
            cachedLighter = new Color(
                    Math.min(255, bg.getRed() + 20),
                    Math.min(255, bg.getGreen() + 20),
                    Math.min(255, bg.getBlue() + 20)
            );
            cachedGradient = new GradientPaint(0, 0, cachedLighter, size.width, size.height, bg);
        }
        g2.setPaint(cachedGradient);
        g2.fillRoundRect(0, 0, size.width, size.height, 16, 16);

        g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
        g2.fillRoundRect(0, 0, 6, size.height, 3, 3);

        g2.dispose();
        super.paintComponent(g);
    }
}
