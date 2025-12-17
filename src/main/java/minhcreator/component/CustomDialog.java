package minhcreator.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomDialog extends javax.swing.JDialog {
    public CustomDialog(JFrame parent, boolean modal, String Header, String message, String iconic_path, String buttonText, List<Integer> ScaleSVG ) {
        super(parent, modal);
        this.Header = Header;
        this.message = message;
        this.iconic_path = iconic_path;
        this.buttonText = buttonText;
        this.parent = parent;
        this.width = ScaleSVG.get(0);
        this.height = ScaleSVG.get(1);
        initComponent();
        settings();
    }

    public CustomDialog(JFrame parent, boolean modal, String header, String message, String buttonText) {
        super(parent, modal);
        this.Header = header;
        this.message = message;
        this.buttonText = buttonText;
        this.parent = parent;
        initComponentNoIcon();
        settings();
    }

    public void settings() {
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    private void initComponent() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        FlatSVGIcon icon = new FlatSVGIcon(iconic_path, width, height);
        JLabel iconic = new JLabel();
        JLabel Header = new JLabel();
        JLabel message = new JLabel();

        iconic.setIcon(icon);
        JButton button = new JButton(buttonText);

        // layout design
        iconic.setHorizontalAlignment(SwingConstants.CENTER);
        button.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 15));
        button.addActionListener(e -> this.dispose());

        Header.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 15));
        Header.setForeground(new java.awt.Color(143, 143, 143));
        Header.setHorizontalAlignment(SwingConstants.CENTER);
        Header.setText(this.Header);

        message.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 12)); // NOI18N
        message.setForeground(new java.awt.Color(143, 143, 143));
        message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        message.setText(this.message);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(iconic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 194, Short.MAX_VALUE)
                                                .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 194, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(iconic)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Header)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(message)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
        );

        pack();

    }

    private void initComponentNoIcon() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel Header = new JLabel();
        JLabel message = new JLabel();

        JButton button = new JButton(buttonText);

        // layout design
        button.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 15));
        button.addActionListener(e -> this.dispose());

        Header.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 15));
        Header.setForeground(new java.awt.Color(143, 143, 143));
        Header.setHorizontalAlignment(SwingConstants.CENTER);
        Header.setText(this.Header);

        message.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 12)); // NOI18N
        message.setForeground(new java.awt.Color(143, 143, 143));
        message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        message.setText(this.message);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 194, Short.MAX_VALUE)
                                                .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 194, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Header)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(message)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
        );

        pack();
    }
    private String message;
    private String Header;
    private String iconic_path;
    private String buttonText;
    private JFrame parent;
    private int width;
    private int height;
}
