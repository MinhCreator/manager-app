package minhcreator.debugPage;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author MinhCreatorVN
 */
public class FrameTest extends javax.swing.JFrame {


    public FrameTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());
//        add(new AnalyticsPanel());

    }


    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        new FrameTest();

    }
}